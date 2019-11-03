package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class B {
    public static void main(String[] args) throws IOException {
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

            var packagesToRemove = new ArrayList<Integer>(r);
            var conflict = false;
            var toRemove = Arrays.stream(buff.readLine().split(" "))
                    .filter(a -> !a.equals(""))
                    .mapToInt(Integer::parseInt)
                    .distinct()
                    .map(a -> a - 1)
                    .toArray();

            for (var pack : toRemove) {
                if (packagesToKeep.contains(pack) && !conflict) {
                    System.out.println("conflict");
                    conflict = true;
                }
                packagesToRemove.add(pack);
            }

            TreeSet<Integer>[] predecessors = new TreeSet[n];
            IntStream.range(0, n).forEach(i -> predecessors[i] = new TreeSet<>());

            for (int i=0; i<d; i++) {
                var dep = Arrays.stream(buff.readLine().split(" "))
                        .mapToInt(Integer::parseInt)
                        .map(a -> a - 1)
                        .toArray();
                predecessors[dep[1]].add(dep[0]);
            }

            if (!conflict) {
                for (var pack : packagesToRemove) {
                    var s =  new Stack<Integer>();
                    predecessors[pack].forEach(s::push);

                    while (!s.isEmpty()) {
                        var pred = s.pop();
                        if (packagesToKeep.contains(pred) || pred.equals(pack)) {
                            System.out.println("conflict");
                            buff.readLine();
                            continue caseIter;
                        }

                        predecessors[pred].forEach(s::push);
                    }
                }
                System.out.println("ok");
            }

            buff.readLine();
        }
    }
}
