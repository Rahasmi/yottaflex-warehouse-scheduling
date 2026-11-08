
import java.time.LocalDateTime;

public class LaborShiftValidatorTest {

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

        LocalDateTime start =
            LocalDateTime.parse("2026-11-08T09:00:00");
        LocalDateTime end =
            LocalDateTime.parse("2026-11-08T17:00:00");

        LaborShift shift = new LaborShift(
            "SHIFT-001", "FAC-001",
            start, end, 5
        );

        check("Valid appointment",
            LaborShiftValidator.canHandle(
                shift, start.plusHours(1),
                start.plusHours(3), 3), true);

        check("Insufficient workers",
            LaborShiftValidator.canHandle(
                shift, start.plusHours(1),
                start.plusHours(3), 7), false);

        check("Appointment before shift",
            LaborShiftValidator.canHandle(
                shift, start.minusHours(1),
                start.plusHours(2), 3), false);

        check("Appointment after shift",
            LaborShiftValidator.canHandle(
                shift, end.minusHours(1),
                end.plusHours(1), 3), false);

        check("Exact shift boundaries",
            LaborShiftValidator.canHandle(
                shift, start, end, 5), true);

        check("Invalid appointment time",
            LaborShiftValidator.canHandle(
                shift, end, start, 3), false);

        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
