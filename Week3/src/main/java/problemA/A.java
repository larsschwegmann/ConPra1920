package problemA;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class A {

    private static class Edge {
        int source;
        int dest;
        int dist;
        Edge(int source, int dest, int dist) {
            this.source = source;
            this.dest = dest;
            this.dist = dist;
        }

        int getSource() {
            return Math.min(source, dest);
        }

        int getDest() {
            return Math.max(source, dest);
        }

        int getDist() {
            return dist;
        }

        @Override
        public String toString() {
            if (this.source < this.dest) {
                return (this.source + 1) + " " + (this.dest + 1) + "\n";
            } else {
                return (this.dest + 1) + " " + (this.source + 1) + "\n";
            }

        }
    }

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var n = Integer.parseInt(buff.readLine());
            var adjacencies = new int[n][n];

            // Read adjacency matrix
            for (int i=0; i<n; i++) {
                adjacencies[i] = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            }

            var prioQueue = new PriorityQueue<>((n*(n-1))/2, Comparator.comparingInt(Edge::getDist));

            // Fill prio queue for prims algorithm
            for (int i=1; i<n; i++) {
                prioQueue.add(new Edge(0, i, adjacencies[0][i]));
            }

            var result = new ArrayList<Edge>();
            var visited = new boolean[n];
            Arrays.fill(visited, false);

            visited[0] = true;

            while (!prioQueue.isEmpty()) {
                var edge = prioQueue.poll();
                if (visited[edge.dest]) {
                    continue;
                }
                result.add(edge);

                visited[edge.dest] = true;

                for (int u=0; u<n; u++) {
                    if (u == edge.dest || visited[u]) {
                        continue;
                    }
                    prioQueue.add(new Edge(edge.dest, u, adjacencies[edge.dest][u]));
                }

            }

            result.sort(Comparator.comparing(Edge::getSource).thenComparing(Edge::getDest));

            var sb = new StringBuilder("Case #");
            sb.append(t);
            sb.append(":\n");
            result.stream().map(Objects::toString).forEach(sb::append);
            System.out.print(sb.toString());
            buff.readLine();
        }

    }
}
