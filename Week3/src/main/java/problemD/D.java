package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class D {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var n = Integer.parseInt(buff.readLine());
            Set<Integer>[] successors = new Set[n];
            IntStream.range(0, n).forEach(i -> successors[i] = new HashSet<>());

            for (int i=0; i<n-1; i++) {
                var xy = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var x = xy[0];
                var y = xy[1];
                successors[x].add(y);
                successors[y].add(x);
            }

            var startPath = getLongestSubtree(successors, new HashSet<>(), new Random().nextInt(n));
            var farthest = startPath.get(startPath.size() - 1);
            var finalPath = getLongestSubtree(successors, new HashSet<>(), farthest);

            System.out.println("Case #" + t + ": " + finalPath.get(finalPath.size() / 2));
            buff.readLine();
        }
    }

    private static ArrayList<Integer> getLongestSubtree(Set<Integer>[] successors, HashSet<Integer> visited, int start) {
        visited.add(start);
        var longest = new ArrayList<Integer>();
        for (var succ : successors[start]) {
            if (!visited.contains(succ)) {
                var tree = getLongestSubtree(successors, visited, succ);
                if (tree.size() > longest.size()) {
                    longest = tree;
                }
            }
        }
        longest.add(0, start);
        return longest;
    }
}
