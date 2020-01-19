package problemG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class G {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var rb = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var r = rb[0];
            var b = rb[1];

            double result = calculatePlateRadius(r, b);
            System.out.println("Case #" + t + ": " + result);
        }
    }

    private static double calculatePlateRadius(int r, int b) {
        switch (b) {
            case 1:
                return r;
            case 2:
                return 2 * r;
            default:
                double s = r * 2.0;
                double polyRadius = s / (2.0 * Math.sin(Math.PI / (double) b));
                return r + polyRadius;
        }
    }
}
