package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class D {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];
            var teamWins = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();

            var matches = new int[n][n];
            IntStream.range(0, n).forEach(i -> Arrays.fill(matches[i], 0));

            for (int i=0; i<m; i++) {
                // Read matches
                var match = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var a = match[0] - 1;
                var b = match[1] - 1;
                matches[a][b]++;
                matches[b][a]++;
            }

            var sb = new StringBuilder("Case #" + t + ": ");

            var eliminated = new ArrayList<Integer>();
            var maxTeamWins = Arrays.stream(teamWins).max().orElse(0);

            outer: for (int i=0; i<n; i++) {
                var remainingMatchesForTeam = Arrays.stream(matches[i]).reduce(Integer::sum).orElse(0);
                var wins = teamWins[i];

                if (maxTeamWins > wins + remainingMatchesForTeam) {
                    eliminated.add(i);
                    sb.append("no");
                    if (i != n - 1)
                        sb.append(" ");
                    continue;
                }

                for (var te : eliminated) {
                    var remainingTe = Arrays.stream(matches[te]).reduce(Integer::sum).orElse(0);
                    var teWins = teamWins[te];
                    if (teWins + remainingTe >= remainingMatchesForTeam + wins) {
                        eliminated.add(i);
                        sb.append("no");
                        if (i != n - 1)
                            sb.append(" ");
                        continue outer;
                    }
                }

                var flowGraph = constructFlowGraph(i, matches, teamWins, remainingMatchesForTeam);
                var maxFlow = fordFulkerson(flowGraph, 0, flowGraph.length - 1);
                var outgoingSourceArcs = Arrays.stream(flowGraph[0]).reduce(Integer::sum).orElse(0);
                boolean elim = maxFlow < outgoingSourceArcs;
                if (elim) eliminated.add(i);
                sb.append(elim ? "no" : "yes");

                if (i != n - 1)
                    sb.append(" ");
            }

            System.out.println(sb.toString());
            buff.readLine();
        }
    }

    private static int[][] constructFlowGraph(int team, int[][] matches, int[] teamWins, int remainingMatchesForTeam) {
        var distinctMatchCount = (matches.length * (matches.length - 1)) / 2;

        // Init flow graph
        var graphSize = 2 + distinctMatchCount + matches.length;
        var flowGraph = new int[graphSize][graphSize];

        // Create team nodes
        for (int t=0; t<matches.length; t++) {
            flowGraph[teamNodeIndex(t, distinctMatchCount)][graphSize - 1] = teamWins[team] + remainingMatchesForTeam - teamWins[t];
        }

        // Create match nodes
        for (int i=0; i<matches.length; i++) {
            if (i == team) continue;
            for (int k=i+1; k<matches.length; k++) {
                if (k == team) continue;
                if (matches[i][k] > 0) {
                    var nodeIndex = gameNodeIndex(i, k, matches.length);
                    flowGraph[0][nodeIndex] = matches[i][k];
                    flowGraph[nodeIndex][teamNodeIndex(i, distinctMatchCount)] = Integer.MAX_VALUE;
                    flowGraph[nodeIndex][teamNodeIndex(k, distinctMatchCount)] = Integer.MAX_VALUE;
                }
            }
        }

        return flowGraph;
    }

    private static int gameNodeIndex(int i, int k, int arrayLength) {
        return (arrayLength * (arrayLength - 1) / 2) - (arrayLength - i) * ((arrayLength - i) - 1) / 2 + k - i;
    }

    private static int teamNodeIndex(int teamIndex, int distinctMatchCount) {
        return 1 + distinctMatchCount + teamIndex;
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
}
