package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class C {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];

            var graphSize = 2 * n - 2;
            var graph = new int[graphSize][graphSize];
            IntStream.range(0, graphSize).forEach(i -> Arrays.fill(graph[i], 0));
            for (int i=1; i<n-1; i++) {
                var p = i * 2 - 1;
                graph[p][p + 1] = 1;
                graph[p + 1][p] = 1;
            }

            for (int i=0; i<m; i++) {
                var conn = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var a = conn[0] - 1;
                var b = conn[1] - 1;

                addEdge(graph, a, b);
            }

            var val = fordFulkerson(graph, 0, graphSize - 1);

            System.out.println("Case #" + t + ": " + val);

            buff.readLine();
        }
    }

    // Parts of this ford fulkerson implementation were heavily inspired by https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
    private static int fordFulkerson(int[][] graph, int source, int sink) {
        var residualNetwork = new int[graph.length][graph.length];
        IntStream.range(0, graph.length).forEach(i -> residualNetwork[i] = Arrays.copyOf(graph[i], graph.length));

        var parent = new int[graph.length];

        int maxFlow = 0;

        while (bfs(residualNetwork, source, sink, parent)) {
            int maxFlowCurrentPath = Integer.MAX_VALUE;

            for (int c=sink; c!=source; c=parent[c]) {
                int pred = parent[c];
                maxFlowCurrentPath = Math.min(maxFlowCurrentPath, residualNetwork[pred][c]);
            }

            for (int c=sink; c!=source; c=parent[c]) {
                int pred = parent[c];
                residualNetwork[pred][c] -= maxFlowCurrentPath;
                residualNetwork[c][pred] += maxFlowCurrentPath;
            }

            maxFlow += maxFlowCurrentPath;
        }

        return maxFlow;
    }

    private static boolean bfs(int[][] residualNetwork, int source, int sink, int[] parent) {
        var visited = new boolean[residualNetwork.length];
        Arrays.fill(visited, false);

        Queue<Integer> next = new LinkedList<>();
        next.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!next.isEmpty()) {
            var vertex = next.poll();
            for (Integer succ : getNewSuccessors(residualNetwork, visited, vertex)) {
                next.add(succ);
                parent[succ] = vertex;
                visited[succ] = true;
                if (succ == sink) {
                    return visited[sink];
                }
            }
        }

        return visited[sink];
    }

    private static ArrayList<Integer> getNewSuccessors(int[][] graph, boolean[] visited, int source) {
        var retval = new ArrayList<Integer>();
        for (int i=0; i<graph.length; i++) {
            if (graph[source][i] > 0 && !visited[i]) {
                retval.add(i);
            }
        }
        return retval;
    }

    private static void addEdge(int[][] graph, int a, int b) {
        if (a != 0)
            a = a * 2 - 1;
        if (b != 0)
            b = b * 2 - 1;
        graph[a == 0 || a == graph.length - 1 ? a : a + 1][b] = 1;
        graph[b == 0 || b == graph.length - 1 ? b : b + 1][a] = 1;
    }
}
