package com.venuevista.service;

import com.venuevista.dto.*;
import com.venuevista.model.*;
import com.venuevista.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final TicketRepository ticketRepository;
    private final TicketTierRepository ticketTierRepository;

    public AnalyticsService(BookingRepository bookingRepository,
                            EventRepository eventRepository,
                            VenueRepository venueRepository,
                            TicketRepository ticketRepository,
                            TicketTierRepository ticketTierRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.ticketRepository = ticketRepository;
        this.ticketTierRepository = ticketTierRepository;
    }

    public AnalyticsOverviewDTO getOverview() {
        AnalyticsOverviewDTO overview = new AnalyticsOverviewDTO();

        BigDecimal grossRevenue = bookingRepository.calculateGrossRevenue();
        Long totalTickets = bookingRepository.calculateTotalTicketsSold();
        long totalEvents = eventRepository.count();
        long totalVenues = venueRepository.count();
        List<Booking> allBookings = bookingRepository.findAll();

        overview.setGrossRevenue(grossRevenue != null ? grossRevenue : BigDecimal.ZERO);
        overview.setTotalTicketsSold(totalTickets != null ? totalTickets : 0L);
        overview.setTotalEvents(totalEvents);
        overview.setActiveVenues(totalVenues);

        if (!allBookings.isEmpty() && grossRevenue != null && grossRevenue.compareTo(BigDecimal.ZERO) > 0) {
            overview.setAverageOrderValue(grossRevenue.divide(BigDecimal.valueOf(allBookings.size()), 2, RoundingMode.HALF_UP));
        } else {
            overview.setAverageOrderValue(BigDecimal.ZERO);
        }

        // Calculate occupancy rate & top events
        List<Event> events = eventRepository.findAll();
        List<EventSalesDTO> eventSales = new ArrayList<>();
        long totalCapacitySum = 0;
        long totalSoldSum = 0;

        for (Event e : events) {
            int cap = e.getVenue() != null ? e.getVenue().getTotalCapacity() : 1000;
            int sold = e.getTicketsSold() != null ? e.getTicketsSold() : 0;
            totalCapacitySum += cap;
            totalSoldSum += sold;

            double occ = cap > 0 ? ((double) sold / cap) * 100.0 : 0.0;
            eventSales.add(new EventSalesDTO(
                    e.getId(),
                    e.getTitle(),
                    e.getCategory(),
                    e.getVenue() != null ? e.getVenue().getName() : "Main Arena",
                    sold,
                    cap,
                    Math.round(occ * 10.0) / 10.0,
                    e.getTotalRevenue() != null ? e.getTotalRevenue() : BigDecimal.ZERO
            ));
        }

        eventSales.sort(Comparator.comparing(EventSalesDTO::getRevenue).reversed());
        overview.setTopEvents(eventSales);

        double overallOccupancy = totalCapacitySum > 0 ? ((double) totalSoldSum / totalCapacitySum) * 100.0 : 0.0;
        overview.setVenueOccupancyRate(Math.round(overallOccupancy * 10.0) / 10.0);

        // Tier Breakdown
        List<Ticket> tickets = ticketRepository.findAll();
        Map<String, Integer> tierCount = new HashMap<>();
        Map<String, BigDecimal> tierRev = new HashMap<>();
        Map<String, String> tierColor = new HashMap<>();

        tierColor.put("VIP Diamond", "#EC4899");
        tierColor.put("Floor Premium", "#8B5CF6");
        tierColor.put("Lower Bowl Gold", "#F59E0B");
        tierColor.put("Balcony Standard", "#3B82F6");
        tierColor.put("Suites & Box", "#10B981");

        for (Ticket t : tickets) {
            String name = t.getTicketTier() != null ? t.getTicketTier().getTierName() : "General";
            tierCount.put(name, tierCount.getOrDefault(name, 0) + 1);
            tierRev.put(name, tierRev.getOrDefault(name, BigDecimal.ZERO).add(t.getPricePaid()));
            if (t.getTicketTier() != null && t.getTicketTier().getColorCode() != null) {
                tierColor.put(name, t.getTicketTier().getColorCode());
            }
        }

        List<TierRevenueDTO> tierDTOs = new ArrayList<>();
        BigDecimal totalTierRev = tierRev.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        for (Map.Entry<String, Integer> entry : tierCount.entrySet()) {
            String tName = entry.getKey();
            int count = entry.getValue();
            BigDecimal rev = tierRev.getOrDefault(tName, BigDecimal.ZERO);
            double pct = totalTierRev.compareTo(BigDecimal.ZERO) > 0
                    ? rev.divide(totalTierRev, 4, RoundingMode.HALF_UP).doubleValue() * 100.0
                    : 0.0;
            tierDTOs.add(new TierRevenueDTO(
                    tName,
                    count,
                    rev,
                    Math.round(pct * 10.0) / 10.0,
                    tierColor.getOrDefault(tName, "#6366F1")
            ));
        }
        tierDTOs.sort(Comparator.comparing(TierRevenueDTO::getRevenue).reversed());
        overview.setTierBreakdown(tierDTOs);

        // Recent 14-day sales trend
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
        Map<LocalDate, Integer> dayTickets = new TreeMap<>();
        Map<LocalDate, BigDecimal> dayRev = new TreeMap<>();
        LocalDate today = LocalDate.now();

        for (int i = 13; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            dayTickets.put(d, 0);
            dayRev.put(d, BigDecimal.ZERO);
        }

        for (Booking b : allBookings) {
            if (b.getCreatedAt() != null) {
                LocalDate bDate = b.getCreatedAt().toLocalDate();
                if (dayTickets.containsKey(bDate)) {
                    dayTickets.put(bDate, dayTickets.get(bDate) + b.getTotalTickets());
                    dayRev.put(bDate, dayRev.get(bDate).add(b.getTotalAmount()));
                }
            }
        }

        List<DailySalesDTO> dailyDTOs = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : dayTickets.entrySet()) {
            LocalDate d = entry.getKey();
            dailyDTOs.add(new DailySalesDTO(
                    d.format(dtf),
                    entry.getValue(),
                    dayRev.get(d)
            ));
        }
        overview.setRecentDailySales(dailyDTOs);

        return overview;
    }
}
