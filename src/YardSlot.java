
public record YardSlot(
    String slotId,
    String facilityId,
    int capacity,
    boolean available
) {
    public YardSlot {
        if (slotId == null || slotId.isBlank()) {
            throw new IllegalArgumentException(
                "Slot ID is required"
            );
        }
        if (facilityId == null || facilityId.isBlank()) {
            throw new IllegalArgumentException(
                "Facility ID is required"
            );
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Capacity must be positive"
            );
        }
    }
}
