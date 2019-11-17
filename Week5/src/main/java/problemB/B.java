package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.IntStream;

public class B {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nmb = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nmb[0];
            var m = nmb[1];
            var b = nmb[2];

            var mealGraphSize = 2 + n + m;
            var bevGraphSize = 2 + n + b;

            var mealGraph = new int[mealGraphSize][mealGraphSize];
            var bevGraph = new int[bevGraphSize][bevGraphSize];

            // Fill up graphs
            IntStream.range(0, mealGraphSize).forEach(i -> Arrays.fill(mealGraph[i], 0));
            IntStream.range(0, bevGraphSize).forEach(i -> Arrays.fill(bevGraph[i], 0));

            for (int meal=0; meal<m; meal++) {
                mealGraph[menuItemIndex(meal, n)][mealGraphSize - 1] = 1;
                mealGraph[mealGraphSize - 1][menuItemIndex(meal, n)] = 1;
            }
            for (int bev=0; bev<b; bev++) {
                bevGraph[menuItemIndex(bev, n)][bevGraphSize - 1] = 1;
                bevGraph[bevGraphSize - 1][menuItemIndex(bev, n)] = 1;
            }

            for (int p=0; p<n; p++) {
                // Populate graph
                mealGraph[0][personIndex(p)] = 1;
                mealGraph[personIndex(p)][0] = 1;
                bevGraph[0][personIndex(p)] = 1;
                bevGraph[personIndex(p)][0] = 1;

                var choiceLine = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var meal = choiceLine[0] - 1;
                var beverage = choiceLine[1] - 1;
                mealGraph[personIndex(p)][menuItemIndex(meal, n)] = 1;
                mealGraph[menuItemIndex(meal, n)][personIndex(p)] = 1;
                bevGraph[personIndex(p)][menuItemIndex(beverage, n)] = 1;
                bevGraph[menuItemIndex(beverage, n)][personIndex(p)] = 1;
            }

            var mealResult = fordFulkerson(mealGraph, 0, mealGraphSize - 1);
            var bevResult = fordFulkerson(bevGraph, 0, bevGraphSize - 1);
            var result = Math.min(mealResult, bevResult);

            System.out.println("Case #" + t + ": " + result);

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

    private static int personIndex(int p) {
        return 1 + p;
    }

    private static int menuItemIndex(int m, int personOffset) {
        return 1 + personOffset + m;
    }
}
