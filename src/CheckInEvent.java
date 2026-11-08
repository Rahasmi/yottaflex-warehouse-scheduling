
import java.time.LocalDateTime;

public record CheckInEvent(
    String eventId,
    String appointmentId,
    String vehicleId,
    LocalDateTime checkInTime,
    String eventType
) {
    public CheckInEvent {
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException(
                "Event ID is required"
            );
        }

        if (appointmentId == null
                || appointmentId.isBlank()) {
            throw new IllegalArgumentException(
                "Appointment ID is required"
            );
        }

        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException(
                "Vehicle ID is required"
            );
        }

        if (checkInTime == null) {
            throw new IllegalArgumentException(
                "Check-in time is required"
            );
        }

        if (eventType == null || eventType.isBlank()) {
            throw new IllegalArgumentException(
                "Event type is required"
            );
        }
    }
}
