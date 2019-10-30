package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class A {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var n = Integer.parseInt(buff.readLine());
            int[][] adjacencies = new int[n][n];

            // Read adjacency matrix
            for (int i=0; i<n; i++) {
                adjacencies[i] = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            }

            

            buff.readLine();
        }

    }
}
