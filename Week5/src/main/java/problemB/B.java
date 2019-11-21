package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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

            var menuGraphSize = 2 + m + b;
            var menuGraph = new int[menuGraphSize][menuGraphSize];

            // Fill up graph
            IntStream.range(0, menuGraphSize).forEach(i -> Arrays.fill(menuGraph[i], 0));

            for (int meal=0; meal<m; meal++) {
                addEdge(menuGraph, 0, mealIndex(meal));
            }
            for (int bev=0; bev<b; bev++) {
                addEdge(menuGraph, beverageIndex(bev, m), menuGraphSize - 1);
            }

            for (int p=0; p<n; p++) {
                var choiceLine = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var meal = choiceLine[0] - 1;
                var beverage = choiceLine[1] - 1;
                addEdge(menuGraph, mealIndex(meal), beverageIndex(beverage, m));
            }

            System.out.println("Case #" + t + ": " + fordFulkerson(menuGraph, 0, menuGraphSize - 1));

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

    private static int mealIndex(int m) {
        return 1 + m;
    }

    private static int beverageIndex(int b, int m) {
        return 1 + m + b;
    }

    private static void addEdge(int[][] graph, int a, int b) {
        graph[a][b] = 1;
        //graph[b][a] = 1;
    }
}
