package problemA;

import java.util.Random;

public class TestGen {

    public static void main(String[] args) {
        int noCases = 20;
        var sb = new StringBuilder();
        sb.append(noCases);
        sb.append("\n");

        var rnd = new Random();
        for (int t=1; t<=noCases; t++) {
            var people = rnd.nextInt(150) + 1;
            sb.append(people);
            sb.append("\n");
            var outMat = new int[people][people];
            for (int row=0; row<people; row++) {
                for (int col=0; col<people; col++) {
                    if (row == col) {
                        outMat[row][col] = 0;
                    } else if (row < col) {
                        outMat[row][col] = rnd.nextInt(10001);
                    } else {
                        outMat[row][col] = outMat[col][row];
                    }
                    sb.append(outMat[row][col]);
                    if (col == people - 1) {
                        sb.append("\n");
                    } else {
                        sb.append(" ");
                    }
                }
            }
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

}
