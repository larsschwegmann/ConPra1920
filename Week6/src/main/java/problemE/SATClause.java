package problemE;

import java.util.Set;
import java.util.TreeSet;

public class SATClause {

    private Set<SATVariable> positiveVariables = new TreeSet<>();
    private Set<SATVariable> negatedVariables = new TreeSet<>();

    public void addPositive(SATVariable a) {
        positiveVariables.add(a);
    }

    public void addNegated(SATVariable a) {
        negatedVariables.add(a);
    }

    public boolean isSatisfied() {
        for (var v : positiveVariables) {
            if (v.isTrue()) {
                return true;
            }
        }
        for (var v : negatedVariables) {
            if (v.isFalse()) {
                return true;
            }
        }
        return false;
    }

    public Set<SATVariable> getVariables() {
        var set = new TreeSet<>(positiveVariables);
        set.addAll(negatedVariables);
        return set;
    }

    public boolean isEmpty() {
        return positiveVariables.isEmpty() && negatedVariables.isEmpty();
    }

    public boolean isUnitClause() {
        return (this.positiveVariables.size() == 1 && this.negatedVariables.isEmpty()) ||
                (this.positiveVariables.isEmpty() && this.negatedVariables.size() == 1);
    }

    public boolean isPositive(SATVariable v) {
        return this.positiveVariables.contains(v);
    }

    public SATVariable getUnitVariable() {
        assert isUnitClause();
        return getVariables().stream().findFirst().get();
    }

}
