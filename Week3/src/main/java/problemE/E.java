package problemE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class E {

    public static class Edge {
        private int source;
        private int destination;
        private int minWaterLevel;
        Edge(int source, int destination, int minWaterLevel) {
            this.source = source;
            this.destination = destination;
            this.minWaterLevel = minWaterLevel;
        }

        int getSource() {
            return source;
        }

        int getDestination() {
            return destination;
        }

        int getMinWaterLevel() {
            return minWaterLevel;
        }

        @Override
        public String toString() {
            return "s: " + source + ", d: " + destination + ",l: " + minWaterLevel;
        }
    }

    public static class ControlRoom {
        private int room;
        private int level;
        ControlRoom(int room, int level) {
            this.room = room;
            this.level = level;
        }
        int getRoom() {
            return room;
        }
        int getLevel() {
            return level;
        }

        @Override
        public String toString() {
            return "r: " + room + ", l: " + level;
        }
    }

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nmkl = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nmkl[0]; // Number of rooms numbered [1; n]
            var m = nmkl[1]; // Number of hallways
            var k = nmkl[2]; // Number of control rooms
            var l = nmkl[3]; // Start water level

            ArrayList<Edge>[] hallways = new ArrayList[n];
            IntStream.range(0, n).forEach(i -> hallways[i] = new ArrayList<>());
            HashMap<Integer, Integer> controlRooms = new HashMap<>();

            for (int i=0; i<m; i++) {
                var abl = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var ai = abl[0] - 1;
                var bi = abl[1] - 1;
                var li = abl[2];
                if (ai != bi) {
                    // kick reflexive edges, they dont hold any value
                    hallways[ai].add(new Edge(ai, bi, li));
                    hallways[bi].add(new Edge(bi, ai, li));
                }
            }

            for (int i=0; i<k; i++) {
                var ad = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var a = ad[0] - 1;
                var d = ad[1];
                controlRooms.put(a, d);
            }

            var currentWaterLevel = l;
            var reachableControlRooms = new TreeSet<>(Comparator.comparing(ControlRoom::getLevel));
            var visited = new HashSet<Integer>();
            var queue = new PriorityQueue<>(Comparator.comparing(Edge::getMinWaterLevel).reversed());
            
            queue.addAll(hallways[0]);
            if (controlRooms.containsKey(0)) reachableControlRooms.add(new ControlRoom(0, controlRooms.get(0)));
            visited.add(0);

            while (!queue.isEmpty() && visited.size() < n) {
                var candidate = queue.peek();
                if (visited.contains(candidate.getDestination())) {
                    queue.poll();
                    continue;
                }
                if (candidate.minWaterLevel >= currentWaterLevel) {
                    // Water level ist still sufficient
                    queue.poll(); // Remove candidate
                    visited.add(candidate.getDestination()); // Add candidate to visited set
                    if (controlRooms.containsKey(candidate.getDestination())) {
                        // Add control room to available control rooms
                        var c = new ControlRoom(candidate.getDestination(), controlRooms.get(candidate.getDestination()));
                        reachableControlRooms.add(c);
                    }
                    // Remove edges leading to this
                    queue.removeIf(e -> e.getDestination() == candidate.getDestination());
                    // Add edges from destination of candidates to queue
                    hallways[candidate.getDestination()]
                            .stream()
                            .filter(e -> !visited.contains(e.getDestination()))
                            .forEach(queue::add);
                } else {
                    // Try lowering the water level
                    if (reachableControlRooms.size() == 0) {
                        // No control room reachable, cant lower water any further, stop
                        break;
                    }

                    var minimumWaterLevel = candidate.getMinWaterLevel();
                    var mostPowerfulControlRoom = reachableControlRooms.first();

                    if (minimumWaterLevel < mostPowerfulControlRoom.getLevel()) {
                        // water level is not achievable with available control rooms, stop
                        break;
                    }

                    // this minimum water level is achievable, set current to required minimum
                    currentWaterLevel = minimumWaterLevel;
                    // Remove control rooms from reachable rooms if they are no longer useful
                    reachableControlRooms.removeIf(r -> r.getLevel() >= minimumWaterLevel);
                }
            }

            System.out.println("Case #" + t + ": " + (visited.size() == n ? currentWaterLevel : "impossible"));

            buff.readLine();
        }
    }
}
