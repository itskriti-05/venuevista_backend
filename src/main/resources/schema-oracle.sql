-- ===================================================================
-- VenueVista: EventPro Ticketing & Venue Management
-- Enterprise Oracle Database DDL Schema (Oracle 19c / 21c / 23ai)
-- ===================================================================

-- 1. Sequences for Primary Keys
CREATE SEQUENCE seq_venues START WITH 100 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_sections START WITH 200 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_seats START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_events START WITH 500 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_ticket_tiers START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_bookings START WITH 5000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_tickets START WITH 10000 INCREMENT BY 1 NOCACHE NOCYCLE;

-- 2. Venues Table
CREATE TABLE venues (
    id NUMBER(19) NOT NULL,
    name VARCHAR2(150) NOT NULL,
    code VARCHAR2(50) NOT NULL UNIQUE,
    venue_type VARCHAR2(50) DEFAULT 'ARENA',
    address VARCHAR2(255) NOT NULL,
    city VARCHAR2(100) NOT NULL,
    state VARCHAR2(100),
    country VARCHAR2(100) DEFAULT 'United States',
    postal_code VARCHAR2(20),
    total_capacity NUMBER(10) NOT NULL,
    image_url VARCHAR2(500),
    description VARCHAR2(1000),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_venues PRIMARY KEY (id)
);

-- 3. Sections Table (e.g. VIP Floor, Orchestra, Lower Tier, Balcony, Skybox)
CREATE TABLE sections (
    id NUMBER(19) NOT NULL,
    venue_id NUMBER(19) NOT NULL,
    name VARCHAR2(100) NOT NULL,
    section_code VARCHAR2(50) NOT NULL,
    tier_category VARCHAR2(50) NOT NULL, -- VIP, PREMIUM, STANDARD, ACCESSIBLE, BOX
    color_hex VARCHAR2(20) DEFAULT '#4F46E5',
    svg_path_data CLOB,
    total_rows NUMBER(5) NOT NULL,
    seats_per_row NUMBER(5) NOT NULL,
    capacity NUMBER(10) NOT NULL,
    elevation_angle NUMBER(5, 2) DEFAULT 0,
    CONSTRAINT pk_sections PRIMARY KEY (id),
    CONSTRAINT fk_section_venue FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE
);

-- 4. Seats Table (Specific physical seats in venue section)
CREATE TABLE seats (
    id NUMBER(19) NOT NULL,
    section_id NUMBER(19) NOT NULL,
    row_label VARCHAR2(10) NOT NULL,
    seat_number NUMBER(5) NOT NULL,
    seat_label VARCHAR2(20) NOT NULL, -- e.g. "A-12"
    seat_type VARCHAR2(30) DEFAULT 'STANDARD', -- STANDARD, VIP, ACCESSIBLE, COMP
    pos_x NUMBER(10, 2) DEFAULT 0,
    pos_y NUMBER(10, 2) DEFAULT 0,
    is_active NUMBER(1) DEFAULT 1,
    CONSTRAINT pk_seats PRIMARY KEY (id),
    CONSTRAINT fk_seat_section FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE CASCADE
);

-- 5. Events Table
CREATE TABLE events (
    id NUMBER(19) NOT NULL,
    venue_id NUMBER(19) NOT NULL,
    title VARCHAR2(200) NOT NULL,
    slug VARCHAR2(220) NOT NULL UNIQUE,
    category VARCHAR2(50) NOT NULL, -- CONCERT, SPORTS, THEATER, CONFERENCE, COMEDY
    genre VARCHAR2(100),
    performer_artist VARCHAR2(200),
    description CLOB,
    banner_url VARCHAR2(500),
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    doors_open TIMESTAMP WITH TIME ZONE,
    status VARCHAR2(30) DEFAULT 'PUBLISHED', -- DRAFT, PUBLISHED, SOLD_OUT, CANCELLED, COMPLETED
    featured NUMBER(1) DEFAULT 0,
    total_revenue NUMBER(15, 2) DEFAULT 0,
    tickets_sold NUMBER(10) DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_event_venue FOREIGN KEY (venue_id) REFERENCES venues(id)
);

-- 6. Ticket Tiers for an Event
CREATE TABLE ticket_tiers (
    id NUMBER(19) NOT NULL,
    event_id NUMBER(19) NOT NULL,
    section_id NUMBER(19),
    tier_name VARCHAR2(100) NOT NULL, -- "Diamond VIP", "Lower Bowl Gold", "General Admission"
    price NUMBER(10, 2) NOT NULL,
    booking_fee NUMBER(10, 2) DEFAULT 5.00,
    tax_rate NUMBER(5, 4) DEFAULT 0.0800,
    capacity NUMBER(10) NOT NULL,
    available_seats NUMBER(10) NOT NULL,
    color_code VARCHAR2(20) DEFAULT '#3B82F6',
    perks CLOB,
    CONSTRAINT pk_ticket_tiers PRIMARY KEY (id),
    CONSTRAINT fk_tier_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT fk_tier_section FOREIGN KEY (section_id) REFERENCES sections(id)
);

-- 7. Bookings / Orders Table
CREATE TABLE bookings (
    id NUMBER(19) NOT NULL,
    booking_reference VARCHAR2(50) NOT NULL UNIQUE, -- e.g. "VV-2026-89472"
    event_id NUMBER(19) NOT NULL,
    customer_name VARCHAR2(150) NOT NULL,
    customer_email VARCHAR2(150) NOT NULL,
    customer_phone VARCHAR2(50),
    total_tickets NUMBER(5) NOT NULL,
    subtotal_amount NUMBER(12, 2) NOT NULL,
    fees_amount NUMBER(12, 2) NOT NULL,
    taxes_amount NUMBER(12, 2) NOT NULL,
    total_amount NUMBER(12, 2) NOT NULL,
    payment_status VARCHAR2(30) DEFAULT 'CONFIRMED', -- PENDING, CONFIRMED, REFUNDED, CANCELLED
    payment_method VARCHAR2(50) DEFAULT 'CREDIT_CARD',
    transaction_id VARCHAR2(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT fk_booking_event FOREIGN KEY (event_id) REFERENCES events(id)
);

-- 8. Tickets (Individual issued ticket per seat)
CREATE TABLE tickets (
    id NUMBER(19) NOT NULL,
    booking_id NUMBER(19) NOT NULL,
    event_id NUMBER(19) NOT NULL,
    ticket_tier_id NUMBER(19) NOT NULL,
    seat_id NUMBER(19) NOT NULL,
    ticket_number VARCHAR2(60) NOT NULL UNIQUE, -- e.g. "TKT-89472-A12"
    qr_code_data VARCHAR2(255) NOT NULL,
    seat_label VARCHAR2(50) NOT NULL,
    section_name VARCHAR2(100) NOT NULL,
    price_paid NUMBER(10, 2) NOT NULL,
    check_in_status NUMBER(1) DEFAULT 0,
    checked_in_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_tickets PRIMARY KEY (id),
    CONSTRAINT fk_ticket_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT fk_ticket_event FOREIGN KEY (event_id) REFERENCES events(id),
    CONSTRAINT fk_ticket_tier FOREIGN KEY (ticket_tier_id) REFERENCES ticket_tiers(id),
    CONSTRAINT fk_ticket_seat FOREIGN KEY (seat_id) REFERENCES seats(id),
    CONSTRAINT uq_event_seat UNIQUE (event_id, seat_id)
);

-- 9. Enterprise Indexes for High-Velocity Querying & Analytics
CREATE INDEX idx_events_start ON events(start_time);
CREATE INDEX idx_events_status ON events(status);
CREATE INDEX idx_bookings_event ON bookings(event_id);
CREATE INDEX idx_bookings_created ON bookings(created_at);
CREATE INDEX idx_tickets_event ON tickets(event_id);
CREATE INDEX idx_tickets_seat ON tickets(seat_id);
CREATE INDEX idx_seats_section ON seats(section_id);
