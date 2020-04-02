package structure;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

// we have to create a new Windower each time we open a new txt file
public class Windower {
    private final Double[] windowSize;
    private Double[] searchWindow;
    private PrioritySearchTree split;

    // read the first line of the "path" file and put it into an array
    private Double[] buildArray(String path) {
        Double[] segments = new Double[4];
        String readed;
        int numberOfLines = 0;
        Double[] tab;
        String[] temp;
        LineNumberReader lnr;
        FileReader fileR;
        try {
            fileR = new FileReader(path);
            lnr = new LineNumberReader(fileR);
            String line = lnr.readLine();
            temp = line.split(" ");
            tab = new Double[4];
            for (int i = 0; i < 4; i++) {
                tab[i] = Double.parseDouble(temp[i]);
            }
            segments = tab;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return segments;
    }

    public Windower(String path) {
        windowSize = buildArray(path);
    }

    // PrioritySearchTree.Node because Node is an inner class
    public ArrayList<PrioritySearchTree.Node> reportInSubtree(PrioritySearchTree pRoot) {
        if (pRoot == null)
            return new ArrayList<>();
        PrioritySearchTree.Node root = pRoot.getRoot();
        ArrayList<PrioritySearchTree.Node> listOfNodes = new ArrayList<>();
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

    //TODO finish it
    public ArrayList<PrioritySearchTree.Node> queryPrioSearchTree(PrioritySearchTree pTree) {
        ArrayList<PrioritySearchTree.Node> splitPath = searchPath(pTree);
        ArrayList<PrioritySearchTree.Node> allPaths = new ArrayList<>(splitPath);
        // follow the path to find y and y' and on this path add the nodes if they're in the window
        // to find y, follow a path till find a leaf depending on the median. Each time the median >= y go to the left
        // for the recursive call and call reportInSubtree on the right subtree. Each time the median < y go to the
        // right for the recursive call and call reportInSubtree on the left subtree.
        // For y' its <= and >.
        ArrayList<PrioritySearchTree.Node> pathToY = pathToY(split.getLeftTree());
        ArrayList<PrioritySearchTree.Node> pathToYp = pathToYp(split.getRightTree());
        allPaths.addAll(pathToY);
        allPaths.addAll(pathToYp);
        return allPaths;
    }

    private ArrayList<PrioritySearchTree.Node> pathToY(PrioritySearchTree pTree) {
        ArrayList<PrioritySearchTree.Node> pathY = new ArrayList<>();
        if (pTree == null)
            return pathY;
        double median = pTree.getMedian();
        PrioritySearchTree.Node root = pTree.getRoot();
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
        }
        return pathY;
    }

    //TODO do the same for y' (juste change >= to <= and > to < and searchWindow[3] instead of 2
    private ArrayList<PrioritySearchTree.Node> pathToYp(PrioritySearchTree pTree) {
        ArrayList<PrioritySearchTree.Node> pathYp = new ArrayList<>();
        if (pTree == null)
            return pathYp;
        double median = pTree.getMedian();
        PrioritySearchTree.Node root = pTree.getRoot();
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
        }
        return pathYp;
    }

    private ArrayList<PrioritySearchTree.Node> searchPath(PrioritySearchTree pTree) {
        PrioritySearchTree.Node root = pTree.getRoot();
        ArrayList<PrioritySearchTree.Node> path = new ArrayList<>();
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
        return path;
    }

    // return true if the node or his link are in the window
    private boolean inWindow(PrioritySearchTree.Node pNode) {
        return pNode != null && pNode.inWindow(searchWindow);
    }
}
