package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class D {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];

            var l = new PriorityQueue<Integer>(Comparator.reverseOrder());
            var sawQueues = new long[m];
            Arrays.fill(sawQueues, 0);

            for (int i=0; i<n; i++) {
                l.add(Integer.parseInt(buff.readLine()));
            }

            var time = 0L;

            while (!l.isEmpty()) {
                var tree = l.poll();
                int insertIndex = shortestSawQueueIndex(sawQueues);
                sawQueues[insertIndex] += tree;
                time = Math.max(time, sawQueues[insertIndex]);
            }

            System.out.println("Case #" + t + ": " + time);

            buff.readLine();
        }
    }

    private static int shortestSawQueueIndex(long[] queues) {
        int shortestIndex = 0;
        for (int i=0; i<queues.length; i++) {
            if (queues[i] < queues[shortestIndex]) {
                shortestIndex = i;
            }
        }
        return shortestIndex;
    }
}
