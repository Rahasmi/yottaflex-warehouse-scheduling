
import java.time.LocalDateTime;
import java.util.Set;

public class IntegratedSchedulingValidatorTest {
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

    static boolean validate(
            int weight, int capacity,
            String load, String dock,
            YardSlot yard, LaborShift shift,
            LocalDateTime start, LocalDateTime end,
            HandlingRequirement req,
            Set<String> equipment) {

        return IntegratedSchedulingValidator.canSchedule(
            weight, capacity, load, dock, yard, shift,
            start, end, req, equipment
        );
    }

    public static void main(String[] args) {
        LocalDateTime day =
            LocalDateTime.parse("2026-11-08T08:00:00");

        YardSlot yard =
            new YardSlot("Y-001", "FAC-001", 2, true);
        YardSlot unavailable =
            new YardSlot("Y-002", "FAC-001", 2, false);

        LaborShift shift = new LaborShift(
            "SHIFT-001", "FAC-001",
            day, day.plusHours(9), 5
        );

        HandlingRequirement req =
            new HandlingRequirement(
                "REQ-001", "STANDARD", 3, "FORKLIFT"
            );

        LocalDateTime start = day.plusHours(1);
        LocalDateTime end = day.plusHours(3);
        Set<String> equipment = Set.of("FORKLIFT");

        check("Valid scheduling",
            validate(5000, 10000, "STANDARD",
                "STANDARD", yard, shift,
                start, end, req, equipment), true);

        check("Vehicle overloaded",
            validate(12000, 10000, "STANDARD",
                "STANDARD", yard, shift,
                start, end, req, equipment), false);

        check("Wrong dock type",
            validate(5000, 10000, "STANDARD",
                "HAZARDOUS", yard, shift,
                start, end, req, equipment), false);

        check("Yard unavailable",
            validate(5000, 10000, "STANDARD",
                "STANDARD", unavailable, shift,
                start, end, req, equipment), false);

        check("Insufficient workers",
            validate(5000, 10000, "STANDARD",
                "STANDARD", yard, shift,
                start, end,
                new HandlingRequirement(
                    "REQ-002", "STANDARD", 8, "FORKLIFT"
                ), equipment), false);

        check("Equipment unavailable",
            validate(5000, 10000, "STANDARD",
                "STANDARD", yard, shift,
                start, end, req, Set.of("CRANE")), false);

        check("Outside labor shift",
            validate(5000, 10000, "STANDARD",
                "STANDARD", yard, shift,
                day.minusHours(1), end,
                req, equipment), false);

        check("Load type mismatch",
            validate(5000, 10000, "STANDARD",
                "STANDARD", yard, shift,
                start, end,
                new HandlingRequirement(
                    "REQ-003", "HAZARDOUS", 3, "FORKLIFT"
                ), equipment), false);

        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);

        if (failed > 0) System.exit(1);
    }
}
