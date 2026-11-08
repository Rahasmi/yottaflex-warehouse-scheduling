
import java.time.LocalTime;

public class SchedulingDecisionTest {

    static String schedule(
            boolean compatible,
            boolean dockAvailable,
            int workersAvailable,
            int workersRequired,
            LocalTime requestedStart,
            LocalTime requestedEnd,
            LocalTime existingStart,
            LocalTime existingEnd) {

        if (!requestedStart.isBefore(requestedEnd))
            return "REJECTED: INVALID_TIME";

        if (!compatible)
            return "REJECTED: INCOMPATIBLE_DOCK";

        if (!dockAvailable)
            return "REJECTED: DOCK_UNAVAILABLE";

        if (workersAvailable < workersRequired)
            return "REJECTED: INSUFFICIENT_LABOR";

        boolean overlap =
            requestedStart.isBefore(existingEnd)
            && existingStart.isBefore(requestedEnd);

        if (overlap)
            return "REJECTED: DOCK_OVERLAP";

        return "APPROVED";
    }

    public static void main(String[] args) {

        LocalTime existingStart = LocalTime.of(10, 0);
        LocalTime existingEnd = LocalTime.of(11, 0);

        System.out.println("Truck A: " +
            schedule(true, true, 5, 3,
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                existingStart, existingEnd));

        System.out.println("Truck B: " +
            schedule(true, true, 5, 3,
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
                existingStart, existingEnd));

        System.out.println("Truck C: " +
            schedule(false, true, 5, 3,
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                existingStart, existingEnd));

        System.out.println("Truck D: " +
            schedule(true, true, 2, 4,
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                existingStart, existingEnd));
    }
}
