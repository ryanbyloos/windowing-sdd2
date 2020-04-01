import org.junit.Assert;
import org.junit.Test;
import structure.PrioritySearchTree;

public class PrioritySearchTreeTest {
    PrioritySearchTree tree = new PrioritySearchTree("/home/herosfate/Documents/Unif/2019-2020/BaB3/Quad1/" +
            "Structures_de_donnees2/Project/windowing-sdd2/src/scenes/1000.txt");
    @Test
    public void conformityTest() {
        Assert.assertTrue(isConform(tree));
    }

    private boolean isConform(PrioritySearchTree pTree) {
        if (pTree.isLeaf())
            return true;
        boolean conform = true;
        PrioritySearchTree left = pTree.getLeftTree();
        PrioritySearchTree right = pTree.getRightTree();
        if (left!=null && right!=null){
            conform = left.getMedian()<=right.getMedian();
            conform&=isConform(left);
            conform&=isConform(right);
        }else if (left!=null)
            conform = isConform(left);
        else if (right!=null)
            conform = isConform(right);
        return conform;
    }
}
