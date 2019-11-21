package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class B_dimi {
    static ArrayList<ArrayList<Integer>> flowGraph = new ArrayList<>();
    static FlowGraphEdge[][] flowGraphEdges;

    static ArrayList<ArrayList<Integer>> residualGraph = new ArrayList<>();
    static int[][] residualGraphEdges;

    static int src = 0;
    static int target;

    static int numPeople;
    static int numNodes;

    public static void main(String[] args) throws IOException {
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(reader);
        int testCases = Integer.parseInt(in.readLine());

        for (int i = 1; i <= testCases; i++) {
            String[] input = in.readLine().split(" ");
            numPeople = Integer.parseInt(input[0]);
            int numDishes = Integer.parseInt(input[1]);
            int numBeverages = Integer.parseInt(input[2]);

            numNodes = numDishes + numBeverages + 2;
            target = numNodes - 1;

            residualGraphEdges = new int[numNodes][numNodes];
            flowGraphEdges = new FlowGraphEdge[numNodes][numNodes];
            flowGraph = new ArrayList<>();
            residualGraph = new ArrayList<>();

            for (int j = 0; j < numNodes; j++) {
                flowGraph.add(new ArrayList<>());
                residualGraph.add(new ArrayList<>());
            }

            // init edges from source to every person
            for (int j = 0; j < numDishes; j++) {
                flowGraph.get(src).add(j + 1);
                flowGraphEdges[src][j + 1] = new FlowGraphEdge(1);
                residualGraph.get(src).add(j + 1);
                residualGraphEdges[src][j + 1] = 1;
            }

            for (int j = 0; j < numPeople; j++) {
                String[] edges = in.readLine().split(" ");
                int dish = Integer.parseInt(edges[0]);
                int drink = Integer.parseInt(edges[1]);

                int drinkId = numDishes + drink;

                flowGraph.get(dish).add(drinkId);
                flowGraphEdges[dish][drinkId] = new FlowGraphEdge(1);
                residualGraphEdges[dish][drinkId] = 1;
                residualGraph.get(dish).add(drinkId);
            }

            for (int j = 0; j < numBeverages; j++) {
                int beverageId = numDishes + j + 1;
                flowGraph.get(beverageId).add(target);
                flowGraphEdges[beverageId][target] = new FlowGraphEdge(1);
                residualGraph.get(beverageId).add(target);
                residualGraphEdges[beverageId][target] = 1;
            }

            dfs(src, target, residualGraph, numNodes);
            int maxFlow = computeMaxFlow(numDishes + 1, numBeverages);
            System.out.println("Case #" + i + ": " + maxFlow);
            in.readLine();
        }
    }

    static int computeMaxFlow(int numBeveragesStart, int numBeverages) {
        int maxFlow = 0;

        for (int j = numBeveragesStart; j < numBeveragesStart + numBeverages; j++) {
            maxFlow += flowGraphEdges[j][target].currentFlow;
        }
        return maxFlow;
    }

    // find augmenting path with dfs
    static void dfs(int src, int target, ArrayList<ArrayList<Integer>> residualGraph, int numNodes) {
        while (true) {
            if (src == target) {
                flowGraphEdges[src][src].currentFlow = flowGraphEdges[src][src].capacity;
                break;
            }
            LinkedList<Integer> stack = new LinkedList<>();
            int[] parent = new int[numNodes];
            parent[src] = -1;
            parent[target] = -1;
            ArrayList<Integer> residualPath = new ArrayList<>();

            HashSet<Integer> visited = new HashSet<>();
            stack.add(src);

            while (!stack.isEmpty()) {
                int v = stack.pop();
                if (visited.contains(v)) {
                    continue;
                }
                visited.add(v);
                for (int neighbor: residualGraph.get(v)) {
                    if (visited.contains(neighbor)) {
                        continue;
                    }
                    if (residualGraphEdges[v][neighbor] == 0) {
                        continue;
                    }
                    stack.push(neighbor);
                    parent[neighbor] = v;
                }
            }

            if (parent[target] == -1) {
                break;
            }

            int curr = parent[target];
            int desc = target;
            while (curr != -1) {
                residualPath.add(desc);
                desc = curr;
                curr = parent[curr];
            }
            residualPath.add(src);
            Collections.reverse(residualPath);

            fordFulkerson(residualPath);
        }
    }

    static void fordFulkerson(ArrayList<Integer> residualPath) {
        // Augment path
        for (int i = 0; i < residualPath.size() - 1; i++) {
            int v1 = residualPath.get(i);
            int v2 = residualPath.get(i + 1);

            if (flowGraphEdges[v1][v2] == null) {
                flowGraphEdges[v2][v1].currentFlow -= 1;
                residualGraphEdges[v2][v1] = flowGraphEdges[v2][v1].capacity - flowGraphEdges[v2][v1].currentFlow;
                residualGraphEdges[v1][v2] = 0;
                residualGraph.get(v1).add(v2);
            } else {
                flowGraphEdges[v1][v2].currentFlow += 1;
                residualGraphEdges[v1][v2] = flowGraphEdges[v1][v2].capacity - flowGraphEdges[v1][v2].currentFlow;
                residualGraphEdges[v2][v1] = 1;
                residualGraph.get(v2).add(v1);
            }
        }
    }
}

class FlowGraphEdge {
    int capacity;
    int currentFlow;

    FlowGraphEdge(int capacity) {
        this.capacity = capacity;
        currentFlow = 0;
    }
}
