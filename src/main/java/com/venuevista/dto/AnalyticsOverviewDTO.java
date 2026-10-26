package com.venuevista.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsOverviewDTO {
    private BigDecimal grossRevenue;
    private Long totalTicketsSold;
    private BigDecimal averageOrderValue;
    private Long totalEvents;
    private Long activeVenues;
    private Double venueOccupancyRate;
    private List<EventSalesDTO> topEvents = new ArrayList<>();
    private List<TierRevenueDTO> tierBreakdown = new ArrayList<>();
    private List<DailySalesDTO> recentDailySales = new ArrayList<>();

    public AnalyticsOverviewDTO() {
    }

    public BigDecimal getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(BigDecimal grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public Long getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(Long totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public Long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(Long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public Long getActiveVenues() {
        return activeVenues;
    }

    public void setActiveVenues(Long activeVenues) {
        this.activeVenues = activeVenues;
    }

    public Double getVenueOccupancyRate() {
        return venueOccupancyRate;
    }

    public void setVenueOccupancyRate(Double venueOccupancyRate) {
        this.venueOccupancyRate = venueOccupancyRate;
    }

    public List<EventSalesDTO> getTopEvents() {
        return topEvents;
    }

    public void setTopEvents(List<EventSalesDTO> topEvents) {
        this.topEvents = topEvents;
    }

    public List<TierRevenueDTO> getTierBreakdown() {
        return tierBreakdown;
    }

    public void setTierBreakdown(List<TierRevenueDTO> tierBreakdown) {
        this.tierBreakdown = tierBreakdown;
    }

    public List<DailySalesDTO> getRecentDailySales() {
        return recentDailySales;
    }

    public void setRecentDailySales(List<DailySalesDTO> recentDailySales) {
        this.recentDailySales = recentDailySales;
    }
}
