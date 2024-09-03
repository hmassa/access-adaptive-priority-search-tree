import static java.lang.Math.floor;
import java.util.*;

public class RestructuringAAPST implements Tree{
    public AAPSTNode root;
    int count = 0;

    public RestructuringAAPST(ArrayList<Integer> xVals) {
        if(xVals.isEmpty()) return;
        Collections.sort(xVals);
        this.root = buildTree(xVals, 0, xVals.size(), null);
    }

    private AAPSTNode buildTree(ArrayList<Integer> xVals, int minInd, int maxInd, AAPSTNode parent) {
        if (minInd >= maxInd){
            return null;
        }

        int middle = (int) floor((maxInd + minInd)/2);
        AAPSTNode node = new AAPSTNode();
        node.parent = parent;
        node.qx = xVals.get(middle);
        node.qy = 0;
        node.px = node.py = -1;
        node.validP = false;

        node.left = buildTree(xVals, minInd, middle, node);
        node.right = buildTree(xVals, middle + 1, maxInd, node);

        return node;
    }

    @Override
    public int find(Comparable xVal){
        count = 0;
        AAPSTNode node = this.root;
        int diff;

        while (node != null) {
            if (node.validP) {
                diff = xCompare(xVal, node.px);
                if (diff == 0) {
                    increment(node, true);
                    return count;
                }
            }

            diff = xCompare(xVal, node.qx);

            if (diff == 0) {
                increment(node, false);
                return count;
            } else if (diff > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return count;
    }

    private void increment(AAPSTNode node, boolean p) {
        Comparable xHold;
        int yHold;

        if (p) {
            node.py++;

            if (node.parent == null || yCompare(node.py, node.parent.py) <= 0)
                return;

            xHold = node.px;
            yHold = node.py;
            deleteP(node);
        } else {
            node.qy++;
            if (node.parent == null)
                return;

            if (node.parent.validP && yCompare(node.qy, node.parent.py) <= 0)
                return;

            xHold = node.qx;
            yHold = node.qy;
        }

        insertP(xHold, yHold, node.parent);
    }

    private void deleteP(AAPSTNode node){
        while (true) {
            if (node.right == null && node.left == null) {
                node.px = -1;
                node.py = -1;
                node.validP = false;
                break;
            } else if (node.right == null) {
                if (node.left.validP) {
                    node.px = node.left.px;
                    node.py = node.left.py;
                    node = node.left;
                } else {
                    node.px = -1;
                    node.py = -1;
                    node.validP = false;
                    break;
                }
            } else if (node.left == null) {
                if (node.right.validP) {
                    node.px = node.right.px;
                    node.py = node.right.py;
                    node = node.right;
                } else {
                    node.px = -1;
                    node.py = -1;
                    node.validP = false;
                    break;
                }
            } else {
                if (node.right.validP && node.left.validP){
                    int diff = yCompare(node.right.py, node.left.py);

                    if (diff > 0) {
                        node.px = node.right.px;
                        node.py = node.right.py;
                        node = node.right;
                    } else {
                        node.px = node.left.px;
                        node.py = node.left.py;
                        node = node.left;
                    }
                } else if (node.right.validP) {
                    node.px = node.right.px;
                    node.py = node.right.py;
                    node = node.right;
                } else if (node.left.validP) {
                    node.px = node.left.px;
                    node.py = node.left.py;
                    node = node.left;
                } else {
                    node.px = -1;
                    node.py = -1;
                    node.validP = false;
                    break;
                }
            }
        }
    }

    private void insertP(Comparable xVal, int yVal, AAPSTNode node) {
        Comparable xHold;
        int yHold;
        int xDiff;

        while (node.parent != null && !node.parent.validP) {
            node = node.parent;
        }

        while (node.parent != null && yCompare(yVal, node.parent.py) > 0) {
            node = node.parent;
        }

        while (node.validP) {
            xHold = node.px;
            yHold = node.py;
            node.px = xVal;
            node.py = yVal;
            xVal  = xHold;
            yVal = yHold;

            xDiff = xCompare(xVal, node.qx);
            if (xDiff > 0) {
                node = node.right;
            } else if (xDiff < 0) {
                node = node.left;
            } else {
                node.qy = yVal;
                return;
            }
        }

        xDiff = xCompare(node.qx, xVal);
        if (xDiff != 0) {
            node.px = xVal;
            node.py = yVal;
            node.validP = true;
        } else {
            node.qy = yVal;
        }
    }

    private int xCompare(Comparable a, Comparable b) {
        count++;
        return a.compareTo(b);
    }

    private int yCompare(int a, int b) {
        count++;
        return a - b;
    }

}