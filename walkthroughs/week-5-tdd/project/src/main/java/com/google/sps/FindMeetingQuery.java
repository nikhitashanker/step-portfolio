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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;

public final class FindMeetingQuery {
  /* Finds all time ranges that satisfy the request given the already existing set of events. */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Find potential conflicts between scheduled events and request using only
    // mandatory attendees.
    ImmutableSet<String> mandatoryAttendees = ImmutableSet.copyOf(request.getAttendees());
    List<TimeRange> mandatoryAttendeeConflicts = getPotentialConflicts(events, mandatoryAttendees);

    // Find potential conflicts between scheduled events and request using all attendees.
    List<TimeRange> allConflicts = new ArrayList(mandatoryAttendeeConflicts);
    List<TimeRange> optionalConflicts = getPotentialConflicts(events, request.getOptionalAttendees());
    allConflicts.addAll(optionalConflicts);

    // Find time ranges based on all conflicts.
    Collection<TimeRange> timeRanges = queryUsingPotentialConflicts(events, request, allConflicts);

    // If time ranges based on all conflicts is not empty or no mandatory attendees
    // return the time ranges based on all conflicts.
    if (!timeRanges.isEmpty() || mandatoryAttendees.isEmpty()) {
      return timeRanges;
    }
    
    Collection<TimeRange> mandatoryTimeRanges = queryUsingPotentialConflicts(events, request, mandatoryAttendeeConflicts);

    Map<TimeRange, Integer> optionalConflictsWithCount = getPotentialConflictsOptional(events, request.getOptionalAttendees());

    // Return the time ranges based on mandatory attendee conflicts.
    Collection<TimeRange> maximumOptionalAttendees = queryMaxNumberOfOptionalAttendess(request.getDuration(), mandatoryTimeRanges, optionalConflictsWithCount);
    if (maximumOptionalAttendees.isEmpty()) {
        return mandatoryTimeRanges;
    }
    return maximumOptionalAttendees;
  }

  // Returns time ranges for the request using potential conflicts provided.
  private static Collection<TimeRange> queryUsingPotentialConflicts(Collection<Event> events, 
        MeetingRequest request, List<TimeRange> potentialConflicts) {
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
            timeRanges.add(TimeRange.fromStartEnd(lastConflictEnd, currentConflictStart, /* inclusive= */ false));
        }

        // Update the latest conflict end time seen so far.
        int currentConflictEnd = currentConflict.end();
        if (currentConflictEnd > lastConflictEnd) {
            lastConflictEnd = currentConflictEnd;
        }
    }

    // Check if the time between the latest end time and the end of the day is a valid time range.
    if (TimeRange.END_OF_DAY - lastConflictEnd >= requestDuration) {
        timeRanges.add(TimeRange.fromStartEnd(lastConflictEnd, TimeRange.END_OF_DAY, /* inclusive= */ true));
    }
    return timeRanges;
  }

  private static List<TimeRange> queryMaxNumberOfOptionalAttendess(long requestDuration, Collection<TimeRange> mandatoryTimeRanges, 
        Map<TimeRange, Integer> optionalConflicts) {
        int min = Integer.MAX_VALUE;
        List<TimeRange> result = new ArrayList<TimeRange>();
        for (TimeRange mandatoryTimeRange : mandatoryTimeRanges) {
            for (Entry<TimeRange, Integer> optionalConflict : optionalConflicts.entrySet()) {
                TimeRange timeRange = optionalConflict.getKey();
                Integer numberOfAttendees = optionalConflict.getValue();
                if (mandatoryTimeRange.contains(timeRange)) {
                    if (timeRange.duration() >= requestDuration) {
                        if (numberOfAttendees < min) {
                            result.clear();
                            result.add(timeRange);
                        } else if (numberOfAttendees == min) {
                            result.add(timeRange);
                        }
                    }
                }
            }
        }
        return result;
  }

  private static List<TimeRange> getPotentialConflicts(Collection<Event> events, 
        Collection<String> requestAttendees) {
    List<TimeRange> potentialConflicts = new ArrayList<TimeRange>();
    for (Event event : events) {
        // Find the intersection between current event attendees and request attendees.
        Set<String> intersection = new HashSet<String>(event.getAttendees());
        intersection.retainAll(requestAttendees);

        // If the intersection is not empty, add this event time to the potential
        // conflicts.
        if (!intersection.isEmpty()) {
            potentialConflicts.add(event.getWhen());
        }
    } 
    return potentialConflicts;
  }

  private static Map<TimeRange, Integer> getPotentialConflictsOptional(Collection<Event> events, 
        Collection<String> requestAttendees) {
    Map<TimeRange, Integer> potentialConflicts = new HashMap<TimeRange, Integer>();
    for (Event event : events) {
        // Find the intersection between current event attendees and request attendees.
        Set<String> intersection = new HashSet<String>(event.getAttendees());
        intersection.retainAll(requestAttendees);

        // If the intersection is not empty, add this event time to the potential
        // conflicts.
        if (!intersection.isEmpty()) {
            potentialConflicts.put(event.getWhen(), intersection.size());
            System.out.println("hiii");
        }
    } 
    return potentialConflicts;
  }
}
