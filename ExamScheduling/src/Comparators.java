import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Comparators {

    //------------------------------------------------------------------------
    // Random Ordering Schedule without Comparator
    public static int RandomSchedule(ArrayList<Course> courses) {
        Collections.shuffle(courses);
        return Constructive.Scheduler(courses);
    }
    //----------------------------------------------------------------------------------

    //implements DegreeComparator
    public static int ScheduleByLargestDegree(ArrayList<Course> courses) {
        Collections.sort(courses, new DegreeComparator());
        return Constructive.Scheduler(courses);
    }

    //implements EnrollmentComparator
    public static int ScheduleByLargestEnrollment(ArrayList<Course> courses) {
        Collections.sort(courses, new EnrollmentComparator());
        return Constructive.Scheduler(courses);
    }

    //---------------------------------------------------------------------------------------------

    // day wise sort
    public static class ScheduleComparator implements Comparator<Course> {
        @Override
        public int compare(Course c1, Course c2) {
            return c1.ScheduledDay - c2.ScheduledDay; // ascending
        }
    }
    // conflict wise sort
    public static class DegreeComparator implements Comparator<Course> {
        @Override
        public int compare(Course c1, Course c2) {
            //descending
            return c2.getOverlappingCourses().length-c1.getOverlappingCourses().length;
        }
    }
    //enrollment wise sort
    public static class EnrollmentComparator implements Comparator<Course> {
        @Override
        public int compare(Course c1, Course c2) {
            //descending
            return c2.StudentEnrolled-c1.StudentEnrolled;}
    }
    //----------------------------------------------------------------------------------------------

}
