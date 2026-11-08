
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentLifecycleValidator {

    public static boolean isValidCheckIn(
            LocalDateTime appointmentStart,
            LocalDateTime checkInTime) {

        if (appointmentStart == null || checkInTime == null) {
            return false;
        }

        // Early arrival on the appointment date is allowed.
        // Check-in on an earlier calendar date is rejected.
        return !checkInTime.toLocalDate()
            .isBefore(appointmentStart.toLocalDate());
    }

    public static boolean hasRecordedNoShow(
            String appointmentStatus,
            List<String> eventTypes) {

        if (appointmentStatus == null || eventTypes == null) {
            return false;
        }

        if (!appointmentStatus.equals("NO_SHOW")) {
            return true;
        }

        return eventTypes.contains("NO_SHOW");
    }
}
