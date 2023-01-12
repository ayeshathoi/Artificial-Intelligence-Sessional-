import java.util.ArrayList;
import java.util.TreeMap;

public class SimpleBackTracking {

    static int BackTrackNodes = 0;
    static int totalNodes = 0;
    static TreeMap<String, ArrayList<Integer>> variableList = new TreeMap<>();
    static ArrayList<String> unassignedList = new ArrayList<>();
    static ArrayList<String> VariableName = new ArrayList<>();
    static int count = 0;
    static int k = 0;

    public static boolean HeuristicApply(int[][] LatinSquare)
    {
        VariableName = Variable.variableNaming(LatinSquare.length);
        unassignedList = new ArrayList<>();
        variableList = new TreeMap<>();
        count = 0;
        k = 0;
        for (int i = 0; i < LatinSquare.length; i++) {
            for (int j = 0; j < LatinSquare.length; j++) {

                if (LatinSquare[i][j] == 0) {
                    unassignedList.add(VariableName.get(k));
                    ArrayList<Integer> SingleVariableDomain = ForwardChecking.findPlacables(LatinSquare, i, j);
                    if (SingleVariableDomain.size() == 0)
                    {
                        return false;
                    }
                    else variableList.put(VariableName.get(k), SingleVariableDomain);
                    count++;
                }
                k++;
            }
        }
        return true;
    }

    public static boolean BackTracking (int[][] LatinSquare,String SelectedHeuristic)
    {
        totalNodes++;

        boolean status = HeuristicApply(LatinSquare);

        if(!status)
        {
            BackTrackNodes++;
            return false;
        }

        if(count == 0) {
            CSP_SOLVER.print(LatinSquare,totalNodes,BackTrackNodes,SelectedHeuristic,"BACKTRACKING");
            BackTrackNodes = 0;
            totalNodes = 0;
            return true;
        }

        String nextItem = Heuristics.Heuristic(variableList,SelectedHeuristic,unassignedList);

        int row = nextItem.charAt(0) - 65;
        int col = nextItem.charAt(1) - 48;


        //backTrack
        for(int i = 1 ; i<=LatinSquare.length ;i++)
        {
            boolean solve = CSP_SOLVER.canPlace(LatinSquare,row,col,i);
            if(solve)
            {
                LatinSquare[row][col] = i;
                boolean stat = BackTracking(LatinSquare,SelectedHeuristic);
                if(stat)
                    return true;
                LatinSquare[row][col] = 0;
            }
            else
            {
                totalNodes++;
                BackTrackNodes++;
            }

        }

        return false;
    }

}
