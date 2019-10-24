package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class C {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var testData = buff.readLine().split(" ");
            var d = Integer.parseInt(testData[0]);
            var p = Integer.parseInt(testData[1]);
            var u = Integer.parseInt(testData[2]);
            var v = Integer.parseInt(testData[3]);

            var x = solve(d, p, u, v);
            System.out.printf(Locale.US, "Case #%d: %.10f", t, x);
            System.out.println();
        }
    }

    private static double solve(int d, int p, int u, int v) {
        if (p == 2) {
            return d;
        }
        if (v - u == 0 || !canyonBlocksOptimalPostDistribution(d, p, u ,v)) {
            return postDistance(d, p);
        }

        double distL = u;
        double distR = d - v;

        int leftCount = 1;
        int rightCount = 1;

        for (int i=2; i<p; i++) {
            var left = postDistance(distL, leftCount + 1);
            var right = postDistance(distR, rightCount + 1);
            if (left >= right) {
                leftCount++;
            } else {
                rightCount++;
            }
        }

        var resultLeft = postDistance(distL, leftCount);
        var resultRight = postDistance(distR, rightCount);
        if (leftCount == 1 || resultLeft == 0) resultLeft = Double.MAX_VALUE;
        if (rightCount == 1 ||resultRight == 0) resultRight = Double.MAX_VALUE;

        var result = Math.min(resultLeft, resultRight);
        result = result == Double.MAX_VALUE ? 0.0 : result;
        if (leftCount > 1 && rightCount > 1) {
            return Math.min(v-u, result);
        } else {
            return result;
        }
    }

    private static boolean canyonBlocksOptimalPostDistribution(int d, int p, int u, int v) {
        var optimalDist = postDistance(d, p);
        for (int i=0; i<d; i++) {
            var postLoc = i * optimalDist;
            if (postLoc > u && postLoc < v) {
                return true;
            }
        }
        return false;
    }

    private static double postDistance(double length, int numPosts) {
        if (numPosts < 2) {
            return length;
        }
        return length / (numPosts - 1);
    }
}
