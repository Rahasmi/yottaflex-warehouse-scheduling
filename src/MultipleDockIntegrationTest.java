
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class MultipleDockIntegrationTest {
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
        LocalDateTime day =
            LocalDateTime.parse("2026-11-08T08:00:00");

        YardSlot yard =
            new YardSlot("Y-001", "FAC-001", 2, true);

        LaborShift shift = new LaborShift(
            "SHIFT-001", "FAC-001",
            day, day.plusHours(10), 5
        );

        HandlingRequirement req =
            new HandlingRequirement(
                "REQ-001", "STANDARD", 3, "FORKLIFT"
            );

        LocalDateTime start = day.plusHours(2);
        LocalDateTime end = day.plusHours(3);

        List<MultipleDockConflictValidator.Booking>
            bookings = List.of(
                new MultipleDockConflictValidator.Booking(
                    "DOCK-001",
                    day.plusHours(1),
                    day.plusHours(3)
                )
            );

        check("Same dock overlap",
            IntegratedSchedulingValidator.canScheduleWithBookings(
                5000, 10000, "STANDARD", "STANDARD",
                yard, shift, start, end, req,
                Set.of("FORKLIFT"), "DOCK-001", bookings
            ), false);

        check("Different dock allowed",
            IntegratedSchedulingValidator.canScheduleWithBookings(
                5000, 10000, "STANDARD", "STANDARD",
                yard, shift, start, end, req,
                Set.of("FORKLIFT"), "DOCK-002", bookings
            ), true);

        check("Back-to-back allowed",
            IntegratedSchedulingValidator.canScheduleWithBookings(
                5000, 10000, "STANDARD", "STANDARD",
                yard, shift, day.plusHours(3),
                day.plusHours(4), req,
                Set.of("FORKLIFT"), "DOCK-001", bookings
            ), true);

        check("Overloaded vehicle rejected",
            IntegratedSchedulingValidator.canScheduleWithBookings(
                12000, 10000, "STANDARD", "STANDARD",
                yard, shift, start, end, req,
                Set.of("FORKLIFT"), "DOCK-002", bookings
            ), false);

        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);

        if (failed > 0) System.exit(1);
    }
}
