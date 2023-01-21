import java.util.*;
import java.io.*;

public class Main {

    private static void runSchemeOn(String fileName, String ch,int type) throws IOException {
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        File crsFile = new File("input/"+fileName+".crs");
        File stuFile = new File("input/"+fileName+".stu");
        File outFile = new File("output/"+fileName+".sol");
        Scanner scanner = new Scanner(crsFile);
        Scanner students_scanner = new Scanner(stuFile);
        StringBuilder stringBuilder = new StringBuilder();

        int[][] conflictMatrix; // Graph where CrsTaken by student is connected by edge
        FileWriter fileWriter = new FileWriter(outFile);

        //-----------------------------------------------------------------------------------------

        while(scanner.hasNextLine()) {
            String[] tempString = scanner.nextLine().split(" ");
            String crsId = tempString[0];
            int studentNumber = Integer.parseInt(tempString[1]);
            Course crs = new Course(crsId,studentNumber);
            courses.add(crs);
        }
        scanner.close();

        //------------------------------------------------------------------------------------------

        // initialize the graph by the vertex (course)
        int crs_sz = courses.size();
        conflictMatrix = new int[crs_sz][crs_sz];
        for(int i=0; i<crs_sz; i++)
            for(int j=0; j<crs_sz; j++)
                conflictMatrix[i][j] = 0;

        //-------------------------------------------------------------------------------------------

        while(students_scanner.hasNextLine()) {
            String[] inputString = students_scanner.nextLine().split(" ");
            int[] inputInteger = new int[inputString.length];

            int count = 0;
            for(String s : inputString)
            {
                inputInteger[count] = Integer.parseInt(s);
                count++;
            }

            ArrayList<Course> takenCrs = new ArrayList<>();

            for(int num : inputInteger)
                takenCrs.add(courses.get(num-1));

            Student student = new Student(takenCrs,ch);
            students.add(student);

        //--------------------------------------------------------------------------------------
            //student's enrolled crs is connected by edge to apply soft condition - more possible gap
            int sz = inputInteger.length;
            for(int i=0; i<sz-1; i++) {
                for(int j=i+1; j<sz; j++) {
                    int edgeF = inputInteger[i]-1;
                    int edgeS = inputInteger[j]-1;
                    if(conflictMatrix[edgeF][edgeS] == 0) {
                        conflictMatrix[edgeF][edgeS] = 1;
                        conflictMatrix[edgeS][edgeF] = 1;
                    }
                }
            }
        }

        students_scanner.close();
        //--------------------------------------------------------------------------------------
        // crs whose has a common student in between
        for(int i=0; i<crs_sz; i++)
            for(int j=0; j<crs_sz; j++)
                if(conflictMatrix[i][j] == 1)
                    courses.get(i).setOverLappingCourses(courses.get(j));

        //--------------------------------------------------------------------------------------

        stringBuilder.append(fileName);
        stringBuilder.append("\nTotal Scheduled Day: ");
        //-------------------------------------------------
        if(type == 1)
            stringBuilder.append(Comparators.ScheduleByLargestDegree(courses));
        else if(type == 2)
            stringBuilder.append(Constructive.ScheduleByDSatur(courses));
        else if(type == 3)
            stringBuilder.append(Comparators.ScheduleByLargestEnrollment(courses));
        else if(type == 4)
            stringBuilder.append(Comparators.RandomSchedule(courses));

        //-------------------------------------------------

        stringBuilder.append("\nAfter Constructive, Penalty : ");
        stringBuilder.append(Student.calculateAveragePenalty(students));

        //-------------------------------------------------

        //penalty Reduction steps
        stringBuilder.append("\nAfter Kempe Penalty Reduction: ");
        new Perturbative(courses,students);
        Perturbative.PenaltyReductionbyKempe();// kempe First
        stringBuilder.append(Student.calculateAveragePenalty(students));

        //---------------------------------------------------------
        stringBuilder.append("\nAfter PairSwap Penalty Reduction: ");
        Perturbative.PenaltyReductionPairSwap(); //PairWise 2nd
        stringBuilder.append(Student.calculateAveragePenalty(students));
        //-------------------------------------------------
        System.out.println(stringBuilder);
        //-------------------------------------------------

        // Day-wise sort kore file e write kortesi
        Collections.sort(courses, new Comparators.ScheduleComparator());
        for(Course crs : courses)
            fileWriter.append(crs.courseID + " "  + crs.ScheduledDay +"\n");

        fileWriter.close();
    }

    public static void main(String[] args) throws IOException {
        Scanner choice = new Scanner(System.in);

        while(true) {
            System.out.println("Want to Quit? : Press 5");
            System.out.println("OR Press 2 to get Linear Penalty Calculation\notherwise you will get Exponential Penalty Calculation");
            String strChoice = "Exponential";

            int ch = choice.nextInt();
            if(ch == 5) {
                System.out.println("Successfully Ended");
                return;
            }
            if (ch == 2) {
                strChoice = "Linear";
                System.out.println("You chose Linear Penalty Calculation");
            } else System.out.println("You chose Exponential Penalty Calculation");

            System.out.println("Press 1 to get LargestDegree\n" +
                    "2 to get DsaturAlgo\n" +
                    "3 to get LargestEnrollment\n" +
                    "4 to get Random");

            int num = choice.nextInt();

            if (num == 1)
                System.out.println("Largest Degree Heuristic Calculation");
            else if (num == 2)
                System.out.println("Dsatur Algo Heuristic Calculation");
            else if (num == 3)
                System.out.println("Largest Enrollment Heuristic Calculation");
            else if (num == 4)
                System.out.println("Random Heuristic Calculation");
            else {
                System.out.println("No mentioned heuristic Selected\nPlease try Again");
                return;
            }
            ArrayList<String> fileName = new ArrayList<>();
            fileName.add("car-s-91");
            fileName.add("car-f-92");
            fileName.add("tre-s-92");
            fileName.add("yor-f-83");
            fileName.add("kfu-s-93");

            for (String str : fileName)
                runSchemeOn(str, strChoice, num);
        }
    }
}
