
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SharedEquipmentCapacityValidator {

    public record Booking(
        String facilityId,
        String equipmentType,
        LocalDateTime start,
        LocalDateTime end,
        int requiredUnits
    ) {}

    private record Change(
        LocalDateTime time,
        int unitDelta
    ) {}

    public static boolean canAllocate(
        String facilityId,
        String equipmentType,
        LocalDateTime start,
        LocalDateTime end,
        int requiredUnits,
        int availableUnits,
        List<Booking> existingBookings
    ) {
        if (facilityId == null || facilityId.isBlank()
            || equipmentType == null || equipmentType.isBlank()
            || start == null || end == null
            || !end.isAfter(start)
            || requiredUnits <= 0
            || availableUnits < requiredUnits
            || existingBookings == null) {
            return false;
        }

        List<Change> changes = new ArrayList<>();

        changes.add(new Change(start, requiredUnits));
        changes.add(new Change(end, -requiredUnits));

        for (Booking booking : existingBookings) {
            if (booking == null
                || booking.facilityId() == null
                || booking.equipmentType() == null
                || booking.start() == null
                || booking.end() == null
                || !booking.end().isAfter(booking.start())
                || booking.requiredUnits() <= 0) {
                return false;
            }

            if (!facilityId.equals(booking.facilityId())
                || !equipmentType.equals(booking.equipmentType())) {
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
                    booking.requiredUnits()
                ));

                changes.add(new Change(
                    overlapEnd,
                    -booking.requiredUnits()
                ));
            }
        }

        // Equipment released at an end time can be
        // reused by a booking starting at that time.
        changes.sort(
            Comparator.comparing(Change::time)
                .thenComparingInt(Change::unitDelta)
        );

        int activeUnits = 0;

        for (Change change : changes) {
            activeUnits += change.unitDelta();

            if (activeUnits > availableUnits) {
                return false;
            }
        }

        return true;
    }
}
