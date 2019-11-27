package problemA;

class Clock {

    private String recordedStartTime;

    private Digit upperHours = new Digit(0, 2);
    private Digit lowerHours = new Digit(0, 3);
    private Digit upperMinutes = new Digit(0, 5);
    private Digit lowerMinutes = new Digit(0, 9);

    public void recordTime(String time, boolean isStart) {
        if (isStart) {
            recordedStartTime = time;
        }

        // Parse time
        var uhDigit = Character.getNumericValue(time.charAt(0));
        var lhDigit = Character.getNumericValue(time.charAt(1));
        var umDigit = Character.getNumericValue(time.charAt(3));
        var lmDigit = Character.getNumericValue(time.charAt(4));

        upperHours.recordDigit(uhDigit, isStart);
        lowerHours.recordDigit(lhDigit, isStart);
        upperMinutes.recordDigit(umDigit, isStart);
        lowerMinutes.recordDigit(lmDigit, isStart);
    }

}
