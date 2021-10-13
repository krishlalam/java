package train_timetable_interview_problem;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class JourneyPlanner {

    private String[][] timetable;

    public JourneyPlanner(String[][] timetable) {
        this.timetable = timetable;
    }

    public int durationOfJourneyStartingAt(String arriveAtStationTime, String startStationName, String endStationName) {
        int startPosition = IntStream.range(0, timetable[0].length).filter(i -> timetable[0][i].equals(startStationName)).findFirst().orElse(-1);
        int endPosition = IntStream.range(0, timetable[0].length).filter(i -> timetable[0][i].equals(endStationName)).findFirst().orElse(-1);
        AtomicBoolean isNotStartTime = new AtomicBoolean(false);
        AtomicReference<Long> tempJourneyTimeDiff = new AtomicReference<>(0l);
        String[] result = Arrays.stream(timetable).skip(1).
                reduce(new String[]{"", "",""}, (a, b) ->
                {
                    if (arriveAtStationTime != null) {
                        if (Integer.parseInt(b[startPosition]) >= Integer.parseInt(arriveAtStationTime) && !isNotStartTime.get()) {
                            a[0] = b[startPosition];
                            a[1] = b[endPosition];
                            if(Integer.parseInt(b[startPosition]) != Integer.parseInt(arriveAtStationTime)){
                                a[2] = arriveAtStationTime;
                            }
                            isNotStartTime.set(true);
                        }
                    } else {
                        LocalTime time1 = LocalTime.parse(b[startPosition], DateTimeFormatter.ofPattern("HHmm"));
                        LocalTime time2 = LocalTime.parse(b[endPosition], DateTimeFormatter.ofPattern("HHmm"));
                        long journeyTime = Duration.between(time1, time2).toMinutes();
                        if (tempJourneyTimeDiff.get() == 0) {
                            tempJourneyTimeDiff.set(journeyTime);
                        }
                        if (journeyTime < tempJourneyTimeDiff.get()) {
                            tempJourneyTimeDiff.set(journeyTime);
                            a[0] = b[startPosition];
                        }
                    }
                    return a;
                });
        if (arriveAtStationTime == null) {
            return Integer.parseInt(result[0]);
        } else {
            LocalTime time1 = LocalTime.parse(result[0], DateTimeFormatter.ofPattern("HHmm"));
            LocalTime time2 = LocalTime.parse(result[1], DateTimeFormatter.ofPattern("HHmm"));
            int duration =  (int) Duration.between(time1, time2).toMinutes();
            if(result.length == 3 && result[2] !=""){
                LocalTime time3 = LocalTime.parse(result[2], DateTimeFormatter.ofPattern("HHmm"));
                int waitTime = (int) Duration.between(time3, time1).toMinutes();
                duration+=waitTime;
            }
            return duration;
        }


    }
}
