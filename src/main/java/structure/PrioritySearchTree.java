package structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PrioritySearchTree {
    //the PST will be a heap with nodes in it.
    private final PSTNode root;
    private final PrioritySearchTree leftTree;
    private final PrioritySearchTree rightTree;
    private final double median;

    /**
     * 1) I start by sorting the segments (x values)
     * 2) Create the nodes
     * 3) I can find the root (lower x coordinate)
     * 4) Sort the nodes with y coordinate
     * 5) Compute (find) the index of the median of the nodes' array
     * 6) Build the tree
     * First constructor. "Initialise" the PST construction
     * @param file The file readed with the nodes in it
     */
    public PrioritySearchTree(File file) {
        // Construction of the ArrayList that contains all the segments (tabs of Double)
        ArrayList<Double[]> segments = buildArray(file);
        // at this point all the segments are in tabs inside the ArrayList.
        ArrayList<PSTNode> nodes = new ArrayList<>();
        /*
        This step has been removed for improvement
         ---> 1) I start by sorting the segments (x values and y values)
         segments = sortCoordinate(segments);
         */
        // ---> 2) Creation of the nodes
        for (Double[] tab : segments) {
            PSTNode node0 = new PSTNode(tab[0], tab[1]);
            PSTNode node1 = new PSTNode(tab[2], tab[3]);
            node0.setLink(node1);
            node1.setLink(node0);
            nodes.add(node0);
            nodes.add(node1);
        }
        // ---> 3) I can find the root (lower x coordinate)
        int positionRootInNodes = findRoot(nodes);
        root = nodes.remove(positionRootInNodes);
        // ---> 4) Sort the nodes with y coordinate
        nodes.sort((node0, node1) -> {
            if (node0.getY() == node1.getY())
                return 0;
            return (node0.getY() <= node1.getY() ? -1 : 1);
        });
        // the lambda expression create a comparator for the nodes. The minimum is going to be the first element of the
        // ArrayList
        // ---> 5) Compute (find) the index of the median of the nodes' array
        int nbrOfNodes = nodes.size();
        int indMedian = (nbrOfNodes % 2 == 0 ? nbrOfNodes / 2 - 1 : nbrOfNodes / 2);
        median = nodes.get(indMedian).getY();
        // ---> 6) Build the tree
        ArrayList<PSTNode> nodesLeft = new ArrayList<>(nodes.subList(0, indMedian + 1));
        ArrayList<PSTNode> nodesRight = new ArrayList<>(nodes.subList(indMedian + 1, nbrOfNodes));
        // +1 because subList to from i to j but j not included
        leftTree = new PrioritySearchTree(nodesLeft);
        rightTree = new PrioritySearchTree(nodesRight);
    }

    /**
     * 1) Find the root
     * 2) Compute (find) the index of the median of the nodes' array
     * 3) Build the tree
     * Second part of the PST constuction. Used using an ArrayList instead of a File.
     * @param pNodes ArrayList with all the nodes in it
     */
    private PrioritySearchTree(ArrayList<PSTNode> pNodes) {
        // ---> 1) Find the root
        int positionRootInNodes = findRoot(pNodes);
        root = pNodes.remove(positionRootInNodes);
        // ---> 2) Compute (find) the index of the median of the nodes' array
        int nbrOfNodes = pNodes.size();
        int indMedian = (nbrOfNodes % 2 == 0 ? nbrOfNodes / 2 - 1 : nbrOfNodes / 2);
        // ---> 3) Build the tree
        if (indMedian > 0) {
            ArrayList<PSTNode> nodesLeft = new ArrayList<>(pNodes.subList(0, indMedian + 1));
            ArrayList<PSTNode> nodesRight = new ArrayList<>(pNodes.subList(indMedian + 1, nbrOfNodes));
            // +1 because subList to from i to j but j not included
            leftTree = new PrioritySearchTree(nodesLeft);
            rightTree = new PrioritySearchTree(nodesRight);
            median = pNodes.get(indMedian).getY();
        }
        else {
            if (indMedian == 0) {
                leftTree = new PrioritySearchTree(pNodes.get(0));
                median = pNodes.get(indMedian).getY();
            }
            else {
                leftTree = null;
                median = root.getY();
            }
            rightTree = null;
        }
    }

    /**
     * Last part of the initialization. Used for add leaves.
     * @param pNode A Node object.
     */
    private PrioritySearchTree(PSTNode pNode) {
        root = pNode;
        leftTree = null;
        rightTree = null;
        median = root.getY();
    }

    public PrioritySearchTree getLeftTree() {
        return leftTree;
    }

    public PrioritySearchTree getRightTree() {
        return rightTree;
    }

    public double getMedian() {
        return median;
    }

    public PSTNode getRoot() {
        return root;
    }

    /**
     * @return True if the current "PST" is a leaf, False otherwise.
     */
    public boolean isLeaf() {
        return leftTree == null && rightTree == null;
    }

    /**
     * @param file The file with the segments.
     * @return An ArrayList with all the segments.
     */
    private ArrayList<Double[]> buildArray(File file) {
        ArrayList<Double[]> segments = new ArrayList<>();
        String readed;
        int numberOfLines = 0;
        Double[] tab;
        String[] temp;
        BufferedReader br;
        FileReader fileR;
        try {
            fileR = new FileReader(file);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return segments;
    }

    @Deprecated
    /**
     * Sort the segments. With x < x' then invert the 2 points.
     * Used to find the minimum x value.
     * @param pSegments The ArrayList with all the segments.
     * @return An ArrayList with the segments sorted.
     * @deprecated
     */
    private ArrayList<Double[]> sortCoordinate(ArrayList<Double[]> pSegments) {
        for (Double[] tab : pSegments) {
            if (tab[0] > tab[2]) {
                invertCoord(tab, 0, 2);
                invertCoord(tab, 1, 3);
            }
        }
        return pSegments;
    }

    @Deprecated
    /**
     * Invert 2 points in a tab.
     * @param pTab The tab with the four points.
     * @param pCoord0 The first coordinate to invert.
     * @param pCoord1 The second coordinate to invert.
     * @deprecated
     */
    private void invertCoord(Double[] pTab, int pCoord0, int pCoord1) {
        Double temp = pTab[pCoord0];
        pTab[pCoord0] = pTab[pCoord1];
        pTab[pCoord1] = temp;
    }

    /**
     * Find the root in an ArrayList of Nodes.
     * @param pNodes The ArrayList with all the nodes.
     * @return The node with the minimum x value.
     */
    private int findRoot(ArrayList<PSTNode> pNodes) {
        if (pNodes.isEmpty()) // not supposed to happen
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

    /**
     * Find the position of the point with the minimum x value on the segment's array.
     * @param pSegments An ArrayList with all the segments.
     * @return The position of the point with the minimum x value.
     */
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
