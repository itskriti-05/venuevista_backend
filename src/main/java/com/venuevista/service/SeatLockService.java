package com.venuevista.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SeatLockService {

    private static final long HOLD_DURATION_SECONDS = 600; // 10 minutes lock

    private static class SeatHold {
        final Long eventId;
        final Long seatId;
        final String holderId;
        final Instant expiresAt;

        SeatHold(Long eventId, Long seatId, String holderId, Instant expiresAt) {
            this.eventId = eventId;
            this.seatId = seatId;
            this.holderId = holderId;
            this.expiresAt = expiresAt;
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }

    // Key: eventId + ":" + seatId
    private final Map<String, SeatHold> activeHolds = new ConcurrentHashMap<>();

    private String key(Long eventId, Long seatId) {
        return eventId + ":" + seatId;
    }

    public synchronized boolean holdSeats(Long eventId, List<Long> seatIds, String holderId) {
        Instant now = Instant.now();
        // Check if any seat is already held by someone else
        for (Long seatId : seatIds) {
            SeatHold existing = activeHolds.get(key(eventId, seatId));
            if (existing != null && !existing.isExpired() && !existing.holderId.equals(holderId)) {
                return false; // Seat conflict
            }
        }

        // Apply holds
        Instant expiry = now.plusSeconds(HOLD_DURATION_SECONDS);
        for (Long seatId : seatIds) {
            activeHolds.put(key(eventId, seatId), new SeatHold(eventId, seatId, holderId, expiry));
        }
        return true;
    }

    public boolean isSeatHeld(Long eventId, Long seatId) {
        SeatHold hold = activeHolds.get(key(eventId, seatId));
        return hold != null && !hold.isExpired();
    }

    public synchronized void releaseHolds(Long eventId, List<Long> seatIds) {
        for (Long seatId : seatIds) {
            activeHolds.remove(key(eventId, seatId));
        }
    }

    public Set<Long> getHeldSeatIdsForEvent(Long eventId) {
        Set<Long> set = new HashSet<>();
        for (SeatHold hold : activeHolds.values()) {
            if (hold.eventId.equals(eventId) && !hold.isExpired()) {
                set.add(hold.seatId);
            }
        }
        return set;
    }

    @Scheduled(fixedRate = 30000) // every 30 seconds
    public void cleanupExpiredHolds() {
        activeHolds.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}
