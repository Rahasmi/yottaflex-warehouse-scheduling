
import java.time.LocalDateTime;

public class LaborShiftValidator {

    public static boolean canHandle(
            LaborShift shift,
            LocalDateTime appointmentStart,
            LocalDateTime appointmentEnd,
            int requiredWorkers) {

        if (shift == null ||
            appointmentStart == null ||
            appointmentEnd == null ||
            !appointmentEnd.isAfter(appointmentStart) ||
            requiredWorkers <= 0) {
            return false;
        }

        boolean withinShift =
            !appointmentStart.isBefore(shift.startTime())
            && !appointmentEnd.isAfter(shift.endTime());

        boolean enoughWorkers =
            shift.availableWorkers() >= requiredWorkers;

        return withinShift && enoughWorkers;
    }
}
