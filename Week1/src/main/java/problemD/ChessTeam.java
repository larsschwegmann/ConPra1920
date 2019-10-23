package problemD;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ChessTeam implements Comparable<ChessTeam> {

    private int[] players;

    ChessTeam(int[] players) throws IllegalArgumentException {
        if (players.length != 5) {
            throw new IllegalArgumentException("5 Players per Chess Team");
        }
        this.players = Arrays.stream(players)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private int getSkillAt(int pos) {
        return players[pos];
    }

    public String toString() {
        return Arrays.stream(players)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(" "));
    }

    @Override
    public int compareTo(ChessTeam other) {
        var pos = 0;
        while (true) {
            var skill = getSkillAt(pos);
            var otherSkill = other.getSkillAt(pos);
            var result = skill - otherSkill;

            if (result == 0 && pos < 4) {
                pos++;
            } else {
                return result;
            }
        }
    }
}
