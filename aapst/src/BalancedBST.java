import java.util.ArrayList;
import java.util.Collections;

/**
 * @authors Mayank Jaiswal at  https://www.geeksforgeeks.org/sorted-array-to-balanced-bst/
 * and Arnab Kundu at https://www.geeksforgeeks.org/iterative-searching-binary-search-tree/
 */
public class BalancedBST implements Tree {
    public Node root;

    public BalancedBST(ArrayList<Integer> points){
        if (points == null) return;
//        Collections.sort(points);
        this.root = sortedArrayToBST(points, 0, points.size()-1);
    }

    private Node sortedArrayToBST(ArrayList<Integer> points, int start, int end) {
        if (start > end) {
            return null;
        }
        int mid = (start + end) / 2;
        Node Node = new Node(points.get(mid));
        Node.left = sortedArrayToBST(points, start, mid - 1);
        Node.right = sortedArrayToBST(points, mid + 1, end);

        return Node;
    }

    @Override
    public int find(Comparable key) {
        int count = 0;
        Node Node = this.root;

        while (Node != null) {
            int diff = key.compareTo(Node.key);
            count++;
            if (diff > 0) {
                Node = Node.right;
            } else if (diff < 0) {
                Node = Node.left;
            } else {
                return count;
            }
        }
        return count;
    }

    public void preOrder(Node Node) {
        if (Node == null) {
            return;
        }
        System.out.print(Node.key + ", ");
        preOrder(Node.left);
        preOrder(Node.right);
    }
}
