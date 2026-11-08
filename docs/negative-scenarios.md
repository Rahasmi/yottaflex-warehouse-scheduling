# Negative Scenario Test Evidence

## Purpose

Verify that the YottaFlex Java validators reject five representative invalid warehouse scheduling or transport requests.

## Test Environment

- Language: Java
- Execution: Existing compiled Java test classes
- Result: Five scenarios passed

## Scenario Results

| Scenario | Expected behavior | Java test class | Result |
|---|---|---|---|
| Dock double booking | Overlapping bookings on the same dock are rejected. | `DockConflictValidatorTest` | PASS |
| Insufficient workers | Bookings exceeding shared labor capacity are rejected. | `SharedLaborCapacityTest` | PASS |
| Equipment shortage | A fifth overlapping forklift request is rejected when only four are available. | `SharedEquipmentCapacityTest` | PASS |
| Overweight shipment | A shipment exceeding its vehicle capacity is rejected. | `TransportAssignmentValidatorTest` | PASS |
| Incompatible load | An incompatible dock/load combination is rejected. | `IntegratedSchedulingValidatorTest` | PASS |

## Verification Method

Each Java test class was executed independently.
The audit required:

1. Java process exit code 0.
2. The expected scenario-specific PASS message.
3. An explicit zero-failure test summary.
4. No FAIL-prefixed output lines.

## Scope and Limitations

- These tests validate individual rules and validators.
- They do not prove that every possible invalid dataset is rejected by the complete generator.
- These five categories are representative scenarios; their exact alignment with the official challenge requirements has not yet been verified.
- No negative cases were intentionally inserted into the production CSV datasets.

## Overall Result

**PASS: 5 of 5 scenarios verified.**
