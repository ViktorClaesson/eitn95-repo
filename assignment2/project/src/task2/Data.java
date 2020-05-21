package task2;

import java.util.*;

public class Data {

    public static int total_meetings;
    public static Map<String, Integer> meetings;

    public static void reset() {
        total_meetings = 0;
        meetings = new HashMap<String, Integer>();
    }

    public static void recordMeeting(Student s1, Student s2) {
        total_meetings++;
        String studentKey = Student.studentKey(s1, s2);
        meetings.put(studentKey, meetings.getOrDefault(studentKey, 0) + 1);
    }

}