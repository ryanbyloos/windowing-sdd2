package structure;

public class PSTNode {
    private final double x;
    private final double y;
    private PSTNode linkedTo;

    public PSTNode(Double pX, Double pY) {
        x = pX;
        y = pY;
    }

    public PSTNode getLinkedTo() {
        return linkedTo;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setLink(PSTNode pNode) {
        linkedTo = pNode;
    }

    public boolean inWindow(Double[] pWindow) {
        boolean xIn = x >= pWindow[0] && x <= pWindow[1];
        boolean yIn = y >= pWindow[2] && y <= pWindow[3];
        boolean lxIn = linkedTo.getX() >= pWindow[0] && linkedTo.getX() <= pWindow[1];
        boolean lyIn = linkedTo.getY() >= pWindow[2] && linkedTo.getY() <= pWindow[3];
        boolean cutX = x < pWindow[0] && linkedTo.getX() > pWindow[1] && yIn;
        // we can check on yIn only because all segments are horizontal
        cutX |= linkedTo.getX() < pWindow[0] && x > pWindow[1] && yIn;
        // "or" applied on cutX. If the initialisation or this line is True, cutX stay True.
        boolean cutY = y < pWindow[2] && linkedTo.getY() > pWindow[3] && xIn;
        cutY |= linkedTo.getY() < pWindow[2] && y > pWindow[3] && xIn;
        return (xIn && yIn) || (lxIn && lyIn) || (cutX || cutY);
        // the actual point is in the window or the link is in the window or the segment cross the window
    }
}
