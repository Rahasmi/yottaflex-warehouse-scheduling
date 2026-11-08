
import java.time.LocalDateTime;
import java.util.Set;

public class GeneratorIntegrationSmokeTest {
    public static void main(String[] args) {
        LocalDateTime start =
            LocalDateTime.of(2026, 10, 5, 9, 0);
        LocalDateTime end = start.plusHours(1);

        YardSlot yard = new YardSlot(
            "YARD-001", "FAC-001", 10, true
        );

        LaborShift shift = new LaborShift(
            "SHIFT-001", "FAC-001",
            start.minusHours(1),
            end.plusHours(1),
            4
        );

        HandlingRequirement validRequirement =
            new HandlingRequirement(
                "REQ-001", "REFRIGERATED",
                3, "REFRIGERATION"
            );

        boolean valid =
            IntegratedSchedulingValidator.canSchedule(
                5000, 8000,
                "REFRIGERATED", "REFRIGERATED",
                yard, shift, start, end,
                validRequirement,
                Set.of("REFRIGERATION")
            );

        HandlingRequirement invalidRequirement =
            new HandlingRequirement(
                "REQ-002", "REFRIGERATED",
                6, "REFRIGERATION"
            );

        boolean invalid =
            IntegratedSchedulingValidator.canSchedule(
                5000, 8000,
                "REFRIGERATED", "REFRIGERATED",
                yard, shift, start, end,
                invalidRequirement,
                Set.of("REFRIGERATION")
            );

        System.out.println(
            "Valid appointment accepted: " + valid
        );
        System.out.println(
            "Insufficient workers rejected: " + !invalid
        );

        if (!valid || invalid) {
            throw new AssertionError(
                "Integrated scheduling test failed"
            );
        }

        System.out.println("FINAL RESULT: PASS");
    }
}
