import java.util.ArrayList;
import java.util.Random;

public class Perturbative {

    private static final Random random = new Random();
    private static ArrayList<Course> courses;
    private static ArrayList<Student> students;
    private static final int num = 1000;

    public Perturbative (ArrayList<Course> crs,ArrayList<Student> students)
    {
        this.courses = crs;
        this.students = students;
    }
    public static void PenaltyReductionbyKempe()
    {
        for(int i=0; i<num; i++) {
            int Bound = courses.size();
            int current = random.nextInt(Bound);

            Course[] overlappingCourses = courses.get(current).getOverlappingCourses();

            if(overlappingCourses.length != 0)
                KChainInterchange(courses.get(current),
                        overlappingCourses[random.nextInt(overlappingCourses.length)].ScheduledDay);
        }
    }


    private static void DFSUtil(Course current, int neighbour_Day) {
        Course[] overlappingCourses = current.getOverlappingCourses();

        current.dfs_Stat = 1;

        for(Course cur : overlappingCourses)
            if(cur.dfs_Stat == 0 && cur.ScheduledDay == neighbour_Day)
                DFSUtil(cur,current.ScheduledDay);

        current.dfs_Stat =2;
    }

    public static void InterChange(int neighbour_Scheduled_Day, int cur_Day) {
        for(Course crs : courses)
            if (crs.dfs_Stat == 2) {
                if (crs.ScheduledDay == neighbour_Scheduled_Day)
                    crs.ScheduledDay = cur_Day;
                else crs.ScheduledDay = neighbour_Scheduled_Day; // if crs Scheduled day = current day change it
            }
    }

    public static void SetUnexplored()
    {
        for(Course crs : courses)
            if(crs.dfs_Stat == 2)
                crs.dfs_Stat = 0;
    }

    private static void KChainInterchange(Course current, int neighbour_Scheduled_Day) {

        // implementation of least number color using in graph coloring

        DFSUtil(current, neighbour_Scheduled_Day);

        int current_day = current.ScheduledDay;
        double current_penalty = Student.calculateAveragePenalty(students);

        InterChange(neighbour_Scheduled_Day, current_day); // current to neighbourday

        if(current_penalty <= Student.calculateAveragePenalty(students))
            InterChange(neighbour_Scheduled_Day, current_day); // penalty jodi bere jay tahole undo  to current from neighbourday

        SetUnexplored();
    }

    public static void PenaltyReductionPairSwap() {

        for(int i=0; i<num; i++) {
            int sz = courses.size();
            int current = random.nextInt(sz);
            int cur = random.nextInt(sz);

            PairSwap(courses.get(cur), courses.get(current));
        }
    }

    private static void PairSwap(Course swap, Course turn) {

        int swap_Day=swap.ScheduledDay;
        int turn_Day=turn.ScheduledDay;

        if(swap_Day == turn_Day)
            return;

        KChainInterchange(swap, turn_Day);
        KChainInterchange(turn,swap_Day);
    }
}
