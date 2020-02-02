package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Coordinate2d {
    private double x;
    private double y;

    public Coordinate2d(double x, double y) {
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
    public String toString() {
        return x + " " + y;
    }
}

class Line {
    private Coordinate2d p;
    private Coordinate2d q;

    public Line(Coordinate2d p, Coordinate2d q) {
        this.p = p;
        this.q = q;
    }

    public double getA() {
        return this.p.getY() - this.q.getY();
    }

    public double getB() {
        return this.q.getX() - this.p.getX();
    }

    public double getC() {
        return this.p.getX() * this.q.getY() - this.q.getX() * this.p.getY();
    }

    public Coordinate2d getReflection(Coordinate2d r) {
        var u = ((Math.pow(getB(), 2) - Math.pow(getA(), 2)) * r.getX() - 2 * getA() * getB() * r.getY() - 2 * getA() * getC()) /
                (Math.pow(getA(), 2) + Math.pow(getB(), 2));
        var v = ((Math.pow(getA(), 2) - Math.pow(getB(), 2)) * r.getY() - 2 * getA() * getB() * r.getX() - 2 * getB() * getC()) /
                (Math.pow(getA(), 2) + Math.pow(getB(), 2));
        return new Coordinate2d(u, v);
    }
}

public class D {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            buff.readLine();
            buff.readLine();

            var wallLine = buff.readLine().split(" ");
            var wallOneX = Integer.parseInt(wallLine[0]);
            var wallOneY = Integer.parseInt(wallLine[1]);
            var wallTwoX = Integer.parseInt(wallLine[2]);
            var wallTwoY = Integer.parseInt(wallLine[3]);

            var enemyLine = buff.readLine().split(" ");
            var enemyX = Double.parseDouble(enemyLine[0]);
            var enemyY = Double.parseDouble(enemyLine[1]);

            var enemyProjected = new Coordinate2d(enemyX, enemyY);
            var wall = new Line(new Coordinate2d(wallOneX, wallOneY), new Coordinate2d(wallTwoX, wallTwoY));

            var enemy = wall.getReflection(enemyProjected);

            System.out.println("Case #" + t + ": " + enemy.getX() + " " + enemy.getY());

            buff.readLine();
        }
    }
}
