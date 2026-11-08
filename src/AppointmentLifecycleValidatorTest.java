
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentLifecycleValidatorTest {

    static int passed = 0;
    static int failed = 0;

    static void check(String name, boolean actual,
                      boolean expected) {

        if (actual == expected) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAIL: " + name);
            failed++;
        }
    }

    public static void main(String[] args) {

        LocalDateTime appointment =
            LocalDateTime.parse("2026-11-08T10:00:00");

        check("Valid check-in at appointment time",
            AppointmentLifecycleValidator.isValidCheckIn(
                appointment, appointment), true);

        check("Valid early arrival on same date",
            AppointmentLifecycleValidator.isValidCheckIn(
                appointment, appointment.minusHours(2)), true);

        check("Reject check-in previous day",
            AppointmentLifecycleValidator.isValidCheckIn(
                appointment, appointment.minusDays(1)), false);

        check("Reject check-in two days early",
            AppointmentLifecycleValidator.isValidCheckIn(
                appointment, appointment.minusDays(2)), false);

        check("Reject missing check-in timestamp",
            AppointmentLifecycleValidator.isValidCheckIn(
                appointment, null), false);

        check("Recorded no-show accepted",
            AppointmentLifecycleValidator.hasRecordedNoShow(
                "NO_SHOW",
                List.of("REQUESTED", "SCHEDULED", "NO_SHOW")),
            true);

        check("Reject missing no-show event",
            AppointmentLifecycleValidator.hasRecordedNoShow(
                "NO_SHOW",
                List.of("REQUESTED", "SCHEDULED")),
            false);

        check("Normal scheduled appointment accepted",
            AppointmentLifecycleValidator.hasRecordedNoShow(
                "SCHEDULED",
                List.of("REQUESTED", "SCHEDULED")),
            true);

        check("Reject missing event history",
            AppointmentLifecycleValidator.hasRecordedNoShow(
                "NO_SHOW", null), false);

        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
