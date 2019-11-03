import java.util.Random;

public class TestGenB {

    public static void main(String[] args) {
        System.out.println(generate());
    }

    public static String generate() {
        var casesCount = 8;
        var sb = new StringBuilder();
        sb.append(casesCount);
        sb.append("\n");

        var rnd = new Random();

        for (int t=1;t<=casesCount; t++) {
            var packagesCount = rnd.nextInt(10000) + 1;
            var keep = rnd.nextInt(packagesCount + 1);
            var remove = rnd.nextInt(packagesCount + 1);
            var deps = rnd.nextInt(200000 + 1);

            sb.append(packagesCount);
            sb.append(" ");
            sb.append(keep);
            sb.append(" ");
            sb.append(remove);
            sb.append(" ");
            sb.append(deps);
            sb.append("\n");

            for (int k=0; k<keep; k++) {
                sb.append(rnd.nextInt(packagesCount) + 1);
                if (k == keep - 1) {
                    sb.append("\n");
                } else {
                    sb.append(" ");
                }
            }

            for (int r=0; r<remove; r++) {
                sb.append(rnd.nextInt(packagesCount) + 1);
                if (r == remove - 1) {
                    sb.append("\n");
                } else {
                    sb.append(" ");
                }
            }

            for (int d=0; d<deps; d++) {
                sb.append(rnd.nextInt(packagesCount) + 1);
                sb.append(" ");
                sb.append(rnd.nextInt(packagesCount) + 1);
                sb.append("\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
