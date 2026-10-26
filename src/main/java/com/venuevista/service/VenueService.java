package com.venuevista.service;

import com.venuevista.model.Venue;
import com.venuevista.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Optional<Venue> getVenueById(Long id) {
        return venueRepository.findById(id);
    }

    public Optional<Venue> getVenueByCode(String code) {
        return venueRepository.findByCode(code);
    }

    @Transactional
    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }
}
