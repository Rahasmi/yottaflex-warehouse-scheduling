
import java.time.LocalDateTime;

public class DockValidator {

    public static boolean overlaps(
            String start1, String end1,
            String start2, String end2) {

        LocalDateTime s1 = LocalDateTime.parse(start1);
        LocalDateTime e1 = LocalDateTime.parse(end1);
        LocalDateTime s2 = LocalDateTime.parse(start2);
        LocalDateTime e2 = LocalDateTime.parse(end2);

        return s1.isBefore(e2) && s2.isBefore(e1);
    }

    public static void main(String[] args) {

        boolean truckA = overlaps(
            "2026-11-08T10:00",
            "2026-11-08T11:00",
            "2026-11-08T10:30",
            "2026-11-08T11:30"
        );

        boolean truckB = overlaps(
            "2026-11-08T10:00",
            "2026-11-08T11:00",
            "2026-11-08T11:00",
            "2026-11-08T12:00"
        );

        System.out.println("Truck A conflict: " + truckA);
        System.out.println("Truck B conflict: " + truckB);
    }
}
