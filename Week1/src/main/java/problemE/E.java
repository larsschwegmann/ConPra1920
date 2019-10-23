package problemE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class E {
    public static void main(String[] args) throws IOException {
        var inStream = new InputStreamReader(System.in);
        var in = new BufferedReader(inStream);

        var noLinesStr = in.readLine();
        var noLines = Integer.parseInt(noLinesStr);

        for (int i=1; i<=noLines; i++) {
            var builder = new StringBuilder("Case #");
            builder.append(i);
            builder.append(": ");

            var line = in.readLine();
            // Regex inspired by https://stackoverflow.com/questions/2206378/how-to-split-a-string-but-also-keep-the-delimiters
            var splitLine = line.split("((?<=plus)|(?=plus)|((?<=minus)|(?=minus))|((?<=times)|(?=times))|((?<=tothepowerof)|(?=tothepowerof)))");

            Integer result = null;

            String operator = null;
            Integer operand = null;

            for (String token : splitLine) {
                try {
                    var number = Integer.parseInt(token);
                    // Token is operand
                    if (result == null) {
                        result = number;
                    } else {
                        operand = number;
                    }
                } catch (Exception e) {
                    // token is operator not operand
                    operator = token;
                }
                if (result != null && operator != null && operand != null) {
                    switch (operator) {
                        case "plus":
                            result += operand;
                            operand = null;
                            operator = null;
                            break;
                        case "minus":
                            result -= operand;
                            operand = null;
                            operator = null;
                            break;
                        case "times":
                            result *= operand;
                            operand = null;
                            operator = null;
                            break;
                        case "tothepowerof":
                            result = (int) Math.round(Math.pow(result, operand));
                            operand = null;
                            operator = null;
                            break;
                    }
                }
            }

            builder.append(result);
            System.out.println(builder.toString());
        }
    }
}
