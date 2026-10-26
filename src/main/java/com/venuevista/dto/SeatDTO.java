package com.venuevista.dto;

import java.math.BigDecimal;

public class SeatDTO {
    private Long id;
    private Long sectionId;
    private String sectionName;
    private String rowLabel;
    private Integer seatNumber;
    private String seatLabel;
    private String seatType;
    private Double posX;
    private Double posY;
    private String status; // AVAILABLE, BOOKED, HELD, SELECTED
    private Long tierId;
    private String tierName;
    private BigDecimal price;

    public SeatDTO() {
    }

    public SeatDTO(Long id, Long sectionId, String sectionName, String rowLabel, Integer seatNumber, String seatLabel, String seatType, Double posX, Double posY, String status, Long tierId, String tierName, BigDecimal price) {
        this.id = id;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.rowLabel = rowLabel;
        this.seatNumber = seatNumber;
        this.seatLabel = seatLabel;
        this.seatType = seatType;
        this.posX = posX;
        this.posY = posY;
        this.status = status;
        this.tierId = tierId;
        this.tierName = tierName;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    public void setSeatLabel(String seatLabel) {
        this.seatLabel = seatLabel;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Double getPosX() {
        return posX;
    }

    public void setPosX(Double posX) {
        this.posX = posX;
    }

    public Double getPosY() {
        return posY;
    }

    public void setPosY(Double posY) {
        this.posY = posY;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
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
}
