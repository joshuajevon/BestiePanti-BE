CREATE TABLE roles (
    id BIGINT  PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE users (
    id BIGINT  PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    role_id BIGINT,
    CONSTRAINT fk_role
        FOREIGN KEY(role_id) 
        REFERENCES roles(id)
);

CREATE TABLE pantis (
    id BIGINT  PRIMARY KEY,
    user_id BIGINT,
    image VARCHAR(255)[], 
    description TEXT,
    phone VARCHAR(255),
    donation_types VARCHAR(255)[],  
    is_urgent INTEGER,
    address VARCHAR(255),
    region VARCHAR(255),
    maps TEXT,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE donaturs (
    id BIGINT  PRIMARY KEY,
    user_id BIGINT,
    phone VARCHAR(255),
    gender VARCHAR(255),
    dob DATE,
    address VARCHAR(255),
    profile VARCHAR(255),
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE donations (
    id BIGINT  PRIMARY KEY,
    donatur_id BIGINT,
    panti_id BIGINT,
    donation_date DATE,
    is_onsite INTEGER,
    donation_type VARCHAR(255)[],
    status VARCHAR(255),
    inserted_timestamp TIMESTAMP,
    verified_timestamp TIMESTAMP,
    CONSTRAINT fk_donatur
        FOREIGN KEY(donatur_id)
        REFERENCES users(id),
    CONSTRAINT fk_panti
        FOREIGN KEY(panti_id)
        REFERENCES users(id)
);

CREATE TABLE funds(
    id BIGINT PRIMARY KEY,
    donation_id BIGINT,
    nominal_amount VARCHAR(255),
    account_number VARCHAR(255),
    account_name VARCHAR(255),
    image VARCHAR(255),
    CONSTRAINT fk_donation
        FOREIGN KEY(donation_id)
        REFERENCES donations(id)
        ON DELETE CASCADE
)

CREATE TABLE nonfunds(
    id BIGINT PRIMARY KEY,
    donation_id BIGINT,
    pic VARCHAR(255),
    active_phone VARCHAR(255),
    notes TEXT,
    CONSTRAINT fk_donation
        FOREIGN KEY(donation_id)
        REFERENCES donations(id)
        ON DELETE CASCADE
)

CREATE TABLE messages (
    id BIGINT  PRIMARY KEY,
    donatur_id BIGINT,
    panti_id BIGINT,
    message TEXT,
    timestamp TIMESTAMP,
    is_shown INTEGER,
    CONSTRAINT fk_donatur
        FOREIGN KEY(donatur_id)
        REFERENCES users(id),
    CONSTRAINT fk_panti
        FOREIGN KEY(panti_id)
        REFERENCES users(id)
);

CREATE TABLE payments (
    ID BIGINT PRIMARY KEY,
    panti_id BIGINT,
    bank_name VARCHAR(255),
    bank_account_number VARCHAR(255),
    bank_account_name VARCHAR(255),
    qris VARCHAR(255),
    CONSTRAINT fk_panti
        FOREIGN KEY(panti_id)
        REFERENCES pantis(id)
)

CREATE TABLE forgot_passwords (
    id BIGINT PRIMARY KEY,
    otp VARCHAR(255),
    expiration_time TIMESTAMP,
    is_used INTEGER,
    user_id BIGINT,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
)

CREATE TABLE email_verifications (
    id BIGINT PRIMARY KEY,
    new_email VARCHAR(255),
    token VARCHAR(64),
    expiration_time TIMESTAMP,
    is_verified BOOLEAN DEFAULT FALSE,
    user_id BIGINT,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
)

CREATE TABLE two_step_verifications (
    id BIGINT PRIMARY KEY,
    otp VARCHAR(255),
    expiration_time TIMESTAMP,
    verified_timestamp TIMESTAMP,
    email VARCHAR(255)
);