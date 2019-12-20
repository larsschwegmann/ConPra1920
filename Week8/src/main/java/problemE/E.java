package problemE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

class Tuple<A, B> {
    private A left;
    private B right;

    public Tuple(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public A getLeft() {
        return left;
    }

    public B getRight() {
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

            HashMap<Integer, Integer>[] dp = new HashMap[n + 1];
            for (int i=0; i<dp.length; i++) {
                dp[i] = new HashMap<>();
            }
            dp[n].put(0, objects[n]);

            var queue = new PriorityQueue<Tuple<Tuple<Integer, Integer>, Integer>>(Comparator.comparing(Tuple::getRight));
            queue.add(new Tuple<>(new Tuple<>(n, 0), objects[n]));

            int max = -1;

            while (!queue.isEmpty()) {
                var tuple = queue.poll();
                var node = tuple.getLeft();

                for (var next : adjancencies.get(node.getLeft())) {
                    if (next >= node.getLeft()) {
                        continue;
                    }
                    if (node.getRight() + distances[node.getLeft()][next] >= cloneDistances[next]) {
                        continue;
                    }
                    var nodeTime = node.getRight();
                    var nextTime = nodeTime + distances[node.getLeft()][next];

                    if (dp[next].getOrDefault(nextTime, Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                        var nodeObjects = dp[node.getLeft()].get(nodeTime);
                        dp[next].put(nextTime, nodeObjects + objects[next]);
                    } else {
                        var nodeObjects = dp[node.getLeft()].get(nodeTime);
                        var nextObjects = dp[next].get(nextTime);
                        dp[next].put(nextTime, Math.max(nodeObjects + objects[next], nextObjects));
                    }

                    var nextObjects = dp[next].get(nextTime);
                    if (next == 0 && nextObjects > max) {
                        max = nextObjects;
                    }
                    queue.add(new Tuple<>(new Tuple<>(next, node.getRight() + distances[node.getLeft()][next]), nextObjects));
                }
            }

            if (max > -1) {
                System.out.println("Case #" + t + ": " + max);
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

        var queue = new PriorityQueue<Tuple<Integer, Integer>>(Comparator.comparing(Tuple::getRight));

        for (int i = 0; i < adjancencies.size(); i++) {
            queue.add(new Tuple<>(i, distancesFromSource[i]));
        }

        while (!queue.isEmpty()) {
            var node = queue.poll();

            for (var next : adjancencies.get(node.getLeft())) {
                if (distancesFromSource[node.getLeft()] + distances[node.getLeft()][next] < distancesFromSource[next]) {
                    distancesFromSource[next] = distancesFromSource[node.getLeft()] + distances[node.getLeft()][next];
                    queue.add(new Tuple<>(next, distancesFromSource[next]));
                }
            }
        }

        return distancesFromSource;
    }
}
