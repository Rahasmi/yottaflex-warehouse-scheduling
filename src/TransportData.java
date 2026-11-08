
import java.util.*;

public class TransportData {
    record Carrier(String carrierId, String name) {
        Carrier {
            if (carrierId == null || carrierId.isBlank()) {
                throw new IllegalArgumentException(
                    "Carrier ID is required");
            }
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(
                    "Carrier name is required");
            }
        }
    }

    record Vehicle(String vehicleId, String carrierId,
                   String vehicleType, int capacityKg) {
        Vehicle {
            if (vehicleId == null || vehicleId.isBlank()) {
                throw new IllegalArgumentException(
                    "Vehicle ID is required");
            }
            if (carrierId == null || carrierId.isBlank()) {
                throw new IllegalArgumentException(
                    "Carrier ID is required");
            }
            if (vehicleType == null || vehicleType.isBlank()) {
                throw new IllegalArgumentException(
                    "Vehicle type is required");
            }
            if (capacityKg <= 0) {
                throw new IllegalArgumentException(
                    "Vehicle capacity must be positive");
            }
        }
    }

    public static void main(String[] args) {
        Carrier carrier = new Carrier("CAR-001", "ABC Logistics");
        Vehicle vehicle = new Vehicle(
            "VEH-001", carrier.carrierId(), "REFRIGERATED", 12000
        );

        System.out.println("Carrier: " + carrier.name());
        System.out.println("Vehicle: " + vehicle.vehicleId());
        System.out.println("Type: " + vehicle.vehicleType());
        System.out.println("Capacity: " + vehicle.capacityKg() + " kg");
    }
}
