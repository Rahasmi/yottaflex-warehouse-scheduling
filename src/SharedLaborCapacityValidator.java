
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SharedLaborCapacityValidator {

    public record Booking(
        String facilityId,
        LocalDateTime start,
        LocalDateTime end,
        int requiredWorkers
    ) {}

    private record Change(
        LocalDateTime time,
        int workerDelta
    ) {}

    public static boolean canAllocate(
        String facilityId,
        LocalDateTime start,
        LocalDateTime end,
        int requiredWorkers,
        int availableWorkers,
        List<Booking> existingBookings
    ) {
        if (facilityId == null || facilityId.isBlank()
            || start == null || end == null
            || !end.isAfter(start)
            || requiredWorkers <= 0
            || availableWorkers < requiredWorkers
            || existingBookings == null) {
            return false;
        }

        List<Change> changes = new ArrayList<>();

        changes.add(new Change(start, requiredWorkers));
        changes.add(new Change(end, -requiredWorkers));

        for (Booking booking : existingBookings) {
            if (booking == null
                || booking.facilityId() == null
                || booking.start() == null
                || booking.end() == null
                || !booking.end().isAfter(booking.start())
                || booking.requiredWorkers() <= 0) {
                return false;
            }

            if (!facilityId.equals(booking.facilityId())) {
                continue;
            }

            if (start.isBefore(booking.end())
                && end.isAfter(booking.start())) {

                LocalDateTime overlapStart =
                    start.isAfter(booking.start())
                    ? start : booking.start();

                LocalDateTime overlapEnd =
                    end.isBefore(booking.end())
                    ? end : booking.end();

                changes.add(new Change(
                    overlapStart,
                    booking.requiredWorkers()
                ));

                changes.add(new Change(
                    overlapEnd,
                    -booking.requiredWorkers()
                ));
            }
        }

        // At equal timestamps, releases happen first.
        changes.sort(
            Comparator.comparing(Change::time)
                .thenComparingInt(Change::workerDelta)
        );

        int activeWorkers = 0;

        for (Change change : changes) {
            activeWorkers += change.workerDelta();

            if (activeWorkers > availableWorkers) {
                return false;
            }
        }

        return true;
    }
}
