package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class D {

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var sb = new StringBuilder("Case #");
            sb.append(t);
            sb.append(":\n");

            var line = buff.readLine().split(" ");
            var stationsCount = Integer.parseInt(line[0]);
            var roomLookupCount = Integer.parseInt(line[1]);

            var map = new TreeMap<Integer, Integer>();

            for (int s=0; s<stationsCount; s++) {
                // Add specified stations
                var stationLine = buff.readLine().split(" ");
                var stationStart = Integer.parseInt(stationLine[0]);
                var stationEnd = Integer.parseInt(stationLine[1]);
                IntStream.rangeClosed(stationStart, stationEnd).forEach(i -> map.put(i, map.getOrDefault(i, 0) + 1));
            }

            var lookups = new ArrayList<Integer>();
            var lookupResults = new HashMap<Integer, Integer>();

            for (int f=0; f<roomLookupCount; f++) {
                lookups.add(Integer.parseInt(buff.readLine()));
            }
            var lookupsOldOrder = new ArrayList<Integer>(lookups);
            Collections.sort(lookups);
            var lookupIter = lookups.iterator();
            var mapIter = map.navigableKeySet().iterator();
            var totalIndex = 0;
            var currentIndex = 0;
            var current = mapIter.next();
            var currentMax = map.get(current);
            while (lookupIter.hasNext()) {
                var friendIndex = lookupIter.next() - 1;
                while (totalIndex != friendIndex) {
                    if (currentIndex == currentMax - 1 && mapIter.hasNext()) {
                        currentIndex = 0;
                        current = mapIter.next();
                        currentMax = map.get(current);
                        totalIndex++;
                    } else {
                        currentIndex++;
                        totalIndex++;
                    }
                }
                lookupResults.put(friendIndex + 1, current);
            }
            for (var lookup : lookupsOldOrder) {
                sb.append(lookupResults.get(lookup));
                sb.append("\n");
            }
            System.out.print(sb.toString());
            buff.readLine();
        }
    }
}
