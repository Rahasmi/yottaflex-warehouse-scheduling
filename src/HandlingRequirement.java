
public record HandlingRequirement(
    String requirementId,
    String loadType,
    int requiredWorkers,
    String requiredEquipment
) {
    public HandlingRequirement {
        if (requirementId == null
                || requirementId.isBlank()) {
            throw new IllegalArgumentException(
                "Requirement ID is required"
            );
        }

        if (loadType == null || loadType.isBlank()) {
            throw new IllegalArgumentException(
                "Load type is required"
            );
        }

        if (requiredWorkers <= 0) {
            throw new IllegalArgumentException(
                "Required workers must be positive"
            );
        }

        if (requiredEquipment == null
                || requiredEquipment.isBlank()) {
            throw new IllegalArgumentException(
                "Equipment is required"
            );
        }
    }
}
