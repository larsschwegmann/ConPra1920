package problemG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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

            var found = false;
            for (int i=0; i<10000; i++) {
                if (canEscape(map)) {
                    System.out.println("Case #" + t + ": yes");
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Case #" + t + ": no");
            }

            buff.readLine();
        }
    }

    private static boolean canEscape(char[][] map) {
        var mapCopy = new char[map.length][map[0].length];
        IntStream.range(0, map.length).forEach(i -> mapCopy[i] = Arrays.copyOf(map[i], map[i].length));
        var toolCount = getToolPositions(mapCopy).size();

        var current = getStartPosition(mapCopy);
        var options = getNeighbours(mapCopy, current);
        var rnd = new Random();

        while (!options.isEmpty()) {
            var randomOpt = options.get(rnd.nextInt(options.size()));
            if (isTool(getAtPos(mapCopy, randomOpt))) {
                if (--toolCount == 0) {
                    return true;
                }
            }
            mapCopy[randomOpt.getRow()][randomOpt.getCol()] = '$';
            current = randomOpt;
            options = getNeighbours(mapCopy, current);
        }
        return false;
    }

    private static List<Coordinate> getNeighbours(char[][] map, Coordinate coord) {
        // dest is the only tool field we're allowed to step on
        var neighbours = new ArrayList<Coordinate>(4);
        // Top
        if (coord.getRow() > 0 && isWalkable(getAtPos(map, coord.above()))) {
            neighbours.add(coord.above());
        }
        // Right
        if (coord.getCol() < map[0].length - 1 && isWalkable(getAtPos(map, coord.right()))) {
            neighbours.add(coord.right());
        }
        // Bottom
        if (coord.getRow() < map.length - 1 && isWalkable(getAtPos(map, coord.below()))) {
            neighbours.add(coord.below());
        }
        // Left
        if (coord.getCol() > 0 && isWalkable(getAtPos(map, coord.left()))) {
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
}
