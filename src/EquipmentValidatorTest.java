
import java.util.Set;

public class EquipmentValidatorTest {

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

    public static void main(String[] args) {

        HandlingRequirement requirement =
            new HandlingRequirement(
                "REQ-001", "STANDARD", 3, "FORKLIFT"
            );

        Set<String> equipment =
            Set.of("FORKLIFT", "CRANE", "PALLET_JACK");

        check("Equipment available",
            EquipmentValidator.isAvailable(
                requirement, equipment), true);

        check("Equipment unavailable",
            EquipmentValidator.isAvailable(
                requirement, Set.of("CRANE")), false);

        check("Case insensitive match",
            EquipmentValidator.isAvailable(
                requirement, Set.of("forklift")), true);

        check("Whitespace handling",
            EquipmentValidator.isAvailable(
                requirement, Set.of(" FORKLIFT ")), true);

        check("Null equipment collection",
            EquipmentValidator.isAvailable(
                requirement, null), false);

        check("Null requirement",
            EquipmentValidator.isAvailable(
                null, equipment), false);

        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);

        if (failed > 0) System.exit(1);
    }
}
