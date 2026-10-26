package com.venuevista.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventDTO {

    @NotNull(message = "Venue ID is required")
    private Long venueId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    private String genre;
    private String performerArtist;
    private String description;
    private String bannerUrl;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private Boolean featured = false;

    private List<TierConfigDTO> tiers = new ArrayList<>();

    public static class TierConfigDTO {
        private String tierName;
        private Long sectionId;
        private BigDecimal price;
        private String colorCode;
        private String perks;

        public TierConfigDTO() {}

        public String getTierName() { return tierName; }
        public void setTierName(String tierName) { this.tierName = tierName; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public String getColorCode() { return colorCode; }
        public void setColorCode(String colorCode) { this.colorCode = colorCode; }
        public String getPerks() { return perks; }
        public void setPerks(String perks) { this.perks = perks; }
    }

    public CreateEventDTO() {}

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getPerformerArtist() { return performerArtist; }
    public void setPerformerArtist(String performerArtist) { this.performerArtist = performerArtist; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }
    public List<TierConfigDTO> getTiers() { return tiers; }
    public void setTiers(List<TierConfigDTO> tiers) { this.tiers = tiers; }
}
