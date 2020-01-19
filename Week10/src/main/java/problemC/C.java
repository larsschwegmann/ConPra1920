package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

class Point implements Comparable<Point> {
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

    @Override
    public int compareTo(Point o) {
        if (Double.compare(this.y, o.y) == 0) {
            return Double.compare(this.x, o.x);
        } else {
            return Double.compare(this.y, o.y);
        }
    }
}

public class C {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var pointsCount = Integer.parseInt(buff.readLine());

            Point firstPoint = null;
            var allPoints = new Point[pointsCount];
            var points = new Point[pointsCount - 1];

            for (int i = 0; i < pointsCount; i++) {
                var pointLine = buff.readLine().split(" ");
                var pointX = Double.parseDouble(pointLine[0]);
                var pointY = Double.parseDouble(pointLine[1]);
                var point = new Point(pointX, pointY);
                allPoints[i] = point;
                if (i == 0) {
                    firstPoint = point;
                } else {
                    points[i - 1] = point;
                }
            }

            var smallestDistanceInPoints = smallestDistance(points);
            var smallestDistanceFromP = smallestDistanceFrom(firstPoint, points);

            // Max First Radius
            var areaWithMaxFirst = calculateArea(pointsCount, smallestDistanceFromP - Double.MIN_VALUE, Double.MIN_VALUE);

            // Max other
            double areaWithMaxOther = calculateArea(pointsCount, smallestDistanceFromP - Math.min(smallestDistanceFromP, smallestDistanceInPoints / 2.0),  Math.min(smallestDistanceFromP, smallestDistanceInPoints / 2.0));

            System.out.println("Case #" + t + ": " + Math.max(areaWithMaxFirst, areaWithMaxOther));

            buff.readLine();
        }
    }

    // Smallest distance in set of points taken from https://algs4.cs.princeton.edu/99hull/ClosestPair.java.html
    private static double smallestDistanceFrom(Point p, Point[] others) {
        double min = Double.POSITIVE_INFINITY;
        for (var o : others) {
            var dist = distance(p, o);
            if (dist < min) {
                min = dist;
            }
        }
        return min;
    }

    // points has to be sorted beforehand
    private static double smallestDistance(Point[] points) {
        var pointsByX = new Point[points.length];
        System.arraycopy(points, 0, pointsByX, 0, points.length);
        Arrays.sort(pointsByX, Comparator.comparing(Point::getX));

        for (int i = 0; i < pointsByX.length - 1; i++) {
            if (pointsByX[i].equals(pointsByX[i+1])) {
                return 0;
            }
        }

        var pointsByY = new Point[points.length];
        System.arraycopy(pointsByX, 0, pointsByY, 0, points.length);

        var aux = new Point[points.length];

        return smallestDistRec(pointsByX, pointsByY, aux, 0, points.length - 1);
    }

    private static double smallestDistRec(Point[] pointsByX, Point[] pointsByY, Point[] aux, int low, int high) {
        if (high <= low) {
            return Double.POSITIVE_INFINITY;
        }

        int mid = low + (high - low) / 2;
        var median = pointsByX[mid];

        var delta1 = smallestDistRec(pointsByX, pointsByY, aux, low, mid);
        var delta2 = smallestDistRec(pointsByX, pointsByY, aux, mid + 1, high);
        var delta = Math.min(delta1, delta2);

        merge(pointsByY, aux, low, mid, high);

        var m = 0;
        for (int i = low; i <= high; i++) {
            if (Math.abs(pointsByY[i].getX() - median.getX()) < delta) {
                aux[m++] = pointsByY[i];
            }
        }

        double bestDistance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < m; i++) {
            for (int j = i + 1; (j < m) && (aux[j].getY() - aux[i].getY() < delta); j++) {
                var dist = distance(aux[i], aux[j]);
                if (dist < delta) {
                    delta = dist;
                    if (dist < bestDistance) {
                        bestDistance = delta;
                    }
                }
            }
        }

        return delta;
    }

    private static void merge(Point[] a, Point[] aux, int low, int mid, int high) {
        // copy to aux[]
        if (high + 1 - low >= 0) {
            System.arraycopy(a, low, aux, low, high + 1 - low);
        }

        // merge back to a[]
        int i = low, j = mid+1;
        for (int k = low; k <= high; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > high) {
                a[k] = aux[i++];
            } else if (aux[j].compareTo(aux[i]) < 0) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
    }

    private static double calculateArea(int pointsCount, double firstRadius, double otherRadius) {
        return Math.PI * (Math.pow(firstRadius, 2) + (pointsCount - 1) * Math.pow(otherRadius, 2));
    }

    private static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }
}
