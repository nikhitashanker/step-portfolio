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
import java.util.HashSet;

public final class FindMeetingQuery {
  /* Finds all time ranges that satisfy the request given the already existing set of events. */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Find potential conflicts.
    List<TimeRange> potentialConflicts = new ArrayList<TimeRange>();
    for (Event e : events) {
        HashSet<String> intersection = new HashSet<String>(e.getAttendees());
        intersection.retainAll(request.getAttendees());
        if (intersection.size() > 0)
            potentialConflicts.add(e.getWhen());
    } 

    // Sort the potential conflicts in order of ascending start time.
    Collections.sort(potentialConflicts, TimeRange.ORDER_BY_START);

    // Iterate through the conflicts, keeping track of latest end time seen so far.
    // If the time between the end time of the current conflict and the latest end time 
    // conflict seen so far is greater that the request duration provided,
    // add a new time range to the time ranges returned.
    Collection<TimeRange> timeRanges = new ArrayList<TimeRange>();
    int lastConflictEnd = TimeRange.START_OF_DAY;
    long requestDuration = request.getDuration();
    for (TimeRange currentConflict : potentialConflicts) {
        int currentConflictStart = currentConflict.start();
        if (currentConflictStart - lastConflictEnd >= requestDuration) {
            timeRanges.add(TimeRange.fromStartEnd(lastConflictEnd, currentConflictStart, false));
        }
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
}
