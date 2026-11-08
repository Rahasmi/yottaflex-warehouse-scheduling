# YottaFlex Warehouse Scheduling — Java Project

## Overview

This project generates deterministic synthetic warehouse scheduling
datasets using Java 21.

It includes facility and dock records, appointments, lifecycle
events, transport entities, scheduling validators, and test classes.

The project has passed several integration and integrity checks,
but compliance with the complete official challenge specification
has not yet been established.

## Requirements

- Java 21
- No third-party Java dependencies

## Compile and Run

From the project root:

```bash
cd src
javac *.java
java Main 1
```

Scale 1 generates:
- 2 facilities
- 20 docks
- 200 appointments
- 999 lifecycle events

For scale 2:

```bash
java Main 2
```

Scale 2 generates:
- 10 facilities
- 100 docks
- 20,000 appointments
- 100,000 lifecycle events

All output is written to `src/output/` when commands are
executed from the `src` directory.

### Generate Transport Datasets

After running the main generator:

```bash
java TransportDatasetGenerator
```

This creates four additional CSV files.

**Important:** The transport generator refuses to overwrite existing
transport CSV files. To regenerate the full dataset, first use a
fresh output directory or preserve and remove the old transport
outputs intentionally.

## CSV Datasets

The main generator creates:

1. `facilities.csv`
2. `docks.csv`
3. `appointments.csv`
4. `events.csv`
5. `ground_truth.csv`

The separate transport generator creates:

6. `carriers.csv`
7. `vehicles.csv`
8. `shipments.csv`
9. `appointment_shipments.csv`

See `docs/data-dictionary.md` for columns and relationships.

## Scheduling Validation

The project includes validators and tests for:

- Dock appointment conflicts
- Dock and load compatibility
- Shared labor capacity
- Shared equipment capacity
- Vehicle weight capacity
- Vehicle and shipment relationships
- Labor shift availability
- Yard slot availability checks

The main generator applies dock occupancy, shared labor,
and shared equipment checks when scheduling appointments.

Yard capacity was checked in a dataset audit, but independent
yard-capacity reservation enforcement in the generator has not
been established.

## Lifecycle Events

Scheduled appointments currently produce:

- REQUESTED
- SCHEDULED
- CHECKED_IN
- SERVICE_STARTED
- DEPARTED

NO_SHOW appointments currently produce:

- REQUESTED
- SCHEDULED
- NO_SHOW

At scale 2:
- 19,354 scheduled appointments
- 646 NO_SHOW appointments
- 100,000 lifecycle events

The generated scale-2 dataset contains exactly 100,000 events.
Lifecycle chronology and dock-overlap checks passed. Other challenge requirements remain under review.

## Verification Results

The following checks were reported as passing:

- Scheduling resource audit
- CSV referential integrity audit
- Event lifecycle chronology audit
- Transport integrity: 18/18 checks
- Transport regeneration: 9/9 SHA-256 matches
- Five negative scenario categories: 5/5 passing

These results describe the tested implementation and do not
constitute a complete external compliance certification.

## Negative Scenarios

The existing Java tests cover:

1. Dock double booking
2. Insufficient workers
3. Equipment shortage
4. Overweight shipment
5. Incompatible load

See `docs/negative-scenarios.md`.

## Documentation

- `docs/data-dictionary.md`
- `docs/negative-scenarios.md`
- `docs/source-manifest.md`
- `docs/prompt-history.md`

## Known Limitations

- The transport generator runs separately from the main generator.
- One vehicle and one shipment per appointment is a provisional
  synthetic-data design choice.
- Event request timestamps are simplified.
- The generator does not yet have verified independent yard
  reservation-capacity enforcement.
- Exact compliance with the official challenge specification,
  including event counts and required negative scenarios,
  remains unverified.
- AI prompt history is incomplete and must be populated with
  the authentic prompts used.
- The source manifest records SHA-256 checksums for project files and must be regenerated whenever covered files change.

## AI Assistance

ChatGPT was used during project development.

The AI prompt history must contain authentic prompts from the
tools and conversations used. Missing prompts should be disclosed,
not reconstructed and presented as exact originals.

The developer should verify and understand the implementation
before submission.

## Verified Full-Scale Dataset — November 2026

The Java 21 generator was tested with fixed seed `20261022`.

Scale-2 results:

- 10 facilities
- 100 docks
- 20,000 appointments
- 100,000 lifecycle events
- 646 no-show ground-truth records
- 8,295 early arrivals
- 17.98 minutes average check-in-to-service wait

The full-scale lifecycle audit found no invalid event
ordering, service-window violations, or actual dock
service overlaps.

Two independent regeneration runs produced SHA-256
checksums identical to the saved five main CSV files.

The five rows in `negative_scenarios.csv` describe
tested negative scenarios. They are not five injected
invalid appointment records.

These checks do not establish compliance with every
official challenge requirement. In particular,
independent yard-capacity enforcement and handling
duration fidelity remain to be assessed.
