package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

class Point {
    private double x;
    private double y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        return Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

public class B {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var postCount = Integer.parseInt(buff.readLine());

            var posts = new ArrayList<Point>();
            var postPositions = new HashMap<Point, Integer>();

            for (int i = 0; i < postCount; i++) {
                var xy = buff.readLine().split(" ");
                var x = Integer.parseInt(xy[0]);
                var y = Integer.parseInt(xy[1]);
                var post = new Point(x, y);

                posts.add(post);
                postPositions.put(post, i);
            }

            // Graham's Scan
            // Implementation heavily inspired by https://www.geeksforgeeks.org/convex-hull-set-1-jarviss-algorithm-or-wrapping/

            int first = postPositions.get(posts.stream().sorted(Comparator.comparing(Point::getX)).findFirst().get());
            int p = first;
            int q;

            var convexHull = new ArrayList<Integer>();

            do {
                convexHull.add(p);

                q = (p + 1) % postCount;

                for (int i = 0; i < postCount; i++) {
                    var o = orientation(posts.get(p), posts.get(i), posts.get(q));
                    if (o < 0 || (o == 0 && distance(posts.get(p), posts.get(q)) < distance(posts.get(p), posts.get(i)))) {
                        q = i;
                    }
                }
                p = q;
            } while (p != first);

            var result = convexHull.stream().sorted().mapToInt(Integer::intValue).map(a -> a + 1).mapToObj(Integer::toString).collect(Collectors.joining(" "));
            System.out.println("Case #" + t + ": " + result);

            buff.readLine();
        }
    }

    private static double orientation(Point p1, Point p2, Point p3) {
        return (p2.getY() - p1.getY()) * (p3.getX() - p2.getX()) -
                (p2.getX() - p1.getX()) * (p3.getY() - p2.getY());
    }

    private static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }
}
