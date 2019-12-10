package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class A {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<= casesCount; t++) {
            var nc = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nc[0];
            var c = nc[1];
            var coinValues = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var cache = new int[c + 1];
            var bestValue = new int[c + 1];
            Arrays.fill(cache, 0);
            Arrays.fill(bestValue, 0);

            solve(cache, bestValue, coinValues, c);

            var result = new int[n];
            var current = c;
            for (int i=0; i<c; i++) {
                result[Arrays.binarySearch(coinValues, bestValue[current])]++;
                current = current - bestValue[current];
                if (current == 0) {
                    break;
                }
            }

            System.out.println("Case #" + t + ": " + Arrays.stream(result).mapToObj(Integer::toString).collect(Collectors.joining(" ")));
            buff.readLine();
        }
    }

    private static void solve(int[] cache, int[] bestValue, int[] coinValues, int c) {
        for (int i=0; i<c+1; i++) {
            var minCount = i;
            var coin = 1;
            for (int coinValue : coinValues) {
                if (coinValue > i) {
                    continue;
                }
                if (cache[i - coinValue] + 1 < minCount) {
                    minCount = cache[i - coinValue] + 1;
                    coin = coinValue;
                }
            }
            cache[i] = minCount;
            bestValue[i] = coin;
        }
    }
}
