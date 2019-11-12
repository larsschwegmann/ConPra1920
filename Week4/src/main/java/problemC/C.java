package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class C {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        var roads = new ArrayList<ArrayList<Integer>>();
        //var x

        for (int t=1; t<= casesCount; t++) {
            var nmsab = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nmsab[0];
            var m = nmsab[1];
            var s = nmsab[2];
            var a = nmsab[3];
            var b = nmsab[4];




            buff.readLine();
        }
    }

    private static String formatMinutes(int minutes) {
        if (minutes / 60 < 10) {
            return "0" + (minutes / 60) + ":" + (minutes % 60);
        }
        return (minutes / 60) + ":" + (minutes % 60);
    }
}
