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
    qris VARCHAR(255),
    region VARCHAR(255),
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
);

CREATE TABLE donaturs (
    id BIGINT  PRIMARY KEY,
    user_id BIGINT,
    phone VARCHAR(255),
    gender VARCHAR(255),
    dob DATE,
    address VARCHAR(255),
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
);

CREATE TABLE donations (
    id BIGINT  PRIMARY KEY,
    donatur_id BIGINT,
    panti_id BIGINT,
    donation_date DATE,
    is_onsite INTEGER,
    donation_type VARCHAR(255)[],
    image VARCHAR(255)[],
    status VARCHAR(255),
    notes TEXT,
    CONSTRAINT fk_donatur
        FOREIGN KEY(donatur_id)
        REFERENCES users(id),
    CONSTRAINT fk_panti
        FOREIGN KEY(panti_id)
        REFERENCES users(id)
);

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

