
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TransportDatasetGenerator {

    static void csv(PrintWriter out, Object... values) {
        for (int i = 0; i < values.length; i++) {
            if (i > 0) out.print(",");
            String value = String.valueOf(values[i]);
            out.print("\"" + value.replace("\"", "\"\"") + "\"");
        }
        out.println();
    }

    public static void main(String[] args) throws Exception {

        Path output = Paths.get("output");
        Path appointments = output.resolve("appointments.csv");

        if (!Files.exists(appointments)) {
            throw new IllegalStateException(
                "Generate appointments.csv first");
        }

        List<String> lines = Files.readAllLines(appointments);

        if (lines.isEmpty()) {
            throw new IllegalStateException(
                "Appointments file is empty");
        }

        List<String[]> records = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            // Existing appointment fields contain no embedded
            // commas, so this parser supports the current schema.
            String[] fields = line.replace("\"", "").split(",", -1);

            if (fields.length != 7) {
                throw new IllegalStateException(
                    "Invalid appointment row: " + (i + 1));
            }

            records.add(fields);
        }

        // Prevalidate all transport assignments before writing.
        List<TransportData.Carrier> carriers = new ArrayList<>();
        List<TransportData.Vehicle> vehicles = new ArrayList<>();
        List<Shipment> shipments = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            carriers.add(new TransportData.Carrier(
                String.format("CAR-%03d", i + 1),
                "Logistics Carrier " + (i + 1)
            ));
        }

        for (int i = 0; i < records.size(); i++) {

            String loadType = records.get(i)[3];

            TransportData.Carrier carrier =
                carriers.get(i % carriers.size());

            TransportData.Vehicle vehicle =
                new TransportData.Vehicle(
                    String.format("VEH-%06d", i + 1),
                    carrier.carrierId(),
                    loadType,
                    10000
                );

            Shipment shipment = new Shipment(
                String.format("SHP-%06d", i + 1),
                vehicle.vehicleId(),
                loadType,
                5000
            );

            if (!TransportAssignmentValidator.canAssign(
                    carrier, vehicle, shipment)) {
                throw new IllegalStateException(
                    "Invalid transport assignment at row " + (i + 2));
            }

            vehicles.add(vehicle);
            shipments.add(shipment);
        }

        Path[] targets = {
            output.resolve("carriers.csv"),
            output.resolve("vehicles.csv"),
            output.resolve("shipments.csv"),
            output.resolve("appointment_shipments.csv")
        };

        for (Path target : targets) {
            if (Files.exists(target)) {
                throw new IllegalStateException(
                    "Refusing to overwrite: " + target);
            }
        }

        try (
            PrintWriter c = new PrintWriter(
                Files.newBufferedWriter(targets[0]));
            PrintWriter v = new PrintWriter(
                Files.newBufferedWriter(targets[1]));
            PrintWriter s = new PrintWriter(
                Files.newBufferedWriter(targets[2]));
            PrintWriter a = new PrintWriter(
                Files.newBufferedWriter(targets[3]))
        ) {
            csv(c, "carrier_id", "name");

            csv(v, "vehicle_id", "carrier_id",
                "vehicle_type", "capacity_kg");

            csv(s, "shipment_id", "vehicle_id",
                "load_type", "weight_kg");

            csv(a, "appointment_id", "shipment_id");

            for (TransportData.Carrier carrier : carriers) {
                csv(c, carrier.carrierId(), carrier.name());
            }

            for (int i = 0; i < records.size(); i++) {

                TransportData.Vehicle vehicle = vehicles.get(i);
                Shipment shipment = shipments.get(i);

                csv(v,
                    vehicle.vehicleId(),
                    vehicle.carrierId(),
                    vehicle.vehicleType(),
                    vehicle.capacityKg());

                csv(s,
                    shipment.shipmentId(),
                    shipment.vehicleId(),
                    shipment.loadType(),
                    shipment.weightKg());

                csv(a,
                    records.get(i)[0],
                    shipment.shipmentId());
            }
        }

        System.out.println("Carriers: " + carriers.size());
        System.out.println("Vehicles: " + vehicles.size());
        System.out.println("Shipments: " + shipments.size());
        System.out.println(
            "Appointment-shipment links: " + records.size());

        System.out.println("FINAL RESULT: PASS");
    }
}
