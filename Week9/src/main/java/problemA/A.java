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

        for (int t = 1; t <= casesCount; t++) {
            buff.readLine();
            var result = Arrays
                    .stream(buff.readLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .reduce(0, A::gcd);
            System.out.println("Case #" + t + ": " + result);
            buff.readLine();
        }

    }

    private static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }


}
