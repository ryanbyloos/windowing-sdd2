package structure;
import java.io.*;
import java.util.ArrayList;

public class PrioritySearchTree {
    //TODO add the nodes here
    //the PST will be a heap with nodes in it.
    private final Node root;

    public class Node {
        private final double x;
        private final double y;
        private Node linkedTo;

        public Node(Double pX, Double pY) {
            x = pX;
            y = pY;
        }

        public double getX() {
            return x;
        }

        public void setLink(Node pNode) {
            linkedTo = pNode;
        }

    }

    public PrioritySearchTree(String path) throws IOException {
        //Construction of the ArrayList that contains all the segments (tabs of Double)
        ArrayList<Double[]> segments = ConstructionArray(path);
        // at this point all the segments are in tabs inside the ArrayList.
        ArrayList<Node> nodes = new ArrayList<Node>();

        //First I have to find the segment with the lower x coordinate.
        // 1) I start by sorting the segments (x values)
        // 2) Create the nodes
        // 3) I can find the root (lower x coordinate)

        // ---> 1) I start by sorting the segments (x values and y values)
        segments = SortCoordinate(segments);
        // ---> 2) Creation of the nodes
        for (Double[] tab : segments) {
            Node node0 = new Node(tab[0], tab[1]);
            Node node1 = new Node(tab[2], tab[3]);
            node0.setLink(node1);
            node1.setLink(node0);
            nodes.add(node0);
            nodes.add(node1);
        }
        // ---> 3) I can find the root (lower x coordinate)
        int positionRootInSegments = FindMinX(segments);
        int positionRootInNodes = 2*positionRootInSegments;
        // ----------> 2 times because a segment is composed of 2 points and the smaller is always the first one.
        root = nodes.remove(positionRootInNodes);

        System.out.println(positionRootInSegments); //TODO DELETE +2 because it's the line in the txt file (for check)
        System.out.println(root.x); //TODO DELETE
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
            if (tab[0] > tab[2]) {
                InvertCoord(tab, 0, 2);
                InvertCoord(tab, 1, 3);
            }
        }
        return pSegments;
    }

    public void InvertCoord(Double[] pTab, int pCoord0, int pCoord1) { //TODO not sure about this method
        Double temp = pTab[pCoord0];
        pTab[pCoord0] = pTab[pCoord1];
        pTab[pCoord1] = temp;
    }

    public int FindMinX(ArrayList<Double[]> pSegments) {
        if (pSegments.isEmpty()) //TODO throw exception instead
            return -1;
        else {
            double compare;
            int positionMinX = 0;
            double compareTo;
            int j = 0;
            for (int i=1; i<pSegments.size(); i++) {
                compare = pSegments.get(j)[0];
                compareTo = pSegments.get(i)[0];
                if (compare > compareTo) {
                    positionMinX = i;
                    j = positionMinX;
                }
            }
            return positionMinX;
        }
    }

    public static void main(String[] args) {
        try {
            PrioritySearchTree prio = new PrioritySearchTree("/home/herosfate/Documents/Unif/2019-2020/BaB3" +
                    "/Quad1/Structures_de_donnees2/Project/windowing-sdd2/src/scenes/1000.txt");
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("rip");
        }
    }
}
