package problemD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class D {
    public static void main(String[] args) throws IOException {
        var inStream = new InputStreamReader(System.in);
        var in = new BufferedReader(inStream);

        var noTestsStr = in.readLine();
        var noTests = Integer.parseInt(noTestsStr);

        // Iterate over test cases
        for (int test=1; test<=noTests; test++) {
            var builder = new StringBuilder();
            // Output Test Case header
            builder.append("Case #");
            builder.append(test);
            builder.append(":\n");

            var noLinesStr = in.readLine();
            var noLines = Integer.parseInt(noLinesStr);

            // Team Creation

            var teams = new ChessTeam[noLines];
            for (int i=0; i<noLines; i++) {
                // Parse Team Members
                var teamStr = in.readLine();
                var tokenizer = new StringTokenizer(teamStr, " ", false);
                var teamMembers = new int[5];
                for (int k=0; k<5; k++) {
                    var memberStr = tokenizer.nextToken();
                    var member = Integer.parseInt(memberStr);
                    teamMembers[k] = member;
                }

                // Init team
                teams[i] =  new ChessTeam(teamMembers);
            }

            // Sort teams based on skill
            Arrays.sort(teams, Comparator.reverseOrder());

            // Print results
            for (ChessTeam team : teams) {
                builder.append(team.toString());
                builder.append("\n");
            }
            System.out.print(builder.toString());

            if (test < noTests) {
                // Skip blank line between test inputs
                in.readLine();
            }
        }
    }
}
