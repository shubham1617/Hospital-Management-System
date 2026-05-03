-- Billing Entity Data Seed
-- Using UUID-style strings for IDs to match your 36-character column length

INSERT IGNORE INTO billing (id, patient_id, amount, status, billing_date, description)
VALUES ('b1a2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d', 'p001-8888-9999-7777', 150.00, 'PAID', '2026-01-15', 'General Consultation Fee');

INSERT IGNORE INTO billing (id, patient_id, amount, status, billing_date, description)
VALUES ('a1b2c3d4-b5c6-d7e8-f9a0-1b2c3d4e5f6g', 'p002-1111-2222-3333', 2500.50, 'PENDING', '2026-03-20', 'Surgical Procedure Deposit');

INSERT IGNORE INTO billing (id, patient_id, amount, status, billing_date, description)
VALUES ('c3d4e5f6-g7h8-i9j0-k1l2-m3n4o5p6q7r8', 'p003-4444-5555-6666', 75.25, 'PAID', '2026-03-21', 'Pharmacy - Antibiotics');

INSERT IGNORE INTO billing (id, patient_id, amount, status, billing_date, description)
VALUES ('d4e5f6g7-h8i9-j0k1-l2m3-n4o5p6q7r8s9', 'p001-8888-9999-7777', 300.00, 'FAILED', '2026-03-10', 'MRI Scan - Payment Declined');

INSERT IGNORE INTO billing (id, patient_id, amount, status, billing_date, description)
VALUES ('e5f6g7h8-i9j0-k1l2-m3n4-o5p6q7r8s9t0', 'p004-9999-0000-1111', 120.00, 'PAID', '2026-02-28', 'Lab Tests - Blood Work');