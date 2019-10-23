package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class B {
    public static void main(String[] args) throws IOException {
        var inStream = new InputStreamReader(System.in);
        var in = new BufferedReader(inStream);

        in.readLine(); // Throwaway line 1, as t always = 1
        var noLinesStr = in.readLine();
        var noLines = Integer.parseInt(noLinesStr);

        System.out.println("Case #1:");
        for (int i=0; i<noLines; i++) {
            var line = in.readLine();
            line = line.replaceAll("entin", "ierende");
            line = line.replaceAll("enten", "ierende");
            line = line.replaceAll("ent", "ierender");
            System.out.println(line);
        }
    }
}
