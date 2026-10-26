package com.venuevista.dto;

import java.math.BigDecimal;

public class EventSalesDTO {
    private Long eventId;
    private String title;
    private String category;
    private String venueName;
    private Integer ticketsSold;
    private Integer totalCapacity;
    private Double occupancyPercentage;
    private BigDecimal revenue;

    public EventSalesDTO() {
    }

    public EventSalesDTO(Long eventId, String title, String category, String venueName, Integer ticketsSold, Integer totalCapacity, Double occupancyPercentage, BigDecimal revenue) {
        this.eventId = eventId;
        this.title = title;
        this.category = category;
        this.venueName = venueName;
        this.ticketsSold = ticketsSold;
        this.totalCapacity = totalCapacity;
        this.occupancyPercentage = occupancyPercentage;
        this.revenue = revenue;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Integer getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Integer ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Double getOccupancyPercentage() {
        return occupancyPercentage;
    }

    public void setOccupancyPercentage(Double occupancyPercentage) {
        this.occupancyPercentage = occupancyPercentage;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
