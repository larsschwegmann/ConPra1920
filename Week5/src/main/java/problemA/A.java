package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.IntStream;

public class A {

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];

            var graph = new int[n][n];
            IntStream.range(0, n).forEach(i -> Arrays.fill(graph[i], 0));

            for (int i=0; i<m; i++) {
                var abw = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var a = abw[0] - 1;
                var b = abw[1] - 1;
                var w = abw[2];

                graph[a][b] = graph[a][b] + w;
                graph[b][a] = graph[b][a] + w;
            }

            var result = fordFulkerson(graph, 0, n - 1);

            System.out.println("Case #" + t + ": " + (result > 0 ? result : "impossible"));

            buff.readLine();
        }
    }

    // Parts of this ford fulkerson implementation were heavily inspired by https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
    private static int fordFulkerson(int[][] graph, int source, int sink) {
        var residualNetwork = new int[graph.length][graph.length];
        IntStream.range(0, graph.length).forEach(i -> residualNetwork[i] = Arrays.copyOf(graph[i], graph.length));

        var parent = new int[graph.length];

        int maxFlow = 0;

        while (dfs(residualNetwork, source, sink, parent)) {
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

    private static boolean dfs(int[][] residualNetwork, int source, int sink, int[] parent) {
        var visited = new boolean[residualNetwork.length];
        Arrays.fill(visited, false);

        var next = new Stack<Integer>();
        next.push(source);
        visited[source] = true;
        parent[source] = -1;

        while (!next.isEmpty()) {
            var vertex = next.pop();
            getNewSuccessors(residualNetwork, visited, vertex).forEach(succ -> {
                next.push(succ);
                parent[succ] = vertex;
                visited[succ] = true;
            });
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
}
