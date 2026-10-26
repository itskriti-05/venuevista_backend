package com.venuevista.repository;

import com.venuevista.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingReference(String bookingReference);
    List<Booking> findByEventIdOrderByCreatedAtDesc(Long eventId);
    List<Booking> findByCustomerEmailIgnoreCaseOrderByCreatedAtDesc(String email);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.paymentStatus = 'CONFIRMED'")
    BigDecimal calculateGrossRevenue();

    @Query("SELECT COALESCE(SUM(b.totalTickets), 0) FROM Booking b WHERE b.paymentStatus = 'CONFIRMED'")
    Long calculateTotalTicketsSold();
}
