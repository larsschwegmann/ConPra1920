package problemB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

class Vector2d {
    private double x;
    private double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector3d homogenized() {
        return new Vector3d(x, y, 1);
    }

    public boolean isOnLineSegment(Vector2d a, Vector2d b) {
        return ((a.x <= x && x <= b.x) || (b.x <= x && x <= a.x)) &&
                ((a.y <= y && y <= b.y) || (b.y <= y && y <= a.y));
    }

    @Override
    public String toString() {
        return "Vector2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

class Vector3d {
    private double x;
    private double y;
    private double z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector2d normalized() {
        return new Vector2d(x / z, y / z);
    }

    public boolean isAtInfinity() {
        return z == 0;
    }

    public static Vector3d crossProduct(Vector3d lhs, Vector3d rhs) {
        return new Vector3d(
                lhs.y * rhs.z - lhs.z * rhs.y,
                lhs.z * rhs.x - lhs.x * rhs.z,
                lhs.x * rhs.y - lhs.y * rhs.x
        );
    }

    @Override
    public String toString() {
        return "Vector3d{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}


public class B {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var p1Line = Arrays.stream(buff.readLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
            var p2Line = Arrays.stream(buff.readLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
            var p3Line = Arrays.stream(buff.readLine().split(" ")).mapToDouble(Double::parseDouble).toArray();

            var p1 = new Vector2d(p1Line[0], p1Line[1]);
            var p2 = new Vector2d(p2Line[0], p2Line[1]);
            var p3 = new Vector2d(p3Line[0], p3Line[1]);

            var mid12 = new Vector2d((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
            var mid13 = new Vector2d((p1.getX() + p3.getX()) / 2, (p1.getY() + p3.getY()) / 2);
            var mid23 = new Vector2d((p2.getX() + p3.getX()) / 2, (p2.getY() + p3.getY()) / 2);

            System.out.println("Case #" + t + ":");

            var centroid = new Vector2d((1.0/3.0) * (p1.getX() + p2.getX() + p3.getX()), (1.0/3.0) * (p1.getY() + p2.getY() + p3.getY()));
            System.out.println(centroid.getX() + " " + centroid.getY());

            var orth12 = getOrthogonalLine(p3.homogenized(), getLineThroughPoints(p1.homogenized(), p2.homogenized()));
            var orth13 = getOrthogonalLine(p2.homogenized(), getLineThroughPoints(p1.homogenized(), p3.homogenized()));

            var orthocenter = Vector3d.crossProduct(orth12, orth13).normalized();

            System.out.println(orthocenter.getX() + " " + orthocenter.getY());

            var eulerline = getLineThroughPoints(centroid.homogenized(), orthocenter.homogenized());
            var orthCircLine = getOrthogonalLine(mid12.homogenized(), getLineThroughPoints(p1.homogenized(), p2.homogenized()));
            var circumcenter = Vector3d.crossProduct(eulerline, orthCircLine).normalized();

            System.out.println(circumcenter.getX() + " " + circumcenter.getY());

            buff.readLine();
        }

    }

    private static Vector3d getLineThroughPoints(Vector3d src, Vector3d dest) {
        return Vector3d.crossProduct(src, dest);
    }

    private static Vector3d getOrthogonalLine(Vector3d src, Vector3d line) {
        var q1 = Vector3d.crossProduct(line, new Vector3d(0, 0, 1));
        var qt1 = new Vector3d(q1.getY(), -q1.getX(), 0);
        return Vector3d.crossProduct(src, qt1);
    }
}
