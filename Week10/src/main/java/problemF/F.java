package problemF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class F {

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];

            var flowGraph = new double[n][n];
            IntStream.range(0, n).forEach(i -> Arrays.fill(flowGraph[i], 0));

            for (int i = 0; i < m; i++) {
                var line = buff.readLine().split(" ");
                var from = Integer.parseInt(line[0]) - 1;
                var to = Integer.parseInt(line[1]) - 1;
                var sideCount = Integer.parseInt(line[2]);
                var sideLength = Double.parseDouble(line[3]);

                if (sideCount > 0) {
                    double p = sideCount * sideLength;
                    double a = sideLength / (2 * Math.tan(Math.toRadians(180) / sideCount));
                    double area = (p * a) / 2;
                    addCapacity(flowGraph, from, to, area);
                } else {
                    double area = Math.PI * Math.pow(sideLength, 2);
                    addCapacity(flowGraph, from, to, area);
                }
            }

            var result = fordFulkerson(flowGraph, 0, n - 1);
            System.out.println("Case #" + t + ": " + (result == 0 ? "impossible" : result));

            buff.readLine();
        }
    }

    // Parts of this ford fulkerson implementation were heavily inspired by https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
    private static double fordFulkerson(double[][] graph, int source, int sink) {
        var residualNetwork = new double[graph.length][graph.length];
        IntStream.range(0, graph.length).forEach(i -> residualNetwork[i] = Arrays.copyOf(graph[i], graph.length));

        var parent = new int[graph.length];

        double maxFlow = 0;

        while (bfs(residualNetwork, source, sink, parent)) {
            double maxFlowCurrentPath = Integer.MAX_VALUE;

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

    private static boolean bfs(double[][] residualNetwork, int source, int sink, int[] parent) {
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

    private static ArrayList<Integer> getNewSuccessors(double[][] graph, boolean[] visited, int source) {
        var retval = new ArrayList<Integer>();
        for (int i=0; i<graph.length; i++) {
            if (graph[source][i] > 0 && !visited[i]) {
                retval.add(i);
            }
        }
        return retval;
    }

    private static void addCapacity(double[][] graph, int from, int to, double capacity) {
        if (graph[from][to] > 0 || graph[to][from] > 0) {
            graph[from][to] += capacity;
            graph[to][from] += capacity;
        } else {
            graph[from][to] = capacity;
            graph[to][from] = capacity;
        }
    }
}
