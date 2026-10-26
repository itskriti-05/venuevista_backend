package com.venuevista.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketDTO {
    private Long id;
    private String ticketNumber;
    private String qrCodeData;
    private String seatLabel;
    private String sectionName;
    private String tierName;
    private BigDecimal pricePaid;
    private Boolean checkInStatus;
    private LocalDateTime checkedInAt;

    public TicketDTO() {
    }

    public TicketDTO(Long id, String ticketNumber, String qrCodeData, String seatLabel, String sectionName, String tierName, BigDecimal pricePaid, Boolean checkInStatus, LocalDateTime checkedInAt) {
        this.id = id;
        this.ticketNumber = ticketNumber;
        this.qrCodeData = qrCodeData;
        this.seatLabel = seatLabel;
        this.sectionName = sectionName;
        this.tierName = tierName;
        this.pricePaid = pricePaid;
        this.checkInStatus = checkInStatus;
        this.checkedInAt = checkedInAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
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
}
