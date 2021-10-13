package train_timetable_interview_problem;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class JourneyPlanner {

    private final String[][] timetable;

    public JourneyPlanner(String[][] timetable) {
        this.timetable = timetable;
    }

    public int durationOfJourneyStartingAt(String arriveAtStationTime, String startStationName, String endStationName) {
        int startStationPosition = IntStream.range(0, timetable[0].length).filter(i -> timetable[0][i].equals(startStationName)).findFirst().orElse(-1);
        int endStationPosition = IntStream.range(0, timetable[0].length).filter(i -> timetable[0][i].equals(endStationName)).findFirst().orElse(-1);
        AtomicBoolean isNotStartTime = new AtomicBoolean(false);
        AtomicReference<Long> tempJourneyTimeDiff = new AtomicReference<>(0l);
        String[] result = Arrays.stream(timetable).skip(1).
                reduce(new String[]{"", "", ""}, (a, b) ->
                {
                    if (arriveAtStationTime != null) {
                        if (Integer.parseInt(b[startStationPosition]) >= Integer.parseInt(arriveAtStationTime) && !isNotStartTime.get()) {
                            a[0] = b[startStationPosition];
                            a[1] = b[endStationPosition];
                            if (Integer.parseInt(b[startStationPosition]) != Integer.parseInt(arriveAtStationTime)) {
                                a[2] = arriveAtStationTime;
                            }
                            isNotStartTime.set(true);
                        }
                    } else {
                        LocalTime time1 = getTime(b[startStationPosition]);
                        LocalTime time2 = getTime(b[endStationPosition]);
                        long journeyTime = Duration.between(time1, time2).toMinutes();
                        if (tempJourneyTimeDiff.get() == 0) {
                            tempJourneyTimeDiff.set(journeyTime);
                        }
                        if (journeyTime < tempJourneyTimeDiff.get()) {
                            tempJourneyTimeDiff.set(journeyTime);
                            a[0] = b[startStationPosition];
                        }
                    }
                    return a;
                });
        if (arriveAtStationTime == null) {
            return Integer.parseInt(result[0]);
        } else {
            LocalTime time1 = getTime(result[0]);
            LocalTime time2 = getTime(result[1]);
            int duration =  (int) Duration.between(time1, time2).toMinutes();
            if(result.length == 3 && !result[2].equals("")){
                LocalTime time3 = getTime(result[2]);
                int waitTime = (int) Duration.between(time3, time1).toMinutes();
                duration+=waitTime;
            }
            return duration;
        }

    }

    private LocalTime getTime(String time){
        return  LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmm"));
    }
}
