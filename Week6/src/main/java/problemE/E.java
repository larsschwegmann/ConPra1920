package problemE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class E {
    public static void main(String[] args) throws IOException {
        var in = new InputStreamReader(System.in);
        var buff = new BufferedReader(in);

        var casesCount = Integer.parseInt(buff.readLine());

        for (int t=1; t<=casesCount; t++) {
            var mn = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            var m = mn[0];
            var n = mn[1];

            var vars = new ArrayList<SATVariable>(m);
            for (int i=1; i<=m; i++) {
                vars.add(new SATVariable(i));
            }

            var formula = new SATFormula();
            for (int i=0; i<n; i++) {
                var clause = new SATClause();
                var judgeLine = Arrays.stream(buff.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                for (var recipe : judgeLine) {
                    if (recipe == 0) {
                        break;
                    }
                    if (recipe > 0) {
                        clause.addPositive(vars.get(recipe - 1));
                    } else {
                        clause.addNegated(vars.get(Math.abs(recipe) - 1));
                    }
                }
                formula.addClause(clause);
            }

            var result = dpll(formula);

            System.out.println("Case #" + t + ": " + (result ? "yes" : "no"));

            buff.readLine();
        }
    }

    private static boolean dpll(SATFormula formula) {
        if (formula.containsEmptyClause()) {
            return false;
        }

        if (formula.getUnitClauses().size() > 0) {
            var unitClause = formula.getUnitClauses().stream().findFirst().get();
            var unitVar = unitClause.getUnitVariable();
            if (unitVar.wasAssignedValue()) {
                if (unitClause.isPositive(unitVar)) {
                    if (unitVar.isFalse()) {
                        return false;
                    }
                } else {
                    if (unitVar.isTrue()) {
                        return false;
                    }
                }
            } else {
                if (unitClause.isPositive(unitVar)) {
                    unitVar.setAssignedValue(true);
                } else {
                    unitVar.setAssignedValue(false);
                }
            }
            formula.removeClause(unitClause);
            return dpll(formula);
        }

        var var = formula.getNextUnassignedVariable();
        if (!var.isPresent()) {
            return formula.isSatisfied();
        }
        var.get().setAssignedValue(true);
        if (dpll(formula)) {
            return true;
        } else {
            var.get().setAssignedValue(false);
            if (dpll(formula)) {
                return true;
            } else {
                var.get().resetAssignment();
                return false;
            }
        }
    }
}
