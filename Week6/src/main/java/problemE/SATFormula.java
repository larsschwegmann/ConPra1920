package problemE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SATFormula {

    private List<SATClause> clauses = new ArrayList<>();

    public void addClause(SATClause clause) {
        this.clauses.add(clause);
    }

    public boolean isSatisfied() {
        for (var c : this.clauses) {
            if (!c.isSatisfied()) {
                return false;
            }
        }
        return true;
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

    public Optional<SATVariable> getNextUnassignedVariable() {
        return this.getAllVariables().stream().filter(v -> !v.wasAssignedValue()).findFirst();
    }

    public void removeClause(SATClause c) {
        this.clauses.remove(c);
    }
}
