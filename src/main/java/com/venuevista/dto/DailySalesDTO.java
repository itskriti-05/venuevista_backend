package com.venuevista.dto;

import java.math.BigDecimal;

public class DailySalesDTO {
    private String date; // YYYY-MM-DD
    private Integer ticketsSold;
    private BigDecimal revenue;

    public DailySalesDTO() {
    }

    public DailySalesDTO(String date, Integer ticketsSold, BigDecimal revenue) {
        this.date = date;
        this.ticketsSold = ticketsSold;
        this.revenue = revenue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
