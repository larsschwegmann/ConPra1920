package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;

public class B {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var line = buff.readLine().split(" ");
            var prizesCount = Integer.parseInt(line[0]);
            var lotCost = Integer.parseInt(line[1]);

            var prizes = Arrays.stream(buff.readLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            var left = 0.0;
            var right = 1.0;
            var needle = 0.0;

            var tries = 0;

            while (left <= right && tries < 50) {
                needle = (left + right) / 2.0;
                var payoff = expectedTotalPayoff(prizes, lotCost, needle);
                if (payoff <= 0) {
                    if (0 - payoff <= Math.pow(10, -6)) {
                        break;
                    }
                    left = (left + needle) / 2.0;
                } else {
                    right = (needle + right) / 2.0;
                }
                tries++;
            }
            System.out.printf(Locale.US, "Case #%d: %.10f", t, needle);
            System.out.println();

            // Skip newline between test cases
            buff.readLine();
        }
    }

    private static double expectedTotalPayoff(int[] prizes, int lotCost, double probability) {
        var cost = 0.0;
        for (int i=0; i<prizes.length; i++) {
            var prizeValue = prizes[i];
            cost += prizeValue * Math.pow(probability, i + 1);
        }
        return cost - lotCost;
    }
}
