package com.venuevista.dto;

import java.util.ArrayList;
import java.util.List;

public class SectionSeatingDTO {
    private Long id;
    private String name;
    private String sectionCode;
    private String tierCategory;
    private String colorHex;
    private Integer totalRows;
    private Integer seatsPerRow;
    private Integer capacity;
    private String svgPathData;
    private Double elevationAngle;
    private List<SeatDTO> seats = new ArrayList<>();

    public SectionSeatingDTO() {
    }

    public SectionSeatingDTO(Long id, String name, String sectionCode, String tierCategory, String colorHex, Integer totalRows, Integer seatsPerRow, Integer capacity, String svgPathData, Double elevationAngle) {
        this.id = id;
        this.name = name;
        this.sectionCode = sectionCode;
        this.tierCategory = tierCategory;
        this.colorHex = colorHex;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        this.capacity = capacity;
        this.svgPathData = svgPathData;
        this.elevationAngle = elevationAngle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }
}
