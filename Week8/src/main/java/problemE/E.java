package problemE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

class Tuple {
    private int left;
    private int right;

    public Tuple(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }
}


public class E {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t<= casesCount; t++) {
            var nmg = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nmg[0];
            var m = nmg[1];
            var g = nmg[2];

            var objects = new int[n + 1];
            var objectsUnshifted = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            objects[0] = 0;
            for (int i = 0; i < objectsUnshifted.length; i++) {
                objects[i + 1] = objectsUnshifted[i];
            }


            var adjancencies = new ArrayList<ArrayList<Integer>>();
            IntStream.range(0, n + 1).forEach(i -> adjancencies.add(new ArrayList<>()));

            var distances = new int[n + 1][n + 1];
            IntStream.range(0, n + 1).forEach(i -> Arrays.fill(distances[i], 0));

            for (int i = 0; i < m; i++) {
                var xyl = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var x = xyl[0];
                var y = xyl[1];
                var l = xyl[2];
                adjancencies.get(x).add(y);
                adjancencies.get(y).add(x);
                if (distances[x][y] < l && distances[x][y] > 0) {
                    continue;
                }
                distances[x][y] = l;
                distances[y][x] = l;
            }

            int[] cloneDistances = djikstra(adjancencies, distances, g);

            // IT IS TIME FIND DA WAY

            var leaDistances = new int[adjancencies.size()];
            Arrays.fill(leaDistances, Integer.MAX_VALUE);
            leaDistances[n] = 0;

            var maxItems = new int[n + 1];
            Arrays.fill(maxItems, -1);
            maxItems[n] = objects[n];

            var queue = new PriorityQueue<>(Comparator.comparing(Tuple::getRight).reversed());
            queue.add(new Tuple(n, objects[n])); // Start node

            while (!queue.isEmpty()) {
                var node = queue.poll();
                var shitFlag = false;
                for (var next : adjancencies.get(node.getLeft())) {
                    if (next >= node.getLeft()) {
                        continue;
                    }
                    if (leaDistances[node.getLeft()] + distances[node.getLeft()][next] >= cloneDistances[next]) {
                        continue;
                    }

                    shitFlag = true;
                    if (objects[next] + maxItems[node.getLeft()] > maxItems[next]) {
                        maxItems[next] = objects[next] + maxItems[node.getLeft()];
                        leaDistances[next] = leaDistances[node.getLeft()] + distances[node.getLeft()][next];
                        queue.add(new Tuple(next, maxItems[next]));
                    }
                    //queue.add(next);
                }
                if (!shitFlag && node.getLeft() != 0) {
                    leaDistances[node.getLeft()] = Integer.MAX_VALUE;
                    maxItems[node.getLeft()] = -1;
                }
            }

            if (maxItems[0] > -1) {
                System.out.println("Case #" + t + ": " + maxItems[0]);
            } else {
                System.out.println("Case #" + t + ": impossible");
            }

            buff.readLine();
        }
    }

    private static int[] djikstra(ArrayList<ArrayList<Integer>> adjancencies, int[][] distances, int source) {
        var distancesFromSource = new int[adjancencies.size()];
        Arrays.fill(distancesFromSource, Integer.MAX_VALUE);
        distancesFromSource[source] = 0;

        var queue = new PriorityQueue<>(Comparator.comparing(Tuple::getRight));

        for (int i = 0; i < adjancencies.size(); i++) {
            queue.add(new Tuple(i, distancesFromSource[i]));
        }

        while (!queue.isEmpty()) {
            var node = queue.poll();

            for (var next : adjancencies.get(node.getLeft())) {
                if (distancesFromSource[node.getLeft()] + distances[node.getLeft()][next] < distancesFromSource[next]) {
                    distancesFromSource[next] = distancesFromSource[node.getLeft()] + distances[node.getLeft()][next];
                    queue.add(new Tuple(next, distancesFromSource[next]));
                }
            }
        }

        return distancesFromSource;
    }
}
