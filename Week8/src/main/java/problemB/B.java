package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class B {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<= casesCount; t++) {
            var a = buff.readLine().toCharArray();
            var b = buff.readLine().toCharArray();

            var max = lowestCommonSubsequence(a, b);;

            for (int i=0; i<a.length; i++) {
                a = rotateRight(a);
                max = Math.max(max, lowestCommonSubsequence(a, b));
            }
            a = flip(a);
            for (int i=0; i<a.length; i++) {
                a = rotateRight(a);
                max = Math.max(max, lowestCommonSubsequence(a, b));
            }

            System.out.println("Case #" + t + ": " + max);
            buff.readLine();
        }
    }

    private static char[] rotateRight(char[] seq) {
        return (new String(seq).substring(1, seq.length) + seq[0]).toCharArray();
    }

    private static char[] flip(char[] seq) {
        var result = new char[seq.length];
        for (int i=0; i<seq.length; i++) {
            result[i] = seq[seq.length - 1 - i];
        }
        return result;
    }

    private static int lowestCommonSubsequence(char[] a, char[] b) {
        var table = new int[a.length + 1][b.length + 1];

        for (int i=0; i<=a.length; i++) {
            for (int k=0; k<=b.length; k++) {
                if (i == 0 || k == 0) {
                    table[i][k] = 0;
                    continue;
                }
                if (a[i - 1] == b[k - 1]) {
                    table[i][k] = table[i - 1][k - 1] + 1;
                    continue;
                }
                table[i][k] = Math.max(table[i - 1][k], table[i][k - 1]);
            }
        }

        return table[a.length][b.length];
    }
}
