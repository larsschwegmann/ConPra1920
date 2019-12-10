package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

class Box {
    private int x;
    private int y;
    private int z;
    public Box(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int baseArea() {
        return this.x * this.y;
    }
}

public class C {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var hn = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var h = hn[0];
            var n = hn[1];
            var boxes = new Box[3 * n];
            var maxHeights = new int[3 * n];

            for (int i=0; i<n; i++) {
                var box = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();

                boxes[3 * i] = new Box(Math.max(box[0], box[1]), Math.min(box[0], box[1]), box[2]);
                boxes[3 * i + 1] = new Box(Math.max(box[1], box[2]), Math.min(box[1], box[2]), box[0]);
                boxes[3 * i + 2] = new Box(Math.max(box[0], box[2]), Math.min(box[0], box[2]), box[1]);

                maxHeights[3 * i] = boxes[3 * i].getZ();
                maxHeights[3 * i + 1] = boxes[3 * i + 1].getZ();
                maxHeights[3 * i + 2] = boxes[3 * i + 2].getZ();
            }

            Arrays.sort(boxes, Comparator.comparing(Box::baseArea).reversed());
            var max = -1;
            for (int i=0; i<boxes.length; i++) {
                var currentBox = boxes[i];
                int currentMaxHeight = 0;
                for (int k=0; k<i; k++) {
                    var lowerBox = boxes[k];
                    if (currentBox.getX() < lowerBox.getX() && currentBox.getY() < lowerBox.getY()) {
                        currentMaxHeight = Math.max(currentMaxHeight, maxHeights[k]);
                    }
                }
                maxHeights[i] = currentMaxHeight + currentBox.getZ();
                if (maxHeights[i] > max) {
                    max = maxHeights[i];
                }
            }

            System.out.println("Case #" + t + ": " + (h <= max ? "yes" : "no"));

            buff.readLine();
        }
    }
}
