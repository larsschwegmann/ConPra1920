package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class C {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            System.out.println("Case #" + t + ":");
            var n = Integer.parseInt(buff.readLine());

            var adjacencies = new ArrayList<ArrayList<Integer>>();
            IntStream.range(0, n).forEach(i -> adjacencies.add(new ArrayList<>()));

            for (int i=0; i<n; i++) {
                int finalI = i;
                Arrays.stream(buff.readLine().split(" "))
                        .mapToInt(Integer::parseInt)
                        .skip(1)
                        .map(r -> r - 1)
                        .forEach(r -> adjacencies.get(finalI).add(r));
            }

            var countryA = IntStream.range(0, n).boxed().collect(Collectors.toCollection(HashSet::new));
            var countryB = new HashSet<Integer>();

            for (int i=0; i<n; i++) {
                var currentCountry = countryForCity(i, countryA, countryB);
                HashSet<Integer> otherCountry;
                if (currentCountry == countryA) {
                    otherCountry = countryB;
                } else {
                    otherCountry = countryA;
                }

                var adjCurrent = outgoingRoadsToCountry(i, currentCountry, adjacencies);
                var adjOther = outgoingRoadsToCountry(i, otherCountry, adjacencies);

                if (adjCurrent > adjOther) {
                    currentCountry.remove(i);
                    otherCountry.add(i);
                }
            }

            var result =  countryA.stream()
                    .sorted(Comparator.naturalOrder())
                    .map(a -> a + 1)
                    .map(Object::toString)
                    .collect(Collectors.joining(" "));
            System.out.println(result);

            buff.readLine();
        }
    }

    private static long outgoingRoadsToCountry(int city, HashSet<Integer> country, ArrayList<ArrayList<Integer>> adjacencies) {
        return adjacencies.get(city).stream().filter(country::contains).count();
    }

    private static HashSet<Integer> countryForCity(int city, HashSet<Integer> countryA, HashSet<Integer> countryB) {
        if (countryA.contains(city)) {
            return countryA;
        } else if (countryB.contains(city)) {
            return countryB;
        } else {
            throw new IllegalArgumentException("No given country contains this city: " + city);
        }
    }
}
