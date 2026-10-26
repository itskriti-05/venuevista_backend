package com.venuevista.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id", nullable = false)
    @JsonIgnoreProperties({"sections"})
    private Venue venue;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 220)
    private String slug;

    @Column(nullable = false, length = 50)
    private String category; // CONCERT, SPORTS, THEATER, CONFERENCE, COMEDY

    @Column(length = 100)
    private String genre;

    @Column(name = "performer_artist", length = 200)
    private String performerArtist;

    @Lob
    @Column
    private String description;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "doors_open")
    private LocalDateTime doorsOpen;

    @Column(length = 30)
    private String status = "PUBLISHED"; // DRAFT, PUBLISHED, SOLD_OUT, CANCELLED, COMPLETED

    @Column
    private Boolean featured = false;

    @Column(name = "total_revenue", precision = 15, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "tickets_sold")
    private Integer ticketsSold = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("event")
    private List<TicketTier> ticketTiers = new ArrayList<>();

    public Event() {
    }

    public Event(Venue venue, String title, String slug, String category, String genre, String performerArtist, String description, String bannerUrl, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime doorsOpen, Boolean featured) {
        this.venue = venue;
        this.title = title;
        this.slug = slug;
        this.category = category;
        this.genre = genre;
        this.performerArtist = performerArtist;
        this.description = description;
        this.bannerUrl = bannerUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doorsOpen = doorsOpen;
        this.featured = featured;
        this.status = "PUBLISHED";
        this.totalRevenue = BigDecimal.ZERO;
        this.ticketsSold = 0;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPerformerArtist() {
        return performerArtist;
    }

    public void setPerformerArtist(String performerArtist) {
        this.performerArtist = performerArtist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getDoorsOpen() {
        return doorsOpen;
    }

    public void setDoorsOpen(LocalDateTime doorsOpen) {
        this.doorsOpen = doorsOpen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Integer ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<TicketTier> getTicketTiers() {
        return ticketTiers;
    }

    public void setTicketTiers(List<TicketTier> ticketTiers) {
        this.ticketTiers = ticketTiers;
    }
}
