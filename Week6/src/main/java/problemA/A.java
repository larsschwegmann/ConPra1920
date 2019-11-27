package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class A {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var n = Integer.parseInt(buff.readLine());

            var clock = new Clock();

            for (int i=0; i<n; i++) {
                var time = buff.readLine();
                clock.recordTime(time, i == 0);
            }
        }
    }
}
