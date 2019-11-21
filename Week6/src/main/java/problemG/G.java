package problemG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

class Coordinate {
    private int row;
    private int col;
    Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }
    int getRow() {
        return row;
    }
    int getCol() {
        return col;
    }

    Coordinate above() {
        return new Coordinate(this.row - 1, this.col);
    }
    Coordinate right() {
        return new Coordinate(this.row, this.col + 1);
    }
    Coordinate below() {
        return new Coordinate(this.row + 1, this.col);
    }
    Coordinate left() {
        return new Coordinate(this.row, this.col - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (row != that.row) return false;
        return col == that.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
}

public class G {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var wh = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var width = wh[0];
            var height = wh[1];

            var map = new char[height][width];
            for (int r=0; r<height; r++) {
                map[r] = buff.readLine().toCharArray();
            }

            System.out.println("Case #" + t + ": " + (canEscape(map) ? "yes" : "no"));

            buff.readLine();
        }
    }

    private static boolean canEscape(char[][] map) {
        var startPos = getStartPosition(map);
        Queue<Coordinate> toolsPos = new PriorityQueue<>(Comparator.comparing(c -> manhattenDistance(startPos, c)));
        toolsPos.addAll(getToolPositions(map));

        if (toolsPos.isEmpty()) {
            // Base case, no tools left to find, we are done
            return true;
        }

        while (!toolsPos.isEmpty()) {
            var dest = toolsPos.poll();
            var path = findPath(map, startPos, dest, a -> manhattenDistance(a, dest));
            if (path != null) {
                // Successfully found path
                // Create new map where start is the visited tool and path is blocked
                var alteredMap = changeMapByBlockingPath(map, path);
                if (canEscape(alteredMap)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static char[][] changeMapByBlockingPath(char[][] map, List<Coordinate> path) {
        var alteredMap = new char[map.length][map[0].length];
        IntStream.range(0, map.length).forEach(i -> alteredMap[i] = Arrays.copyOf(map[i], map[i].length));
        var it = path.listIterator();
        for (int i=0; i<path.size(); i++) {
            var coord = it.next();
            if (i == path.size() - 1) {
                // Set end of path (== visited tool position) to new start pos
                alteredMap[coord.getRow()][coord.getCol()] = 'L';
            } else {
                // Set path waypoints to debug obstacle so that we can differentiate between wall and previous paths
                alteredMap[coord.getRow()][coord.getCol()] = '$';
            }
        }

        return alteredMap;
    }

    private static List<Coordinate> findPath(char[][] map, Coordinate start, Coordinate dest, Function<Coordinate, Integer> h) {
        // Basically A*

        var openSet = new HashSet<Coordinate>();
        openSet.add(start);

        var cameFrom = new HashMap<Coordinate, Coordinate>();

        var gScore = new HashMap<Coordinate, Integer>();
        gScore.put(start, Integer.MAX_VALUE);

        var fScore = new HashMap<Coordinate, Integer>();
        fScore.put(start, h.apply(start));

        while (!openSet.isEmpty()) {
            var current = openSet.stream().min(Comparator.comparing(c -> fScore.getOrDefault(c, Integer.MAX_VALUE))).get();
            if (current.equals(dest)) {
                return reconstructPath(cameFrom, current, start);
            }

            openSet.remove(current);

            for (var neighbour : getNeighbours(map, current, dest)) {
                var tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1; // distance between two adjacent nodes is always 1
                if (tentativeGScore < gScore.getOrDefault(neighbour, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbour, current);
                    gScore.put(neighbour, tentativeGScore);
                    fScore.put(neighbour, tentativeGScore + h.apply(neighbour));
                    openSet.add(neighbour);
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

    private static List<Coordinate> getNeighbours(char[][] map, Coordinate coord, Coordinate dest) {
        // dest is the only tool field we're allowed to step on
        var neighbours = new ArrayList<Coordinate>(4);
        // Top
        if (coord.getRow() > 0 && isWalkable(getAtPos(map, coord.above())) && (!isTool(getAtPos(map, coord.above())) || coord.above().equals(dest))) {
            neighbours.add(coord.above());
        }
        // Right
        if (coord.getCol() < map[0].length - 1 && isWalkable(getAtPos(map, coord.right())) && (!isTool(getAtPos(map, coord.above())) || coord.above().equals(dest))) {
            neighbours.add(coord.right());
        }
        // Bottom
        if (coord.getRow() < map.length - 1 && isWalkable(getAtPos(map, coord.below())) && (!isTool(getAtPos(map, coord.above())) || coord.above().equals(dest))) {
            neighbours.add(coord.below());
        }
        // Left
        if (coord.getCol() > 0 && isWalkable(getAtPos(map, coord.left())) && (!isTool(getAtPos(map, coord.above())) || coord.above().equals(dest))) {
            neighbours.add(coord.left());
        }
        return neighbours;
    }

    private static boolean isWalkable(char c) {
        return c != '#' && c != '$' && c != 'L';
    }

    private static boolean isStart(char c) {
        return c == 'L';
    }

    private static boolean isTool(char c) {
        return c == 'T';
    }

    private static Coordinate getStartPosition(char[][] map) {
        for (int r=0; r<map.length; r++) {
            for (int c=0; c<map[r].length; c++) {
                if (isStart(map[r][c])) {
                    return new Coordinate(r, c);
                }
            }
        }
        throw new IllegalArgumentException("map does not contain start position L!");
    }

    private static LinkedList<Coordinate> getToolPositions(char[][] map) {
        var toolList = new LinkedList<Coordinate>();
        for (int r=0; r<map.length; r++) {
            for (int c=0; c<map[r].length; c++) {
                if (isTool(map[r][c])) {
                    toolList.add(new Coordinate(r, c));
                }
            }
        }
        return toolList;
    }

    private static char getAtPos(char[][] map, Coordinate coord) {
        return map[coord.getRow()][coord.getCol()];
    }

    private static int manhattenDistance(Coordinate a, Coordinate b) {
        return Math.abs(b.getRow() - a.getRow()) + Math.abs(b.getCol() - a.getCol());
    }
}
