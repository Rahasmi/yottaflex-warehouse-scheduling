
import java.time.LocalDateTime;

public class DockConflictValidator {

    public static boolean hasConflict(
            String requestedDock,
            LocalDateTime requestedStart,
            LocalDateTime requestedEnd,
            String existingDock,
            LocalDateTime existingStart,
            LocalDateTime existingEnd) {

        if (requestedDock == null ||
            existingDock == null ||
            requestedStart == null ||
            requestedEnd == null ||
            existingStart == null ||
            existingEnd == null) {
            throw new IllegalArgumentException(
                "Dock and time values are required");
        }

        if (requestedDock.isBlank() ||
            existingDock.isBlank() ||
            !requestedEnd.isAfter(requestedStart) ||
            !existingEnd.isAfter(existingStart)) {
            throw new IllegalArgumentException(
                "Invalid dock or time range");
        }

        if (!requestedDock.trim().equals(
                existingDock.trim())) {
            return false;
        }

        return requestedStart.isBefore(existingEnd)
            && requestedEnd.isAfter(existingStart);
    }
}
