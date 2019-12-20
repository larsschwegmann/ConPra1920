package problemC;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class C {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            buff.readLine();

            var guests = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();

            var bitset = new BitSet();

            var possibleGuestCounts = new ArrayList<Integer>();
            bitset.set(1, guests.length, false);
            bitset.set(0);

            for (int i = 1; i < Math.pow(2, guests.length); i++) {
                var sum = 1;
                for (int k = 0; k < guests.length; k++) {
                    if (bitset.get(k)) {
                        sum += guests[k];
                    }
                }
                possibleGuestCounts.add(sum);
                var longarr = bitset.toLongArray();
                long longval = 0;
                if (longarr.length > 0) {
                    longval = longarr[0];
                }
                bitset = BitSet.valueOf(new long[] {longval + 1});
            }

            long result = possibleGuestCounts.get(0);
            for (int i=1; i<possibleGuestCounts.size(); i++) {
                result = lcm(result, possibleGuestCounts.get(i));
            }

            System.out.println("Case #" + t + ": " + result);


            buff.readLine();
        }

    }

    private static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }
}

