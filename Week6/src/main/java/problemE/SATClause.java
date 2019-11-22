package problemE;

import java.util.Set;
import java.util.TreeSet;

public class SATClause {
    private int id;
    private Set<SATVariable> positiveVariables = new TreeSet<>();
    private Set<SATVariable> negatedVariables = new TreeSet<>();

    public SATClause(int id) {
        this.id = id;
    }

    public void addPositive(SATVariable a) {
        positiveVariables.add(a);
    }

    public void addNegated(SATVariable a) {
        negatedVariables.add(a);
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

    public boolean contains(SATVariable a) {
        return this.positiveVariables.contains(a) || this.negatedVariables.contains(a);
    }

    public void removeVariable(SATVariable a) {
        this.positiveVariables.remove(a);
        this.negatedVariables.remove(a);
    }

    public SATVariable getUnitVariable() {
        assert isUnitClause();
        return getVariables().stream().findFirst().get();
    }

    public SATClause copy() {
        var copy = new SATClause(this.id);
        copy.positiveVariables = new TreeSet<>(this.positiveVariables);
        copy.negatedVariables = new TreeSet<>(this.negatedVariables);
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SATClause) {
            return ((SATClause) obj).id == this.id;
        } else {
            return false;
        }
    }
}
