INSERT INTO user_types (type_name, description) VALUES ('admin', '관리자');
INSERT INTO user_types (type_name, description) VALUES ('student', '학생');
INSERT INTO gender_codes (code_name, description) VALUES ('male', '남성');
INSERT INTO gender_codes (code_name, description) VALUES ('female', '여성');
INSERT INTO addresses (postal_name, do, si, detail_address, created_at) VALUES ('11111', '경상남도', '창원시', '냠냠동 고양이아파트 123동 301호', '2012-11-10');
INSERT INTO addresses (postal_name, do, si, detail_address, created_at) VALUES ('22222', '경상북도', '구미시', '히히동 멍멍이아파트 321동 103호', '2022-11-11');
INSERT INTO addresses (postal_name, do, si, detail_address, created_at) VALUES ('33333', '충청남도', '창원시', '냐핳동 토깽이아파트 231동 202호', '2022-01-01');
INSERT INTO users (uid, login_password, salt, user_name, phone_number, created_at, updated_at, user_type_id, gender_code_id, address_id)
VALUES (11111111,123123,'abc','김선명','010-1234-5678', '2012-11-10', '2012-11-10', 1,1,1);
INSERT INTO users (uid, login_password, salt, user_name, phone_number, created_at, updated_at, user_type_id, gender_code_id, address_id)
VALUES (20222222,321321,'abc','노가영','010-4321-5678', '2022-11-11', '2022-11-11', 2,2,3);
INSERT INTO users (uid, login_password, salt, user_name, phone_number, created_at, updated_at, user_type_id, gender_code_id, address_id)
VALUES (20223333,231231,'abc','함서현','010-8765-4321', '2022-01-01', '2022-01-01', 2,2,2);
INSERT INTO grade_levels (level_name, scaled_score) VALUES ('A+', 4.5);
INSERT INTO grade_levels (level_name, scaled_score) VALUES ('A', 4.0);
INSERT INTO grade_levels (level_name, scaled_score) VALUES ('B+', 3.5);
INSERT INTO grade_levels (level_name, scaled_score) VALUES ('B', 3.0);
INSERT INTO grade_levels (level_name, scaled_score) VALUES ('C+', 2.5);
INSERT INTO grade_levels (level_name, scaled_score) VALUES ('C', 2.0);
INSERT INTO grade_levels (level_name, scaled_score) VALUES ('D+', 1.5);
INSERT INTO grade_levels (level_name, scaled_score) VALUES ('D', 1.0);
INSERT INTO grade_levels (level_name, scaled_score) VALUES ('F', 0);
INSERT INTO subjects (subject_name, description, credit, created_at, updated_at, professor_user_id)
VALUES ('컴퓨터네트워크', '컴퓨터네트워크 수업', 4, '2012-11-10', '2012-11-10', 1);
INSERT INTO subjects (subject_name, description, credit, created_at, updated_at, professor_user_id)
VALUES ('융합프로젝트', '융합프로젝트 수업', 3, '2012-11-10', '2012-11-10', 1);
INSERT INTO grades (created_at, updated_at, subject_id, student_user_id, grade_level_id)
VALUES ('2023-03-10', '2023-03-10', 1, 2, 3);
INSERT INTO grades (created_at, updated_at, subject_id, student_user_id, grade_level_id)
VALUES ('2023-03-10', '2023-03-10', 2, 2, 1);
INSERT INTO grades (created_at, updated_at, subject_id, student_user_id, grade_level_id)
VALUES ('2023-03-10', '2023-03-10', 1, 3, 2);
INSERT INTO grades (created_at, updated_at, subject_id, student_user_id, grade_level_id)
VALUES ('2023-03-10', '2023-03-10', 2, 3, 1);
INSERT INTO selection_application_statuses (status_name, description) VALUES ('선발', '선발 상태를 나타낸다.');
INSERT INTO selection_application_statuses (status_name, description) VALUES ('미선발', '선발 실패 상태를 나타낸다.');
INSERT INTO selection_application_statuses (status_name, description) VALUES ('선발대기', '선발 대기중 상태를 나타낸다.');
INSERT INTO selection_schedules (title, created_at, started_at, ended_at) VALUES ('일반 선발', '2024-06-02', '2024-07-15', '2024-07-25');
INSERT INTO selection_schedules (title, created_at, started_at, ended_at) VALUES ('추가선발', '2024-06-02', '2024-08-01', '2024-08-05');
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('일반선발 공지', '일반선발 공지기간을 나타낸다', '2024-07-07', '2024-07-16', '2024-06-02', '2024-06-02', 1);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('입사 신청', '일반선발 입사 신청기간을 나타낸다', '2024-07-15', '2024-07-19', '2024-06-02', '2024-06-02', 1);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('일반선발 배정 및 합격자 발표', '일반선발 합격자 기숙사 배정 및 합격자 발표 기간을 나타낸다', '2024-07-21', '2024-07-21 23:00:00', '2024-06-02', '2024-06-02', 1);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('일반선발 생활관비 납부', '일반선발 합격자 생활관비 납부기간을 나타낸다', '2024-07-21 10:00:00', '2024-07-21 16:00:00', '2024-06-02', '2024-06-02', 1);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('결핵 진단서 제출', '합격자 결핵 진단서 제출하는 기간을 나타낸다.', '2024-07-21', '2024-07-25', '2024-06-02', '2024-06-02', 1);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('추가 선발 공고', '추가선발 입사 공지기간을 나타낸다', '2024-07-30', '2024-08-01', '2024-06-02', '2024-06-02', 2);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('추가 선발 신청', '추가선발 입사 신청기간을 나타낸다', '2024-08-01', '2024-08-02', '2024-06-02', '2024-06-02', 2);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('추가 선발 합격자 발표', '추가선발 합격자 발표 및 배정기간을 나타낸다', '2024-08-03 10:00:00', '2024-08-04', '2024-06-02', '2024-06-02', 2);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('추가 선발 생활관비 납부', '추가선발 합격자 생활관비 납부기간을 나타낸다', '2024-08-03 10:00:00', '2024-08-03 16:00:00', '2024-06-02', '2024-06-02', 2);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('추가 선발 결핵 진단서 제출', '추가선발 결핵 진단서 제출하는 기간을 나타낸다', '2024-08-03 10:00:00', '2024-08-05', '2024-06-02', '2024-06-02', 2);
INSERT INTO room_types (type_name, description, max_person) VALUES ('2인실', '모든 2인실에 대한 설명이다.', 2);
INSERT INTO room_types (type_name, description, max_person) VALUES ('4인실', '모든 4인실에 대한 설명이다.', 4);
INSERT INTO dormitories (name, description) VALUES ('푸름관 1동', '여자 푸름관 2인실에 대한 설명이다.');
INSERT INTO dormitories (name, description) VALUES ('푸름관 2동', '남자 푸름관 2인실에 대한 설명이다.');
INSERT INTO dormitories (name, description) VALUES ('푸름관 3동', '여자 푸름관 4인실에 대한 설명이다.');
INSERT INTO dormitories (name, description) VALUES ('푸름관 4동', '남자 푸름관 4인실에 대한 설명이다.');
INSERT INTO dormitories (name, description) VALUES ('오름관 1동', '여자 오름관 2인실에 대한 설명이다.');
INSERT INTO dormitories (name, description) VALUES ('오름관 2동', '남자 오름관 2인실에 대한 설명이다.');
INSERT INTO dormitory_room_types (price, created_at, updated_at, room_type_id, dormitory_id)
VALUES (462000,'2024-06-02','2024-06-02',1,1);
INSERT INTO dormitory_room_types (price, created_at, updated_at, room_type_id, dormitory_id)
VALUES (462000,'2024-06-02','2024-06-02',1,2);
INSERT INTO dormitory_room_types (price, created_at, updated_at, room_type_id, dormitory_id)
VALUES (385000,'2024-06-02','2024-06-02',2,3);
INSERT INTO dormitory_room_types (price, created_at, updated_at, room_type_id, dormitory_id)
VALUES (385000,'2024-06-02','2024-06-02',2,4);
INSERT INTO dormitory_room_types (price, created_at, updated_at, room_type_id, dormitory_id)
VALUES (628800,'2024-06-02','2024-06-02',1,5);
INSERT INTO dormitory_room_types (price, created_at, updated_at, room_type_id, dormitory_id)
VALUES (628800,'2024-06-02','2024-06-02',1,6);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('101', 1,1);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('102', 1,1);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('103', 1,1);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('201', 1,1);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('202', 1,1);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('203', 1,1);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('301', 1,1);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('302', 1,1);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('303', 1,1);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('101', 2,2);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('102', 2,2);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('103', 2,2);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('201', 2,2);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('202', 2,2);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('203', 2,2);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('301', 2,2);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('302', 2,2);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('303', 2,2);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('101', 3,3);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('102', 3,3);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('103', 3,3);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('201', 3,3);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('202', 3,3);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('203', 3,3);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('301', 3,3);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('302', 3,3);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('303', 3,3);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('101', 4,4);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('102', 4,4);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('103', 4,4);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('201', 4,4);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('202', 4,4);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('203', 4,4);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('301', 4,4);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('302', 4,4);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('303', 4,4);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('101', 5,5);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('102', 5,5);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('103', 5,5);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('201', 5,5);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('202', 5,5);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('203', 5,5);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('301', 5,5);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('302', 5,5);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('303', 5,5);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('101', 6,6);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('102', 6,6);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('103', 6,6);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('201', 6,6);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('202', 6,6);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('203', 6,6);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('301', 6,6);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('302', 6,6);
INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES ('303', 6,6);
INSERT INTO meal_plan_types (type_name, description) VALUES ('푸름 7일식', '푸름 7일식을 표현한다.');
INSERT INTO meal_plan_types (type_name, description) VALUES ('푸름 5일식', '푸름 5일식을 표현한다.');
INSERT INTO meal_plan_types (type_name, description) VALUES ('푸름 안함', '푸름 식사안함을 표현한다.');
INSERT INTO meal_plan_types (type_name, description) VALUES ('오름 7일식', '오름 7일식을 표현한다.');
INSERT INTO meal_plan_types (type_name, description) VALUES ('오름 5일식', '오름 5일식을 표현한다.');
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (622720, 1, 2);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (503670, 2, 2);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (0, 3, 2);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (622720, 1, 4);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (503670, 2, 4);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (0, 3, 4);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (622720, 1, 1);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (503670, 2, 1);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (0, 3, 1);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (622720, 1, 3);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (503670, 2, 3);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (0, 3, 3);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (716800, 4, 5);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (575390, 5, 5);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (716800, 4, 6);
INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (575390, 5, 6);
INSERT INTO move_out_request_statuses (status_name, description) VALUES ('퇴사대기', '퇴사 대기상태');
INSERT INTO move_out_request_statuses (status_name, description) VALUES ('퇴사완료', '퇴사 완료상태');
INSERT INTO banks (bank_name, bank_code) VALUES ('농협', '011');
INSERT INTO banks (bank_name, bank_code) VALUES ('국민', '004');
INSERT INTO banks (bank_name, bank_code) VALUES ('기업', '003');
INSERT INTO card_issuers (issuer_name, issuer_code) VALUES ('BC', '361');
INSERT INTO card_issuers (issuer_name, issuer_code) VALUES ('KB', '381');
INSERT INTO card_issuers (issuer_name, issuer_code) VALUES ('NH', '371');
INSERT INTO payment_codes (payment_code, description) VALUES ('111', '대략적으로 아무거나 생성. 실제로 어떻게 동작하는지는 모름.');
INSERT INTO payment_codes (payment_code, description) VALUES ('222', '대략적으로 아무거나 생성. 실제로 어떻게 동작하는지는 모름.');
INSERT INTO payment_statuses (status_name, description) VALUES ('결제완료', '완료된 상태를 표시한다.');
INSERT INTO payment_statuses (status_name, description) VALUES ('결제대기', '대기 상태를 표시한다.');
INSERT INTO payment_methods (method_name, description) VALUES ('카드결제', '카드결제를 나타내는 상태이다.');
INSERT INTO payment_methods (method_name, description) VALUES ('계좌이체', '계좌이체를 나타내는 상태이다.');
INSERT INTO payments (payment_amount, created_at, payment_code_id, payment_status_id, payment_method_id)
VALUES (628800, '2024-07-21 12:32:26', 1,2,1);
INSERT INTO payments (payment_amount, created_at, payment_code_id, payment_status_id, payment_method_id)
VALUES (385000, '2024-07-21 13:12:43', 1,1,2);
INSERT INTO selection_applications (preference, has_sleep_habit, is_year_room, created_at, updated_at, selection_application_status_id,
                               selection_schedule_id, dormitory_room_type_id, meal_plan_id, roommate_user_id, user_id)
VALUES (1, 1, 0, '2024-07-21', '2024-07-21', 1, 1, 1,1,3,2);
INSERT INTO selection_applications (preference, has_sleep_habit, is_year_room, created_at, updated_at, selection_application_status_id,
                               selection_schedule_id, dormitory_room_type_id, meal_plan_id, roommate_user_id, user_id)
VALUES (1, 1, 0, '2024-07-21', '2024-07-21', 1, 1, 1,1,2,3);
INSERT INTO selections (is_final_approved, created_at, updated_at, selection_application_id, tuberculosis_certificate_file_id, additional_proof_file_id)
VALUES (1, '2024-07-21','2024-07-21',1,null,null);
INSERT INTO selections (is_final_approved, created_at, updated_at, selection_application_id, tuberculosis_certificate_file_id, additional_proof_file_id)
VALUES (1, '2024-07-21','2024-07-21',2,null,null);
INSERT INTO card_payments (card_number, card_issuer_id, payment_id) VALUES (11111111,2,1);
INSERT INTO bank_transfer_payments (account_number, account_holder_name, created_at, payment_id, bank_id)
VALUES (12321321, '함서현', '2024-07-21', 2, 2);
INSERT INTO payment_histories (user_id, payment_id) VALUES (2,1);
INSERT INTO payment_histories (user_id, payment_id) VALUES (3,2);
INSERT INTO selection_payments (selection_id, payment_id) VALUES (1,1);
INSERT INTO selection_payments (selection_id, payment_id) VALUES (2,2);
INSERT INTO selection_schedules (title, created_at, started_at, ended_at)
VALUES ('우선선발', '2024-06-02', '2024-07-01', '2024-07-14');
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('우선선발 공지' , '우선선발 공지 기간을 나타낸다', '2024-06-25', '2024-07-01', '2024-06-02', '2024-06-02', 3);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('우선선발 신청' , '우선선발 신청 기간을 나타낸다', '2024-07-01', '2024-07-05', '2024-06-02', '2024-06-02', 3);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('우선선발 배정 및 합격자 발표' , '우선선발 합격자 발표 및 배정하는 기간을 나타낸다', '2024-07-03', '2024-07-03 23:00:00', '2024-06-02', '2024-06-02', 3);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('우선선발 생활관비 납부' , '우선선발 생활관비 납부 기간을 나타낸다', '2024-07-03 10:00:00', '2024-07-03 16:00:00', '2024-06-02', '2024-06-02', 3);
INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id)
VALUES ('우선선발 결핵 진단서 제출' , '우선선발 결핵 진단서 제출 기간을 나타낸다', '2024-07-03', '2024-07-07', '2024-06-02', '2024-06-02', 3);