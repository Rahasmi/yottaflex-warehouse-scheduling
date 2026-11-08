
import java.time.LocalDateTime;
import java.util.Set;

public class IntegratedSchedulingValidator {

    public static boolean canSchedule(
            int shipmentWeightKg,
            int vehicleCapacityKg,
            String loadType,
            String dockType,
            YardSlot yardSlot,
            LaborShift laborShift,
            LocalDateTime appointmentStart,
            LocalDateTime appointmentEnd,
            HandlingRequirement requirement,
            Set<String> availableEquipment) {

        if (shipmentWeightKg <= 0 ||
            vehicleCapacityKg <= 0 ||
            loadType == null ||
            dockType == null ||
            yardSlot == null ||
            !yardSlot.available() ||
            laborShift == null ||
            requirement == null ||
            !loadType.equalsIgnoreCase(
                requirement.loadType())) {
            return false;
        }

        boolean capacityValid =
            shipmentWeightKg <= vehicleCapacityKg;

        boolean dockCompatible =
            SchedulingRules.compatible(
                loadType, dockType);

        boolean laborValid =
            LaborShiftValidator.canHandle(
                laborShift,
                appointmentStart,
                appointmentEnd,
                requirement.requiredWorkers());

        boolean equipmentValid =
            EquipmentValidator.isAvailable(
                requirement, availableEquipment);

        return capacityValid
            && dockCompatible
            && laborValid
            && equipmentValid;
    }

    public static boolean canSchedule(
            int shipmentWeightKg,
            int vehicleCapacityKg,
            String loadType,
            String dockType,
            YardSlot yardSlot,
            LaborShift laborShift,
            LocalDateTime appointmentStart,
            LocalDateTime appointmentEnd,
            HandlingRequirement requirement,
            Set<String> availableEquipment,
            String requestedDock,
            String existingDock,
            LocalDateTime existingStart,
            LocalDateTime existingEnd) {

        boolean basicValid = canSchedule(
            shipmentWeightKg,
            vehicleCapacityKg,
            loadType,
            dockType,
            yardSlot,
            laborShift,
            appointmentStart,
            appointmentEnd,
            requirement,
            availableEquipment
        );

        if (!basicValid) {
            return false;
        }

        return !DockConflictValidator.hasConflict(
            requestedDock,
            appointmentStart,
            appointmentEnd,
            existingDock,
            existingStart,
            existingEnd
        );
    }


    public static boolean canScheduleWithBookings(
            int shipmentWeightKg,
            int vehicleCapacityKg,
            String loadType,
            String dockType,
            YardSlot yardSlot,
            LaborShift laborShift,
            java.time.LocalDateTime appointmentStart,
            java.time.LocalDateTime appointmentEnd,
            HandlingRequirement requirement,
            java.util.Set<String> availableEquipment,
            String requestedDockId,
            java.util.List<MultipleDockConflictValidator.Booking>
                existingBookings) {

        boolean valid = canSchedule(
            shipmentWeightKg,
            vehicleCapacityKg,
            loadType,
            dockType,
            yardSlot,
            laborShift,
            appointmentStart,
            appointmentEnd,
            requirement,
            availableEquipment
        );

        if (!valid || requestedDockId == null
                || requestedDockId.isBlank()
                || existingBookings == null) {
            return false;
        }

        boolean conflict =
            MultipleDockConflictValidator.hasAnyConflict(
                requestedDockId,
                appointmentStart,
                appointmentEnd,
                existingBookings
            );

        return !conflict;
    }

}
