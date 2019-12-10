package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

class Tournament {
    private int start;
    private int end;
    private int prize;
    public Tournament(int start, int end, int prize) {
        this.start = start;
        this.end = end;
        this.prize = prize;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getPrize() {
        return prize;
    }
}

public class D {

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var n = Integer.parseInt(buff.readLine());
            var tournaments = new Tournament[n];

            for (int i = 0; i < n; i++) {
                var line = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                tournaments[i] = new Tournament(line[0], line[1], line[2]);
            }

            Arrays.sort(tournaments, Comparator.comparing(Tournament::getStart));

            var maxPrizes = new int[n];
            Arrays.fill(maxPrizes, 0);

            var max = -1;
            for (int i = 0; i < n; i++) {
                var current = tournaments[i];

                var prevMaxPrize = 0;
                for (int k = 0; k < i; k++) {
                    var prev = tournaments[k];
                    if (prev.getEnd() < current.getStart()) {
                        prevMaxPrize = Math.max(prevMaxPrize, maxPrizes[k]);
                    }
                }

                maxPrizes[i] = prevMaxPrize + current.getPrize();
                max = Math.max(max, maxPrizes[i]);
            }

            System.out.println("Case #" + t + ": " + max);

            buff.readLine();
        }
    }
}
