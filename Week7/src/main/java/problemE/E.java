package problemE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.IntStream;

public class E {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<= casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];

            var humanDNA = new int[n][];
            var miceDNA = new int[m][];

            for (int i=0; i<n; i++) {
                humanDNA[i] = buff.readLine().chars().mapToObj(a -> (char) a).mapToInt(E::dnaCharToInt).toArray();
            }
            for (int i=0; i<m; i++) {
                miceDNA[i] = buff.readLine().chars().mapToObj(a -> (char) a).mapToInt(E::dnaCharToInt).toArray();
            }

            var freqs = combinationFrequencies(humanDNA, miceDNA);
            Arrays.sort(freqs, 4, freqs.length);

            var bestScore = 0;

            for (int i=0; i<=10; i++) {
                for (int k=0; k<=10; k++) {
                    for (int o=0; o<=10; o++) {
                        for (int p=0; p<=10; p++) {
                            if ((i + k + o + p) % 2 == 0) {
                                var scoreDiag = i * freqs[0] + k * freqs[1] + o * freqs[2] + p * freqs[3];
                                var compensateTriag = 10 - (i + k + o + p) / 2;
                                var scoreTri = -10 * (freqs[4] + freqs[5] + freqs[6]) + compensateTriag * freqs[7] + 10 * (freqs[8] + freqs[9]);
                                var score = scoreDiag + scoreTri;
                                if (score > bestScore) {
                                    bestScore = score;
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("Case #" + t + ": " + bestScore);

            buff.readLine();
        }

    }

    private static Integer dnaCharToInt(char a) {
        switch (a) {
            case 'A':
                return 0;
            case 'C':
                return 1;
            case 'G':
                return 2;
            case 'T':
                return 3;
            default:
                throw new IllegalArgumentException("oof");
        }
    }

    private static int combinationIndex(int a, int b) {
        if (a == b) {
            return a;
        }

        if (a == 0 && b == 1 || a == 1 && b == 0) {
            return 4;
        }

        if (a == 0 && b == 2 || a == 2 && b == 0) {
            return 5;
        }

        if (a == 0 && b == 3 || a == 3 && b == 0) {
            return 6;
        }

        if (a == 1 && b == 2 || a == 2 && b == 1) {
            return 7;
        }

        if (a == 1 && b == 3 || a == 3 && b == 1) {
            return 8;
        }

        if (a == 2 && b == 3 || a == 3 && b == 2) {
            return 9;
        }

        throw new IllegalArgumentException("oof");
    }

    private static int[] combinationFrequencies(int[][] humanDNA, int[][] miceDNA) {
        var combinations = new int[10];
        for (var human : humanDNA) {
            for (var mice : miceDNA) {
                for (int i=0; i<human.length; i++) {
                    var h = human[i];
                    var m = mice[i];
                    combinations[combinationIndex(h, m)]++;
                }
            }
        }
        return combinations;
    }
}
