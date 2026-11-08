
import java.time.LocalDateTime;
import java.util.List;

public class MultipleDockConflictValidator {

    public record Booking(
        String dockId,
        LocalDateTime start,
        LocalDateTime end
    ) {}

    public static boolean hasAnyConflict(
            String requestedDock,
            LocalDateTime requestedStart,
            LocalDateTime requestedEnd,
            List<Booking> existingBookings) {

        if (existingBookings == null) {
            throw new IllegalArgumentException(
                "Booking list cannot be null");
        }

        for (Booking booking : existingBookings) {
            if (booking == null) {
                throw new IllegalArgumentException(
                    "Booking cannot be null");
            }

            if (DockConflictValidator.hasConflict(
                    requestedDock,
                    requestedStart,
                    requestedEnd,
                    booking.dockId(),
                    booking.start(),
                    booking.end())) {
                return true;
            }
        }

        return false;
    }
}
