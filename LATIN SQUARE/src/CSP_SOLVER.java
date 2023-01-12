import java.io.File;
import java.io.InputStream;
import java.util.*;;

public class CSP_SOLVER {
    //---------------------------------------------------------------------------------------

    public static boolean canPlace(int[][] square, int row, int column, int num) {
        for (int i = 0; i < square.length; i++) {
            if (square[row][i] == num || square[i][column] == num)
                return false;
        }

        return true;
    }



    public static int[][] ArrayCopy (int[][] arr, int [][] arrcpy)
    {
        for(int i = 0; i<arr.length;i++)
        {
            for(int j = 0; j< arr.length;j++)
            {
                arrcpy[i][j] = arr[i][j];
            }
        }
        return arrcpy;
    }



    public static void print(int[][] latinsquareSolution, int totalnodes, int back,String SelectedHeuristic,String process)
    {
        System.out.println("------------------------------------------------");
        if(totalnodes > 0) {
            System.out.println("Chosen Solver " + process);
            System.out.println("TOTAL NODE FOR " + SelectedHeuristic + " : " + totalnodes);
            System.out.println("TOTAL BackTrack NODE " + SelectedHeuristic + " : " + back);
        }
        for(int i = 0;i< latinsquareSolution.length; i++)
        {
            for(int j = 0;j<latinsquareSolution.length;j++)
            {
                System.out.print(latinsquareSolution[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("------------------------------------------------");
    }

    public static void main(String []args) {


        ArrayList<Integer> list = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int [][] latinsquare = new int [num][num];

        //Some Value Pre-assigned
        for(int i = 0;i< num; i++)
        {
            for(int j = 0;j<num;j++)
            {
                latinsquare[i][j] = sc.nextInt();
                list.add(latinsquare[i][j]);
            }
        }

        ArrayList<String> HeuristicList = new ArrayList<>();
        HeuristicList.add("VAH1");
        HeuristicList.add("VAH2");
        HeuristicList.add("VAH3");
        HeuristicList.add("VAH4");
        HeuristicList.add("VAH5");

        double sec = 1000;

        int[][] arrCpy = new int[latinsquare.length][latinsquare.length];

        for (String Heuristic : HeuristicList)
        {
            CSP_SOLVER.ArrayCopy(latinsquare,arrCpy);
            long start = System.currentTimeMillis();
            ForwardChecking.solveForwardChecking(latinsquare, Heuristic);
            long end = System.currentTimeMillis();
            System.out.println("TIME TAKEN for " + Heuristic+ " " + (end-start)/sec + " sec ");
            CSP_SOLVER.ArrayCopy(arrCpy,latinsquare);
        }

        for (String Heuristic : HeuristicList)
        {
            CSP_SOLVER.ArrayCopy(latinsquare,arrCpy);
            long start = System.currentTimeMillis();
            SimpleBackTracking.BackTracking(latinsquare,Heuristic);
            long end = System.currentTimeMillis();
            System.out.println("TIME TAKEN for " + Heuristic+ " " + (end-start)/sec + " sec ");
            CSP_SOLVER.ArrayCopy(arrCpy,latinsquare);
        }
    }

}