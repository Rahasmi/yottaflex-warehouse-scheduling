
public class TransportEntityTest {

    static int passed = 0;
    static int failed = 0;

    static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    static boolean rejects(Runnable action) {
        try {
            action.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    public static void main(String[] args) {

        TransportData.Carrier carrier =
            new TransportData.Carrier(
                "CAR-001", "ABC Logistics");

        TransportData.Vehicle vehicle =
            new TransportData.Vehicle(
                "VEH-001",
                carrier.carrierId(),
                "REFRIGERATED",
                12000
            );

        check("Valid carrier",
            carrier.name().equals("ABC Logistics"));

        check("Valid vehicle",
            vehicle.capacityKg() == 12000);

        check("Vehicle references carrier ID",
            vehicle.carrierId().equals(carrier.carrierId()));

        check("Reject missing carrier ID",
            rejects(() -> new TransportData.Carrier(
                "", "ABC Logistics")));

        check("Reject missing carrier name",
            rejects(() -> new TransportData.Carrier(
                "CAR-001", "")));

        check("Reject missing vehicle ID",
            rejects(() -> new TransportData.Vehicle(
                "", "CAR-001", "STANDARD", 10000)));

        check("Reject missing vehicle carrier ID",
            rejects(() -> new TransportData.Vehicle(
                "VEH-001", "", "STANDARD", 10000)));

        check("Reject missing vehicle type",
            rejects(() -> new TransportData.Vehicle(
                "VEH-001", "CAR-001", "", 10000)));

        check("Reject zero capacity",
            rejects(() -> new TransportData.Vehicle(
                "VEH-001", "CAR-001", "STANDARD", 0)));

        check("Reject negative capacity",
            rejects(() -> new TransportData.Vehicle(
                "VEH-001", "CAR-001", "STANDARD", -500)));

        Shipment shipment = new Shipment(
            "SHP-001",
            vehicle.vehicleId(),
            "REFRIGERATED",
            5000
        );

        check("Shipment references vehicle",
            shipment.vehicleId().equals(vehicle.vehicleId()));

        check("Shipment within vehicle capacity",
            shipment.weightKg() <= vehicle.capacityKg());

        check("Reject shipment over capacity",
            !(
                new Shipment(
                    "SHP-002",
                    vehicle.vehicleId(),
                    "REFRIGERATED",
                    15000
                ).weightKg() <= vehicle.capacityKg()
            ));

        System.out.println(
            "\nTests passed: " + passed
        );

        System.out.println(
            "Tests failed: " + failed
        );

        System.out.println(
            "FINAL RESULT: " +
            (failed == 0 ? "PASS" : "FAIL")
        );

        if (failed > 0) {
            System.exit(1);
        }
    }
}
