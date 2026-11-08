
public class AllocationValidator {

    public static boolean canAllocate(
            Shipment shipment,
            YardSlot yardSlot,
            HandlingRequirement requirement,
            String dockCapability,
            int vehicleCapacityKg,
            int availableWorkers) {

        if (shipment == null || yardSlot == null
                || requirement == null
                || dockCapability == null) {
            return false;
        }

        boolean weightValid =
            shipment.weightKg() <= vehicleCapacityKg;

        boolean dockCompatible =
            SchedulingRules.compatible(
                shipment.loadType(), dockCapability);

        boolean workersAvailable =
            availableWorkers >= requirement.requiredWorkers();

        boolean yardAvailable = yardSlot.available();

        boolean handlingCompatible =
            shipment.loadType().equals(
                requirement.loadType());

        return weightValid
            && dockCompatible
            && workersAvailable
            && yardAvailable
            && handlingCompatible;
    }
}
