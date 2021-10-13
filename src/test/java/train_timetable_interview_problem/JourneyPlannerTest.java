package train_timetable_interview_problem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JourneyPlannerTest {

    private JourneyPlanner journeyPlanner;

    @BeforeEach
    void setup() {
        journeyPlanner = new JourneyPlanner(TestData.TIMETABLE);
    }

    @AfterEach
    void tearDown() {
        journeyPlanner = null;
    }

    @Test
    public void shouldReportDurationOfJourneyBetweenTwoStations() {

        // When
        int duration = journeyPlanner.durationOfJourneyStartingAt("0907", "Camborne", "Exeter St Davids");

        // Then
        assertEquals(duration, 150);
    }

    @Test
    public void shouldReportDurationForFirstAvailableTrain() {

        // When
        int duration = journeyPlanner.durationOfJourneyStartingAt("1023", "Camborne", "Exeter St Davids");

        // Then
        assertEquals(duration, 159);
    }

    @Test
    public void shouldReportDurationIncludingWaitingTimeOnPlatform() {
        // When
        int duration = journeyPlanner.durationOfJourneyStartingAt("1101", "St Austell", "Par");

        // Then
        assertEquals(duration, 56);
    }


    @Test
    public void shouldReportDurationFastestTrainBetweenTwoStations() {

        // When
        int duration = journeyPlanner.durationOfJourneyStartingAt(null, "Exeter St Davids", "London Paddington");

        // Then
        assertEquals(duration, 1357);
    }


    @Test
    public void shouldReportDurationEarliestRouteWhenThereIsATieBetweenFastestTrains() {

        // When
        int duration = journeyPlanner.durationOfJourneyStartingAt(null, "Par", "Bodmin Parkway");

        // Then
        assertEquals(duration, 1108);
    }
}
