package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class D {

    private static class Station {
        int start;
        int end;
        Station(int start, int end) {
            this.start = start;
            this.end = end;
        }

        long roomCountBelow(long roomNo) {
            var res = roomNo - this.start;
            if (res < 0) {
                return 0;
            } else if (res > (end - start + 1)) {
                return end - start + 1;
            } else {
                return res;
            }
        }
        long evenCount(long roomNo) {
            return (roomNo >= this.start && roomNo <= this.end) ? 1 : 0;
        }
        long aboveCount(long roomNo) {
            var res = this.end - roomNo;
            if (res < 0) {
                return 0;
            } else if (res > (end - start + 1)) {
                return end - start + 1;
            } else {
                return res;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var line = buff.readLine().split(" ");
            var stationsCount = Integer.parseInt(line[0]);
            var roomLookupCount = Integer.parseInt(line[1]);

            var stationList = new ArrayList<Station>();

            var minRoom = Integer.MAX_VALUE;
            var maxRoom = 0;

            for (int s=0; s < stationsCount; s++) {
                // Add specified stations
                var stationLine = buff.readLine().split(" ");
                var stationStart = Integer.parseInt(stationLine[0]);
                var stationEnd = Integer.parseInt(stationLine[1]);
                var station = new Station(stationStart, stationEnd);

                if (maxRoom < stationEnd) {
                    maxRoom = stationEnd;
                }
                if (stationStart < minRoom) {
                    minRoom = stationStart;
                }

                stationList.add(station);
            }

            System.out.println("Case #" + t + ":");

            for (int r=0; r<roomLookupCount; r++) {
                var lookup = Integer.parseInt(buff.readLine());

                long left = minRoom;
                long right = maxRoom;
                long needle = 0;
                while (left <= right) {
                    needle = (left + right) / 2;
                    long belowCount = 0;
                    long evenCount = 0;
                    for (var station : stationList) {
                        belowCount += station.roomCountBelow(needle);
                        evenCount += station.evenCount(needle);
                    }

                    if (belowCount < lookup && lookup <= belowCount + evenCount) {
                        break;
                    }
                    if (belowCount >= lookup) {
                        right = needle - 1;
                    } else {
                        left = needle + 1;
                    }
                }

                System.out.println(needle);
            }
            buff.readLine();
        }
    }
}
