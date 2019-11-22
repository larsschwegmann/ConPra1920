package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class B {

    private static int[] blacklist;

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];

            blacklist = new int[13];
            Arrays.fill(blacklist, -1);

            var numVertices = 0;
            var numChapters = new int[n];
            var graph = new ArrayList<ArrayList<Integer>>();

            for (int i=0; i<n; i++) {
                var a = Integer.parseInt(buff.readLine());
                numChapters[i] = a;
                IntStream.range(0, a).forEach(k -> graph.add(new ArrayList<>()));
                for (int j=0; j<a-1; j++) {
                    graph.get(numVertices + j).add(numVertices + j + 1);
                    blacklist[numVertices + j] = numVertices + j + 1;
                }
                numVertices += a;
            }


            for (int i=0; i<m; i++) {
                var cpdq = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var c = cpdq[0] - 1;
                var p = cpdq[1] - 1;
                var d = cpdq[2] - 1;
                var q = cpdq[3] - 1;

                var belowC = 0;
                for (int j=0; j<c; j++) {
                    belowC += numChapters[j];
                }
                var belowD = 0;
                for (int j=0; j<d; j++) {
                    belowD += numChapters[j];
                }

                graph.get(belowC + p).add(belowD + q);
            }

            System.out.println("Case #" + t + ": " + allTopologicalSortAmount(graph));

            buff.readLine();
        }
    }

    private static int allTopologicalSortAmount(ArrayList<ArrayList<Integer>> graph) {
        var indeg = new int[graph.size()];
        for (ArrayList<Integer> list : graph) {
            for (var vertex : list) {
                indeg[vertex]++;
            }
        }

        var visited = new boolean[graph.size()];

        return topoSort(indeg, visited, 0, graph, new ArrayList<>());
    }

    private static int topoSort(int[] indegree, boolean[] visited, int recDepth, ArrayList<ArrayList<Integer>> graph, ArrayList<Integer> sorted) {
        if (sorted.size() == graph.size()) {
            return 1;
        }

        var ready = new ArrayList<Integer>();
        for (int i=0; i<graph.size(); i++) {
            if (indegree[i] == 0 && !visited[i]) {
                ready.add(i);
            }
        }

        if (ready.isEmpty()) {
            return 0;
        }

        var count = 0;
        for (var vertex : ready) {
            if (sorted.size() > 0 && vertex == blacklist[sorted.get(recDepth - 1)]) {
                continue;
            }
            visited[vertex] = true;
            for (var outgoing : graph.get(vertex)) {
                indegree[outgoing]--;
            }
            sorted.add(vertex);
            count += topoSort(indegree, visited, recDepth + 1, graph, sorted);
            visited[vertex] = false;
            for (var outgoing : graph.get(vertex)) {
                indegree[outgoing]++;
            }
            sorted.remove(recDepth);
        }
        return count;
    }
}
