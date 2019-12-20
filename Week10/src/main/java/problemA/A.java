package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


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
}

class Line {
    private Point a;
    private Point b;
    public Line(Point a, Point b) {
        this.a = a;
        this.b = b;
    }
    public Point getA() {
        return a;
    }
    public Point getB() {
        return b;
    }

    public boolean containsPoint(Point p) {
        return (p.getY() - a.getY()) * (b.getX() - a.getX()) == (p.getX() - a.getX()) * (b.getY() - a.getY());
    }

    public boolean intersects(Line other) {
        return (b.getY() - a.getY()) * (other.b.getX() - other.a.getX()) !=
                (b.getX() - a.getX()) * (other.b.getY() - other.a.getY());
    }

    public Point getIntersectionPoint(Line other) {
        if (!this.intersects(other)) {
            return null;
        }

        double denominator = ((b.getX() - a.getX()) * (other.b.getY() - other.a.getY())) - ((b.getY() - a.getY()) * (other.getB().getX() - other.a.getX()));
        double x = ((b.getX() - a.getX()) * (other.a.getX() * other.b.getY() - other.b.getX() * other.a.getY()) -
                (other.b.getX() - other.a.getX()) * (a.getX() * b.getY() - b.getX() * a.getY())) /
                denominator;
        double y = ((b.getY() - a.getY()) * (other.a.getX() * other.b.getY() - other.b.getX() * other.a.getY()) -
                (other.b.getY() - other.a.getY()) * (a.getX() * b.getY() - b.getX() * a.getY())) /
                denominator;

        return new Point(x, y);
    }

    private boolean segmentContainsPoint(Point segStart, Point segEnd, Point p) {
        if (!this.containsPoint(segStart) || !this.containsPoint(segEnd)) {
            return false;
        }

        return ((a.getX() <= p.getX() && p.getX() <= b.getX()) || (b.getX() <= p.getX() && p.getX() <= a.getX())) &&
                ((a.getY() <= p.getY() && p.getY() <= b.getY()) || (b.getY() <= p.getY() && p.getY() <= a.getY()));
    }

    public boolean segmentContainsPoint(Point p) {
        return this.segmentContainsPoint(a, b, p);
    }
}

class Ray {
    private Point start;
    private double angle;
    public Ray(Point start, double angle) {
        this.start = start;
        this.angle = angle;
    }
    public Point getStart() {
        return start;
    }
    public double getAngle() {
        return angle;
    }

    public Line getLineWithLength(double length) {
        var endX = Math.cos(Math.atan(angle)) * length + start.getX();
        var endY = Math.sin(Math.atan(angle)) * length + start.getY();
        var endPoint = new Point(endX, endY);
        return new Line(start, endPoint);
    }

    public static Ray withRandomAngle(Point start) {
        double random = ThreadLocalRandom.current().nextDouble(- Math.PI, Math.PI);
        return new Ray(start, random);
    }
}

class Polygon {
    private ArrayList<Line> lines;

    public Polygon(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public boolean containsPoint(Point p) {
        int intersectCount = 0;
        var ray = Ray.withRandomAngle(p);
        var rayLine = ray.getLineWithLength(3000);

        for (var line : lines) {
            if (!rayLine.intersects(line)) {
                continue;
            }

            var intersection = rayLine.getIntersectionPoint(line);

            if (rayLine.segmentContainsPoint(intersection) &&
                    line.segmentContainsPoint(intersection)) {
                intersectCount++;
            }
        }

        return intersectCount % 2 != 0;
    }
}

public class A {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var xyz = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var impactX = xyz[0];
            var impactY = xyz[1];
            var impactPoint = new Point(impactX, impactY);


            var polygonSideCount = xyz[2];
            var polyLines = new ArrayList<Line>();

            for (int i = 0; i < polygonSideCount; i++) {
                var sideLine = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var x1 = sideLine[0];
                var y1 = sideLine[1];
                var p1 = new Point(x1, y1);
                var x2 = sideLine[2];
                var y2 = sideLine[3];
                var p2 = new Point(x2, y2);

                var line = new Line(p1, p2);
                polyLines.add(line);
            }

            var poly = new Polygon(polyLines);

            System.out.println("Case #" + t + ": " + (poly.containsPoint(impactPoint) ? "jackpot" : "too bad"));
            buff.readLine();
        }
    }
}
