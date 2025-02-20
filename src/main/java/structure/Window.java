package structure;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

// we have to create a new Windower each time we open a new txt file
public class Window {

    public final Double[] windowSize;
    public Double[] searchWindow;
    private PrioritySearchTree split;

    public Window(File file) {
        this.windowSize = buildArray(file);
        this.searchWindow = windowSize;
    }

    public Window(File file, Double[] searchWindow) {
        this.windowSize = buildArray(file);
        this.searchWindow = searchWindow;
    }

    /**
     * Read the first line of the file and put it into an array.
     * @param file The file with the segments.
     * @return An array with the coordinates of the window.
     */
    private Double[] buildArray(File file) {
        Double[] segments = new Double[4];
        try {
            FileReader fileReader = new FileReader(file);
            LineNumberReader lnr = new LineNumberReader(fileReader);
            String line = lnr.readLine();
            String[] temp = line.split(" ");
            for (int i = 0; i < 4; i++)
                segments[i] = Double.parseDouble(temp[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return segments;
    }

    /**
     * follow the path to find y and y' and on this path add the nodes if they're in the window
     * to find y, follow a path till find a leaf depending on the median. Each time the median >= y go to the left
     * for the recursive call and call reportInSubtree on the right subtree. Each time the median < y go to the
     * right for the recursive call and call reportInSubtree on the left subtree.
     * For y' its <= and >.
     * @param pTree A PST.
     * @return An ArrayList with the nodes that are in the window.
     */
    public ArrayList<PSTNode> queryPST(PrioritySearchTree pTree) {
        ArrayList<PSTNode> splitPath = searchPath(pTree);
        ArrayList<PSTNode> allPaths = new ArrayList<>(splitPath);
        ArrayList<PSTNode> pathToY = pathToY(split.getLeftTree());
        ArrayList<PSTNode> pathToYp = pathToYp(split.getRightTree());
        allPaths.addAll(pathToY);
        allPaths.addAll(pathToYp);
        return allPaths;
    }

    /**
     * Find the path to the node where the path splits.
     * @param pTree A PST
     * @return An ArrayList with to nodes to the "split" point.
     */
    private ArrayList<PSTNode> searchPath(PrioritySearchTree pTree) {
        PSTNode root = pTree.getRoot();
        ArrayList<PSTNode> path = new ArrayList<>();
        if (inWindow(root))
            path.add(root);
        if (!pTree.isLeaf()) {
            if (pTree.getMedian() > searchWindow[3])
                path.addAll(searchPath(pTree.getLeftTree()));
            else if (pTree.getMedian() < searchWindow[2])
                path.addAll(searchPath(pTree.getRightTree()));
            else
                split = pTree;
        }
        if (pTree.isLeaf())
            split = pTree;
        return path;
    }

    /**
     * Find the path to the node that is on the very left in the window.
     * @param pTree A PST
     * @return The path to the node that is the most on the "left" in the window.
     */
    private ArrayList<PSTNode> pathToY(PrioritySearchTree pTree) {
        ArrayList<PSTNode> pathY = new ArrayList<>();
        if (pTree == null)
            return pathY;
        double median = pTree.getMedian();
        PSTNode root = pTree.getRoot();
        if (inWindow(root))
            pathY.add(root);
        if (pTree.isLeaf())
            return pathY;
        if (median >= searchWindow[2]) {
            pathY.addAll(pathToY(pTree.getLeftTree()));
            pathY.addAll(reportInSubtree(pTree.getRightTree()));
        }
        else {
            pathY.addAll(pathToY(pTree.getRightTree()));
            pathY.addAll(reportInSubtree(pTree.getLeftTree()));
            // We add the ones on the right to find those who cut the window.
        }
        return pathY;
    }

    /**
     * @param pTree A PST
     * @return The same as pathToY but on the right.
     */
    private ArrayList<PSTNode> pathToYp(PrioritySearchTree pTree) {
        ArrayList<PSTNode> pathYp = new ArrayList<>();
        if (pTree == null)
            return pathYp;
        double median = pTree.getMedian();
        PSTNode root = pTree.getRoot();
        if (inWindow(root))
            pathYp.add(root);
        if (pTree.isLeaf())
            return pathYp;
        if (median <= searchWindow[3]) {
            pathYp.addAll(pathToY(pTree.getRightTree()));
            pathYp.addAll(reportInSubtree(pTree.getLeftTree()));
        }
        else {
            pathYp.addAll(pathToY(pTree.getLeftTree()));
            pathYp.addAll(reportInSubtree(pTree.getRightTree()));
            // We add the ones on the right to find those who cut the window.
        }
        return pathYp;
    }

    /**
     * @param pRoot A PST
     * @return All the nodes on the tree.
     */
    public ArrayList<PSTNode> reportInSubtree(PrioritySearchTree pRoot) {
        if (pRoot == null)
            return new ArrayList<>();
        PSTNode root = pRoot.getRoot();
        ArrayList<PSTNode> listOfNodes = new ArrayList<>();
        if (root.getX() > searchWindow[1])
            // I can return the array now because if the x of the link is smaller it may have already been added
            return listOfNodes;
        if (inWindow(root))
            listOfNodes.add(root);
        if (!pRoot.isLeaf()) {
            listOfNodes.addAll(reportInSubtree(pRoot.getLeftTree()));
            listOfNodes.addAll(reportInSubtree(pRoot.getRightTree()));
        }
        return listOfNodes;
    }

    /**
     * @param pNode A node
     */
    // return true if the node or his link are in the window
    private boolean inWindow(PSTNode pNode) {
        return pNode != null && pNode.inWindow(searchWindow);
    }
}
