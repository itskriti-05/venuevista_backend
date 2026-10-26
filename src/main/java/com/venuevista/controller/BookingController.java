package com.venuevista.controller;

import com.venuevista.dto.BookingRequestDTO;
import com.venuevista.dto.BookingResponseDTO;
import com.venuevista.service.BookingService;
import com.venuevista.service.SeatLockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;
    private final SeatLockService seatLockService;

    public BookingController(BookingService bookingService, SeatLockService seatLockService) {
        this.bookingService = bookingService;
        this.seatLockService = seatLockService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequestDTO request) {
        try {
            BookingResponseDTO response = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{reference}")
    public ResponseEntity<BookingResponseDTO> getBookingByReference(@PathVariable String reference) {
        return bookingService.getBookingByReference(reference)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(bookingService.getBookingsByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<?> refundBooking(@PathVariable Long id) {
        boolean success = bookingService.cancelOrRefundBooking(id);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Booking refunded successfully", "bookingId", id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/tickets/{ticketNumber}/checkin")
    public ResponseEntity<?> checkInTicket(@PathVariable String ticketNumber) {
        boolean success = bookingService.checkInTicket(ticketNumber);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Ticket successfully verified and checked in", "ticketNumber", ticketNumber));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Ticket not found"));
    }

    @PostMapping("/seats/hold")
    public ResponseEntity<?> holdSeats(@RequestBody Map<String, Object> body) {
        Long eventId = Long.valueOf(body.get("eventId").toString());
        List<?> seatIdsRaw = (List<?>) body.get("seatIds");
        List<Long> seatIds = seatIdsRaw.stream().map(s -> Long.valueOf(s.toString())).toList();
        String holderId = body.getOrDefault("holderId", "guest").toString();

        boolean locked = seatLockService.holdSeats(eventId, seatIds, holderId);
        if (locked) {
            return ResponseEntity.ok(Map.of("status", "HELD", "message", "Seats reserved for 10 minutes", "seatIds", seatIds));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "One or more seats are currently held by another user"));
        }
    }

    @PostMapping("/seats/release")
    public ResponseEntity<?> releaseSeats(@RequestBody Map<String, Object> body) {
        Long eventId = Long.valueOf(body.get("eventId").toString());
        List<?> seatIdsRaw = (List<?>) body.get("seatIds");
        List<Long> seatIds = seatIdsRaw.stream().map(s -> Long.valueOf(s.toString())).toList();

        seatLockService.releaseHolds(eventId, seatIds);
        return ResponseEntity.ok(Map.of("status", "RELEASED", "seatIds", seatIds));
    }
}
