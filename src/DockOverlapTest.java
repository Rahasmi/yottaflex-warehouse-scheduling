
import java.time.LocalTime;

public class DockOverlapTest {

    static boolean overlaps(
            LocalTime start1, LocalTime end1,
            LocalTime start2, LocalTime end2) {

        return start1.isBefore(end2)
            && start2.isBefore(end1);
    }

    public static void main(String[] args) {

        LocalTime start1 = LocalTime.of(10, 0);
        LocalTime end1 = LocalTime.of(11, 0);

        System.out.println("Truck B overlapping: "
            + overlaps(start1, end1,
                LocalTime.of(10, 30),
                LocalTime.of(11, 30)));

        System.out.println("Truck C overlapping: "
            + overlaps(start1, end1,
                LocalTime.of(11, 0),
                LocalTime.of(12, 0)));
    }
}
