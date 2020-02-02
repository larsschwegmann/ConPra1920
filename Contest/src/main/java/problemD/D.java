package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        return x + " " + y;
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
        return Math.abs(z) < 0.000001;
    }

    public static Vector3d crossProduct(Vector3d lhs, Vector3d rhs) {
        return new Vector3d(
                lhs.y * rhs.z - lhs.z * rhs.y,
                lhs.z * rhs.x - lhs.x * rhs.z,
                lhs.x * rhs.y - lhs.y * rhs.x
        );
    }

    public static Vector3d add(Vector3d a, Vector3d b) {
        return new Vector3d(a.x + b.x, a.y + b.y, 1);
    }

    public static Vector3d orthogonalLine(Vector3d src, Vector3d line) {
        var q1 = Vector3d.crossProduct(line, new Vector3d(0, 0, 1));
        var qt1 = new Vector3d(q1.getY(), -q1.getX(), 0);
        return Vector3d.crossProduct(src, qt1);
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

class Matrix3d {
    private double[][] a;

    private Matrix3d(double[][] a) {
        this.a = a;
    }

    public double getAt(int x, int y) {
        if (x < 0 || x >= a.length || y < 0 || y >= a[x].length) {
            throw new IndexOutOfBoundsException("du lolek du");
        }

        return a[x][y];
    }

    public static Matrix3d rotationMatrix(double angle) {
        double[][] a = new double[][] {
                { Math.cos(angle),  - Math.sin(angle),  0 },
                { Math.sin(angle),  Math.cos(angle),    0 },
                { 0,                0,                  1 }
        };
        return new Matrix3d(a);
    }

    public static Matrix3d translationMatrix(double tx, double ty) {
        double[][] a = new double[][] {
                { 1, 0, tx },
                { 0, 1, ty },
                { 0, 0, 1  }
        };
        return new Matrix3d(a);
    }

    public static Vector3d vectorProduct(Vector3d v, Matrix3d m) {
        return new Vector3d(
                m.getAt(0, 0) * v.getX() + m.getAt(0, 1) * v.getY() + m.getAt(0, 2) * v.getZ(),
                m.getAt(1, 0) * v.getX() + m.getAt(1, 1) * v.getY() + m.getAt(1, 2) * v.getZ(),
                m.getAt(2, 0) * v.getX() + m.getAt(2, 1) * v.getY() + m.getAt(2, 2) * v.getZ()
        );
    }
}

public class D {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var leaLine = buff.readLine().split(" ");
            var leaX = Integer.parseInt(leaLine[0]);
            var leaY = Integer.parseInt(leaLine[1]);

            var snowLine = buff.readLine().split(" ");
            var snowballX = Double.parseDouble(snowLine[0]);
            var snowballY = Double.parseDouble(snowLine[1]);

            var wallLine = buff.readLine().split(" ");
            var wallOneX = Integer.parseInt(wallLine[0]);
            var wallOneY = Integer.parseInt(wallLine[1]);
            var wallTwoX = Integer.parseInt(wallLine[2]);
            var wallTwoY = Integer.parseInt(wallLine[3]);

            var enemyLine = buff.readLine().split(" ");
            var enemyX = Double.parseDouble(enemyLine[0]);
            var enemyY = Double.parseDouble(enemyLine[1]);

            var lea = new Vector2d(leaX, leaY).homogenized();
            var snowball = new Vector2d(snowballX, snowballY).homogenized();
            var wall = Vector3d.crossProduct(new Vector2d(wallOneX, wallOneY).homogenized(), new Vector2d(wallTwoX, wallTwoY).homogenized());
            var enemyProjected = new Vector2d(enemyX, enemyY).homogenized();

            // Line through Lea and Snowball
            var leaSnowball = Vector3d.crossProduct(lea, snowball);

            // Perpendicular line through enemy projected and wall
            var projectionLine = Vector3d.orthogonalLine(enemyProjected, wall);

            // Get intersection betweeen leaSnowball and projectionLine
            var enemy = Vector3d.crossProduct(leaSnowball, projectionLine);

            if (enemy.isAtInfinity()) {
                // Handle parallel case here
                // Intersect wall with projectLine
                var intersect = Vector3d.crossProduct(projectionLine, wall);
                var intersectDirection = Vector3d.crossProduct(intersect, new Vector3d(0, 0, 1));



                continue;
            }

            var enemy2d = enemy.normalized();

            System.out.println("Case #" + t + ": " + enemy2d.getX() + " " + enemy2d.getY());

            buff.readLine();
        }
    }
}
