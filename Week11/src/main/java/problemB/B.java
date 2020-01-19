package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

class SegmentTree {

    int left;
    int right;
    long val;
    long lazy;

    SegmentTree leftChild;
    SegmentTree rightChild;

    public SegmentTree(long[] arr) {
        this(0, arr.length - 1, arr);
    }

    private SegmentTree(int left, int right, long[] arr) {
        this.left = left;
        this.right = right;
        if (left == right) {
            val = arr[left];
            return;
        }
        int m = (left + right) / 2;
        leftChild = new SegmentTree(left, m, arr);
        rightChild = new SegmentTree(m + 1, right, arr);
        val = leftChild.val + rightChild.val;
    }

    public long sum(int left, int right) {
        propagate();
        if (left > this.right || right < this.left) {
            return 0;
        }
        if (left <= this.left && this.right <= right) {
            return this.val;
        }
        return leftChild.sum(left, right) + rightChild.sum(left, right);
    }

    public void propagate() {
        this.val += ((long)(this.right - this.left + 1)) * this.lazy;
        if (this.left != this.right) {
            leftChild.lazy += this.lazy;
            rightChild.lazy += this.lazy;
        }
        this.lazy = 0;
    }

    public void rangeAdd(int left, int right, long value) {
        propagate();
        if (left > this.right || right < this.left) {
            return;
        }
        if (left <= this.left && this.right <= right) {
            this.lazy += value;
            propagate();
        } else if (this.left != this.right) {
            this.leftChild.rangeAdd(left, right, value);
            this.rightChild.rangeAdd(left, right, value);
            this.val = this.leftChild.val + this.rightChild.val;
        }
    }

}

public class B {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var nk = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nk[0];
            var k = nk[1];

            var arr = new long[n];
            Arrays.fill(arr, 0);
            var segTree = new SegmentTree(arr);

            long result = 0L;

            for (int i = 0; i < k; i++) {
                var instr = buff.readLine().split(" ");
                if (instr[0].equals("i")) {
                    var l = Integer.parseInt(instr[1]) - 1;
                    var r = Integer.parseInt(instr[2]) - 1;
                    var v = Integer.parseInt(instr[3]);
                    segTree.rangeAdd(l, r, v);
                }

                if (instr[0].equals("q")) {
                    var a = Integer.parseInt(instr[1]) - 1;
                    result = (result + segTree.sum(a, a)) % 1000000007;
                }
            }

            System.out.println("Case #" + t + ": " + result);

            buff.readLine();
        }
    }
}
