package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.IntStream;

public class A {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCountStr = buff.readLine();
        var casesCount = Integer.parseInt(casesCountStr);

        for (int t=1; t<=casesCount; t++) {
            var abcLine = buff.readLine().split(" ");
            var notablePeopleCount = Integer.parseInt(abcLine[0]);
            var relationsCount = Integer.parseInt(abcLine[1]);
            var marriagesCount = Integer.parseInt(abcLine[2]);

            var graph = new RelationshipGraph(notablePeopleCount);

            // Parse wealth info
            var wealthStr = buff.readLine().split(" ");
            IntStream.range(0, notablePeopleCount - 1).forEach(i -> graph.setWealth(i, Integer.parseInt(wealthStr[i])));

            // Parse relationships
            for (int i=0; i<relationsCount; i++) {
                var deLine = buff.readLine().split(" ");
                var d = Integer.parseInt(deLine[0]) - 1;
                var e = Integer.parseInt(deLine[1]) - 1;
                graph.addRelationship(d, e);
            }

            // Parse marriages
            for (int i=0; i<marriagesCount; i++) {
                var fgLine = buff.readLine().split(" ");
                var f = Integer.parseInt(fgLine[0]) - 1;
                var g = Integer.parseInt(fgLine[1]) - 1;
                graph.addMarriage(f, g);
            }

            var maxWealth = graph.findWealthiestPartner();

            System.out.println("Case #" + t + ": " + (maxWealth != -1 ? maxWealth : "impossible"));
            // Skip newline between problems
            buff.readLine();
        }
    }
}
