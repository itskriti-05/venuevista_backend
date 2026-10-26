package com.venuevista.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id", nullable = false)
    @JsonIgnoreProperties("sections")
    private Venue venue;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "section_code", nullable = false, length = 50)
    private String sectionCode;

    @Column(name = "tier_category", nullable = false, length = 50)
    private String tierCategory; // VIP, PREMIUM, STANDARD, ACCESSIBLE, BOX

    @Column(name = "color_hex", length = 20)
    private String colorHex = "#6366F1";

    @Column(name = "total_rows", nullable = false)
    private Integer totalRows;

    @Column(name = "seats_per_row", nullable = false)
    private Integer seatsPerRow;

    @Column(nullable = false)
    private Integer capacity;

    @Lob
    @Column(name = "svg_path_data")
    private String svgPathData;

    @Column(name = "elevation_angle")
    private Double elevationAngle = 0.0;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("section")
    private List<Seat> seats = new ArrayList<>();

    public Section() {
    }

    public Section(Venue venue, String name, String sectionCode, String tierCategory, String colorHex, Integer totalRows, Integer seatsPerRow, Integer capacity, String svgPathData) {
        this.venue = venue;
        this.name = name;
        this.sectionCode = sectionCode;
        this.tierCategory = tierCategory;
        this.colorHex = colorHex;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        this.capacity = capacity;
        this.svgPathData = svgPathData;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getTierCategory() {
        return tierCategory;
    }

    public void setTierCategory(String tierCategory) {
        this.tierCategory = tierCategory;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(Integer seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getSvgPathData() {
        return svgPathData;
    }

    public void setSvgPathData(String svgPathData) {
        this.svgPathData = svgPathData;
    }

    public Double getElevationAngle() {
        return elevationAngle;
    }

    public void setElevationAngle(Double elevationAngle) {
        this.elevationAngle = elevationAngle;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
