
public class SchedulingExceptionTest {
    public static void main(String[] args) {
        try {
            throw new SchedulingException(
                "DOCK_OVERLAP",
                "Dock is already occupied"
            );
        } catch (SchedulingException e) {
            System.out.println("Error code: " + e.getReasonCode());
            System.out.println("Message: " + e.getMessage());
        }
    }
}
