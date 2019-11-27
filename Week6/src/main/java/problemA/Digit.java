package problemA;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class Digit {

    private int validLowerBound;
    private int validUpperBound;

    private Set<Integer> startSetOptions = new HashSet<>();

    Digit(int lowerBound, int upperBound) {
        assert lowerBound > 0;
        assert upperBound <= 9;
        assert lowerBound <= upperBound;

        this.validLowerBound = lowerBound;
        this.validUpperBound = upperBound;
    }

    public void recordDigit(int d, boolean isStart) {
        if (isStart) {
            this.startSetOptions = possibleValues(d);
        } else {
            if (isValidInRange(d)) {

            }
        }
    }

    public Set<Integer> possibleValues(int d) {
        var resultSet = new HashSet<Integer>();
        switch (d) {
            case 0:
                resultSet.add(0);
                resultSet.add(8);
                break;
            case 1:
                resultSet.add(0);
                resultSet.add(1);
                resultSet.add(3);
                resultSet.add(4);
                resultSet.add(8);
                resultSet.add(9);
                break;
            case 2:
                resultSet.add(2);
                resultSet.add(8);
                break;
            case 3:
                resultSet.add(3);
                resultSet.add(8);
                resultSet.add(9);
                break;
            case 4:
                resultSet.add(4);
                resultSet.add(8);
                resultSet.add(9);
                break;
            case 5:
                resultSet.add(5);
                resultSet.add(6);
                resultSet.add(8);
                resultSet.add(9);
                break;
            case 6:
                resultSet.add(6);
                resultSet.add(8);
                break;
            case 7:
                resultSet.add(3);
                resultSet.add(7);
                resultSet.add(8);
                resultSet.add(9);
                break;
            case 8:
                resultSet.add(8);
                break;
            case 9:
                resultSet.add(8);
                resultSet.add(9);
                break;
            default:
                break;
        }
        return resultSet.stream().filter(this::isValidInRange).collect(Collectors.toSet());
    }

    private boolean isValidInRange(int d) {
        return validLowerBound <= d && d <= validUpperBound;
    }

}
