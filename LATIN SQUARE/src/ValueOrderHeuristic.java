import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

public class ValueOrderHeuristic {

    //-----------------------------------------------------------------------------------------

    public static ArrayList<Integer> leastConstraining(TreeMap<String, ArrayList<Integer>> square, String val, int sz) {

        ArrayList<Integer> allList = new ArrayList<>();
        ArrayList<Integer> current = square.get(val);
        allList.addAll(current);

        ArrayList<String> NeighbourList = Variable.FindOutNeighbour(val,sz);


        for (int i = 0; i < NeighbourList.size(); i++) {
            if (square.containsKey(NeighbourList.get(i)))
            {
                ArrayList<Integer> domainList = square.get(NeighbourList.get(i));
                for (int j = 0; j < domainList.size(); j++) {
                    int freq =  Collections.frequency(current,domainList.get(j));
                    if(freq > 0)
                    {
                        allList.add(domainList.get(j));
                    }
                }
            }
        }

        TreeMap<Integer, Integer> tree_map = new TreeMap<>();

        for (int i = 0; i < current.size(); i++) {
            tree_map.put(current.get(i),Collections.frequency(allList,current.get(i)));
        }

        Comparator<Integer> freqComp = new Comparator<Integer>(){
            @Override public int compare(Integer a, Integer b){
                if(tree_map.get(a) - tree_map.get(b) == 0)return a - b;
                return tree_map.get(a) - tree_map.get(b);
            }
        };

        Collections.sort(current, freqComp);

        return current;
    }



    //Random Order Heuristic
    public static ArrayList<Integer> RandomValueHeuristic(ArrayList<Integer> DomainList)
    {
        Collections.shuffle(DomainList);
        return DomainList;
    }

}
