
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public class AppointmentConflictScanner {
    record Appointment(
        String id, String dock,
        LocalDateTime start, LocalDateTime end
    ) {}

    public static void main(String[] args) throws Exception {
        List<String> lines =
            Files.readAllLines(Path.of("appointments.csv"));

        String[] headers = lines.get(0).split(",", -1);
        Map<String, Integer> cols = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            cols.put(headers[i].replace("\uFEFF", "").replace("\"", "").trim(), i);
        }

        Map<String, List<Appointment>> byDock =
            new HashMap<>();

        int scheduled = 0;

        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) continue;

            String[] v = lines.get(i).split(",", -1);

            if (!v[cols.get("status")].replace("\"", "").trim()
                    .equals("SCHEDULED")) continue;

            scheduled++;

            Appointment a = new Appointment(
                v[cols.get("appointment_id")].trim(),
                v[cols.get("dock_id")].trim(),
                LocalDateTime.parse(
                    v[cols.get("scheduled_start")].replace("\"", "").trim()),
                LocalDateTime.parse(
                    v[cols.get("scheduled_end")].replace("\"", "").trim())
            );

            byDock.computeIfAbsent(
                a.dock(), k -> new ArrayList<>()
            ).add(a);
        }

        int conflicts = 0;

        for (var entry : byDock.entrySet()) {
            List<Appointment> items = entry.getValue();

            items.sort(Comparator.comparing(
                Appointment::start
            ));

            for (int i = 0; i < items.size(); i++) {
                for (int j = i + 1; j < items.size(); j++) {
                    Appointment a = items.get(i);
                    Appointment b = items.get(j);

                    if (!b.start().isBefore(a.end()))
                        break;

                    if (a.start().isBefore(b.end())) {
                        conflicts++;
                        System.out.println(
                            "DOCK_OVERLAP: " + entry.getKey()
                            + " | " + a.id()
                            + " | " + b.id()
                        );
                    }
                }
            }
        }

        System.out.println("Scheduled appointments: "
            + scheduled);
        System.out.println("Total conflicts: " + conflicts);
    }
}
