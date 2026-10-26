package com.venuevista.service;

import com.venuevista.dto.*;
import com.venuevista.model.*;
import com.venuevista.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final SectionRepository sectionRepository;
    private final SeatRepository seatRepository;
    private final TicketTierRepository ticketTierRepository;
    private final TicketRepository ticketRepository;
    private final SeatLockService seatLockService;

    public EventService(EventRepository eventRepository,
                        VenueRepository venueRepository,
                        SectionRepository sectionRepository,
                        SeatRepository seatRepository,
                        TicketTierRepository ticketTierRepository,
                        TicketRepository ticketRepository,
                        SeatLockService seatLockService) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.sectionRepository = sectionRepository;
        this.seatRepository = seatRepository;
        this.ticketTierRepository = ticketTierRepository;
        this.ticketRepository = ticketRepository;
        this.seatLockService = seatLockService;
    }

    public List<Event> getAllEvents(String category, String query) {
        if (query != null && !query.trim().isEmpty()) {
            return eventRepository.searchEvents(query.trim());
        }
        if (category != null && !category.equalsIgnoreCase("ALL")) {
            return eventRepository.findByCategory(category.toUpperCase());
        }
        return eventRepository.findAll();
    }

    public List<Event> getFeaturedEvents() {
        return eventRepository.findByFeaturedTrue();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Optional<Event> getEventBySlug(String slug) {
        return eventRepository.findBySlug(slug);
    }

    public SeatingChartDTO getSeatingChart(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        Venue venue = event.getVenue();
        List<Section> sections = sectionRepository.findByVenueId(venue.getId());
        List<TicketTier> tiers = ticketTierRepository.findByEventId(eventId);

        // Map section ID to tier if assigned
        Map<Long, TicketTier> sectionTierMap = new HashMap<>();
        TicketTier fallbackTier = tiers.isEmpty() ? null : tiers.get(0);
        for (TicketTier tier : tiers) {
            if (tier.getSection() != null) {
                sectionTierMap.put(tier.getSection().getId(), tier);
            }
        }

        List<Long> bookedSeatIds = ticketRepository.findBookedSeatIdsByEventId(eventId);
        Set<Long> bookedSet = new HashSet<>(bookedSeatIds);
        Set<Long> heldSet = seatLockService.getHeldSeatIdsForEvent(eventId);

        int totalSeatsCount = 0;
        int availableSeatsCount = 0;
        int bookedSeatsCount = 0;
        int heldSeatsCount = 0;

        List<SectionSeatingDTO> sectionDTOs = new ArrayList<>();
        for (Section section : sections) {
            SectionSeatingDTO secDto = new SectionSeatingDTO(
                    section.getId(),
                    section.getName(),
                    section.getSectionCode(),
                    section.getTierCategory(),
                    section.getColorHex(),
                    section.getTotalRows(),
                    section.getSeatsPerRow(),
                    section.getCapacity(),
                    section.getSvgPathData(),
                    section.getElevationAngle()
            );

            TicketTier sectionTier = sectionTierMap.getOrDefault(section.getId(), fallbackTier);
            List<Seat> physicalSeats = seatRepository.findBySectionId(section.getId());
            List<SeatDTO> seatDTOs = new ArrayList<>();

            for (Seat seat : physicalSeats) {
                totalSeatsCount++;
                String status = "AVAILABLE";
                if (bookedSet.contains(seat.getId())) {
                    status = "BOOKED";
                    bookedSeatsCount++;
                } else if (heldSet.contains(seat.getId())) {
                    status = "HELD";
                    heldSeatsCount++;
                } else {
                    availableSeatsCount++;
                }

                SeatDTO sDto = new SeatDTO(
                        seat.getId(),
                        section.getId(),
                        section.getName(),
                        seat.getRowLabel(),
                        seat.getSeatNumber(),
                        seat.getSeatLabel(),
                        seat.getSeatType(),
                        seat.getPosX(),
                        seat.getPosY(),
                        status,
                        sectionTier != null ? sectionTier.getId() : null,
                        sectionTier != null ? sectionTier.getTierName() : "General",
                        sectionTier != null ? sectionTier.getPrice() : BigDecimal.valueOf(50.00)
                );
                seatDTOs.add(sDto);
            }
            secDto.setSeats(seatDTOs);
            sectionDTOs.add(secDto);
        }

        List<TicketTierDTO> tierDTOs = new ArrayList<>();
        for (TicketTier tier : tiers) {
            tierDTOs.add(new TicketTierDTO(
                    tier.getId(),
                    tier.getTierName(),
                    tier.getPrice(),
                    tier.getBookingFee(),
                    tier.getTaxRate(),
                    tier.getCapacity(),
                    tier.getAvailableSeats(),
                    tier.getColorCode(),
                    tier.getPerks(),
                    tier.getSection() != null ? tier.getSection().getId() : null,
                    tier.getSection() != null ? tier.getSection().getName() : "All Sections"
            ));
        }

        return new SeatingChartDTO(
                event.getId(),
                event.getTitle(),
                venue.getId(),
                venue.getName(),
                totalSeatsCount,
                availableSeatsCount,
                bookedSeatsCount,
                heldSeatsCount,
                sectionDTOs,
                tierDTOs
        );
    }

    @Transactional
    public Event createEvent(CreateEventDTO dto) {
        Venue venue = venueRepository.findById(dto.getVenueId())
                .orElseThrow(() -> new IllegalArgumentException("Venue not found with ID: " + dto.getVenueId()));

        String slug = dto.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-") + "-" + System.currentTimeMillis() % 10000;

        Event event = new Event(
                venue,
                dto.getTitle(),
                slug,
                dto.getCategory().toUpperCase(),
                dto.getGenre(),
                dto.getPerformerArtist(),
                dto.getDescription(),
                dto.getBannerUrl() != null && !dto.getBannerUrl().isEmpty() ? dto.getBannerUrl() : "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=1200&q=80",
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getStartTime().minusHours(1),
                dto.getFeatured() != null ? dto.getFeatured() : false
        );

        Event savedEvent = eventRepository.save(event);

        if (dto.getTiers() != null && !dto.getTiers().isEmpty()) {
            for (CreateEventDTO.TierConfigDTO tConfig : dto.getTiers()) {
                Section section = null;
                if (tConfig.getSectionId() != null) {
                    section = sectionRepository.findById(tConfig.getSectionId()).orElse(null);
                }
                int cap = section != null ? section.getCapacity() : venue.getTotalCapacity() / dto.getTiers().size();

                TicketTier tier = new TicketTier(
                        savedEvent,
                        section,
                        tConfig.getTierName(),
                        tConfig.getPrice() != null ? tConfig.getPrice() : BigDecimal.valueOf(99.00),
                        BigDecimal.valueOf(5.00),
                        BigDecimal.valueOf(0.08),
                        cap,
                        cap,
                        tConfig.getColorCode() != null ? tConfig.getColorCode() : "#6366F1",
                        tConfig.getPerks() != null ? tConfig.getPerks() : "Standard Event Admission"
                );
                ticketTierRepository.save(tier);
            }
        }

        return savedEvent;
    }
}
