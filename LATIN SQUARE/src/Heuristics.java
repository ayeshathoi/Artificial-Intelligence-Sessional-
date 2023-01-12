import java.util.*;

public class Heuristics {

    static int minLength = 0;
    static Set<Map.Entry<String, ArrayList<Integer>>> entrySet;
    static  Map.Entry<String, ArrayList<Integer>>[] entryArray;


    private static int getCount(TreeMap<String, ArrayList<Integer>> square, int count, int i, String val) {
        String RowSearch;
        String ColSearch;
        char letter = val.charAt(0);
        RowSearch = Character.toString(letter) + i;
        ColSearch = (char) (65 + i) + val.substring(1);
        for (int j = 0; j < square.size(); j++) {
            if (square.containsKey(RowSearch) && !RowSearch.equals(val))
                count++;
            if(square.containsKey(ColSearch) && !ColSearch.equals(val))
                count++;
        }
        return count;
    }


    public static String Heuristic(TreeMap<String,ArrayList<Integer>> variableList,String SelectedHeuristic,
                                   ArrayList<String> unassignedList)
    {
        String nextItem = "";
        if(SelectedHeuristic.equals("VAH1"))
            nextItem = VAH1(variableList);
        else if(SelectedHeuristic.equals("VAH2"))
            nextItem = Heuristics.VAH2(variableList);
        else if(SelectedHeuristic.equals("VAH3"))
            nextItem = Heuristics.VAH3(variableList);
        else if(SelectedHeuristic.equals("VAH4"))
            nextItem = Heuristics.VAH4(variableList);
        else if(SelectedHeuristic.equals("VAH5"))
            nextItem = Heuristics.VAH5(unassignedList);

        return nextItem;
    }

    //------------------------------------------------SMALLEST DOMAIN  ------------------------------
    public static String VAH1(TreeMap<String, ArrayList<Integer>> square)
    {
        entrySet = square.entrySet();
        entryArray = entrySet.toArray(new Map.Entry[entrySet.size()]);

        int sz = entryArray.length;
        minLength = entryArray[0].getValue().size();
        ArrayList<Integer> SingleVariable;
        String returnString = entryArray[0].getKey();

        for (int i = 0; i < sz; i++)
        {
            SingleVariable = entryArray[i].getValue();

            if(SingleVariable.size() < minLength)
            {
                minLength = SingleVariable.size();
                returnString = entryArray[i].getKey();
            }
        }
        return returnString;
    }

    //------------------------------------------------MAX NEIGHBOUR ------------------------------
    public static String VAH2(TreeMap<String,ArrayList<Integer>> square)
    {
        entrySet = square.entrySet();
        entryArray = entrySet.toArray(new Map.Entry[entrySet.size()]);

        int count = 0;
        int max = Integer.MIN_VALUE;
        String returnString = "";

        for (int i = 0;i<square.size();i++) {
            String val = entryArray[i].getKey();
            count = getCount(square, count, i, val);

            if(count>max)
            {
                max = count;
                returnString = val;
            }
            count = 0;

        }

        return returnString;
    }


    //--------------------------------------------- tiebreaker -----------------------------------------
    public static String VAH3(TreeMap<String,ArrayList<Integer>> square)
    {
        TreeMap<String,ArrayList<Integer>> list = new TreeMap<>();
        entrySet = square.entrySet();
        entryArray = entrySet.toArray(new Map.Entry[entrySet.size()]);
        VAH1(square); // to determine the minimum domain size

        int sz = square.size();
        for(int i = 0;i< sz;i++)
        {
            if(entryArray[i].getValue().size() == minLength)
                list.put(entryArray[i].getKey(),entryArray[i].getValue());
        }
        String tiebreaker = VAH2(list);

        return tiebreaker;
    }


    //-----------------------minimizes VAH1/VAH2
    public static String VAH4(TreeMap<String,ArrayList<Integer>> square)
    {

        entrySet = square.entrySet();
        entryArray = entrySet.toArray(new Map.Entry[entrySet.size()]);

        int count = 0;
        double min = Double.MAX_VALUE;

        String returnString = entryArray[0].getKey();

        for (int i = 0;i<square.size();i++) {
            int cnt = entryArray[i].getValue().size();
            String val = entryArray[i].getKey();
            count = getCount(square, count, i, val);
            if(count!=0) {
                float compare = (float) (1.0 * cnt / count);
                if(compare < min)
                {
                    min = compare;
                    returnString = val;
                }
            }
            count = 0;

        }
        return returnString;

    }

    public static String VAH5(ArrayList<String> square){
        int max = square.size();
        Random random = new Random();
        int randomItem = random.nextInt(max);
        return square.get(randomItem);

    }



}
