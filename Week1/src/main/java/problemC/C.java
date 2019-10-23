package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class C {
    public static void main(String[] args) throws IOException {
        var inStream = new InputStreamReader(System.in);
        var in = new BufferedReader(inStream);

        var noLinesStr = in.readLine();
        var noLines = Integer.parseInt(noLinesStr);

        long c = 299792458;
        long cSquared = c * c;

        for (int i=1; i<=noLines; i++) {
            var massStr = in.readLine();
            int mass = Integer.parseInt(massStr);
            long energy = mass * cSquared;

            System.out.println("Case #" + i + ": " + energy);
        }
    }
}
