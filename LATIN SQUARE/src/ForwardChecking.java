import java.util.*;

public class ForwardChecking {

    static int totalNodes = 0;
    static int BackTrackNodes = 0;
    static int count = 0;
    static ArrayList<String> VariableName ;
    static ArrayList<String> unassignedList;
    static TreeMap<String,ArrayList<Integer>> variableList;
    //----------------------------------Forward Checking--------------------------------------


    public static ArrayList<Integer> findPlacables(int[][] square, int row, int column) {
        //FINDING CSP DOMAIN FOR A VARIABLE
        ArrayList<Integer> Domain = new ArrayList<>();

        for (int i = 1; i <= square.length; i++) {
            if (CSP_SOLVER.canPlace(square, row, column, i))
                Domain.add(i);
        }
        return Domain;
    }

    public static boolean check(int[][] latinSquare)
    {
        VariableName = Variable.variableNaming(latinSquare.length);
        variableList = new TreeMap<>();
        unassignedList = new ArrayList<>();
        int k = 0;
        count = 0;

        // ------------------------DOMAIN ASSIGNED--------------------
        for (int i = 0; i < latinSquare.length; i++) {
            for (int j = 0; j < latinSquare.length; j++) {

                if (latinSquare[i][j] == 0) {
                    unassignedList.add(VariableName.get(k));
                    ArrayList<Integer> SingleVariableDomain = ForwardChecking.findPlacables(latinSquare, i, j);
                    if (SingleVariableDomain.size() == 0)
                        return false ;
                    else variableList.put(VariableName.get(k), SingleVariableDomain);
                    count++;
                }
                k++;
            }
        }
        return true;
    }


    public static boolean solveForwardChecking(int[][] latinSquare, String SelectedHeuristic) {

        totalNodes++;

        boolean st = check(latinSquare);

        if(!st) {
            BackTrackNodes++;
            return false;
        }
        if (count == 0) {
            CSP_SOLVER.print(latinSquare,totalNodes,BackTrackNodes,SelectedHeuristic,"ForwardChecking");
            totalNodes = 0;
            BackTrackNodes = 0;
            return true;
        }

        //------------------------------------------
        String nextItem = Heuristics.Heuristic(variableList,SelectedHeuristic,unassignedList);

        int row = (int) nextItem.charAt(0) - 65;
        int col = Integer.parseInt(nextItem.substring(1));

        ArrayList<Integer> domain = variableList.get(nextItem);
        domain = ValueOrderHeuristic.RandomValueHeuristic(domain);
        if(domain.size()==0)
            BackTrackNodes++;


        //ArrayList<Integer> leastOrderVal = ValueOrderHeuristic.leastConstraining(variableList, nextItem, latinSquare.length);

        for (int i = 0; i < domain.size(); i++) {
            //latinSquare[row][col] = leastOrderVal.get(i);
            latinSquare[row][col] = domain.get(i);
            if(solveForwardChecking(latinSquare,SelectedHeuristic))
            {
                return true;
            }

            else {
                latinSquare[row][col] = 0;
            }
        }
        return false;
    }
}



