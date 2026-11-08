
public class SchedulingRules {

    static boolean compatible(
            String loadType,
            String dockType) {

        if (loadType.equals("REFRIGERATED")) {
            return dockType.equals("REFRIGERATED");
        }

        if (loadType.equals("HAZARDOUS")) {
            return dockType.equals("HAZARDOUS");
        }

        return dockType.equals("STANDARD");
    }

    public static void main(String[] args) {
        System.out.println("Frozen food at refrigerated dock: "
            + compatible("REFRIGERATED", "REFRIGERATED"));

        System.out.println("Frozen food at standard dock: "
            + compatible("REFRIGERATED", "STANDARD"));

        System.out.println("Hazardous load at standard dock: "
            + compatible("HAZARDOUS", "STANDARD"));
    }
}
