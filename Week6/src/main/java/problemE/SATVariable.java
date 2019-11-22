package problemE;

public class SATVariable implements Comparable<SATVariable> {
    private int name;
    private boolean assignedValue = false;
    private boolean unassigned = true;

    public SATVariable(int name) {
        this.name = name;
    }

    public void setAssignedValue(boolean assignedValue) {
        this.unassigned = false;
        this.assignedValue = assignedValue;
    }

    public boolean isTrue() {
        return this.assignedValue;
    }

    public boolean isFalse() {
        return !this.assignedValue;
    }

    public boolean wasAssignedValue() {
        return !this.unassigned;
    }

    public void resetAssignment() {
        this.unassigned = true;
        this.assignedValue = false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SATVariable that = (SATVariable) o;

        if (name != that.name) return false;
        return assignedValue == that.assignedValue;
    }

    @Override
    public int hashCode() {
        int result = name;
        result = 31 * result + (assignedValue ? 1 : 0);
        return result;
    }

    @Override
    public int compareTo(SATVariable o) {
        return Integer.compare(this.name, o.name);
    }
}
