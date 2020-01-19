package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Tree {
    public int val;
    public List<Tree> children = new ArrayList<>();
    public Tree(int val) {
        this.val = val;
    }
}

class SegmentTree {
    int left;
    int right;
    int min;
    Integer lazy = null;

    SegmentTree leftChild;
    SegmentTree rightChild;

    public SegmentTree(int[] arr) {
        this(0, arr.length - 1, arr);
    }

    private SegmentTree(int left, int right, int[] arr) {
        this.left = left;
        this.right = right;
        if (left == right) {
            min = arr[left];
            return;
        }
        int m = (left + right) / 2;
        leftChild = new SegmentTree(left, m, arr);
        rightChild = new SegmentTree(m + 1, right, arr);
        min = Math.min(leftChild.min, rightChild.min);
    }

    public int min(int left, int right) {
        propagate();
        if (left > this.right || right < this.left) {
            return Integer.MAX_VALUE;
        }
        if (left <= this.left && this.right <= right) {
            return this.min;
        }
        return Math.min(leftChild.min(left, right), rightChild.min(left, right));
    }

    public void propagate() {
        if (this.lazy != null) {
            this.min = this.lazy;
        }
        if (this.left != this.right && this.lazy != null) {
            leftChild.lazy = this.lazy;
            rightChild.lazy = this.lazy;
        }
        this.lazy = null;
    }
}

public class D {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var n = Integer.parseInt(buff.readLine());

            HashMap<Integer, Tree> treeMap = new HashMap<>();

            // Build Tree
            for (int i = 0; i < n; i++) {
                var branches = Arrays.stream(buff.readLine().split(" "))
                        .mapToInt(Integer::parseInt)
                        .map(a -> a - 1)
                        .skip(1)
                        .toArray();
                var tree = treeMap.getOrDefault(i, new Tree(i));

                for (var child : branches) {
                    var childTree = treeMap.getOrDefault(child, new Tree(child));
                    tree.children.add(childTree);
                    if (!treeMap.containsKey(child)) {
                        treeMap.put(child, childTree);
                    }
                }
                if (!treeMap.containsKey(i)) {
                    treeMap.put(i, tree);
                }
            }

            var route = Arrays.stream(buff.readLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .map(a -> a - 1)
                    .skip(1)
                    .toArray();

            // Root is at 0
            var root = treeMap.get(0);
            var etr = new int[2 * n - 1];
            var depth = new int[2 * n - 1];
            var firstAppearance = new int[n];
            Arrays.fill(firstAppearance, -1);

            // Do the Euler Tour
            eulerTour(root, etr, depth, firstAppearance);

            // Construct Segment Tree from depth array
            var segTree = new SegmentTree(depth);
            var pathLength = 0;
            var source = 0;
            for (var dest : route) {
                // Find lowest common ancestor of source and dest
                var sourceIndex = firstAppearance[source];
                var destIndex = firstAppearance[dest];
                if (sourceIndex > destIndex) {
                    var x = sourceIndex;
                    sourceIndex = destIndex;
                    destIndex = x;
                }
                var result = segTree.min(sourceIndex, destIndex);

                pathLength += depth[sourceIndex] - result + depth[destIndex] - result;

                // Move on
                source = dest;
            }

            System.out.println("Case #" + t + ": " + pathLength);

            buff.readLine();
        }
    }

    private static void eulerTour(Tree root, int[] etr, int[] depth, int[] firstAppearance) {
        eulerTour(root, etr, depth, firstAppearance, 0, 0);
    }

    private static int eulerTour(Tree node, int[] etr, int[] depth, int[] firstAppearance, int currentDepth, int currentIndex) {
        etr[currentIndex] = node.val;
        depth[currentIndex] = currentDepth;
        if (firstAppearance[node.val] == -1) {
            firstAppearance[node.val] = currentIndex;
        }

        for (var child : node.children) {
            currentIndex = eulerTour(child, etr, depth, firstAppearance, currentDepth + 1, currentIndex + 1);
            etr[currentIndex] = node.val;
            depth[currentIndex] = currentDepth;
        }

        return currentIndex + 1;
    }
}