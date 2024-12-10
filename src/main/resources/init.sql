# create image storage
create table if not exists images
(
    id        int primary key auto_increment,
    name      varchar(200) not null,
    data      mediumblob   not null,
    width     int          not null,
    height    int          not null,
    extension varchar(5)   not null,
    check ( width > 0 and height > 0 )
);

# create user management system
create table if not exists user_types
(
    id          int primary key auto_increment,
    type_name   varchar(20) not null unique,
    description varchar(100)
);

create table if not exists gender_codes
(
    id          int primary key auto_increment,
    code_name   varchar(10) unique,
    description varchar(100)
);

create table if not exists address
(
    id             int primary key auto_increment,
    postal_name    char(5)     not null,
    do             varchar(5),
    si             varchar(10) not null,
    detail_address text        not null,
    created_at     timestamp   not null default now()
);

create table if not exists users
(
    id             int primary key auto_increment,
    uid            varchar(10)  not null unique,
    login_id       varchar(20)  not null unique,
    login_password varchar(255) not null comment 'hashed with salt',
    salt           varchar(255) not null,
    user_name      varchar(50)  not null,
    phone_number   varchar(20)  not null unique,
    created_at     timestamp    not null default now(),
    updated_at     timestamp    not null default now() on update now(),
    user_type_id   int          not null,
    gender_code_id int          not null,
    address_id     int          not null,
    profile_image  int,
    foreign key (user_type_id) references user_types (id) on delete restrict,
    foreign key (gender_code_id) references gender_codes (id) on delete restrict,
    foreign key (address_id) references address (id) on delete restrict,
    foreign key (profile_image) references images (id) on delete set null,
    index (phone_number)
);

# subjects management system

create table if not exists grade_levels
(
    id           int primary key auto_increment,
    level_name   varchar(2)    not null unique,
    scaled_score decimal(2, 1) not null unique
);

create table if not exists subjects
(
    id                int primary key auto_increment,
    subject_name      varchar(100) not null,
    description       varchar(200),
    credit            tinyint      not null,
    created_at        timestamp    not null default now(),
    updated_at        timestamp    not null default now() on update now(),
    professor_user_id int          not null,
    foreign key (professor_user_id) references users (id) on delete restrict
);

create table if not exists grades
(
    id              int primary key auto_increment,
    created_at      timestamp not null default now(),
    updated_at      timestamp not null default now() on update now(),
    subject_id      int       not null,
    student_user_id int       not null,
    grade_level_id  int       not null,
    foreign key (subject_id) references subjects (id) on delete restrict,
    foreign key (student_user_id) references users (id) on delete restrict,
    foreign key (grade_level_id) references grade_levels (id) on delete restrict
);

# dormitory management system

create table if not exists dormitories
(
    id          int primary key auto_increment,
    name        varchar(50) not null unique,
    description varchar(100)
);

create table if not exists room_types
(
    id          int primary key auto_increment,
    type_name   varchar(10) not null unique,
    description varchar(100),
    max_person  int         not null default 1,
    check ( max_person > 0 )
);

create table if not exists dormitory_room_types
(
    id           int primary key auto_increment,
    price        int       not null,
    created_at   timestamp not null default now(),
    updated_at   timestamp not null default now() on update now(),
    room_type_id int       not null,
    dormitory_id int       not null,
    foreign key (room_type_id) references room_types (id) on delete restrict,
    foreign key (dormitory_id) references dormitories (id) on delete cascade,
    unique key (room_type_id, dormitory_id),
    check ( price > -1 )
);

create table if not exists rooms
(
    id                     int primary key auto_increment,
    room_number            varchar(10) not null,
    dormitory_room_type_id int         not null,
    dormitory_id           int         not null,
    foreign key (dormitory_room_type_id) references dormitory_room_types (id) on delete restrict,
    foreign key (dormitory_id) references dormitories (id) on delete cascade,
    unique key (room_number, dormitory_id),
    index (room_number)
);

create table if not exists meal_plan_types
(
    id          int primary key auto_increment,
    type_name   varchar(10) not null unique,
    description varchar(100)
);

create table if not exists meal_plans
(
    id                int primary key auto_increment,
    price             int not null,
    meal_plan_type_id int not null,
    dormitory_id      int not null,
    foreign key (meal_plan_type_id) references meal_plan_types (id),
    foreign key (dormitory_id) references dormitories (id),
    unique key (meal_plan_type_id, dormitory_id),
    check ( price > -1 )
);

# payment system
create table if not exists payment_statues
(
    id          int primary key auto_increment,
    status_name varchar(20) not null unique,
    description varchar(100)
);

create table if not exists payment_codes
(
    id           int primary key auto_increment,
    payment_code varchar(10) not null unique,
    description  varchar(100)
);

create table if not exists payment_methods
(
    id          int primary key auto_increment,
    method_name varchar(20) not null,
    description varchar(100)
);

create table if not exists payments
(
    id                int primary key auto_increment,
    payment_amount    int       not null,
    created_at        timestamp not null default now(),
    payment_code_id   int       not null,
    payment_status_id int       not null,
    payment_method_id int       not null,
    foreign key (payment_code_id) references payment_codes (id) on delete restrict,
    foreign key (payment_status_id) references payment_statues (id) on delete restrict,
    foreign key (payment_method_id) references payment_methods (id) on delete restrict,
    check ( payment_amount > -1 )
);

create table if not exists card_issuers
(
    id          int primary key auto_increment,
    issuer_name varchar(50) not null unique,
    issuer_code varchar(10) not null unique
);

create table if not exists card_payments
(
    id             int primary key auto_increment,
    card_number    char(16)  not null,
    created_at     timestamp not null default now(),
    card_issuer_id int       not null,
    payment_id     int       not null unique,
    foreign key (card_issuer_id) references card_issuers (id) on delete restrict,
    foreign key (payment_id) references payments (id) on delete cascade
);

create table if not exists banks
(
    id        int primary key auto_increment,
    bank_name varchar(50) not null unique,
    bank_code varchar(10) not null unique
);

create table if not exists bank_transfer_payments
(
    id                  int primary key auto_increment,
    account_number      varchar(30)  not null,
    account_holder_name varchar(100) not null,
    created_at          timestamp    not null default now(),
    payment_id          int          not null unique,
    bank_id             int          not null,
    foreign key (payment_id) references payments (id) on delete cascade,
    foreign key (bank_id) references banks (id) on delete restrict
);

create table if not exists payment_refunds
(
    id                  int primary key auto_increment,
    refund_reason       varchar(200) not null,
    account_number      varchar(30)  not null,
    account_holder_name varchar(100) not null,
    created_at          timestamp    not null default now(),
    payment_id          int          not null unique,
    bank_id             int          not null,
    foreign key (payment_id) references payments (id) on delete cascade,
    foreign key (bank_id) references banks (id) on delete restrict
);

# dormitory selection system

create table if not exists selection_schedules
(
    id         int primary key auto_increment,
    title      varchar(100) not null unique,
    created_at timestamp    not null default now(),
    stated_at  timestamp    not null
);

create table if not exists selection_quotas
(
    id                     int primary key auto_increment,
    quota                  int not null,
    selection_schedule_id  int not null,
    dormitory_room_type_id int not null,
    foreign key (selection_schedule_id) references selection_schedules (id) on delete cascade,
    foreign key (dormitory_room_type_id) references dormitory_room_types (id) on delete restrict,
    unique key (selection_schedule_id, dormitory_room_type_id),
    check ( quota > -1 )
);

create table if not exists selection_phases
(
    id                    int primary key auto_increment,
    phase_name            varchar(20) not null,
    description           varchar(100),
    start_at              timestamp   not null,
    end_at                timestamp   not null,
    created_at            timestamp   not null default now(),
    updated_at            timestamp   not null default now() on update now(),
    selection_schedule_id int         not null,
    foreign key (selection_schedule_id) references selection_schedules (id) on delete cascade,
    unique key (phase_name, selection_schedule_id),
    check ( start_at < end_at )
);

create table if not exists selection_application_statues
(
    id          int primary key auto_increment,
    status_name varchar(10) not null unique,
    description varchar(100)
);

create table if not exists selection_applications
(
    id                              int primary key auto_increment,
    preference                      tinyint   not null default 1,
    has_sleep_habit                 boolean   not null default false,
    is_year_room                    boolean   not null default false,
    created_at                      timestamp not null default now(),
    updated_at                      timestamp not null default now() on update now(),
    selection_application_status_id int       not null,
    selection_schedule_id           int       not null,
    dormitory_room_type_id          int       not null,
    meal_plan_id                    int       not null,
    roommate_user_id                int       null,
    user_id                         int       not null,
    foreign key (selection_application_status_id) references selection_application_statues (id) on delete restrict,
    foreign key (selection_schedule_id) references selection_schedules (id) on delete cascade,
    foreign key (dormitory_room_type_id) references dormitory_room_types (id) on delete restrict,
    foreign key (meal_plan_id) references meal_plans (id) on delete restrict,
    foreign key (roommate_user_id) references users (id) on delete set null,
    foreign key (user_id) references users (id) on delete cascade,
    unique key (selection_schedule_id, preference, user_id),
    unique key (selection_schedule_id, dormitory_room_type_id, user_id),
    check ( preference > 0 )
);

create table if not exists selections
(
    id                               int primary key auto_increment,
    is_final_approved                boolean   not null default false,
    created_at                       timestamp not null default now(),
    updated_at                       timestamp not null default now() on update now(),
    selection_application_id         int       not null,
    tuberculosis_certificate_file_id int,
    additional_proof_file_id         int,
    foreign key (tuberculosis_certificate_file_id) references images (id) on delete set null,
    foreign key (additional_proof_file_id) references images (id) on delete set null
);

create table if not exists room_assignments
(
    id           int primary key auto_increment,
    bed_number   int       not null default 1,
    move_in_at   timestamp not null,
    move_out_at  timestamp null,
    created_at   timestamp not null default now(),
    selection_id int       not null,
    room_id      int       not null,
    foreign key (selection_id) references selections (id) on delete cascade,
    foreign key (room_id) references rooms (id) on delete cascade,
    unique key (bed_number, selection_id, room_id),
    check ( bed_number > 0 ),
    check ( move_out_at is not null and move_in_at < move_out_at)
);

create table if not exists move_out_request_statuses
(
    id          int primary key auto_increment,
    status_name varchar(10) not null unique,
    description varchar(100)
);

create table if not exists move_out_requests
(
    id                 int primary key auto_increment,
    checkout_at        timestamp   not null,
    expect_checkout_at timestamp   not null,
    account_number     varchar(50) not null,
    created_at         timestamp   not null default now(),
    updated_at         timestamp   not null default now() on update now(),
    move_out_status_id int         not null,
    selection_id       int         not null unique,
    bank_id            int         not null,
    foreign key (move_out_status_id) references move_out_request_statuses (id) on delete restrict,
    foreign key (selection_id) references selections (id) on delete cascade,
    foreign key (bank_id) references banks (id) on delete restrict
);

create table if not exists demerit_points
(
    id                 int primary key auto_increment,
    description        varchar(200) not null,
    point              tinyint      not null default 1,
    created_at         timestamp    not null default now(),
    user_id            int          not null,
    room_assignment_id int,
    foreign key (user_id) references users (id) on delete cascade,
    foreign key (room_assignment_id) references room_assignments (id) on delete cascade,
    check ( point > 0 )
);

# payments to dormitory selections
create table if not exists payment_history
(
    id         int primary key auto_increment,
    user_id    int not null,
    payment_id int not null unique,
    foreign key (user_id) references users (id) on delete cascade,
    foreign key (payment_id) references payments (id) on delete cascade
);

create table if not exists selection_payments
(
    id           int primary key auto_increment,
    selection_id int not null,
    payment_id   int not null unique,
    foreign key (selection_id) references selections (id) on delete restrict,
    foreign key (payment_id) references payments (id) on delete cascade
);