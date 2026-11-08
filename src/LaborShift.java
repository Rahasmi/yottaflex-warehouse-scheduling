
import java.time.LocalDateTime;

public record LaborShift(
    String shiftId,
    String facilityId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    int availableWorkers
) {
    public LaborShift {
        if (shiftId == null || shiftId.isBlank()) {
            throw new IllegalArgumentException(
                "Shift ID is required"
            );
        }

        if (facilityId == null || facilityId.isBlank()) {
            throw new IllegalArgumentException(
                "Facility ID is required"
            );
        }

        if (startTime == null || endTime == null
                || !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                "Invalid shift time range"
            );
        }

        if (availableWorkers < 0) {
            throw new IllegalArgumentException(
                "Workers cannot be negative"
            );
        }
    }
}
