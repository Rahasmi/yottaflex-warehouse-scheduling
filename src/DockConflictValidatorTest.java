
import java.time.LocalDateTime;

public class DockConflictValidatorTest {

    static int passed = 0;
    static int failed = 0;

    static LocalDateTime time(int hour, int minute) {
        return LocalDateTime.of(
            2026, 11, 8, hour, minute);
    }

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

    static void invalid(String name, Runnable action) {
        try {
            action.run();
            System.out.println("FAIL: " + name);
            failed++;
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + name);
            passed++;
        }
    }

    public static void main(String[] args) {

        check("Same dock overlap",
            DockConflictValidator.hasConflict(
                "DOCK-01", time(11,0), time(13,0),
                "DOCK-01", time(10,0), time(12,0)),
            true);

        check("No overlap",
            DockConflictValidator.hasConflict(
                "DOCK-01", time(13,0), time(15,0),
                "DOCK-01", time(10,0), time(12,0)),
            false);

        check("Different docks",
            DockConflictValidator.hasConflict(
                "DOCK-02", time(11,0), time(13,0),
                "DOCK-01", time(10,0), time(12,0)),
            false);

        check("Exact boundary",
            DockConflictValidator.hasConflict(
                "DOCK-01", time(12,0), time(14,0),
                "DOCK-01", time(10,0), time(12,0)),
            false);

        invalid("Invalid time range", () ->
            DockConflictValidator.hasConflict(
                "DOCK-01", time(13,0), time(12,0),
                "DOCK-01", time(10,0), time(12,0)));

        invalid("Missing dock ID", () ->
            DockConflictValidator.hasConflict(
                null, time(11,0), time(13,0),
                "DOCK-01", time(10,0), time(12,0)));

        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);

        if (failed > 0) System.exit(1);
    }
}
