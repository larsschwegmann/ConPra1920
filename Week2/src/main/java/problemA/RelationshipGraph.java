package problemA;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

class RelationshipGraph {

    // Graph properties
    private int[] wealth; // stores wealth of notable people in the world

    // Union Find Properties
    private int[] parent; // Parent of an element in `wealth`
    private int[] size; // Size of a subset of `wealth`
    private boolean[] married;

    // Constructor
    RelationshipGraph(int notablePeopleCount) {
        this.wealth = new int[notablePeopleCount - 1];
        this.parent = new int[notablePeopleCount];
        this.size = new int[notablePeopleCount];
        this.married = new boolean[notablePeopleCount - 1];

        // Fill with default values
        Arrays.fill(this.wealth, 0);
        //Arrays.stream(this.edges).forEach(arr -> Arrays.fill(arr, 0));
        IntStream.range(0, this.parent.length).forEach(i -> this.parent[i] = i);
        Arrays.fill(this.size, 1);
        Arrays.fill(this.married, false);
    }

    // Public functions

    void setWealth(int person, int wealth) {
        this.wealth[person] = wealth;
    }

    void addRelationship(int personA, int personB) {
        union(personA, personB);
    }

    void addMarriage(int personA, int personB) {
        this.married[personA] = true;
        this.married[personB] = true;
        union(personA, personB);
    }

    int findWealthiestPartner() {
        var fugger = this.wealth.length;
        var fuggerSet = find(fugger);
        var wealthiest = IntStream.range(0, fugger)
                .filter(v -> !this.married[v] && fuggerSet != find(v)) // Remove all married people and people who are related to fugger
                .map(i -> this.wealth[i]) // Map indices of people to their wealthiness
                .boxed()
                .sorted(Comparator.reverseOrder()) // Sort by descending wealthiness
                .mapToInt(Integer::intValue)
                .findFirst(); // Get wealthiest person
        return wealthiest.orElse(-1);
    }

    // Private functions

    private int find(int person) {
        var root = person;
        while (true) {
            var parent = this.parent[root];
            if (parent == root) {
                break;
            }
            root = parent;
        }
        // Compress path
        var current = person;
        while (current != root) {
            var next = this.parent[current];
            this.parent[current] = root;
            current = next;
        }

        return root;
    }

    private void union(int personA, int personB) {
        var a = find(personA);
        var b = find(personB);
        if (a == b) {
            return;
        }

        var aSize = this.size[a];
        var bSize = this.size[b];

        if (aSize < bSize) {
            a = b;
            b = a;
        }

        this.parent[b] = a;
        this.size[a] = aSize + bSize;
    }

}
