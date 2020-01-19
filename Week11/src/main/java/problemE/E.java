package problemE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

class Trie {
    private Trie[] children;
    private boolean isWord;
    public Trie() {
        this.children = new Trie[26];
        this.isWord = false;
    }

    public Trie[] getChildren() {
        return children;
    }

    public void insert(String s) {
        if (s.length() == 0) {
            this.isWord = true;
            return;
        }

        var first = s.charAt(0);
        var pos = charToAlphabetPos(first);

        if (this.children[pos] == null) {
            this.children[pos] = new Trie();
        }
        var sub = s.substring(1);
        this.children[pos].insert(sub);
    }

    public int countChildren() {
        return (int) Arrays.stream(this.children).filter(Objects::nonNull).count();
    }

    public int getPlayerWhoCanForceWin() {
        if (isWord) {
            return 1;
        }

        var result = Arrays.stream(this.children).filter(Objects::nonNull).map(Trie::getPlayerWhoCanForceWin).anyMatch(a -> a == 2);
        if (result) {
            return 1;
        }

        return 2;
    }

    public int getPlayerWhoCanForceLoss() {
        if (isWord) {
            return 2;
        }

        var result = Arrays.stream(this.children).filter(Objects::nonNull).map(Trie::getPlayerWhoCanForceLoss).anyMatch(a -> a == 2);
        if (result) {
            return 1;
        }

        return 2;
    }

    private static int charToAlphabetPos(char c) {
        if ('a' <= c && c <= 'z') {
            return c - 'a';
        } else {
            return -1;
        }
    }
}

public class E {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t <= casesCount; t++) {
            var nm = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var n = nm[0];
            var m = nm[1];

            var root = new Trie();

            for (int i = 0; i < m; i++) {
                root.insert(buff.readLine());
            }

            System.out.println("Case #" + t + ":");

            var winForcingPlayer = root.getPlayerWhoCanForceWin();
            var lossForcingPlayer = root.getPlayerWhoCanForceLoss();

            for (int k = 0; k < 4; k++) {
                boolean leaBegins = k == 0 || k == 1;
                boolean winnerBeginsNext = k == 0 || k == 2;

                var leaPlayer = leaBegins ? 1 : 2;
                var beaPlayer = leaBegins ? 2 : 1;

                if (winnerBeginsNext) {
                    if (leaBegins) {
                        if (winForcingPlayer == leaPlayer) {
                            System.out.println("victory");
                        } else {
                            if (lossForcingPlayer == leaPlayer) {
                                System.out.println(n % 2 == 0 ? "victory" : "defeat");
                            } else {
                                System.out.println("defeat");
                            }
                        }
                    } else {
                        if (winForcingPlayer == beaPlayer) {
                            System.out.println("defeat");
                        } else {
                            if (lossForcingPlayer == beaPlayer) {
                                System.out.println(n % 2 == 0 ? "defeat" : "victory");
                            } else {
                                System.out.println("victory");
                            }
                        }
                    }
                } else {
                    if (leaBegins) {
                        if (winForcingPlayer != leaPlayer) {
                            System.out.println("defeat");
                        } else {
                            if (lossForcingPlayer != leaPlayer) {
                                System.out.println(n % 2 == 0 ? "defeat" : "victory");
                            } else {
                                System.out.println("victory");
                            }
                        }
                    } else {
                        if (winForcingPlayer != beaPlayer) {
                            System.out.println("victory");
                        } else {
                            if (lossForcingPlayer != beaPlayer) {
                                System.out.println(n % 2 == 0 ? "victory": "defeat");
                            } else {
                                System.out.println("defeat");
                            }
                        }
                    }
                }
            }

            buff.readLine();
        }

    }
}
