import java.util.ArrayList;
import java.util.Collections;

public class Student {

    private final ArrayList<Course> CourseTaken;
    private final String Choice;
    public static double avgPenalty;
    public static double total_penalty;

    public Student(ArrayList<Course> crs, String choice) {
        this.CourseTaken = crs;
        this.Choice = choice;
    }

    public double calculateTotalPenalty() {

        //Exam Day sorting - to understand the gap between nearest exams
        Collections.sort(CourseTaken, new Comparators.ScheduleComparator());

        total_penalty = 0;
        int sz = CourseTaken.size();

        int i = 0;
        while(i<sz-1){
            int j = i+1;
            //count starts from next day - j = i+1
            while(j < sz)
            {
                int gap_Between_Nearest_Exam = CourseTaken.get(j).ScheduledDay - CourseTaken.get(i).ScheduledDay;
              //  if(gap_Between_Nearest_Exam == 0) System.err.println("Error");
                if(gap_Between_Nearest_Exam <= 5 ) {
                    if(this.Choice.equalsIgnoreCase("Exponential"))
                        total_penalty += Math.pow(2, 5-gap_Between_Nearest_Exam);
                    else if(this.Choice.equalsIgnoreCase("Linear"))
                        total_penalty += 2*(5-gap_Between_Nearest_Exam);
                }

                j++;
            }
            i++;
        }
        return total_penalty;
    }

    public static double calculateAveragePenalty(ArrayList<Student> students) {
        double penalty  = 0;
        for(Student student : students)
            penalty += student.calculateTotalPenalty();
        avgPenalty = penalty/students.size();
        return avgPenalty;
    }
}
