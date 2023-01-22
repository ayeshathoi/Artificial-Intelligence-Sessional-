import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class Constructive {

    private static int total_Scheduled_Day = 0;
    private static int suitableDay = 0;

    //-------------------------------------------------
    static int max_saturation_degree;
    static int max_index;
    static int count ;



    public static void IncreaseDay(int[] dayOccupited,Course crs)
    {
        for(int Day : dayOccupited) {
            if (Day != -1) {
                if (suitableDay < Day)
                    crs.ScheduledDay = suitableDay;
                else if (suitableDay == Day)
                    suitableDay++;
            }
        }
    }

    public static void SelectDay(Course crs)
    {
        if(suitableDay != total_Scheduled_Day)
            crs.ScheduledDay = suitableDay;
        else{
            crs.ScheduledDay = total_Scheduled_Day;
            total_Scheduled_Day++;
        }
    }

    public static void IncreaseSaturation(ArrayList<Course>courses)
    {
        courses.get(max_index).ScheduledDay = max_saturation_degree;
        if(max_saturation_degree == total_Scheduled_Day)
            total_Scheduled_Day++;
    }

    public static boolean check(int sz, Course c, ArrayList<Course> courses)
    {
        boolean checkGreater = sz > max_saturation_degree; // previous saturation < cur
        // tiebreaking by conflict crs size
        boolean checkEqual = sz == max_saturation_degree;
        boolean courseCheck = c.getOverlappingCourses().length >
                courses.get(max_index).getOverlappingCourses().length;
        return checkGreater || (checkEqual && courseCheck);
    }

    public static int Scheduler(ArrayList<Course> courses) {
        total_Scheduled_Day = 0;
        int[] DayOccupied;

        for(Course crs : courses) {
            Course[] overlappingCourses = crs.getOverlappingCourses();
            int sz = overlappingCourses.length;
            DayOccupied = new int[sz];
            int count = 0;
            suitableDay = 0;
            for(Course c : overlappingCourses)
            {
                DayOccupied[count] = c.ScheduledDay;
                count++;
            }

            Arrays.sort(DayOccupied);

            IncreaseDay(DayOccupied,crs);

            if(crs.ScheduledDay == -1) {
                SelectDay(crs);
            }
        }
        return total_Scheduled_Day;
    }
    public static int ScheduleByDSatur(ArrayList<Course> courses) {

        // Conflict wise sort - Greedy method
        Collections.sort(courses, new Comparators.DegreeComparator());

        total_Scheduled_Day = 1; // day 0 start for course.get(0)

        //current_unscheduled_vertex_with_conflict_crs
        HashSet<Integer> conflictCourses;
        HashSet<Integer> selected = null;

        courses.get(0).ScheduledDay = 0; //starts day count with 0

        int i = 1;
        int sz = courses.size();
        while (i < sz) {

            max_saturation_degree = -1;
            max_index= 0;
            count = 0;
          //  int a = 0;
            for (Course c : courses){
                if(c.ScheduledDay == -1) {
                    // unscheduled vertex denoted with -1
                  //  a++;
                    conflictCourses = new HashSet<>();
                    for(Course crs : c.getOverlappingCourses())
                        if(crs.ScheduledDay != -1) //scheduled crs with conflict -- calculating saturation
                            conflictCourses.add((crs.ScheduledDay)); //unique vertex - HashSet diye ensure korte hobe

                    boolean check = check(conflictCourses.size(),c,courses);
                    if(check) {
                        selected = conflictCourses;
                        max_saturation_degree = conflictCourses.size(); // colored conflict vertex er number
                        max_index = count; // at present jei node e asi tar number
                    }
                }
                count++;
            }
          //  System.out.println(a);

            max_saturation_degree = 0;

            while(true) {
                if(courses.get(max_index).ScheduledDay != -1)
                    break;
                if(selected.contains(max_saturation_degree))
                    max_saturation_degree++;
                else IncreaseSaturation(courses);
             //   System.out.println(max_saturation_degree);
            }
            //System.out.println("SET ER POR: " + max_saturation_degree);
            i++;
        }

       // System.out.println("SHESH");

        return total_Scheduled_Day;
    }
}
