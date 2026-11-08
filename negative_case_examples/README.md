# Negative Case Examples

## Purpose

This folder contains five controlled negative-case examples
derived from the generated warehouse scheduling dataset.

These examples are separate from the production CSV output.
The original 20,000 appointments and 100,000 lifecycle
events have not been modified.

## Files

- `negative_case_appointments.csv`: Five example appointments.
- `negative_case_events.csv`: One invalid check-in event.
- `negative_case_ground_truth.csv`: Five expected issue labels.

## Scenarios

| ID | Expected issue | Evidence |
|---|---|---|
| NEG-001 | DOCK_CAPACITY_EXCEEDED | Appointment overlaps an existing booking on the same dock. |
| NEG-002 | INCOMPATIBLE_LOAD | Appointment load type differs from the dock capability. |
| NEG-003 | CHECKIN_BEFORE_APPOINTMENT_DATE | Check-in occurs on the previous calendar date. |
| NEG-004 | LABOR_SHIFT_UNAVAILABLE | Appointment begins at 05:00, outside the configured 06:00 operating window. |
| NEG-005 | NO_SHOW_NOT_RECORDED | Appointment is marked NO_SHOW without a corresponding NO_SHOW event in this example dataset. |

## Verification

Six structural checks passed in Google Colab:

1. Dock overlap exists.
2. Dock/load incompatibility exists.
3. Check-in date precedes appointment date.
4. Appointment begins before 06:00.
5. NO_SHOW event is absent from the example events.
6. Five ground-truth labels match the scenario IDs.

## Limitations

- These are standalone negative-case examples, not
  anomalies injected into the primary generated dataset.
- NEG-004 demonstrates an operating-hours violation.
  It does not independently prove a labor-shift conflict.
- NEG-005 is evaluated against the example events file,
  not a complete appointment lifecycle.
- These checks do not establish that the full generator
  detects or rejects all five cases.
- Full compliance with the official challenge requirements
  has not been established.
