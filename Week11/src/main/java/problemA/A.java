package problemA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

class TrieNode {
    private TrieNode[] children;
    private boolean isWord;
    public TrieNode() {
        this.children = new TrieNode[26 * 2];
        this.isWord = false;

    }

    public TrieNode[] getChildren() {
        return children;
    }

    public boolean isWord() {
        return isWord;
    }

    public boolean lookup(String s) {
        if (s.length() == 0) {
            return this.isWord;
        }

        var first = s.charAt(0);
        var pos = charToAlphabetPos(first);

        if (this.children[pos] == null) {
            return false;
        }

        return this.children[pos].lookup(s.substring(1));
    }

    public void insert(String s) {
        if (s.length() == 0) {
            this.isWord = true;
            return;
        }

        var first = s.charAt(0);
        var pos = charToAlphabetPos(first);

        if (this.children[pos] == null) {
            this.children[pos] = new TrieNode();
        }
        this.children[pos].insert(s.substring(1));
    }

    public int countChildren() {
        return (int) Arrays.stream(this.children).filter(Objects::nonNull).count();
    }

    public int countCommonPrefixes() {
        if (this.countChildren() == 0) {
            return 0;
        }

        int retval = 0;

        for (int i = 0; i < this.children.length; i++) {
            if (this.children[i] == null) {
                continue;
            }

            retval += this.children[i].countCommonPrefixes();
        }

        if (this.isWord) {
            retval++;
        }

        return retval;
    }


    private static int charToAlphabetPos(char c) {
        if ('A' <= c && c <= 'Z') {
            return c - 'A';
        } else if ('a' <= c && c <= 'z') {
            return c - 'a' + 26;
        } else {
            return -1;
        }
    }
}

public class A {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t = 1; t <= casesCount; t++) {
            var nameCount = Integer.parseInt(buff.readLine());

            var root = new TrieNode();

            for (int i = 0; i < nameCount; i++) {
                var name = buff.readLine();
                root.insert(name);
            }

            System.out.println("Case #" + t + ": " + root.countCommonPrefixes());

            buff.readLine();
        }

    }
}
