package structure;
import java.io.*;
import java.util.ArrayList;

public class PrioritySearchTree {
    //TODO add the nodes here
    //the PST will be a heap with nodes in it.

    public PrioritySearchTree(String path) throws IOException {
        //Construction of the ArrayList that contains all the segments (tabs of Double)
        ArrayList<Double[]> segments = ConstructionArray(path);
        // at this point all the segments are in tabs inside the ArrayList.

        //First I have to find the segment with the lower x coordinate.
        // 1) I start by sorting the segments (x values)
        // 2) I can find the root (lower x coordinate)

        // ---> 1) I start by sorting the segments (x values and y values)
        segments = SortCoordinate(segments);
        // --- > 2) I can find the root (lower x coordinate AND y)
        int positionRoot = FindMinX(segments);
        System.out.println(segments.get(positionRoot)[0]); //TODO DELETE
    }

    public ArrayList<Double[]> ConstructionArray(String path) throws IOException {
        ArrayList<Double[]> segments = new ArrayList<Double[]>();
        String readed = "";
        int numberOfLines = 0;
        Double[] tab = null;
        String[] temp;
        BufferedReader br;
        FileReader fileR;
        fileR = new FileReader(path);
        br = new BufferedReader(fileR);
        while ((readed = br.readLine()) != null) {
            if (numberOfLines != 0) { // line 0 is the size of the window with all the segments inside
                tab = new Double[4];
                temp = readed.split(" ");
                for (int i = 0; i < 4; i++) {
                    tab[i] = Double.parseDouble(temp[i]);
                }
                segments.add(tab);
            }
            numberOfLines += 1;
        }
        return segments;
    }

    public ArrayList<Double[]> SortCoordinate(ArrayList<Double[]> pSegments) {
        for (Double[] tab : pSegments) {
            if (tab[0] > tab[1])
                InvertCoord(tab, 0, 1);
            if (tab[2] > tab[3])
                InvertCoord(tab, 2, 3);
        }
        return pSegments;
    }

    public void InvertCoord(Double[] pTab, int pCoord0, int pCoord1) { //TODO not sure about this method
        Double temp = pTab[pCoord0];
        pTab[pCoord0] = pTab[pCoord1];
        pTab[pCoord1] = temp;
    }

    public int FindMinX(ArrayList<Double[]> pSegments) {
            if (pSegments.isEmpty())
                return -1;
            else {
                double compare;
                int positionMinX = 0;
                double compareTo;
                int j = 0;
                for (int i=1; i<pSegments.size(); i++) {
                    compare = pSegments.get(j)[0];
                    compareTo = pSegments.get(i)[0];
                    if (compare > compareTo) { //TODO if the x is =, then look at coordinate y
                        positionMinX = i;
                        j = positionMinX;
                    }
                    else if (compare == compareTo) {
                        if () //TODO look at y here go to sleep now
                    }
                }
                return positionMinX;
            }
    }

    public static void main(String[] args) throws IOException {
        try {
            PrioritySearchTree prio = new PrioritySearchTree("/home/herosfate/Documents/Unif/2019-2020/BaB3" +
                    "/Quad1/Structures_de_donnees2/Project/windowing-sdd2/src/scenes/1000.txt");
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("rip");
        }
    }
}
