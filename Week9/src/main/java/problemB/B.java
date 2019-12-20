package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

class Triple<A, B, C> {
    private A a;
    private B b;
    private C c;
    public Triple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }
}

public class B {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var ny = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = ny[0];
            var y = ny[1];

            var digits = (long) Math.pow(10, n);
            var result = gcdExt(y, digits);

            System.out.println("Case #" + t + ": " + ((result.getB() % digits) + digits) % digits);

        }
    }

    private static Triple<Long, Long, Long> gcdExt(long a, long b) {
        long s = 0;
        long _s = 1;
        long t = 1;
        long _t = 0;
        long r = b;
        long _r = a;

        while (r != 0) {
            long q = _r / r;

            long rOld = r;
            r = _r - q * r;
            _r = rOld;

            long sOld = s;
            s = _s - q * s;
            _s = sOld;

            long tOld = t;
            t = _t - q * t;
            _t = tOld;
        }

        return new Triple<>(_r, _s, _t);
    }
}
