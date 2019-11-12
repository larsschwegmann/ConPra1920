package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class B {

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

            var graph = new double[n][n];
            Arrays.stream(graph).forEach(a -> Arrays.fill(a, Double.NEGATIVE_INFINITY));

            for (int i=0; i<m; i++) {
                var exchangeLine = buff.readLine().split(" ");
                var a = Integer.parseInt(exchangeLine[0]) - 1;
                var b = Integer.parseInt(exchangeLine[1]) - 1;
                var c = Double.parseDouble(exchangeLine[2]);
                graph[a][b] = Math.log(c);
            }

            var result = bellmannFord(graph, 0, n - 1);

            if (result.getRight()) {
                System.out.println("Case #" + t + ": Jackpot");
            } else {
                if (result.getLeft() == Double.MAX_VALUE) {
                    System.out.println("Case #" + t + ": impossible");
                } else {
                    System.out.printf(Locale.US, "Case #%d: %.6f\n", t, Math.exp(result.getLeft()));
                }
            }

            buff.readLine();
        }
    }

    private static Tuple<Double, Boolean> bellmannFord(double[][] graph, int start, int dest) {
        var dist = new double[graph.length];
        Arrays.fill(dist, Double.MAX_VALUE);

        dist[start] = 0;

        // Make this a normal queue
        Queue<Integer> q = new LinkedList<>();
        Queue<Integer> qdash = new LinkedList<>();

        q.add(start);
        for (int i=0; i<graph.length; i++) {
            while (!q.isEmpty()) {
                var v = q.poll();
                for (var w : getNeighbours(graph, v)) {
                    if (dist[v] + graph[v][w] < dist[w]) {
                        dist[w] = dist[v] + graph[v][w];
                        if (!qdash.contains(w)) {
                            qdash.add(w);
                        }
                    }
                }
            }
            var cpy = q;
            q = qdash;
            qdash = cpy;
        }

        return new Tuple<>(dist[dest], !q.isEmpty());
    }

    private static ArrayList<Integer> getNeighbours(double[][] graph, int node) {
        var list = new ArrayList<Integer>();
        for (int i=0; i<graph.length; i++) {
            if (graph[node][i] != Double.NEGATIVE_INFINITY) list.add(i);
        }
        return list;
    }
}
