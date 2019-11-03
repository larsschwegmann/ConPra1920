package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class C {

    private static class RoadGraph {
        private int[][] adjacencies;

        RoadGraph(int intersectionsCount) {
            this.adjacencies = new int[intersectionsCount][intersectionsCount];
            Arrays.stream(this.adjacencies).forEach(a -> Arrays.fill(a, 0));
        }

        void addOnewayRoad(int a, int b) {
            this.adjacencies[a][b] = 1;
        }

        int[] getTopoOrder() {
            var o = new int[this.adjacencies.length];
            var pre = new int[this.adjacencies.length];
            Arrays.fill(o, Integer.MAX_VALUE);
            for (int v=0; v<this.adjacencies.length; v++) {
                pre[v] = getIncomingRoads(v).size();
            }

            var s = new LinkedList<Integer>();
            var i = 1;

            for (int v=0; v<this.adjacencies.length; v++) {
                if (pre[v] == 0) {
                    s.add(v);
                    while (!s.isEmpty()) {
                        var v2 = s.poll();
                        o[v2] = i++;
                        for (var u : getOutgoingRoads(v2)) {
                            pre[u]--;
                            if (pre[u] == 0) {
                                s.add(u);
                            }
                        }
                    }
                }
            }

            return o;
        }

        boolean isAcyclic() {
            var o = new int[this.adjacencies.length];
            var pre = new int[this.adjacencies.length];
            Arrays.fill(o, Integer.MAX_VALUE);
            for (int v=0; v<this.adjacencies.length; v++) {
                pre[v] = getIncomingRoads(v).size();
            }

            var s = new LinkedList<Integer>();
            var i = 1;

            for (int v=0; v<this.adjacencies.length; v++) {
                if (pre[v] == 0) {
                    s.add(v);
                    while (!s.isEmpty()) {
                        var v2 = s.poll();
                        o[v2] = i++;
                        for (var u : getOutgoingRoads(v2)) {
                            pre[u]--;
                            if (pre[u] == 0) {
                                s.add(u);
                            }
                        }
                    }
                }
            }

            for (var r : o) {
                if (r == Integer.MAX_VALUE) return false;
            }
            return true;
        }

        private List<Integer> getOutgoingRoads(int intersection) {
            var result = new ArrayList<Integer>();
            for (int i=0; i<adjacencies.length; i++) {
                if (this.adjacencies[intersection][i] != 0) {
                    result.add(i);
                }
            }
            return result;
        }

        private List<Integer> getIncomingRoads(int intersection) {
            var result = new ArrayList<Integer>();
            for (int i=0; i<adjacencies.length; i++) {
                if (this.adjacencies[i][intersection] != 0) {
                    result.add(i);
                }
            }
            return result;
        }
    }

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nml = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nml[0];
            var m = nml[1];
            var l = nml[2];

            var graph = new RoadGraph(n);

            for (int i=0; i<m; i++) {
                var ab = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).map(a -> a - 1).toArray();
                var a = ab[0];
                var b = ab[1];
                graph.addOnewayRoad(a, b);
            }

            if (!graph.isAcyclic()) {
                System.out.println("Case #" + t + ": no");
                for (int i=0; i<=l; i++) buff.readLine();
                continue;
            }

            System.out.println("Case #" + t + ": yes");

            var order = graph.getTopoOrder();

            var sb = new StringBuilder();

            for (int i=0; i<l; i++) {
                var ab = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var a = ab[0];
                var b = ab[1];
                var posA = order[a - 1];
                var posB = order[b - 1];
                if (posA <= posB) {
                    sb.append(a);
                    sb.append(" ");
                    sb.append(b);
                    sb.append("\n");
                } else {
                    sb.append(b);
                    sb.append(" ");
                    sb.append(a);
                    sb.append("\n");
                }
            }
            System.out.print(sb.toString());
            buff.readLine();
        }
    }
}
