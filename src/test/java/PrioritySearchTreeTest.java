import org.junit.Assert;
import org.junit.Test;
import structure.PrioritySearchTree;

import java.io.File;

public class PrioritySearchTreeTest {
    PrioritySearchTree tree1k = new PrioritySearchTree(new File("src/scenes/1000.txt"));
    PrioritySearchTree tree5k = new PrioritySearchTree(new File("src/scenes/5000.txt"));
    PrioritySearchTree tree10k = new PrioritySearchTree(new File("src/scenes/10000.txt"));
    PrioritySearchTree tree100k = new PrioritySearchTree(new File("src/scenes/100000.txt"));

    @Test
    public void conformityTest() {
        Assert.assertTrue(isConform(tree1k));
        Assert.assertTrue(isConform(tree5k));
        Assert.assertTrue(isConform(tree10k));
        Assert.assertTrue(isConform(tree100k));
    }

    private boolean isConform(PrioritySearchTree pTree) {
        if (pTree.isLeaf())
            return true;
        boolean conform = true;
        PrioritySearchTree left = pTree.getLeftTree();
        PrioritySearchTree right = pTree.getRightTree();
        if (left != null && right != null) {
            conform = left.getMedian() <= right.getMedian();
            if (!conform)
                System.out.println(left.getMedian() + " " + right.getMedian());
            conform = conform && isConform(left) && isConform(right);
        }
        else if (left != null)
            conform = isConform(left);
        else if (right != null)
            conform = isConform(right);
        return conform;
    }
}
