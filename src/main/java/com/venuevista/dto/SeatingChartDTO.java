package com.venuevista.dto;

import java.util.ArrayList;
import java.util.List;

public class SeatingChartDTO {
    private Long eventId;
    private String eventTitle;
    private Long venueId;
    private String venueName;
    private Integer totalSeats;
    private Integer availableSeats;
    private Integer bookedSeats;
    private Integer heldSeats;
    private List<SectionSeatingDTO> sections = new ArrayList<>();
    private List<TicketTierDTO> tiers = new ArrayList<>();

    public SeatingChartDTO() {
    }

    public SeatingChartDTO(Long eventId, String eventTitle, Long venueId, String venueName, Integer totalSeats, Integer availableSeats, Integer bookedSeats, Integer heldSeats, List<SectionSeatingDTO> sections, List<TicketTierDTO> tiers) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.venueId = venueId;
        this.venueName = venueName;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.bookedSeats = bookedSeats;
        this.heldSeats = heldSeats;
        this.sections = sections;
        this.tiers = tiers;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Integer getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(Integer bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public Integer getHeldSeats() {
        return heldSeats;
    }

    public void setHeldSeats(Integer heldSeats) {
        this.heldSeats = heldSeats;
    }

    public List<SectionSeatingDTO> getSections() {
        return sections;
    }

    public void setSections(List<SectionSeatingDTO> sections) {
        this.sections = sections;
    }

    public List<TicketTierDTO> getTiers() {
        return tiers;
    }

    public void setTiers(List<TicketTierDTO> tiers) {
        this.tiers = tiers;
    }
}
