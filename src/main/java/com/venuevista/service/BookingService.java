package com.venuevista.service;

import com.venuevista.dto.*;
import com.venuevista.model.*;
import com.venuevista.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final TicketTierRepository ticketTierRepository;
    private final SeatLockService seatLockService;

    public BookingService(BookingRepository bookingRepository,
                          TicketRepository ticketRepository,
                          EventRepository eventRepository,
                          SeatRepository seatRepository,
                          TicketTierRepository ticketTierRepository,
                          SeatLockService seatLockService) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
        this.ticketTierRepository = ticketTierRepository;
        this.seatLockService = seatLockService;
    }

    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + request.getEventId()));

        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new IllegalArgumentException("One or more selected seats were not found.");
        }

        // Check if any seat is already booked for this event
        for (Seat seat : seats) {
            if (ticketRepository.existsByEventIdAndSeatId(event.getId(), seat.getId())) {
                throw new IllegalStateException("Seat " + seat.getSeatLabel() + " is already booked for this event.");
            }
        }

        // Match tiers for seats
        List<TicketTier> tiers = ticketTierRepository.findByEventId(event.getId());
        Map<Long, TicketTier> sectionTierMap = new HashMap<>();
        TicketTier defaultTier = tiers.isEmpty() ? null : tiers.get(0);
        for (TicketTier tier : tiers) {
            if (tier.getSection() != null) {
                sectionTierMap.put(tier.getSection().getId(), tier);
            }
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalFees = BigDecimal.ZERO;
        BigDecimal totalTaxes = BigDecimal.ZERO;

        for (Seat seat : seats) {
            TicketTier tier = sectionTierMap.getOrDefault(seat.getSection().getId(), defaultTier);
            BigDecimal price = (tier != null) ? tier.getPrice() : BigDecimal.valueOf(50.00);
            BigDecimal fee = (tier != null && tier.getBookingFee() != null) ? tier.getBookingFee() : BigDecimal.valueOf(5.00);
            BigDecimal taxRate = (tier != null && tier.getTaxRate() != null) ? tier.getTaxRate() : BigDecimal.valueOf(0.08);

            subtotal = subtotal.add(price);
            totalFees = totalFees.add(fee);
            totalTaxes = totalTaxes.add(price.multiply(taxRate).setScale(2, RoundingMode.HALF_UP));
        }

        BigDecimal totalAmount = subtotal.add(totalFees).add(totalTaxes);

        String bookingRef = "VV-" + LocalDateTime.now().getYear() + "-" + (100000 + new Random().nextInt(900000));
        String txnId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Booking booking = new Booking(
                bookingRef,
                event,
                request.getCustomerName(),
                request.getCustomerEmail(),
                request.getCustomerPhone(),
                seats.size(),
                subtotal,
                totalFees,
                totalTaxes,
                totalAmount,
                "CONFIRMED",
                request.getPaymentMethod(),
                txnId
        );

        Booking savedBooking = bookingRepository.save(booking);

        List<Ticket> savedTickets = new ArrayList<>();
        for (Seat seat : seats) {
            TicketTier tier = sectionTierMap.getOrDefault(seat.getSection().getId(), defaultTier);
            BigDecimal price = (tier != null) ? tier.getPrice() : BigDecimal.valueOf(50.00);
            String ticketNumber = "TKT-" + bookingRef.substring(8) + "-" + seat.getSeatLabel().replace("-", "");
            String qrData = "VENUEVISTA:" + bookingRef + ":" + ticketNumber + ":" + event.getId() + ":" + seat.getSeatLabel();

            Ticket ticket = new Ticket(
                    savedBooking,
                    event,
                    tier,
                    seat,
                    ticketNumber,
                    qrData,
                    seat.getSeatLabel(),
                    seat.getSection().getName(),
                    price
            );
            savedTickets.add(ticketRepository.save(ticket));

            // Reduce tier available seats
            if (tier != null && tier.getAvailableSeats() > 0) {
                tier.setAvailableSeats(tier.getAvailableSeats() - 1);
                ticketTierRepository.save(tier);
            }
        }
        savedBooking.setTickets(savedTickets);

        // Update event statistics
        event.setTicketsSold(event.getTicketsSold() + seats.size());
        event.setTotalRevenue(event.getTotalRevenue().add(totalAmount));
        eventRepository.save(event);

        // Release holds
        seatLockService.releaseHolds(event.getId(), request.getSeatIds());

        return mapToBookingResponse(savedBooking);
    }

    @Transactional(readOnly = true)
    public Optional<BookingResponseDTO> getBookingByReference(String reference) {
        return bookingRepository.findByBookingReference(reference)
                .map(this::mapToBookingResponse);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByEmail(String email) {
        return bookingRepository.findByCustomerEmailIgnoreCaseOrderByCreatedAtDesc(email)
                .stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponseDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public boolean checkInTicket(String ticketNumber) {
        Optional<Ticket> ticketOpt = ticketRepository.findByTicketNumber(ticketNumber);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            ticket.setCheckInStatus(true);
            ticket.setCheckedInAt(LocalDateTime.now());
            ticketRepository.save(ticket);
            return true;
        }
        return false;
    }

    public boolean cancelOrRefundBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setPaymentStatus("REFUNDED");
            bookingRepository.save(booking);

            // Re-adjust event numbers
            Event event = booking.getEvent();
            if (event != null) {
                event.setTicketsSold(Math.max(0, event.getTicketsSold() - booking.getTotalTickets()));
                event.setTotalRevenue(event.getTotalRevenue().subtract(booking.getTotalAmount()));
                eventRepository.save(event);
            }
            return true;
        }
        return false;
    }

    private BookingResponseDTO mapToBookingResponse(Booking b) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(b.getId());
        dto.setBookingReference(b.getBookingReference());
        dto.setEventId(b.getEvent().getId());
        dto.setEventTitle(b.getEvent().getTitle());
        dto.setEventBannerUrl(b.getEvent().getBannerUrl());
        dto.setEventStartTime(b.getEvent().getStartTime());
        dto.setVenueName(b.getEvent().getVenue().getName());
        dto.setCustomerName(b.getCustomerName());
        dto.setCustomerEmail(b.getCustomerEmail());
        dto.setCustomerPhone(b.getCustomerPhone());
        dto.setTotalTickets(b.getTotalTickets());
        dto.setSubtotalAmount(b.getSubtotalAmount());
        dto.setFeesAmount(b.getFeesAmount());
        dto.setTaxesAmount(b.getTaxesAmount());
        dto.setTotalAmount(b.getTotalAmount());
        dto.setPaymentStatus(b.getPaymentStatus());
        dto.setPaymentMethod(b.getPaymentMethod());
        dto.setTransactionId(b.getTransactionId());
        dto.setCreatedAt(b.getCreatedAt());

        List<TicketDTO> tDtos = b.getTickets().stream().map(t -> new TicketDTO(
                t.getId(),
                t.getTicketNumber(),
                t.getQrCodeData(),
                t.getSeatLabel(),
                t.getSectionName(),
                t.getTicketTier() != null ? t.getTicketTier().getTierName() : "General",
                t.getPricePaid(),
                t.getCheckInStatus(),
                t.getCheckedInAt()
        )).collect(Collectors.toList());
        dto.setTickets(tDtos);

        return dto;
    }
}
