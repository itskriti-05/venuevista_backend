package com.venuevista.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonIgnoreProperties("tickets")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnoreProperties({"ticketTiers", "venue"})
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_tier_id", nullable = false)
    @JsonIgnoreProperties({"event", "section"})
    private TicketTier ticketTier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_id", nullable = false)
    @JsonIgnoreProperties("section")
    private Seat seat;

    @Column(name = "ticket_number", nullable = false, unique = true, length = 60)
    private String ticketNumber;

    @Column(name = "qr_code_data", nullable = false)
    private String qrCodeData;

    @Column(name = "seat_label", nullable = false, length = 50)
    private String seatLabel;

    @Column(name = "section_name", nullable = false, length = 100)
    private String sectionName;

    @Column(name = "price_paid", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePaid;

    @Column(name = "check_in_status")
    private Boolean checkInStatus = false;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Ticket() {
    }

    public Ticket(Booking booking, Event event, TicketTier ticketTier, Seat seat, String ticketNumber, String qrCodeData, String seatLabel, String sectionName, BigDecimal pricePaid) {
        this.booking = booking;
        this.event = event;
        this.ticketTier = ticketTier;
        this.seat = seat;
        this.ticketNumber = ticketNumber;
        this.qrCodeData = qrCodeData;
        this.seatLabel = seatLabel;
        this.sectionName = sectionName;
        this.pricePaid = pricePaid;
        this.checkInStatus = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public TicketTier getTicketTier() {
        return ticketTier;
    }

    public void setTicketTier(TicketTier ticketTier) {
        this.ticketTier = ticketTier;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    public void setSeatLabel(String seatLabel) {
        this.seatLabel = seatLabel;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public BigDecimal getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(BigDecimal pricePaid) {
        this.pricePaid = pricePaid;
    }

    public Boolean getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(Boolean checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
