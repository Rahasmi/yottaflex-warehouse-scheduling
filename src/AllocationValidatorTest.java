
public class AllocationValidatorTest {

    static int passed = 0;
    static int failed = 0;

    static void check(
            String name,
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

        Shipment shipment = new Shipment(
            "SHIP-001", "VEH-001",
            "REFRIGERATED", 12000
        );

        YardSlot availableYard = new YardSlot(
            "YARD-001", "FAC-001", 2, true
        );

        YardSlot unavailableYard = new YardSlot(
            "YARD-002", "FAC-001", 2, false
        );

        HandlingRequirement requirement =
            new HandlingRequirement(
                "REQ-001", "REFRIGERATED",
                3, "FORKLIFT"
            );

        check("Valid allocation",
            AllocationValidator.canAllocate(
                shipment, availableYard,
                requirement, "REFRIGERATED",
                15000, 5), true);

        check("Vehicle overloaded",
            AllocationValidator.canAllocate(
                shipment, availableYard,
                requirement, "REFRIGERATED",
                10000, 5), false);

        check("Wrong dock capability",
            AllocationValidator.canAllocate(
                shipment, availableYard,
                requirement, "STANDARD",
                15000, 5), false);

        check("Insufficient workers",
            AllocationValidator.canAllocate(
                shipment, availableYard,
                requirement, "REFRIGERATED",
                15000, 2), false);

        check("Yard unavailable",
            AllocationValidator.canAllocate(
                shipment, unavailableYard,
                requirement, "REFRIGERATED",
                15000, 5), false);

        System.out.println(
            "TOTAL PASSED: " + passed);
        System.out.println(
            "TOTAL FAILED: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }
}
