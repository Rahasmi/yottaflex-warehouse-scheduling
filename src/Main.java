import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;

public class Main {
  static final long SEED = 20261022L;
  static final LocalDate START = LocalDate.of(2026, 10, 1);
  static final String[] TYPES = {"STANDARD", "REFRIGERATED", "HAZARDOUS"};
  static void csv(PrintWriter out, Object... fields) {
    for(int i=0;i<fields.length;i++) {
      if(i>0) out.print(',');
      String s=String.valueOf(fields[i]);
      out.print('"'+s.replace("\"","\"\"")+'"');
    }
    out.println();
  }
  public static void main(String[] args) throws Exception {
    int scale = args.length>0 ? Integer.parseInt(args[0]) : 1;
    if(scale<1 || scale>20) throw new IllegalArgumentException("scale must be 1..20");
    int facilities=scale==1?2:10, docks=scale==1?20:100;
    int appointments=scale==1?200:20000, events=scale==1?1000:100000;
    Path dir=Paths.get("output"); Files.createDirectories(dir);
    Random rng=new Random(SEED);
    try(PrintWriter f=new PrintWriter(Files.newBufferedWriter(dir.resolve("facilities.csv")));
        PrintWriter d=new PrintWriter(Files.newBufferedWriter(dir.resolve("docks.csv")));
        PrintWriter a=new PrintWriter(Files.newBufferedWriter(dir.resolve("appointments.csv")));
        PrintWriter e=new PrintWriter(Files.newBufferedWriter(dir.resolve("events.csv")));
        PrintWriter g=new PrintWriter(Files.newBufferedWriter(dir.resolve("ground_truth.csv")))) {
      csv(f,"facility_id","name"); csv(d,"dock_id","facility_id","capability");
      csv(a,"appointment_id","facility_id","dock_id","load_type","scheduled_start","scheduled_end","status");
      csv(e,"event_id","appointment_id","event_type","event_time");
      csv(g,"appointment_id","issue_type");
      for(int i=1;i<=facilities;i++) csv(f,String.format("FAC-%03d",i),"Facility "+i);
      for(int i=1;i<=docks;i++) csv(d,String.format("DOCK-FAC-%03d-%03d",(i-1)/(docks/facilities)+1,(i-1)%(docks/facilities)+1),String.format("FAC-%03d",(i-1)/(docks/facilities)+1),TYPES[i%3]);
      Map<String, List<LocalDateTime[]>> occupied = new HashMap<>();
       List<SharedLaborCapacityValidator.Booking> laborBookings =
           new ArrayList<>();
       final int WORKERS_PER_FACILITY = 27;
       final int WORKERS_PER_APPOINTMENT = 3;
       final int EQUIPMENT_UNITS_PER_TYPE = 4;
       List<SharedEquipmentCapacityValidator.Booking>
           equipmentBookings = new ArrayList<>();
      String[] ids=new String[appointments]; LocalDateTime[] times=new LocalDateTime[appointments];
      for(int i=0;i<appointments;i++) {
        int fac=rng.nextInt(facilities)+1;
        int slot=rng.nextInt(docks/facilities)+1;
        int global=(fac-1)*(docks/facilities)+slot;
        String capability=TYPES[global%3];
        String id=String.format("APPT-2026-%06d",i+1); ids[i]=id;
        LocalDateTime start=START.plusDays(rng.nextInt(60)).atTime(6+rng.nextInt(12),rng.nextInt(4)*15);

         String status=i%31==0?"NO_SHOW":"SCHEDULED";
         String dockId=String.format("DOCK-FAC-%03d-%03d",fac,slot);
         String requiredEquipment = switch (capability) {
    case "REFRIGERATED" -> "REFRIGERATION";
    case "HAZARDOUS" -> "SAFETY_KIT";
    default -> "FORKLIFT";
};




         // Keep NO_SHOW appointment times within
         // illustrative 06:00-18:00 operating hours.
         if(status.equals("NO_SHOW") &&
            start.toLocalTime().isAfter(LocalTime.of(17,0))) {
           start = start.toLocalDate().atTime(17,0);
         }

         if(status.equals("SCHEDULED")) {
           List<LocalDateTime[]> booked =
               occupied.computeIfAbsent(
                   dockId, k -> new ArrayList<>());

           LocalDate lastAllowedDate =
               START.plusDays(59);

           boolean allocated = false;

           // First try forward scheduling.
           while(!allocated &&
                 !start.toLocalDate().isAfter(lastAllowedDate)) {

             LocalDateTime end = start.plusMinutes(60);

             if(start.toLocalTime().isBefore(
                 LocalTime.of(6,0))) {
               start = start.toLocalDate().atTime(6,0);
               continue;
             }

             if(end.toLocalDate().isAfter(
                 start.toLocalDate()) ||
                end.toLocalTime().isAfter(
                 LocalTime.of(18,0))) {
               start = start.toLocalDate()
                   .plusDays(1).atTime(6,0);
               continue;
             }

             boolean conflict = false;

             for(LocalDateTime[] interval : booked) {
               if(start.isBefore(interval[1]) &&
                  end.isAfter(interval[0])) {
                 start = interval[1];
                 conflict = true;
                 break;
               }
             }

             if(!conflict) {
               boolean laborAvailable =
                   SharedLaborCapacityValidator.canAllocate(
                       String.format("FAC-%03d", fac),
                       start, end,
                       WORKERS_PER_APPOINTMENT,
                       WORKERS_PER_FACILITY,
                       laborBookings
                   );

boolean equipmentAvailable =
    SharedEquipmentCapacityValidator.canAllocate(
        String.format("FAC-%03d", fac),
        requiredEquipment,
        start,
        end,
        1,
        EQUIPMENT_UNITS_PER_TYPE,
        equipmentBookings
    );

               if(laborAvailable && equipmentAvailable) {
                 booked.add(new LocalDateTime[]{start,end});
                 laborBookings.add(
                     new SharedLaborCapacityValidator.Booking(
                         String.format("FAC-%03d", fac),
                         start, end,
                         WORKERS_PER_APPOINTMENT
                     )
                 );

equipmentBookings.add(
    new SharedEquipmentCapacityValidator.Booking(
        String.format("FAC-%03d", fac),
        requiredEquipment,
        start,
        end,
        1
    )
);
                 allocated = true;
               } else {
                 start = start.plusMinutes(15);
               }
             }
           }

           // If the 60-day limit is reached,
           // search backward for a free slot.
           if(!allocated) {
             LocalDate day = lastAllowedDate;

             while(!allocated &&
                   !day.isBefore(START)) {

               LocalDateTime candidate =
                   day.atTime(6,0);

               LocalDateTime closing =
                   day.atTime(18,0);

               while(!allocated &&
                     !candidate.plusMinutes(60)
                         .isAfter(closing)) {

                 LocalDateTime candidateEnd =
                     candidate.plusMinutes(60);

                 boolean conflict = false;

                 for(LocalDateTime[] interval : booked) {
                   if(candidate.isBefore(interval[1]) &&
                      candidateEnd.isAfter(interval[0])) {
                     conflict = true;
                     break;
                   }
                 }

                 if(!conflict) {
                 boolean laborAvailable =
                     SharedLaborCapacityValidator.canAllocate(
                         String.format("FAC-%03d", fac),
                         candidate, candidateEnd,
                         WORKERS_PER_APPOINTMENT,
                         WORKERS_PER_FACILITY,
                         laborBookings
                     );

boolean equipmentAvailable =
    SharedEquipmentCapacityValidator.canAllocate(
        String.format("FAC-%03d", fac),
        requiredEquipment,
        candidate,
        candidateEnd,
        1,
        EQUIPMENT_UNITS_PER_TYPE,
        equipmentBookings
    );

                 if(laborAvailable && equipmentAvailable) {
                   start = candidate;
                   booked.add(new LocalDateTime[]{
                       start, candidateEnd
                   });
                   laborBookings.add(
                       new SharedLaborCapacityValidator.Booking(
                           String.format("FAC-%03d", fac),
                           start, candidateEnd,
                           WORKERS_PER_APPOINTMENT
                       )
                   );

equipmentBookings.add(
    new SharedEquipmentCapacityValidator.Booking(
        String.format("FAC-%03d", fac),
        requiredEquipment,
        candidate,
        candidateEnd,
        1
    )
);
                   allocated = true;
                 } else {
                   candidate = candidate.plusMinutes(15);
                 }
               } else {
                 candidate = candidate.plusMinutes(15);
               }
               }

               day = day.minusDays(1);
             }
           }

           if(!allocated) {
             throw new IllegalStateException(
                 "No available dock slot for " + id);
           }
         }

         // Validate appointment resource compatibility.
         String facilityId = String.format("FAC-%03d", fac);

         YardSlot yardSlot = new YardSlot(
             "YARD-" + facilityId,
             facilityId,
             10,
             true
         );

         LaborShift laborShift = new LaborShift(
             "SHIFT-" + facilityId,
             facilityId,
             start.minusHours(1),
             start.plusHours(2),
             WORKERS_PER_FACILITY
         );

         String equipment = switch (capability) {
             case "REFRIGERATED" -> "REFRIGERATION";
             case "HAZARDOUS" -> "SAFETY_KIT";
             default -> "FORKLIFT";
         };

         HandlingRequirement requirement =
             new HandlingRequirement(
                 "REQ-" + id,
                 capability,
                 3,
                 equipment
             );

         boolean resourceValid =
             IntegratedSchedulingValidator.canSchedule(
                 5000,
                 10000,
                 capability,
                 capability,
                 yardSlot,
                 laborShift,
                 start,
                 start.plusMinutes(60),
                 requirement,
                 Set.of(equipment)
             );

         if (!resourceValid) {
             throw new IllegalStateException(
                 "Resource validation failed for " + id
             );
         }

         times[i]=start;
        csv(a,id,String.format("FAC-%03d",fac),String.format("DOCK-FAC-%03d-%03d",fac,slot),capability,start,start.plusMinutes(60),status);
        if(i%31==0) csv(g,id,"NO_SHOW");
      }
       int eventCount=0;
       int yardTarget=scale==1 ? 13 : 1292;
       int yardEmitted=0;

       for(int i=0;i<appointments;i++) {
         boolean noShow=i%31==0;
         LocalDateTime appointmentTime=times[i];

         LocalDateTime requestedAt=
             appointmentTime.minusDays(1+(i%5));
         LocalDateTime scheduledAt=
             requestedAt.plusMinutes(15+(i%4)*10);

         eventCount++;
         csv(e,String.format("EVT-%06d",eventCount),
             ids[i],"REQUESTED",requestedAt);

         eventCount++;
         csv(e,String.format("EVT-%06d",eventCount),
             ids[i],"SCHEDULED",scheduledAt);

         if(noShow) {
           eventCount++;
           csv(e,String.format("EVT-%06d",eventCount),
               ids[i],"NO_SHOW",
               appointmentTime.plusMinutes(60));
         } else {
           int arrivalOffset=(i%7-3)*5;
           LocalDateTime checkInAt=
               appointmentTime.plusMinutes(arrivalOffset);

           eventCount++;
           csv(e,String.format("EVT-%06d",eventCount),
               ids[i],"CHECKED_IN",checkInAt);

           if(yardEmitted<yardTarget) {
             eventCount++;
             csv(e,String.format("EVT-%06d",eventCount),
                 ids[i],"YARD_ENTERED",
                 checkInAt.plusMinutes(2));
             yardEmitted++;
           }

           int waitMinutes=5+(i%6)*5;
           LocalDateTime serviceAt=
               checkInAt.plusMinutes(waitMinutes);

           if(serviceAt.isBefore(appointmentTime)) {
             serviceAt=appointmentTime;
           }

           LocalDateTime reservationEnd =
               appointmentTime.plusMinutes(60);

           // Keep departure inside the reserved dock window.
           // Variable service duration is synthetic.
           int requestedServiceMinutes=20+(i%4)*5;
           long remainingMinutes=
               java.time.Duration.between(
                   serviceAt,reservationEnd).toMinutes();

           if(remainingMinutes<10) {
             serviceAt=reservationEnd.minusMinutes(10);
             remainingMinutes=10;
           }

           int serviceMinutes=(int)Math.min(
               requestedServiceMinutes,remainingMinutes);

           LocalDateTime departedAt=
               serviceAt.plusMinutes(serviceMinutes);

           eventCount++;
           csv(e,String.format("EVT-%06d",eventCount),
               ids[i],"SERVICE_STARTED",serviceAt);

           eventCount++;
           csv(e,String.format("EVT-%06d",eventCount),
               ids[i],"DEPARTED",departedAt);
         }
       }

       if(yardEmitted!=yardTarget) {
         throw new IllegalStateException(
             "Yard milestone count mismatch");
       }
    }
    int noShows=(appointments+30)/31;
    int actualEvents=appointments*5-noShows*2+(scale==1?13:1292);
    System.out.printf("Generated facilities=%d docks=%d appointments=%d events=%d seed=%d%n",facilities,docks,appointments,actualEvents,SEED);
    System.out.println("Files are in output/");
  }
}
