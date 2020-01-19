package problemA;

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

public class A {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var swordInfo = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();

            var sword1Crossguard1 = new Vector2d(swordInfo[0], swordInfo[1]).homogenized();
            var sword1Crossguard2 = new Vector2d(swordInfo[2], swordInfo[3]).homogenized();
            var sword1Pommel = new Vector2d(swordInfo[4], swordInfo[5]).homogenized();

            var sword2Crossguard1 = new Vector2d(swordInfo[6], swordInfo[7]).homogenized();
            var sword2Crossguard2 = new Vector2d(swordInfo[8], swordInfo[9]).homogenized();
            var sword2Pommel = new Vector2d(swordInfo[10], swordInfo[11]).homogenized();

            var sword1CrossguardLine = Vector3d.crossProduct(sword1Crossguard1, sword1Crossguard2);
            var sword2CrossguardLine = Vector3d.crossProduct(sword2Crossguard1, sword2Crossguard2);

            var q1 = Vector3d.crossProduct(sword1CrossguardLine, new Vector3d(0, 0, 1));
            var qt1 = new Vector3d(q1.getY(), -q1.getX(), 0);
            var m1 = Vector3d.crossProduct(sword1Pommel, qt1);

            var q2 = Vector3d.crossProduct(sword2CrossguardLine, new Vector3d(0, 0, 1));
            var qt2 = new Vector3d(q2.getY(), -q2.getX(), 0);
            var m2 = Vector3d.crossProduct(sword2Pommel, qt2);

            var swordIntersectPoint = Vector3d.crossProduct(m1, m2);

            System.out.print("Case #" + t + ": ");

            if (swordIntersectPoint.isAtInfinity()) {
                System.out.println("strange");
                continue;
            }

            var swordIntersectNormalized = swordIntersectPoint.normalized();
            var sword1CrossguardIntersect = Vector3d.crossProduct(m1, sword1CrossguardLine).normalized();
            var sword2CrossguardIntersect = Vector3d.crossProduct(m1, sword1CrossguardLine).normalized();

            if (swordIntersectNormalized.isOnLineSegment(sword1CrossguardIntersect, sword1Pommel.normalized()) ||
                    swordIntersectNormalized.isOnLineSegment(sword2CrossguardIntersect, sword2Pommel.normalized())) {
                System.out.println("strange");
                continue;
            }

            System.out.println(swordIntersectNormalized.getX() + " " + swordIntersectNormalized.getY());
        }
    }
}
