
import java.time.LocalDateTime;
import java.util.List;

public class SharedLaborCapacityTest {

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
        LocalDateTime elevenThirty =
            ten.plusMinutes(90);

        var existing = List.of(
            new SharedLaborCapacityValidator.Booking(
                "FAC-001", ten, eleven, 3
            )
        );

        check(
            "Overlapping bookings exceed 5 workers",
            SharedLaborCapacityValidator.canAllocate(
                "FAC-001", tenThirty, elevenThirty,
                3, 5, existing
            ),
            false
        );

        check(
            "Overlapping bookings fit 6 workers",
            SharedLaborCapacityValidator.canAllocate(
                "FAC-001", tenThirty, elevenThirty,
                3, 6, existing
            ),
            true
        );

        check(
            "Back-to-back appointments",
            SharedLaborCapacityValidator.canAllocate(
                "FAC-001", eleven, eleven.plusHours(1),
                3, 5, existing
            ),
            true
        );

        check(
            "Different facilities",
            SharedLaborCapacityValidator.canAllocate(
                "FAC-002", tenThirty, elevenThirty,
                3, 5, existing
            ),
            true
        );

        check(
            "Required workers exceed capacity",
            SharedLaborCapacityValidator.canAllocate(
                "FAC-001", ten, eleven,
                6, 5, List.of()
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
