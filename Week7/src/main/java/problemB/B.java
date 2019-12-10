package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class B {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];

            int[] facilityCosts = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int[][] deliveryCosts = new int[n][m];

            for (int i=0; i<n; i++) {
                deliveryCosts[i] = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            }

            HashSet<Integer>[] assignedCustomers = new HashSet[n];
            int[] suppliers = new int[m];
            IntStream.range(0, n).forEach(i -> assignedCustomers[i] = new HashSet<>());
            Arrays.fill(suppliers, -1);

            for (int i=0; i<m; i++) {
                var bestCustomer = -1;
                var currentBestFacility = -1;
                var currentBestFacilityCost = Integer.MAX_VALUE;
                for (int c=0; c<m; c++){
                    if (suppliers[c] != -1) {
                        continue;
                    }

                    var bestFacility = -1;
                    var bestFacilityCost = Integer.MAX_VALUE;
                    for (int f=0; f<n; f++) {
                        var cost = deliveryCosts[f][c];
                        if (assignedCustomers[f].isEmpty()) {
                            cost += facilityCosts[f];
                        }
                        if (cost < bestFacilityCost) {
                            bestFacility = f;
                            bestFacilityCost = cost;
                        }
                    }

                    if (bestFacilityCost < currentBestFacilityCost) {
                        bestCustomer = c;
                        currentBestFacility = bestFacility;
                        currentBestFacilityCost = bestFacilityCost;
                    }
                }

                setSupplier(bestCustomer, currentBestFacility, suppliers, assignedCustomers);
            }

            var improved = true;
            long currentCost = calcCost(facilityCosts, deliveryCosts, assignedCustomers);

            while (improved) {
                improved = false;
                for (int c=0; c<m; c++) {
                    for (int f=0; f<n; f++) {
                        if (suppliers[c] == f) {
                            continue;
                        }

                        var oldSupplier = suppliers[c];
                        switchSupplier(c, f, suppliers, assignedCustomers);
                        long newCost = calcCost(facilityCosts, deliveryCosts, assignedCustomers);

                        if (newCost < currentCost) {
                            currentCost = newCost;
                            improved = true;
                        } else {
                            switchSupplier(c, oldSupplier, suppliers, assignedCustomers);
                        }
                    }
                }
            }

            // Output result

            System.out.println("Case #" + t + ": " + calcCost(facilityCosts, deliveryCosts, assignedCustomers));

            for (int f=0; f<n; f++) {
                if (assignedCustomers[f].isEmpty()) {
                    continue;
                }
                System.out.println((f + 1) + " " + (assignedCustomers[f].stream().map(a -> a + 1).map(Object::toString).collect(Collectors.joining(" "))));
            }

            buff.readLine();
        }
    }

    private static void switchSupplier(int customer, int newSupplier, int[] suppliers, HashSet<Integer>[] assignedCustomers) {
        var old = suppliers[customer];
        assignedCustomers[old].remove(customer);
        setSupplier(customer, newSupplier, suppliers, assignedCustomers);
    }

    private static void setSupplier(int customer, int newSupplier, int[] suppliers, HashSet<Integer>[] assignedCustomers) {
        assignedCustomers[newSupplier].add(customer);
        suppliers[customer] = newSupplier;
    }

    private static long calcCost(int[] facilityCosts, int[][] deliveryCosts, HashSet<Integer>[] assignedCustomers) {
        long cost = 0;
        for (int f=0; f<assignedCustomers.length; f++) {
            if (!assignedCustomers[f].isEmpty()) {
                for (var c : assignedCustomers[f]) {
                    cost += deliveryCosts[f][c];
                }
                cost += facilityCosts[f];
            }
        }
        return cost;
    }
}
