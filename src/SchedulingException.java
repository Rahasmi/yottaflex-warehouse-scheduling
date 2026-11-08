
public class SchedulingException extends Exception {

    private final String reasonCode;

    public SchedulingException(
        String reasonCode,
        String message
    ) {
        super(message);
        this.reasonCode = reasonCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }
}
