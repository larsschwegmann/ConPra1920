package problemE;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SATFormula {

    private List<SATClause> clauses = new ArrayList<>();

    public void addClause(SATClause clause) {
        this.clauses.add(clause);
    }

    public boolean containsEmptyClause() {
        for (var c : clauses) {
            if (c.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public List<SATClause> getUnitClauses() {
        return this.clauses.stream().filter(SATClause::isUnitClause).collect(Collectors.toList());
    }

    public List<SATVariable> getAllVariables() {
        var varSet = new TreeSet<SATVariable>();
        for (var c : this.clauses) {
            varSet.addAll(c.getVariables());
        }
        return new ArrayList<>(varSet);
    }

    public boolean isEmpty() {
        return this.clauses.isEmpty();
    }

    public SATFormula copy() {
        var copy = new SATFormula();
        this.clauses.forEach(c -> copy.addClause(c.copy()));
        return copy;
    }

    public SATFormula simplify(SATVariable a) {
        var copy = this.copy();
        var toRemove = new ArrayList<SATClause>();
        for (var c : copy.clauses) {
            if (c.contains(a)) {
                if (c.isPositive(a)) {
                    if (a.isTrue()) {
                        // Remove clause
                        toRemove.add(c);
                    } else {
                        c.removeVariable(a);
                    }
                } else {
                    if (a.isFalse()) {
                        // Remove var from clause
                        toRemove.add(c);
                    } else {
                        c.removeVariable(a);
                    }
                }
            }
        }
        copy.clauses.removeAll(toRemove);
        return copy;
    }
}
