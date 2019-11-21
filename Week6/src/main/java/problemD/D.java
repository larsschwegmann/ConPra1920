package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class D {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            System.out.println("Case #" + t + ":");
            var n = Integer.parseInt(buff.readLine());
            var grid = new char[n][n];
            for (int i=0; i<n; i++) {
                grid[i] = buff.readLine().toCharArray();
            }

            System.out.println(solveNQueens(grid));

            buff.readLine();
        }
    }

    private static String solveNQueens(char[][] inputGrid) {
        if (!placedCorrectly(inputGrid)) {
            // Can't add any more queens, impossible
            return "impossible";
        }

        if (queenCount(inputGrid) == inputGrid.length) {
            // We are done, it is already solved
            return gridToString(inputGrid);
        }

        // Find row to start with, cant have any queens
        int row = nextFreeRow(inputGrid, 0);

        if (placeQueen(inputGrid, row)) {
            return gridToString(inputGrid);
        } else {
            return "impossible";
        }
    }

    private static boolean placeQueen(char[][] grid, int r) {
        for (int c=0; c<grid.length; c++) {
            grid[r][c] = 'x';
            if (placedCorrectly(grid)) {
                if (r == grid.length - 1) {
                    // Last row, return solution
                    return true;
                } else {
                    var nextFreeRow = nextFreeRow(grid, r);
                    if (nextFreeRow < grid.length) {
                        var result = placeQueen(grid, nextFreeRow);
                        if (result) {
                            return true;
                        }
                    } else {
                        if (placedCorrectly(grid)) {
                            return true;
                        }
                    }
                }
            }
            grid[r][c] = '.';
        }
        return false;
    }

    private static boolean placedCorrectly(char[][] grid) {
        int overallQueenCount = 0;
        // Check rows & cols
        for (int i=0; i<grid.length; i++) {
            var queenInRow = false;
            var queenInCol = false;
            for (int k=0; k<grid.length; k++) {
                if (isQueen(grid[i][k])) {
                    if (queenInRow) {
                        return false;
                    } else {
                        queenInRow = true;
                        overallQueenCount++;
                    }
                }
                if (isQueen(grid[k][i])) {
                    if (queenInCol) {
                        return false;
                    } else {
                        queenInCol = true;
                    }
                }
            }
        }

        if (overallQueenCount > grid.length) {
            return false;
        }

        // Check Diagonals
        // Top left to Bot right
        for (int i=0; i<grid.length; i++) {
            var queenInDiag = false;
            for (int k=0; k+i<grid.length; k++) {
                if (isQueen(grid[i + k][k])) {
                    if (queenInDiag) {
                        return false;
                    } else {
                        queenInDiag = true;
                    }
                }
            }
        }
        for (int i=1; i<grid.length; i++) {
            var queenInDiag = false;
            for (int k=0; i+k<grid.length; k++) {
                if (isQueen(grid[k][i + k])) {
                    if (queenInDiag) {
                        return false;
                    } else {
                        queenInDiag = true;
                    }
                }
            }
        }

        // Bot left to top right
        for (int i=grid.length - 1; i>=0; i--) {
            var queenOnDiag = false;
            for (int k=0; i - k >= 0; k++) {
                if (isQueen(grid[i - k][k])) {
                    if (queenOnDiag) {
                        return false;
                    } else {
                        queenOnDiag = true;
                    }
                }
            }
        }
        for (int i=0; i<grid.length; i++) {
            var queenOnDiag = false;
            for (int k=0; i+k<grid.length; k++) {
                if (isQueen(grid[grid.length - k - 1][k + i])) {
                    if (queenOnDiag) {
                        return false;
                    } else {
                        queenOnDiag = true;
                    }
                }
            }
        }

        return true;
    }

    private static boolean isQueen(char c) {
        return c == 'x';
    }

    private static String gridToString(char[][] grid) {
        return Arrays.stream(grid).map(String::new).collect(Collectors.joining("\n"));
    }

    private static int queenCount(char[][] grid) {
        var count = 0;
        for (int i=0; i<grid.length; i++) {
            for (int k=0; k<grid.length; k++) {
                if (isQueen(grid[i][k])) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean rowContainsQueen(char[][] grid, int row) {
        for (int c=0; c<grid.length; c++) {
            if (isQueen(grid[row][c])) {
                return true;
            }
        }
        return false;
    }

    private static int nextFreeRow(char[][] grid, int currentRow) {
        int row = currentRow;
        while (row < grid.length && rowContainsQueen(grid, row)) {
            row++;
        }
        return row;
    }
}
