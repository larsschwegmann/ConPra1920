package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class B {
    public static void main(String[] args) throws IOException {
        //var file = new FileInputStream("/Users/lars/Documents/ConPra/Solutions/Week3/src/test/resources/problemB/dimi.in");
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        caseIter: for (int t=1; t<=casesCount; t++) {
            var nkrd = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nkrd[0];
            var k = nkrd[1];
            var r = nkrd[2];
            var d = nkrd[3];

            System.out.print("Case #" + t + ": ");

            var packagesToKeep = Arrays.stream(buff.readLine().split(" "))
                    .filter(a -> !a.equals(""))
                    .mapToInt(Integer::parseInt)
                    .distinct()
                    .map(a -> a - 1)
                    .boxed()
                    .collect(Collectors.toSet());

            var packagesToRemove = new HashSet<Integer>(r);
            var toRemove = Arrays.stream(buff.readLine().split(" "))
                    .filter(a -> !a.equals(""))
                    .mapToInt(Integer::parseInt)
                    .distinct()
                    .map(a -> a - 1)
                    .toArray();

            for (var pack : toRemove) {
                if (packagesToKeep.contains(pack)) {
                    System.out.println("conflict");
                    for (int i=0; i<=d; i++) buff.readLine();
                    continue caseIter;
                }
                packagesToRemove.add(pack);
            }

            ArrayList<Integer>[] successors = new ArrayList[n];
            IntStream.range(0, n).forEach(i -> successors[i] = new ArrayList<>());

            for (int i=0; i<d; i++) {
                var dep = Arrays.stream(buff.readLine().split(" "))
                        .mapToInt(Integer::parseInt)
                        .map(a -> a - 1)
                        .toArray();
                successors[dep[0]].add(dep[1]);
            }

            for (var keep : packagesToKeep) {
                var s = new Stack<Integer>();
                var visited = new HashSet<Integer>();
                s.push(keep);

                while (!s.isEmpty()) {
                    var x = s.pop();

                    if (packagesToRemove.contains(x)) {
                        System.out.println("conflict");
                        buff.readLine();
                        continue caseIter;
                    }

                    if (visited.contains(x)) {
                        continue;
                    }

                    successors[x].stream().filter(y -> !visited.contains(y)).forEach(s::push);
                    visited.add(x);
                }
            }

            System.out.println("ok");

            buff.readLine();
        }
    }
}
