package com.venuevista.repository;

import com.venuevista.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTicketNumber(String ticketNumber);
    List<Ticket> findByBookingId(Long bookingId);
    List<Ticket> findByEventId(Long eventId);

    @Query("SELECT t.seat.id FROM Ticket t WHERE t.event.id = :eventId")
    List<Long> findBookedSeatIdsByEventId(Long eventId);

    boolean existsByEventIdAndSeatId(Long eventId, Long seatId);
}
