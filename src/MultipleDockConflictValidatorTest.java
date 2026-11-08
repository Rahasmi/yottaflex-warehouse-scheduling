
import java.time.LocalDateTime;
import java.util.List;

public class MultipleDockConflictValidatorTest {

    static int passed = 0;
    static int failed = 0;

    static void check(String name,
                      boolean actual,
                      boolean expected) {
        if (actual == expected) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAIL: " + name);
            failed++;
        }
    }

    static LocalDateTime time(int hour, int minute) {
        return LocalDateTime.of(
            2026, 11, 8, hour, minute);
    }

    public static void main(String[] args) {

        List<MultipleDockConflictValidator.Booking> bookings =
            List.of(
                new MultipleDockConflictValidator.Booking(
                    "DOCK-01", time(9, 0), time(10, 0)),
                new MultipleDockConflictValidator.Booking(
                    "DOCK-01", time(11, 0), time(12, 0)),
                new MultipleDockConflictValidator.Booking(
                    "DOCK-01", time(14, 0), time(15, 0))
            );

        check("Overlaps second booking",
            MultipleDockConflictValidator.hasAnyConflict(
                "DOCK-01", time(11, 30), time(12, 30),
                bookings), true);

        check("Available time slot",
            MultipleDockConflictValidator.hasAnyConflict(
                "DOCK-01", time(12, 0), time(13, 0),
                bookings), false);

        check("Different dock",
            MultipleDockConflictValidator.hasAnyConflict(
                "DOCK-02", time(11, 30), time(12, 30),
                bookings), false);

        check("Back-to-back booking",
            MultipleDockConflictValidator.hasAnyConflict(
                "DOCK-01", time(10, 0), time(11, 0),
                bookings), false);

        check("Empty booking list",
            MultipleDockConflictValidator.hasAnyConflict(
                "DOCK-01", time(9, 0), time(10, 0),
                List.of()), false);

        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);

        if (failed > 0) System.exit(1);
    }
}
