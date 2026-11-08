
import java.util.Set;

public class EquipmentValidator {

    public static boolean isAvailable(
            HandlingRequirement requirement,
            Set<String> availableEquipment) {

        if (requirement == null ||
            availableEquipment == null) {
            return false;
        }

        String required =
            requirement.requiredEquipment();

        if (required == null || required.isBlank()) {
            return false;
        }

        return availableEquipment.stream()
            .filter(e -> e != null)
            .anyMatch(e ->
                e.trim().equalsIgnoreCase(required.trim()));
    }
}
