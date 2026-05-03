-- Sample data for development and testing

-- Doctor Entity Data Seed
INSERT IGNORE INTO doctors (doctor_id, doctor_name, doctor_email, doctor_phone, specialization, is_active, joining_date, created_at, updated_at)
VALUES ('DOC-2026-001', 'Dr. Arnab Das', 'arnab.das@healthmail.com', '9876543210', 'Cardiology', b'1', '2020-05-12', '2026-03-01 10:00:00.000000', NULL);

INSERT IGNORE INTO doctors (doctor_id, doctor_name, doctor_email, doctor_phone, specialization, is_active, joining_date, created_at, updated_at)
VALUES ('DOC-2026-002', 'Dr. Sarah Jenkins', 's.jenkins@cityhospital.org', '8765432109', 'Neurology', b'1', '2021-08-24', '2026-03-05 14:30:15.123456', '2026-03-20 09:15:00.000000');

INSERT  IGNORE INTO doctors (doctor_id, doctor_name, doctor_email, doctor_phone, specialization, is_active, joining_date, created_at, updated_at)
VALUES ('DOC-2026-003', 'Dr. Michael Chen', 'm.chen@medcenter.com', '7654321098', 'Pediatrics', b'1', '2019-11-03', '2026-01-15 08:45:00.000000', NULL);

INSERT IGNORE  INTO doctors (doctor_id, doctor_name, doctor_email, doctor_phone, specialization, is_active, joining_date, created_at, updated_at)
VALUES ('DOC-2026-004', 'Dr. Elena Rodriguez', 'elena.r@specialist.net', '6543210987', 'Dermatology', b'0', '2023-02-14', '2026-02-28 11:20:00.000000', '2026-03-25 16:45:30.000000');

INSERT IGNORE INTO doctors (doctor_id, doctor_name, doctor_email, doctor_phone, specialization, is_active, joining_date, created_at, updated_at)
VALUES ('DOC-2026-005', 'Dr. James Wilson', 'j.wilson@ortho-clinic.com', '5432109876', 'Orthopedics', b'1', '2022-06-30', '2026-03-10 12:00:00.000000', NULL);