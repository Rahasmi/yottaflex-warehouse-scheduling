
public class AppointmentScheduler {

    static boolean canSchedule(
            boolean compatible,
            boolean dockAvailable,
            int availableWorkers,
            int requiredWorkers) {

        return compatible
            && dockAvailable
            && availableWorkers >= requiredWorkers;
    }

    public static void main(String[] args) {

        System.out.println("Appointment 1: "
            + canSchedule(true, true, 5, 3));

        System.out.println("Appointment 2: "
            + canSchedule(true, false, 5, 3));

        System.out.println("Appointment 3: "
            + canSchedule(true, true, 2, 4));

        System.out.println("Appointment 4: "
            + canSchedule(false, true, 5, 3));
    }
}
