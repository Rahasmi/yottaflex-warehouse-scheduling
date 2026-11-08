
import java.time.LocalDateTime;
import java.util.List;

public class SharedEquipmentCapacityTest {

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

        LocalDateTime ten =
            LocalDateTime.of(2026, 10, 1, 10, 0);

        LocalDateTime tenThirty = ten.plusMinutes(30);
        LocalDateTime eleven = ten.plusHours(1);
        LocalDateTime elevenThirty = ten.plusMinutes(90);

        var existing = List.of(
            new SharedEquipmentCapacityValidator.Booking(
                "FAC-001", "FORKLIFT", ten, eleven, 4
            )
        );

        check(
            "Reject fifth forklift",
            SharedEquipmentCapacityValidator.canAllocate(
                "FAC-001", "FORKLIFT",
                tenThirty, elevenThirty,
                1, 4, existing
            ),
            false
        );

        check(
            "Accept four forklifts",
            SharedEquipmentCapacityValidator.canAllocate(
                "FAC-001", "FORKLIFT",
                tenThirty, elevenThirty,
                1, 4,
                List.of(
                    new SharedEquipmentCapacityValidator.Booking(
                        "FAC-001", "FORKLIFT",
                        ten, eleven, 3
                    )
                )
            ),
            true
        );

        check(
            "Back-to-back equipment reuse",
            SharedEquipmentCapacityValidator.canAllocate(
                "FAC-001", "FORKLIFT",
                eleven, eleven.plusHours(1),
                1, 4, existing
            ),
            true
        );

        check(
            "Different facility",
            SharedEquipmentCapacityValidator.canAllocate(
                "FAC-002", "FORKLIFT",
                tenThirty, elevenThirty,
                1, 4, existing
            ),
            true
        );

        check(
            "Different equipment type",
            SharedEquipmentCapacityValidator.canAllocate(
                "FAC-001", "REFRIGERATION",
                tenThirty, elevenThirty,
                1, 4, existing
            ),
            true
        );

        check(
            "Reject invalid appointment time",
            SharedEquipmentCapacityValidator.canAllocate(
                "FAC-001", "FORKLIFT",
                eleven, ten,
                1, 4, existing
            ),
            false
        );

        System.out.println(
            "\nTests passed: " + passed
        );
        System.out.println(
            "Tests failed: " + failed
        );

        if (failed > 0) {
            System.exit(1);
        }
    }
}
