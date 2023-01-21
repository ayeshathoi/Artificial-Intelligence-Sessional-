import java.util.ArrayList;
import java.util.Random;

public class Perturbative {

    private static Random random = new Random();
    private static ArrayList<Course> courses;
    private static ArrayList<Student> students;

    public Perturbative (ArrayList<Course> crs,ArrayList<Student> students)
    {
        this.courses = crs;
        this.students = students;
    }

    private static void DFSUtil(Course current, int neighbor_time_slot) {
        Course[] overlappingCourses = current.getOverlappingCourses();

        current.dfs_Stat = 1;

        for(Course cur : overlappingCourses)
            if(cur.dfs_Stat == 0 && cur.ScheduledDay == neighbor_time_slot)
                DFSUtil(cur,current.ScheduledDay);

        current.dfs_Stat =2;
    }

    public static void PenaltyReductionbyKempe()
    {
        for(int i=0; i<10000; i++) {
            int Bound = courses.size();
            int current = random.nextInt(Bound);

            Course[] overlappingCourses = courses.get(current).getOverlappingCourses();

            if(overlappingCourses.length != 0)
                KChainInterchange(courses.get(current),
                        overlappingCourses[random.nextInt(overlappingCourses.length)].ScheduledDay);
        }
    }

    public static void InterChange(int neighbour_Scheduled_Day, int cur_Day) {
        for(Course crs : courses)
            if (crs.dfs_Stat == 2) {
                if (crs.ScheduledDay != cur_Day)
                    crs.ScheduledDay = cur_Day;
                else crs.ScheduledDay = neighbour_Scheduled_Day;
            }
    }

    public static void SetUnexplored()
    {
        for(Course crs : courses)
            if(crs.dfs_Stat == 2)
                crs.dfs_Stat = 0;
    }

    private static void KChainInterchange(Course current, int neighbour_Scheduled_Day) {

        DFSUtil(current, neighbour_Scheduled_Day);

        int current_day = current.ScheduledDay;
        double current_penalty = Student.calculateAveragePenalty(students);

        InterChange(neighbour_Scheduled_Day, current_day);

        if(current_penalty <= Student.calculateAveragePenalty(students))
            InterChange(neighbour_Scheduled_Day, current_day); // penalty jodi bere jay tahole undo

        SetUnexplored();
    }

    public static void PenaltyReductionPairSwap() {

        for(int i=0; i<10000; i++) {
            int sz = courses.size();
            int current = random.nextInt(sz);
            int cur = random.nextInt(sz);

            PairSwap(courses.get(cur), courses.get(current));
        }
    }

    public static boolean check(Course[] crs, int ScheduledDay)
    {
        for(Course courses : crs)
            if(courses.ScheduledDay == ScheduledDay)
                return true;
        return false;
    }

    private static void PairSwap(Course swap, Course turn) {

        int swap_Day=swap.ScheduledDay;
        int turn_Day=turn.ScheduledDay;
        Course[] overlappingCourses;

        if(swap_Day == turn_Day)
            return ;

        overlappingCourses = swap.getOverlappingCourses();
        if(check(overlappingCourses, turn_Day))
            return;

        overlappingCourses = turn.getOverlappingCourses();
        if(check(overlappingCourses, swap_Day))
            return;

        double current_penalty = Student.calculateAveragePenalty(students);

        swap.ScheduledDay = turn_Day;
        turn.ScheduledDay = swap_Day;

        if(current_penalty <= Student.calculateAveragePenalty(students)) {
            swap.ScheduledDay = swap_Day;
            turn.ScheduledDay = turn_Day;
        }
    }
}
