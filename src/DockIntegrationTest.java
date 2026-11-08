
import java.time.LocalDateTime;
import java.util.Set;

public class DockIntegrationTest {

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

    static boolean schedule(
            String requestedDock,
            String existingDock,
            String requestedStart,
            String requestedEnd,
            String existingStart,
            String existingEnd,
            int shipmentWeight) {

        LocalDateTime start =
            LocalDateTime.parse(requestedStart);
        LocalDateTime end =
            LocalDateTime.parse(requestedEnd);

        YardSlot yard = new YardSlot(
            "YARD-01", "FAC-01", 5, true
        );

        LaborShift shift = new LaborShift(
            "SHIFT-01", "FAC-01",
            LocalDateTime.parse("2026-11-08T08:00"),
            LocalDateTime.parse("2026-11-08T18:00"),
            10
        );

        HandlingRequirement requirement =
            new HandlingRequirement(
                "REQ-01", "STANDARD", 2, "FORKLIFT"
            );

        return IntegratedSchedulingValidator.canSchedule(
            shipmentWeight,
            12000,
            "STANDARD",
            "STANDARD",
            yard,
            shift,
            start,
            end,
            requirement,
            Set.of("FORKLIFT"),
            requestedDock,
            existingDock,
            LocalDateTime.parse(existingStart),
            LocalDateTime.parse(existingEnd)
        );
    }

    public static void main(String[] args) {

        String day = "2026-11-08T";

        check("Same dock overlap",
            schedule("DOCK-01", "DOCK-01",
                day+"11:00", day+"13:00",
                day+"10:00", day+"12:00", 5000),
            false);

        check("Different docks",
            schedule("DOCK-02", "DOCK-01",
                day+"11:00", day+"13:00",
                day+"10:00", day+"12:00", 5000),
            true);

        check("Back-to-back booking",
            schedule("DOCK-01", "DOCK-01",
                day+"12:00", day+"14:00",
                day+"10:00", day+"12:00", 5000),
            true);

        check("Vehicle overloaded",
            schedule("DOCK-02", "DOCK-01",
                day+"11:00", day+"13:00",
                day+"10:00", day+"12:00", 15000),
            false);

        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
