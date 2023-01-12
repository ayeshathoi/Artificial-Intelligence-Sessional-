import java.util.*;
public class Variable {
    public static ArrayList<String> variableNaming (int size)
    {
        ArrayList<String> Variable = new ArrayList<>();
        String var = "";

        for(int i = 0;i<size;i++)
        {
            for(int j = 0;j<size;j++)
            {
                char letter = (char) ((char) 65+i);
                var = Character.toString(letter) + j;
                Variable.add(var);
            }
        }
        return Variable;
    }

    public static ArrayList<String> FindOutNeighbour (String val,int size)
    {
        char rowInitial = val.charAt(0);
        String RowSearch = "";
        String ColSearch = "";

        ArrayList<String> Neighbour = new ArrayList<>();

        int columnNum = Integer.parseInt(val.substring(1));

        for(int i = 0;i<size;i++)
        {
                RowSearch = Character.toString(rowInitial) + i;
                if(!RowSearch.equals(val))
                    Neighbour.add(RowSearch);
                ColSearch = Character.toString((char) (65 + i)) + columnNum;
                if(!ColSearch.equals(val))
                    Neighbour.add(ColSearch);
        }
        return Neighbour;
    }


}
