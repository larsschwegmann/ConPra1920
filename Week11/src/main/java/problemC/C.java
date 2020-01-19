package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

class SegmentTree {

    int left;
    int right;
    long max;
    Long lazy = null;

    SegmentTree leftChild;
    SegmentTree rightChild;

    public SegmentTree(long[] arr) {
        this(0, arr.length - 1, arr);
    }

    private SegmentTree(int left, int right, long[] arr) {
        this.left = left;
        this.right = right;
        if (left == right) {
            max = arr[left];
            return;
        }
        int m = (left + right) / 2;
        leftChild = new SegmentTree(left, m, arr);
        rightChild = new SegmentTree(m + 1, right, arr);
        max = leftChild.max + rightChild.max;
    }

    public long max(int left, int right) {
        propagate();
        if (left > this.right || right < this.left) {
            return 0;
        }
        if (left <= this.left && this.right <= right) {
            return this.max;
        }
        return Math.max(leftChild.max(left, right), rightChild.max(left, right));
    }

    public void propagate() {
        if (this.lazy != null) {
            this.max = this.lazy;
        }
        if (this.left != this.right && this.lazy != null) {
            leftChild.lazy = this.lazy;
            leftChild.max = this.lazy;
            rightChild.lazy = this.lazy;
            rightChild.max = this.lazy;
        }
        this.lazy = null;
    }

    public void rangeUpdate(int left, int right, long value) {
        propagate();
        if (left > this.right || right < this.left) {
            return;
        }
        if (left <= this.left && this.right <= right) {
            this.lazy = value;
            propagate();
        } else if (this.left != this.right) {
            this.leftChild.rangeUpdate(left, right, value);
            this.rightChild.rangeUpdate(left, right, value);
            this.max = Math.max(this.leftChild.max, this.rightChild.max);
        }
    }

}

public class C {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var nk = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nk[0];
            var k = nk[1];

            var segTree = new SegmentTree(new long[n]);

            var sb = new StringBuilder("Case #" + t + ": ");

            for (int i = 0; i < k; i++) {
                var whp = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var w = whp[0]; // width
                var h = whp[1]; // height
                var p = whp[2]; // offset

                var rangeMax = segTree.max(p, p + w - 1);
                segTree.rangeUpdate(p, p + w - 1, rangeMax + h);
                sb.append(segTree.max(0, n - 1));
                sb.append(" ");
            }

            System.out.println(sb.toString());
            buff.readLine();
        }

    }
}
