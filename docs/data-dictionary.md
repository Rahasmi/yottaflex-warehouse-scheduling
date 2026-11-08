# YottaFlex Warehouse Scheduling — Data Dictionary

## Overview

This document describes the nine generated CSV datasets,
their column names, record counts, and relationships.

## Dataset Summary

| Dataset | Description | Records |
|---|---|---:|
| `facilities.csv` | Warehouse facility master data | 10 |
| `docks.csv` | Loading dock master data | 100 |
| `appointments.csv` | Warehouse scheduling appointments | 20,000 |
| `events.csv` | Appointment lifecycle events | 98,708 |
| `ground_truth.csv` | Known labeled appointment issues | 646 |
| `carriers.csv` | Transport carrier master data | 10 |
| `vehicles.csv` | Vehicle records and capacities | 20,000 |
| `shipments.csv` | Shipment records and weights | 20,000 |
| `appointment_shipments.csv` | Appointment-to-shipment relationships | 20,000 |

## Column Definitions

### facilities.csv

Warehouse facility master data.

| Column | Description |
|---|---|
| `facility_id` | Unique facility identifier |
| `name` | Human-readable name |

### docks.csv

Loading dock master data.

| Column | Description |
|---|---|
| `dock_id` | Unique loading dock identifier |
| `facility_id` | Unique facility identifier |
| `capability` | Supported dock load type |

### appointments.csv

Warehouse scheduling appointments.

| Column | Description |
|---|---|
| `appointment_id` | Unique appointment identifier |
| `facility_id` | Unique facility identifier |
| `dock_id` | Unique loading dock identifier |
| `load_type` | Type of goods being handled |
| `scheduled_start` | Scheduled service start date and time |
| `scheduled_end` | Scheduled service end date and time |
| `status` | Appointment scheduling status |

### events.csv

Appointment lifecycle events.

| Column | Description |
|---|---|
| `event_id` | Unique event identifier |
| `appointment_id` | Unique appointment identifier |
| `event_type` | Lifecycle event category |
| `event_time` | Date and time of the event |

### ground_truth.csv

Known labeled appointment issues.

| Column | Description |
|---|---|
| `appointment_id` | Unique appointment identifier |
| `issue_type` | Labeled issue category |

### carriers.csv

Transport carrier master data.

| Column | Description |
|---|---|
| `carrier_id` | Unique transport carrier identifier |
| `name` | Human-readable name |

### vehicles.csv

Vehicle records and capacities.

| Column | Description |
|---|---|
| `vehicle_id` | Unique vehicle identifier |
| `carrier_id` | Unique transport carrier identifier |
| `vehicle_type` | Vehicle's supported load type |
| `capacity_kg` | Vehicle maximum weight capacity in kilograms |

### shipments.csv

Shipment records and weights.

| Column | Description |
|---|---|
| `shipment_id` | Unique shipment identifier |
| `vehicle_id` | Unique vehicle identifier |
| `load_type` | Type of goods being handled |
| `weight_kg` | Shipment weight in kilograms |

### appointment_shipments.csv

Appointment-to-shipment relationships.

| Column | Description |
|---|---|
| `appointment_id` | Unique appointment identifier |
| `shipment_id` | Unique shipment identifier |

## Dataset Relationships

| Source dataset | Column | Referenced dataset | Column |
|---|---|---|---|
| `docks.csv` | `facility_id` | `facilities.csv` | `facility_id` |
| `appointments.csv` | `facility_id` | `facilities.csv` | `facility_id` |
| `appointments.csv` | `dock_id` | `docks.csv` | `dock_id` |
| `events.csv` | `appointment_id` | `appointments.csv` | `appointment_id` |
| `ground_truth.csv` | `appointment_id` | `appointments.csv` | `appointment_id` |
| `vehicles.csv` | `carrier_id` | `carriers.csv` | `carrier_id` |
| `shipments.csv` | `vehicle_id` | `vehicles.csv` | `vehicle_id` |
| `appointment_shipments.csv` | `appointment_id` | `appointments.csv` | `appointment_id` |
| `appointment_shipments.csv` | `shipment_id` | `shipments.csv` | `shipment_id` |

## Data Generation

- Generator seed: `20261022`.
- Scale used for these counts: `2`.
- Appointment scheduling window begins October 1, 2026.
- Scheduling uses a 60-day date window.
- Load types: `STANDARD`, `REFRIGERATED`, `HAZARDOUS`.
- Transport datasets are currently generated separately.

## Important Limitations

- Transport generation currently assigns one vehicle
  and one shipment per appointment.
- This is a provisional synthetic-data design choice.
- Transport records are linked through
  `appointment_shipments.csv` rather than additional
  columns in `appointments.csv`.
- The dictionary documents observed CSV schemas;
  it is not an official challenge specification.
- Dataset counts shown above describe the current
  scale-2 output and may change after regeneration.

## Validation Evidence

- Transport integrity: 18 of 18 checks passed.
- Deterministic regeneration: 9 of 9 files matched.
- Negative scenarios: 5 of 5 passed.

See `negative-scenarios.md` for negative-test evidence.
