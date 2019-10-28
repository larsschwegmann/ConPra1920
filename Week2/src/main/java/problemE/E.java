package problemE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.IntStream;

public class E {

    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nmLine = buff.readLine().split(" ");
            var n = Integer.parseInt(nmLine[0]);
            var m = Integer.parseInt(nmLine[1]);

            // Prepare unionfind
            var parent = new int[n];
            var size = new int[n];
            var hatreds = new ArrayList<HashSet<Integer>>();
            IntStream.range(0, n).forEach(i -> {
                parent[i] = i;
                size[i] = 1;
                hatreds.add(i, new HashSet<>());
            });

            for (int i=0; i<m; i++) {
                var relationLine = buff.readLine().split(" ");
                var type = relationLine[0].charAt(0);
                var countryA = Integer.parseInt(relationLine[1]) - 1;
                var countryB = Integer.parseInt(relationLine[2]) - 1;

                if (type == 'F') {
                    // Add new friend
                    union(parent, size, hatreds, countryA, countryB);
                } else {
                    var a = find(parent, countryA);
                    var b = find(parent, countryB);
                    hatreds.get(a).add(b);
                    hatreds.get(b).add(a);
                }
            }

            // Compute Friends resulting from hate relationships
            Arrays.stream(parent).distinct().forEach(p -> {
                hatreds.get(p).forEach(h -> {
                    hatreds.get(h).forEach(h2 -> {
                        if (p != h2) {
                            union(parent, size, p, h2);
                        }
                    });
                });
            });

            System.out.println("Case #" + t + ": " + (size[find(parent, 0)] > n/2 ? "yes" : "no"));
            buff.readLine();
        }
    }

    // Unionfind calls

    private static int find(int[] parent, int country) {
        var root = country;
        while (true) {
            var p = parent[root];
            if (p == root) {
                break;
            }
            root = p;
        }
        // Compress path
        var current = country;
        while (current != root) {
            var next = parent[current];
            parent[current] = root;
            current = next;
        }

        return root;
    }

    private static void union(int[] parent, int[] size, ArrayList<HashSet<Integer>> hatreds, int countryA, int countryB) {
        var a = find(parent, countryA);
        var b = find(parent, countryB);
        if (a == b) {
            return;
        }

        var aSize = size[a];
        var bSize = size[b];

        if (aSize < bSize) {
            var temp = a;
            a = b;
            b = temp;
        }

        parent[b] = a;
        size[a] = aSize + bSize;
        hatreds.get(a).addAll(hatreds.get(b));
    }
    private static void union(int[] parent, int[] size, int countryA, int countryB) {
        var a = find(parent, countryA);
        var b = find(parent, countryB);
        if (a == b) {
            return;
        }

        var aSize = size[a];
        var bSize = size[b];

        if (aSize < bSize) {
            var temp = a;
            a = b;
            b = temp;
        }

        parent[b] = a;
        size[a] = aSize + bSize;
    }
}
