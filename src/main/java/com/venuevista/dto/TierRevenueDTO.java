package com.venuevista.dto;

import java.math.BigDecimal;

public class TierRevenueDTO {
    private String tierName;
    private Integer ticketsSold;
    private BigDecimal revenue;
    private Double percentageOfTotal;
    private String colorCode;

    public TierRevenueDTO() {
    }

    public TierRevenueDTO(String tierName, Integer ticketsSold, BigDecimal revenue, Double percentageOfTotal, String colorCode) {
        this.tierName = tierName;
        this.ticketsSold = ticketsSold;
        this.revenue = revenue;
        this.percentageOfTotal = percentageOfTotal;
        this.colorCode = colorCode;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public Integer getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Integer ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Double getPercentageOfTotal() {
        return percentageOfTotal;
    }

    public void setPercentageOfTotal(Double percentageOfTotal) {
        this.percentageOfTotal = percentageOfTotal;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
