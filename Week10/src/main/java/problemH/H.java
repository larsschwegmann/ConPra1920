package problemH;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Coordinate {
    private double x;
    private double y;
    Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }
    double getX() {
        return x;
    }
    double getY() {
        return y;
    }

    public Coordinate addingX(double x) {
        return new Coordinate(this.x + x, this.y);
    }

    public Coordinate addingY(double y) {
        return new Coordinate(this.x, this.y + y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        return y == that.y;
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

class Line {
    private Coordinate a;
    private Coordinate b;
    public Line(Coordinate a, Coordinate b) {
        this.a = a;
        this.b = b;
    }
    public Coordinate getA() {
        return a;
    }
    public Coordinate getB() {
        return b;
    }

    public double getD(Coordinate p) {
        return (b.getX() - a.getX()) * (p.getY() - a.getY()) - (p.getX() - a.getX()) * (b.getY() - a.getY());
    }

    public boolean containsCoordinate(Coordinate p) {
        return (p.getY() - a.getY()) * (b.getX() - a.getX()) == (p.getX() - a.getX()) * (b.getY() - a.getY());
    }

    public boolean intersects(Line other) {
        return (b.getY() - a.getY()) * (other.b.getX() - other.a.getX()) !=
                (b.getX() - a.getX()) * (other.b.getY() - other.a.getY());
    }

    public Coordinate getIntersectionCoordinate(Line other) {
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

        return new Coordinate(x, y);
    }

    private boolean segmentContainsCoordinate(Coordinate segStart, Coordinate segEnd, Coordinate p) {
        if (!this.containsCoordinate(segStart) || !this.containsCoordinate(segEnd)) {
            return false;
        }

        return ((a.getX() < p.getX() && p.getX() < b.getX()) || (b.getX() < p.getX() && p.getX() < a.getX())) &&
                ((a.getY() < p.getY() && p.getY() < b.getY()) || (b.getY() < p.getY() && p.getY() < a.getY()));
    }

    public boolean segmentContainsCoordinate(Coordinate p) {
        return this.segmentContainsCoordinate(a, b, p);
    }
}

class ImpassableObject {
    private Coordinate location;
    private int width;
    private int height;

    public ImpassableObject(Coordinate location, int width, int height) {
        this.location = location;
        this.width = width;
        this.height = height;
    }

    public Coordinate getLocation() {
        return location;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(Coordinate c) {
        return location.getX() < c.getX() && c.getX() < location.getX() + this.width &&
                location.getY() < c.getY() && c.getY() < location.getY() + this.height;
    }

    public boolean containsInclusive(Coordinate c) {
        return location.getX() <= c.getX() && c.getX() <= location.getX() + this.width &&
                location.getY() <= c.getY() && c.getY() <= location.getY() + this.height;
    }
}

public class H {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var whn = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var w = whn[0];
            var h = whn[1];
            var n = whn[2];

            var impassableObjects = new ArrayList<ImpassableObject>();

            for (int i = 0; i < n; i++) {
                var xywh = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                var obj = new ImpassableObject(new Coordinate(xywh[0], xywh[1]), xywh[2], xywh[3]);
                impassableObjects.add(obj);
            }

            var startLine = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var unitStart = new Coordinate(startLine[0], startLine[1]);

            var endLine = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var unitTarget = new Coordinate(endLine[0], endLine[1]);

            var path = thetaStar(unitStart, unitTarget, impassableObjects, a -> euclidianDistance(a, unitTarget), w, h);
            System.out.println("Case #" + t + ": " + pathToString(path));

            buff.readLine();
        }
    }

    private static String pathToString(List<Coordinate> path) {
        return path.stream().map(c -> "(" + c.getX() + "," + c.getY() + ")").collect(Collectors.joining(" "));
    }

    private static List<Coordinate> thetaStar(Coordinate start, Coordinate target, ArrayList<ImpassableObject> obstacles, Function<Coordinate, Double> h, int width, int height) {
        var openSet = new HashSet<Coordinate>();
        openSet.add(start);

        var cameFrom = new HashMap<Coordinate, Coordinate>();
        cameFrom.put(start, start);

        var gScore = new HashMap<Coordinate, Double>();
        gScore.put(start, 0.0);

        var fScore = new HashMap<Coordinate, Double>();
        fScore.put(start, h.apply(start));

        while (!openSet.isEmpty()) {
            var current = openSet.stream().min(Comparator.comparing(c -> fScore.getOrDefault(c, Double.MAX_VALUE))).get();
            if (current.equals(target)) {
                return reconstructPath(cameFrom, current, start);
            }

            openSet.remove(current);

            for (var neighbour : getNeighbours(current, width, height)) {
                if (!neighbourDoesntIntersects(current, neighbour, obstacles)) {
                    continue;
                }
                if (lineOfSight(cameFrom.get(current), neighbour, obstacles)) {
                    var tentativeGScore = gScore.getOrDefault(cameFrom.get(current), Double.MAX_VALUE) + euclidianDistance(cameFrom.get(current), neighbour); // distance between two adjacent nodes is always 1
                    if (tentativeGScore < gScore.getOrDefault(neighbour, Double.MAX_VALUE)) {
                        cameFrom.put(neighbour, cameFrom.get(current));
                        gScore.put(neighbour, tentativeGScore);
                        fScore.put(neighbour, tentativeGScore + h.apply(neighbour));
                        openSet.add(neighbour);
                    }
                } else {
                    var tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) + 1; // distance between two adjacent nodes is always 1
                    if (tentativeGScore < gScore.getOrDefault(neighbour, Double.MAX_VALUE)) {
                        cameFrom.put(neighbour, current);
                        gScore.put(neighbour, tentativeGScore);
                        fScore.put(neighbour, tentativeGScore + h.apply(neighbour));
                        openSet.add(neighbour);
                    }
                }

            }
        }

        return null;
    }

    private static List<Coordinate> reconstructPath(HashMap<Coordinate, Coordinate> cameFrom, Coordinate current, Coordinate start) {
        var path = new LinkedList<Coordinate>();
        path.add(current);

        while (!current.equals(start)) {
            var pred = cameFrom.get(current);
            path.addFirst(pred);
            current = pred;
        }

        return path;
    }

    private static List<Coordinate> getNeighbours(Coordinate coord, int width, int height) {
        // dest is the only tool field we're allowed to step on
        var neighbours = new ArrayList<Coordinate>(4);
        // Top
        if (coord.getY() > 1) {
            neighbours.add(new Coordinate(coord.getX(), coord.getY() - 1));
        }
        // Right
        if (coord.getX() < width) {
            neighbours.add(new Coordinate(coord.getX() + 1, coord.getY()));
        }
        // Bottom
        if (coord.getY() < height) {
            neighbours.add(new Coordinate(coord.getX(), coord.getY() + 1));
        }
        // Left
        if (coord.getX() > 1) {
            neighbours.add(new Coordinate(coord.getX() - 1, coord.getY()));
        }
        return neighbours;
    }

    private static double euclidianDistance(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }

    private static boolean lineOfSight(Coordinate a, Coordinate b, List<ImpassableObject> obstacles) {
        var lineOfSight = new Line(a, b);

        for (var obst : obstacles) {
            if (obst.contains(a) || obst.contains(b)) {
                return false;
            }

            var tl = obst.getLocation();
            var tr = tl.addingX(obst.getWidth());
            var bl = tl.addingY(obst.getHeight());
            var br = bl.addingX(obst.getWidth());

            var top = new Line(tl, tr);
            var left = new Line(tl, bl);
            var right = new Line(tr, br);
            var bottom = new Line(bl, br);

            // Top
            if (lineOfSight.intersects(top) && top.segmentContainsCoordinate(lineOfSight.getIntersectionCoordinate(top)) && lineOfSight.segmentContainsCoordinate(lineOfSight.getIntersectionCoordinate(top))) {
                return false;
            }

            // Left
            if (lineOfSight.intersects(left) && left.segmentContainsCoordinate(lineOfSight.getIntersectionCoordinate(left)) && lineOfSight.segmentContainsCoordinate(lineOfSight.getIntersectionCoordinate(left))) {
                return false;
            }

            // Right
            if (lineOfSight.intersects(right) && right.segmentContainsCoordinate(lineOfSight.getIntersectionCoordinate(right)) && lineOfSight.segmentContainsCoordinate(lineOfSight.getIntersectionCoordinate(right))) {
                return false;
            }

            // Bottom
            if (lineOfSight.intersects(bottom) && bottom.segmentContainsCoordinate(lineOfSight.getIntersectionCoordinate(bottom)) && lineOfSight.segmentContainsCoordinate(lineOfSight.getIntersectionCoordinate(bottom))) {
                return false;
            }
        }

        return true;
    }

    private static boolean neighbourDoesntIntersects(Coordinate source, Coordinate dest, ArrayList<ImpassableObject> obstacles) {
        for (var obst : obstacles) {
            if (obst.contains(dest)) {
                return false;
            }

            var tl = obst.getLocation();
            var tr = tl.addingX(obst.getWidth());
            var bl = tl.addingY(obst.getHeight());
            var br = bl.addingX(obst.getWidth());

            var top = new Line(tl, tr);
            var left = new Line(tl, bl);
            var right = new Line(tr, br);
            var bottom = new Line(bl, br);

            if (obst.containsInclusive(source) && obst.containsInclusive(dest)) {
                var result = false;
                if (top.getD(source) == 0 && top.getD(dest) == 0) {
                    result = true;
                }

                if (left.getD(source) == 0 && left.getD(dest) == 0) {
                    result = true;
                }

                if (right.getD(source) == 0 && right.getD(dest) == 0) {
                    result = true;
                }

                if (bottom.getD(source) == 0 && bottom.getD(dest) == 0) {
                    result = true;
                }
                if (!result) {
                    return false;
                }
            }
        }

        return true;
    }
}
