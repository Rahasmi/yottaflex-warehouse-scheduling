
public record Shipment(
    String shipmentId,
    String vehicleId,
    String loadType,
    int weightKg
) {
    public Shipment {
        if (shipmentId == null || shipmentId.isBlank()) {
            throw new IllegalArgumentException(
                "Shipment ID is required"
            );
        }
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException(
                "Vehicle ID is required"
            );
        }
        if (loadType == null || loadType.isBlank()) {
            throw new IllegalArgumentException(
                "Load type is required"
            );
        }
        if (weightKg <= 0) {
            throw new IllegalArgumentException(
                "Weight must be positive"
            );
        }
    }
}
