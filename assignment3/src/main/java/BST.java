/*************************************************************************
 *  Binary Search Tree class.
 *  Adapted from Sedgewick and Wayne.
 *
 *  @version 3.0 1/11/15 16:49:42
 *  @author Jack O'Sullivan
 *
 *************************************************************************/

public class BST<K extends Comparable<K>, V> {
    private Node root;             // root of BST

    /**
     * Check if the symbol table is empty
     *
     * @return true if this symbol table is empty
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        }

        return x.N;
    }

    /**
     * Get the number of key-value pairs in the symbol table
     *
     * @return the number of key-value pairs
     */
    public int size() {
        return size(root);
    }

    private Node getNode(Node x, K key) {
        if (x == null) {
            return null;
        }

        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return getNode(x.left, key);
        }
        if (cmp > 0) {
            return getNode(x.right, key);
        }

        return x;
    }

    /**
     * Search BST for given key.
     * What is the value associated with given key?
     *
     * @param key the search key
     * @return value associated with the given key if found, or null if no such key exists.
     */
    public V get(K key) {
        Node found = getNode(root, key);
        return found == null ? null : found.val;
    }

    /**
     * Search BST for given key.
     * Does there exist a key-value pair with given key?
     *
     * @param key the search key
     * @return true if key is found and false otherwise
     */
    public boolean contains(K key) {
        return get(key) != null;
    }

    private Node put(Node x, K key, V val) {
        if (x == null) {
            return new Node(key, val, 1);
        }

        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;
        }

        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }

    /**
     * Insert key-value pair into BST.
     * If key already exists, update with new value.
     *
     * @param key the key to insert
     * @param val the value associated with key
     */
    public void put(K key, V val) {
        if (val == null) {
            delete(key);
            return;
        }

        root = put(root, key, val);
    }

    private int height(Node x) {
        if (x == null) {
            return -1;
        }

        return 1 + (size(x.right) > size(x.left) ? height(x.right) : height(x.left));
    }
    /**
     * Tree height.
     * <p>
     * Asymptotic worst-case running time using Theta notation: Theta(n)
     * This function traverses the tree recursively to its deepest point by following the link to the node
     * with the largest subtree on every iteration. The worst scenario for this would be if every node has only a
     * single child. For example:
     *   5
     *  /
     *  2
     *  \
     *   3
     *   \
     *    9
     *   /
     *   4
     *  /
     * 1
     * In this case, there are n nodes and it will take n recursive calls to find the height of the tree.
     *
     * @return the number of links from the root to the deepest leaf.
     * <p>
     * Example 1: for an empty tree this should return -1.
     * Example 2: for a tree with only one node it should return 0.
     * Example 3: for the following tree it should return 2.
     *     B
     *    / \
     *   A   C
     *    \
     *    D
     */
    public int height() {
        return height(root);
    }

    // iterative
    /*public int height() {
        if (isEmpty()) {
            return -1;
        }

        int height = 0;
        for (Node current = root; size(current) > 1; height++) {
            if (size(current.right) > size(current.left)) {
                current = current.right;
            } else {
                current = current.left;
            }
        }

        return height;
    }*/

    private Node select(Node x, int rank) {
        int t = size(x.left);
        if (t > rank) {
            return select(x.left, rank);
        } else if (t < rank) {
            return select(x.right, rank - t - 1);
        } else {
            return x;
        }
    }

    /**
     * Get the key for a given rank
     *
     * @param rank rank of the key to find
     * @return the key for the given rank, or null if rank >= size
     */
    public K select(int rank) {
        if (rank < 0 || rank >= size()) {
            return null;
        }

        return select(root, rank).key;
    }

    /**
     * Median key.
     * If the tree has N keys k1 < k2 < k3 < ... < kN, then their median key
     * is the element at position (N+1)/2 (where "/" here is integer division)
     *
     * @return the median key, or null if the tree is empty.
     */
    public K median() {
        return select(((size() + 1) / 2) - 1);
    }

    private String printKeys(Node x) {
        if (x == null) {
            return "()";
        }

        return "(" + printKeys(x.left) + x.key + printKeys(x.right) + ")";
    }

    /**
     * Print all keys of the tree in a sequence, in-order.
     * That is, for each node, the keys in the left subtree should appear before the key in the node.
     * Also, for each node, the keys in the right subtree should appear before the key in the node.
     * For each subtree, its keys should appear within a parenthesis.
     * <p>
     * Example 1: Empty tree -- output: "()"
     * Example 2: Tree containing only "A" -- output: "(()A())"
     * Example 3: Tree:
     * B
     * / \
     * A   C
     * \
     * D
     * <p>
     * output: "((()A())B(()C(()D())))"
     * <p>
     * output of example in the assignment: (((()A(()C()))E((()H(()M()))R()))S(()X()))
     *
     * @return a String with all keys in the tree, in order, parenthesized.
     */
    public String printKeysInOrder() {
        return printKeys(root);
    }

    private String pretty(Node x, String prefix) {
        StringBuilder result = new StringBuilder(prefix);
        result.append("-");
        if (x == null) {
            result.append("null\n");
        } else {
            result.append(x.key);
            result.append('\n');
            result.append(pretty(x.left, prefix + " |"));
            result.append(pretty(x.right, prefix + "  "));
        }

        return result.toString();
    }

    /**
     * Pretty Printing the tree. Each node is on one line -- see assignment for details.
     *
     * @return a multi-line string with the pretty ascii picture of the tree.
     */
    public String prettyPrintKeys() {
        if (size() == 0) {
            return "-null";
        }

        return pretty(root, "");
    }

    private Node max(Node x) {
        if (x.right == null) {
            return x;
        }
        return max(x.right);
    }
    private Node deleteMax(Node x) {
        if (x.right == null) {
            return x.left;
        }

        x.right = deleteMax(x.right);
        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }
    private Node delete(Node x, K key) {
        if (x == null) {
            return null;
        }

        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = delete(x.left, key);
        } else if (cmp > 0) {
            x.right = delete(x.right, key);
        } else {
            if (x.right == null) {
                return x.left;
            }
            if (x.left == null) {
                return x.right;
            }

            Node t = x;
            x = max(t.left);
            x.left = deleteMax(t.left);
            x.right = t.right;
        }

        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * Deletes a key from a tree (if the key is in the tree).
     * Note that this method works symmetrically from the Hibbard deletion:
     * If the node to be deleted has two child nodes, then it needs to be
     * replaced with its predecessor (not its successor) node.
     *
     * @param key the key to delete
     */
    public void delete(K key) {
        if (size() == 1 && root.key == key) {
            root = null;
            return;
        }

        delete(root, key);
    }

    /**
     * Private node class.
     */
    private class Node {
        private K key;
        private V val;
        private Node left, right;
        private int N;

        public Node(K key, V val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }
}
