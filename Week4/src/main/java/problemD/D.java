package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class D {

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
            var n = Integer.parseInt(buff.readLine());
            var graph = new int[n][n];
            var endDist = 0;
            Arrays.stream(graph).forEach(a -> Arrays.fill(a, 0));
            for (int i=0; i<n; i++) {
                var taskLine = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var p = taskLine[0]; // Time units for task i
                var s = taskLine[1]; // Successors for task i
                for (int k=2; k<(s + 2); k++) {
                    var succ = taskLine[k] - 1;
                    graph[i][succ] = p;
                }
                if (i == n - 1) {
                    endDist = p;
                }
            }

            var topoOrder = getTopoOrder(graph);
            var verticesInTopoOrder = IntStream.range(0, n)
                    .boxed()
                    .sorted(Comparator.comparingInt(o -> topoOrder[o]))
                    .mapToInt(Integer::intValue)
                    .toArray();

            var dist = new int[n];
            Arrays.fill(dist, Integer.MIN_VALUE);
            dist[0] = 0;

            for (var v : verticesInTopoOrder) {
                for (var w : getSuccessors(graph, v)) {
                    if ((dist[v] + graph[v][w]) > dist[w]) {
                        dist[w] = dist[v] + graph[v][w];
                    }
                }
            }

            System.out.println("Case #" + t + ": " + (dist[n - 1] + endDist));

            buff.readLine();
        }
    }

    private static int[] getTopoOrder(int[][] graph) {
        var o = new int[graph.length];
        var pre = new int[graph.length];
        Arrays.fill(o, Integer.MAX_VALUE);
        for (int v=0; v<graph.length; v++) {
            pre[v] = getPredecessors(graph, v).size();
        }

        var s = new LinkedList<Integer>();
        var i = 1;

        for (int v=0; v<graph.length; v++) {
            if (pre[v] == 0) {
                s.add(v);
            }
        }

        while (!s.isEmpty()) {
            var v2 = s.poll();
            o[v2] = i++;
            for (var u : getSuccessors(graph, v2)) {
                pre[u] = pre[u] - 1;
                if (pre[u] == 0) {
                    s.add(u);
                }
            }
        }

        return o;
    }

    private static ArrayList<Integer> getSuccessors(int[][] graph, int node) {
        var list = new ArrayList<Integer>();
        for (int i=0; i<graph.length; i++) {
            if (graph[node][i] > 0) list.add(i);
        }
        return list;
    }

    private static ArrayList<Integer> getPredecessors(int[][] graph, int node) {
        var list = new ArrayList<Integer>();
        for (int i=0; i<graph.length; i++) {
            if (graph[i][node] > 0) list.add(i);
        }
        return list;
    }
}
