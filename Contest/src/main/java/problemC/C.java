package problemC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Algorithm and code partially taken from https://www.nayuki.io/page/smallest-enclosing-circle and https://www.nayuki.io/res/smallest-enclosing-circle/computational-geometry-lecture-6.pdf

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

    public double distance(Point other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }

    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    public double det(Point other) {
        return this.x * other.y - other.x * this.y;
    }
}

class Circle {
    private Point center;
    private double radius;

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public boolean containsPoint(Point p) {
        return this.center.distance(p) < this.radius + 1e-14;
    }
}

public class C {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var pointCount = Integer.parseInt(buff.readLine());
            var points = new ArrayList<Point>();
            for (int p = 0; p < pointCount; p++) {
                var pointLine = buff.readLine().split(" ");
                var pX = Integer.parseInt(pointLine[0]);
                var pY = Integer.parseInt(pointLine[1]);
                points.add(new Point(pX, pY));
            }

            var circle = smallestEnclosingCircle(points);

            System.out.println("Case #" + t + ": " + circle.getRadius());
            buff.readLine();
        }
    }

    private static Circle smallestEnclosingCircle(List<Point> points) {
        Collections.shuffle(points);
        Circle c = null;
        for (int i = 0; i < points.size(); i++) {
            var p = points.get(i);
            if (c == null || !c.containsPoint(p)) {
                c = smallestEnclosingCircleOnePoint(points.subList(0, i + 1), p);
            }
        }
        return c;
    }

    private static Circle smallestEnclosingCircleOnePoint(List<Point> points, Point p) {
        var c = new Circle(p, 0);
        for (int i = 0; i < points.size(); i++) {
            var q = points.get(i);
            if (!c.containsPoint(q)) {
                if (c.getRadius() == 0) {
                    //  Circle only contains one point at center, create inital circle with radius != 0 and two points
                    c = circleWithTwoBoundingPoints(p, q);
                } else {
                    c = smallestEnclosingCircleTwoPoints(points.subList(0, i + 1), p, q);
                }
            }
        }
        return c;
    }

    private static Circle smallestEnclosingCircleTwoPoints(List<Point> points, Point p, Point q) {
        var circle = circleWithTwoBoundingPoints(p, q);
        Circle left = null;
        Circle right = null;

        var pq = q.subtract(p);
        for (var r : points) {
            if (circle.containsPoint(r)) {
                continue;
            }

            var det = pq.det(r.subtract(p));
            var c = circleWithThreeBoundingPoints(p, q, r);
            if (det > 0 && (left == null || pq.det(c.getCenter().subtract(p)) > pq.det(left.getCenter().subtract(p)))) {
                left = c;
            } else if (det < 0 && (right == null || pq.det(c.getCenter().subtract(p)) < pq.det(right.getCenter().subtract(p)))) {
                right = c;
            }
        }

        if (left == null && right == null) {
            return circle;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        return left.getRadius() <= right.getRadius() ? left : right;
    }

    private static Circle circleWithTwoBoundingPoints(Point p, Point q) {
        var center = new Point((p.getX() + q.getX()) / 2, (p.getY() + q.getY()) / 2);
        return new Circle(center, Math.max(center.distance(p), center.distance(q)));
    }

    private static Circle circleWithThreeBoundingPoints(Point p, Point q, Point r) {
        var ox = (Math.min(Math.min(p.getX(), q.getX()), r.getX()) + Math.max(Math.min(p.getX(), q.getX()), r.getX())) / 2;
        var oy = (Math.min(Math.min(p.getY(), q.getY()), r.getY()) + Math.max(Math.min(p.getY(), q.getY()), r.getY())) / 2;
        var px = p.getX() - ox;
        var py = p.getY() - oy;
        var qx = q.getX() - ox;
        var qy = q.getY() - oy;
        var rx = r.getX() - ox;
        var ry = r.getY() - oy;

        var d = (px * (qy - ry) + qx * (ry - py) + rx * (py - qy)) * 2;
        if (d == 0) {
            return null;
        }

        var v = Math.pow(px, 2) + Math.pow(py, 2);
        var v1 = Math.pow(qx, 2) + Math.pow(qy, 2);
        var v2 = Math.pow(rx, 2) + Math.pow(ry, 2);
        var x = (v * (qy - ry) + v1 * (ry - py) + v2 * (py - qy)) / 2;
        var y = (v * (rx - qx) + v1 * (px - rx) + v2 * (qx - px)) / 2;

        var center = new Point(ox + x, oy + y);
        var radius = Math.max(Math.max(center.distance(p), center.distance(q)), center.distance(r));
        return new Circle(center, radius);
    }
}
