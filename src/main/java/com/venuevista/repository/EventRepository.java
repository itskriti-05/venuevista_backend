package com.venuevista.repository;

import com.venuevista.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findBySlug(String slug);
    List<Event> findByStatus(String status);
    List<Event> findByCategory(String category);
    List<Event> findByFeaturedTrue();

    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(e.performerArtist) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(e.category) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Event> searchEvents(String query);
}
