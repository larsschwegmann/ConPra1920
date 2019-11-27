package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.TreeSet;

class Range {
    private int start;
    private int end;
    Range(int start, int end) {
        this.start = start;
        this.end = end;
    }
    public int getStart() {
        return start;
    }
    public int getEnd() {
        return end;
    }
}

public class A {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        outer: for (int t=1; t<=casesCount; t++) {
            var lnd = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var l = lnd[0]; // Street Length
            var n = lnd[1]; // Lamppost count
            var d = lnd[2]; // light cone radius

            if (n == 0) {
                buff.readLine();
                buff.readLine();
                System.out.println("Case #" + t + ": impossible");
                continue;
            }

            var pos = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).sorted().toArray();

            var lit = new TreeSet<Integer>();
            var currentPos = 0;

            for (int i=0; i<pos.length; i++) {
                if (currentPos >= l) {
                    break;
                }
                int finalCurrentPos = currentPos;
                var opt = Arrays.stream(pos).filter(p -> lightRange(d, p).getStart() <= finalCurrentPos).max();
                if (!opt.isPresent()) {
                    buff.readLine();
                    System.out.println("Case #" + t + ": impossible");
                    continue outer;
                }
                lit.add(opt.getAsInt());
                currentPos = lightRange(d, opt.getAsInt()).getEnd();
            }

            if (!isValid(l, d, lit)) {
                buff.readLine();
                System.out.println("Case #" + t + ": impossible");
                continue;
            }

            System.out.println("Case #" + t + ": " + lit.size());

            buff.readLine();
        }
    }

    private static boolean isValid(int l, int d, TreeSet<Integer> lit) {
        int currentMax = 0;
        for (int lamp : lit) {
            var lightRange = lightRange(d, lamp);
            if (lightRange.getStart() > currentMax) {
                return false;
            }
            currentMax = lightRange.getEnd();
        }
        return currentMax >= l;
    }

    private static Range lightRange(int d, int x) {
        return new Range(x - d, x + d);
    }
}
