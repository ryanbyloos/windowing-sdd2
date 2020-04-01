package structure;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PrioritySearchTree {
    //TODO add the nodes here
    //the PST will be a heap with nodes in it.
    private final Node root;
    private final PrioritySearchTree leftTree;
    private final PrioritySearchTree rightTree;
    private final double median;

    public PrioritySearchTree getLeftTree() {
        return leftTree;
    }

    public PrioritySearchTree getRightTree() {
        return rightTree;
    }

    public double getMedian() {
        return median;
    }

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

        public double getY() {
            return y;
        }

        public void setLink(Node pNode) {
            linkedTo = pNode;
        }
    }

    public PrioritySearchTree(String path) {
        // Construction of the ArrayList that contains all the segments (tabs of Double)
        ArrayList<Double[]> segments = buildArray(path);
        // at this point all the segments are in tabs inside the ArrayList.
        ArrayList<Node> nodes = new ArrayList<>();

        // First I have to find the segment with the lower x coordinate.
        // 1) I start by sorting the segments (x values)
        // 2) Create the nodes
        // 3) I can find the root (lower x coordinate)
        // 4) Sort the nodes with y coordinate
        // 5) Compute (find) the index of the median of the nodes' array
        // 6) Build the tree

        // ---> 1) I start by sorting the segments (x values and y values)
        segments = sortCoordinate(segments);
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
        int positionRootInSegments = findMinX(segments);
        int positionRootInNodes = 2 * positionRootInSegments;
        // ----------> 2 times because a segment is composed of 2 points and the smaller is always the first one.
        root = nodes.remove(positionRootInNodes);
        // ---> 4) Sort the nodes with y coordinate
        nodes.sort((node0, node1) -> {
            if (node0.y == node1.y)
                return 0;
            return (node0.y <= node1.y ? -1 : 1);
        });
        // the lambda expression create a comparator for the nodes. The minimum is going to be the first element of the
        // ArrayList
        // ---> 5) Compute (find) the index of the median of the nodes' array
        int nbrOfNodes = nodes.size();
        int indMedian = (nbrOfNodes % 2 == 0 ? nbrOfNodes / 2 - 1 : nbrOfNodes / 2);
        median = nodes.get(indMedian).getY();
        // ---> 6) Build the tree
        ArrayList<Node> nodesLeft = new ArrayList<>(nodes.subList(0, indMedian + 1));
        ArrayList<Node> nodesRight = new ArrayList<>(nodes.subList(indMedian + 1, nbrOfNodes));
        // +1 because subList to from i to j but j not included
        leftTree = new PrioritySearchTree(nodesLeft);
        rightTree = new PrioritySearchTree(nodesRight);

        System.out.println(positionRootInSegments); //TODO DELETE +2 because it's the line in the txt file (for check)
        System.out.println(root.x); //TODO DELETE
    }

    private PrioritySearchTree(ArrayList<Node> pNodes) {
        // 1) Find the root
        // 2) Compute (find) the index of the median of the nodes' array
        // 3) Build the tree

        // ---> 1) Find the root
        int positionRootInNodes = findRoot(pNodes);
        root = pNodes.remove(positionRootInNodes);
        // ---> 2) Compute (find) the index of the median of the nodes' array
        int nbrOfNodes = pNodes.size();
        int indMedian = (nbrOfNodes % 2 == 0 ? nbrOfNodes / 2 - 1 : nbrOfNodes / 2);
        median = pNodes.get(indMedian).getY();
        // ---> 3) Build the tree
        if (indMedian > 0) {
            ArrayList<Node> nodesLeft = new ArrayList<>(pNodes.subList(0, indMedian + 1));
            ArrayList<Node> nodesRight = new ArrayList<>(pNodes.subList(indMedian + 1, nbrOfNodes));
            // +1 because subList to from i to j but j not included
            leftTree = new PrioritySearchTree(nodesLeft);
            rightTree = new PrioritySearchTree(nodesRight);
        } else {
            if (indMedian == 0)
                leftTree = new PrioritySearchTree(pNodes.get(0));
            else
                leftTree = null;
            rightTree = null;
        }
    }

    private PrioritySearchTree(Node pNode) {
        root = pNode;
        leftTree = null;
        rightTree = null;
        median = root.getY();
    }

    public boolean isLeaf() {
        return leftTree == null && rightTree == null;
    }

    private ArrayList<Double[]> buildArray(String path) {
        ArrayList<Double[]> segments = new ArrayList<>();
        String readed;
        int numberOfLines = 0;
        Double[] tab;
        String[] temp;
        BufferedReader br;
        FileReader fileR;
        try {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return segments;
    }

    private ArrayList<Double[]> sortCoordinate(ArrayList<Double[]> pSegments) {
        for (Double[] tab : pSegments) {
            if (tab[0] > tab[2]) {
                invertCoord(tab, 0, 2);
                invertCoord(tab, 1, 3);
            }
        }
        return pSegments;
    }

    private void invertCoord(Double[] pTab, int pCoord0, int pCoord1) { //TODO not sure about this method
        Double temp = pTab[pCoord0];
        pTab[pCoord0] = pTab[pCoord1];
        pTab[pCoord1] = temp;
    }

    private int findRoot(ArrayList<Node> pNodes) {
        if (pNodes.isEmpty()) //TODO move condition to PrioritySearchTree
            return -1;
        else {
            double compare;
            int positionMinX = 0;
            double compareTo;
            int j = 0;
            for (int i = 1; i < pNodes.size(); i++) {
                compare = pNodes.get(j).getX();
                compareTo = pNodes.get(i).getX();
                if (compare > compareTo) {
                    positionMinX = i;
                    j = positionMinX;
                }
            }
            return positionMinX;
        }
    }

    private int findMinX(ArrayList<Double[]> pSegments) {
        if (pSegments.isEmpty()) //TODO move condition to PrioritySearchTree
            return -1;
        else {
            double compare;
            int positionMinX = 0;
            double compareTo;
            int j = 0;
            for (int i = 1; i < pSegments.size(); i++) {
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
}
