// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public final class FindMeetingQuery {
  /* Finds all time ranges that satisfy the request given the already existing set of events. */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Find potential conflicts between scheduled events and request.
    List<TimeRange> potentialConflicts = getPotentialConflicts(events, request);

    // Sort the potential conflicts in order of ascending start time.
    Collections.sort(potentialConflicts, TimeRange.ORDER_BY_START);
   
    Collection<TimeRange> timeRanges = new ArrayList<TimeRange>();
    int lastConflictEnd = TimeRange.START_OF_DAY;
    long requestDuration = request.getDuration();
    for (TimeRange currentConflict : potentialConflicts) {
        // If the time between the end time of the current conflict and the latest end time 
        // conflict seen so far is greater that the request duration provided,
        // add a new time range to the time ranges returned.
        int currentConflictStart = currentConflict.start();
        if (currentConflictStart - lastConflictEnd >= requestDuration) {
            timeRanges.add(TimeRange.fromStartEnd(lastConflictEnd, currentConflictStart, false));
        }

        // Update the latest conflict end time seen so far.
        int currentConflictEnd = currentConflict.end();
        if (currentConflictEnd > lastConflictEnd)
            lastConflictEnd = currentConflictEnd;
    }

    // Check if the time between the latest end time and the end of the day is a valid time range.
    if (TimeRange.END_OF_DAY - lastConflictEnd >= requestDuration) {
        timeRanges.add(TimeRange.fromStartEnd(lastConflictEnd, TimeRange.END_OF_DAY, true));
    }
    return timeRanges;
  }

  private List<TimeRange> getPotentialConflicts(Collection<Event> events, MeetingRequest request) {
    List<TimeRange> potentialConflicts = new ArrayList<TimeRange>();
    Collection<String> requestAttendees = request.getAttendees();
    for (Event event : events) {
        // Find the intersection between current event attendees and request attendees.
        HashSet<String> intersection = new HashSet<String>(event.getAttendees());
        intersection.retainAll(requestAttendees);

        // If the intersection size is greater than 0, add this event time to the potential
        // conflicts.
        if (intersection.size() > 0)
            potentialConflicts.add(event.getWhen());
    } 
    return potentialConflicts;
  }
}
