import java.util.ArrayList;

public class Course {
    public final String courseID;
    public final int StudentEnrolled;
    public int ScheduledDay = -1;
    private final ArrayList<Course> overlappingCourses = new ArrayList<>();
    public int dfs_Stat;  // 0 -> not explored, 1-> visited , 2-> explored complete

    //------------------------------------------------------------
    public Course(String courseID, int total_enrollment) {
        this.courseID = courseID;
        this.StudentEnrolled = total_enrollment;
    }

    public void setOverLappingCourses(Course course) {overlappingCourses.add(course);}

    public Course[] getOverlappingCourses() {

        int sz = this.overlappingCourses.size();
        Course[] overlappingCourses = new Course[sz];

        for(int i=0; i<sz; i++)
            overlappingCourses[i] = this.overlappingCourses.get(i);

        return overlappingCourses;
    }
}
