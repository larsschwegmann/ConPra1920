package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class A {

    private static class Tuple<A, B> {
        A left;
        B right;
        Tuple(A left, B right) {
            this.left = left;
            this.right = right;
        }
        A getLeft() {
            return left;
        }
        B getRight() {
            return right;
        }
    }

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];

            var graph = new int[n][n];
            Arrays.stream(graph).forEach(a -> Arrays.fill(a, 0));

            for (int i=0; i<m; i++) {
                var trailLine = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var v = trailLine[0] - 1;
                var w = trailLine[1] - 1;
                var c = trailLine[2];

                graph[v][w] = c;
                graph[w][v] = c;
            }

            System.out.println("Case #" + t + ": " + djikstra(graph, 0, n - 1));

            buff.readLine();
        }
    }

    private static int djikstra(int[][] graph, int start, int dest) {
        var dist = new int[graph.length];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        // New prio queue by comparing distance, Tuple<Node, Dist>
        var pq = new PriorityQueue<Tuple<Integer, Integer>>(Comparator.comparing(Tuple::getRight));
        for (int i=0; i<graph.length; i++) {
            pq.add(new Tuple<>(i, dist[i]));
        }

        while (!pq.isEmpty()) {
            var v = pq.poll().getLeft();
            for (var w : getNeighbours(graph, v)) {
                if (dist[v] + graph[v][w] < dist[w]) {
                    dist[w] = dist[v] + graph[v][w];
                    pq.add(new Tuple<>(w, dist[w]));
                }
            }
        }

        return dist[dest];
    }

    private static ArrayList<Integer> getNeighbours(int[][] graph, int node) {
        var list = new ArrayList<Integer>();
        for (int i=0; i<graph.length; i++) {
            if (graph[node][i] > 0) list.add(i);
        }
        return list;
    }
}
