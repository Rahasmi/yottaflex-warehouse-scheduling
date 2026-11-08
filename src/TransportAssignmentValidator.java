
public class TransportAssignmentValidator {

    public static boolean canAssign(
            TransportData.Carrier carrier,
            TransportData.Vehicle vehicle,
            Shipment shipment) {

        if (carrier == null || vehicle == null
                || shipment == null) {
            return false;
        }

        boolean carrierMatches =
            vehicle.carrierId().equals(carrier.carrierId());

        boolean vehicleMatches =
            shipment.vehicleId().equals(vehicle.vehicleId());

        boolean capacityValid =
            shipment.weightKg() <= vehicle.capacityKg();

        boolean loadCompatible =
            SchedulingRules.compatible(
                shipment.loadType(),
                vehicle.vehicleType()
            );

        return carrierMatches
            && vehicleMatches
            && capacityValid
            && loadCompatible;
    }
}
