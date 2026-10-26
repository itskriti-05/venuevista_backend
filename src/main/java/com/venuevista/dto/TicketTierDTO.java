package com.venuevista.dto;

import java.math.BigDecimal;

public class TicketTierDTO {
    private Long id;
    private String tierName;
    private BigDecimal price;
    private BigDecimal bookingFee;
    private BigDecimal taxRate;
    private Integer capacity;
    private Integer availableSeats;
    private String colorCode;
    private String perks;
    private Long sectionId;
    private String sectionName;

    public TicketTierDTO() {
    }

    public TicketTierDTO(Long id, String tierName, BigDecimal price, BigDecimal bookingFee, BigDecimal taxRate, Integer capacity, Integer availableSeats, String colorCode, String perks, Long sectionId, String sectionName) {
        this.id = id;
        this.tierName = tierName;
        this.price = price;
        this.bookingFee = bookingFee;
        this.taxRate = taxRate;
        this.capacity = capacity;
        this.availableSeats = availableSeats;
        this.colorCode = colorCode;
        this.perks = perks;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getBookingFee() {
        return bookingFee;
    }

    public void setBookingFee(BigDecimal bookingFee) {
        this.bookingFee = bookingFee;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getPerks() {
        return perks;
    }

    public void setPerks(String perks) {
        this.perks = perks;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
