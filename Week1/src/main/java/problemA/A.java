package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class A {

    public static void main(String[] args) throws IOException {
        var stream = new InputStreamReader(System.in);
        var in = new BufferedReader(stream);

        var noLinesStr = in.readLine();
        var noLines = Integer.parseInt(noLinesStr);
        for (int i=1; i<=noLines; i++) {
            var name = in.readLine();
            System.out.println("Case #" + i + ": Hello " + name + "!");
        }
    }

}
