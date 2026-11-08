
public class TransportAssignmentValidatorTest {

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

    public static void main(String[] args) {

        TransportData.Carrier carrier =
            new TransportData.Carrier(
                "CAR-001", "ABC Logistics");

        TransportData.Vehicle vehicle =
            new TransportData.Vehicle(
                "VEH-001", "CAR-001",
                "REFRIGERATED", 12000);

        Shipment valid = new Shipment(
            "SHP-001", "VEH-001",
            "REFRIGERATED", 5000);

        check("Valid transport assignment",
            TransportAssignmentValidator.canAssign(
                carrier, vehicle, valid));

        Shipment overweight = new Shipment(
            "SHP-002", "VEH-001",
            "REFRIGERATED", 15000);

        check("Reject overweight shipment",
            !TransportAssignmentValidator.canAssign(
                carrier, vehicle, overweight));

        Shipment wrongVehicle = new Shipment(
            "SHP-003", "VEH-999",
            "REFRIGERATED", 5000);

        check("Reject wrong vehicle reference",
            !TransportAssignmentValidator.canAssign(
                carrier, vehicle, wrongVehicle));

        TransportData.Carrier wrongCarrier =
            new TransportData.Carrier(
                "CAR-999", "Other Logistics");

        check("Reject wrong carrier",
            !TransportAssignmentValidator.canAssign(
                wrongCarrier, vehicle, valid));

        Shipment wrongType = new Shipment(
            "SHP-004", "VEH-001",
            "HAZARDOUS", 5000);

        check("Reject incompatible load type",
            !TransportAssignmentValidator.canAssign(
                carrier, vehicle, wrongType));

        check("Reject null carrier",
            !TransportAssignmentValidator.canAssign(
                null, vehicle, valid));

        check("Reject null vehicle",
            !TransportAssignmentValidator.canAssign(
                carrier, null, valid));

        check("Reject null shipment",
            !TransportAssignmentValidator.canAssign(
                carrier, vehicle, null));

        System.out.println(
            "\nTests passed: " + passed);

        System.out.println(
            "Tests failed: " + failed);

        System.out.println(
            "FINAL RESULT: " +
            (failed == 0 ? "PASS" : "FAIL"));

        if (failed > 0) {
            System.exit(1);
        }
    }
}
