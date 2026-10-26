package com.venuevista.config;

import com.venuevista.model.*;
import com.venuevista.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final VenueRepository venueRepository;
    private final SectionRepository sectionRepository;
    private final SeatRepository seatRepository;
    private final EventRepository eventRepository;
    private final TicketTierRepository ticketTierRepository;
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    public DataInitializer(VenueRepository venueRepository,
                           SectionRepository sectionRepository,
                           SeatRepository seatRepository,
                           EventRepository eventRepository,
                           TicketTierRepository ticketTierRepository,
                           BookingRepository bookingRepository,
                           TicketRepository ticketRepository) {
        this.venueRepository = venueRepository;
        this.sectionRepository = sectionRepository;
        this.seatRepository = seatRepository;
        this.eventRepository = eventRepository;
        this.ticketTierRepository = ticketTierRepository;
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void run(String... args) {
        if (venueRepository.count() > 0) {
            log.info("VenueVista database already seeded. Skipping initial data seeding.");
            return;
        }

        log.info("🚀 Seeding VenueVista with enterprise venue layouts, events, seating charts, and sales history...");

        // 1. Create Venues
        Venue arena = new Venue(
                "Apex Arena & Center",
                "APEX-LA",
                "ARENA",
                "1111 S Figueroa St",
                "Los Angeles",
                "CA",
                "United States",
                "90015",
                18500,
                "https://images.unsplash.com/photo-1540039155733-5bb30b53aa14?auto=format&fit=crop&w=1200&q=80",
                "Premier multi-purpose entertainment arena featuring cutting-edge acoustic engineering, 360-degree LED canopy, and luxury box suites."
        );

        Venue symphony = new Venue(
                "Starlight Symphony Hall",
                "STARLIGHT-NYC",
                "CONCERT_HALL",
                "10 Lincoln Center Plaza",
                "New York",
                "NY",
                "United States",
                "10023",
                2800,
                "https://images.unsplash.com/photo-1514306191717-452ec28c7814?auto=format&fit=crop&w=1200&q=80",
                "A world-renowned acoustic masterpiece designed with warm cherrywood tiers, pristine sound baffles, and royal balcony viewing."
        );

        Venue amphitheater = new Venue(
                "Grand Horizon Amphitheater",
                "HORIZON-CO",
                "AMPHITHEATER",
                "18300 W Alameda Pkwy",
                "Morrison",
                "CO",
                "United States",
                "80465",
                8500,
                "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=1200&q=80",
                "Iconic red-rock natural open air amphitheater providing breathtaking panoramic sunset vistas and legendary festival experiences."
        );

        Venue theater = new Venue(
                "Metropolis Blackbox Theater",
                "METRO-CHI",
                "THEATER",
                "180 N Michigan Ave",
                "Chicago",
                "IL",
                "United States",
                "60601",
                850,
                "https://images.unsplash.com/photo-1507676184212-d03ab07a01bf?auto=format&fit=crop&w=1200&q=80",
                "Intimate urban theater featuring immersive proscenium staging, plush velvet seating, and state-of-the-art Dolby Atmos spatial audio."
        );

        venueRepository.saveAll(List.of(arena, symphony, amphitheater, theater));

        // 2. Generate Sections and Physical Seats for Apex Arena
        List<Section> arenaSections = createSectionsAndSeatsForArena(arena);
        List<Section> symphonySections = createSectionsAndSeatsForSymphony(symphony);
        createSectionsAndSeatsForAmphitheater(amphitheater);
        createSectionsAndSeatsForTheater(theater);

        // 3. Create Events
        LocalDateTime now = LocalDateTime.now();

        Event event1 = new Event(
                arena,
                "Taylor Swift: The Eras Tour Experience",
                "taylor-swift-eras-tour",
                "CONCERT",
                "Pop / Stadium Anthem",
                "Taylor Swift",
                "Experience the cultural phenomenon live in Los Angeles. Over 3 hours spanning every iconic musical era with ground-breaking stage production and immersive visuals.",
                "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?auto=format&fit=crop&w=1200&q=80",
                now.plusDays(14).withHour(20).withMinute(0),
                now.plusDays(14).withHour(23).withMinute(30),
                now.plusDays(14).withHour(18).withMinute(0),
                true
        );

        Event event2 = new Event(
                symphony,
                "Hans Zimmer Live: Cinematic Odyssey",
                "hans-zimmer-cinematic-odyssey",
                "CONCERT",
                "Orchestral / Film Score",
                "Hans Zimmer & The Royal Philharmonic",
                "A monumental sensory journey through legendary film scores including Interstellar, Inception, Dune, Gladiator, and The Dark Knight with live choir and rock ensemble.",
                "https://images.unsplash.com/photo-1465847899084-d164df4dedc6?auto=format&fit=crop&w=1200&q=80",
                now.plusDays(21).withHour(19).withMinute(30),
                now.plusDays(21).withHour(22).withMinute(30),
                now.plusDays(21).withHour(18).withMinute(30),
                true
        );

        Event event3 = new Event(
                arena,
                "NBA Championship Finals: Game 7",
                "nba-finals-2026-game-7",
                "SPORTS",
                "Basketball",
                "Lakers vs Celtics",
                "The ultimate clash of titans. Historic rivalry meets the highest stakes in professional basketball for the Larry O'Brien Championship Trophy.",
                "https://images.unsplash.com/photo-1504450758481-7338eba7524a?auto=format&fit=crop&w=1200&q=80",
                now.plusDays(28).withHour(18).withMinute(0),
                now.plusDays(28).withHour(21).withMinute(30),
                now.plusDays(28).withHour(16).withMinute(30),
                true
        );

        Event event4 = new Event(
                theater,
                "Cirque du Soleil: Celestial Dreams",
                "cirque-du-soleil-celestial-dreams",
                "THEATER",
                "Acrobatic / Avant-Garde",
                "Cirque du Soleil Troupe",
                "Defying gravity and imagination, Celestial Dreams weaves astonishing aerial acrobatics, whimsical storytelling, and mesmerizing stagecraft into an unforgettable night.",
                "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?auto=format&fit=crop&w=1200&q=80",
                now.plusDays(9).withHour(19).withMinute(0),
                now.plusDays(9).withHour(21).withMinute(30),
                now.plusDays(9).withHour(18).withMinute(0),
                false
        );

        Event event5 = new Event(
                amphitheater,
                "Coldplay: Music of the Spheres World Tour",
                "coldplay-music-of-the-spheres",
                "CONCERT",
                "Alternative Rock / Pop",
                "Coldplay",
                "Under the star-lit Colorado sky, join Coldplay for an eco-powered kaleidoscope of lasers, confetti cannons, and glowing LED wristbands celebrating universal love.",
                "https://images.unsplash.com/photo-1429962714451-bb934ecdc4ec?auto=format&fit=crop&w=1200&q=80",
                now.plusDays(35).withHour(19).withMinute(30),
                now.plusDays(35).withHour(23).withMinute(0),
                now.plusDays(35).withHour(17).withMinute(30),
                true
        );

        Event event6 = new Event(
                theater,
                "Global AI & Media Innovation Summit 2026",
                "global-ai-media-summit-2026",
                "CONFERENCE",
                "Technology & Media",
                "Industry Visionaries & Tech Leaders",
                "The premier conference convening world leaders in artificial intelligence, autonomous media, interactive venue architectures, and digital ticketing.",
                "https://images.unsplash.com/photo-1511578314322-379afb476865?auto=format&fit=crop&w=1200&q=80",
                now.plusDays(42).withHour(9).withMinute(0),
                now.plusDays(42).withHour(17).withMinute(30),
                now.plusDays(42).withHour(8).withMinute(0),
                false
        );

        eventRepository.saveAll(List.of(event1, event2, event3, event4, event5, event6));

        // 4. Create Ticket Tiers
        createTiersForEvent(event1, arenaSections);
        createTiersForEvent(event2, symphonySections);
        createTiersForEvent(event3, arenaSections);

        // 5. Seed Realistic Historical Bookings & Sold Tickets
        seedSampleBookings(event1, arenaSections);
        seedSampleBookings(event2, symphonySections);

        log.info(" VenueVista data seeding successfully completed! 4 Venues, 6 Events, Complete Seating Maps & Bookings ready.");
    }

    private List<Section> createSectionsAndSeatsForArena(Venue venue) {
        List<Section> sections = new ArrayList<>();

        // Section 1: VIP Diamond Floor (close to stage)
        Section floorVip = new Section(
                venue,
                "VIP Diamond Floor",
                "SEC-FLR-VIP",
                "VIP",
                "#EC4899",
                4,
                10,
                40,
                "M 250 180 L 750 180 L 710 320 L 290 320 Z"
        );

        // Section 2: Lower Bowl Center
        Section lowerCenter = new Section(
                venue,
                "Lower Bowl Center",
                "SEC-LWR-101",
                "PREMIUM",
                "#8B5CF6",
                5,
                12,
                60,
                "M 220 340 L 780 340 L 820 480 L 180 480 Z"
        );

        // Section 3: Upper Balcony
        Section balcony = new Section(
                venue,
                "Balcony Tier 200",
                "SEC-BALC-200",
                "STANDARD",
                "#3B82F6",
                4,
                14,
                56,
                "M 150 500 L 850 500 L 890 620 L 110 620 Z"
        );

        // Section 4: Luxury Box Suites
        Section suites = new Section(
                venue,
                "Skybox Executive Suites",
                "SEC-SKY-BOX",
                "BOX",
                "#10B981",
                2,
                8,
                16,
                "M 80 200 L 210 200 L 210 320 L 80 320 Z"
        );

        sections.add(sectionRepository.save(floorVip));
        sections.add(sectionRepository.save(lowerCenter));
        sections.add(sectionRepository.save(balcony));
        sections.add(sectionRepository.save(suites));

        // Generate individual seats
        generateSeatsForSection(floorVip, 4, 10, 310, 210, 42, 28, "VIP");
        generateSeatsForSection(lowerCenter, 5, 12, 250, 360, 46, 25, "PREMIUM");
        generateSeatsForSection(balcony, 4, 14, 210, 520, 44, 26, "STANDARD");
        generateSeatsForSection(suites, 2, 8, 100, 230, 45, 38, "BOX");

        return sections;
    }

    private List<Section> createSectionsAndSeatsForSymphony(Venue venue) {
        List<Section> sections = new ArrayList<>();

        Section orchestra = new Section(
                venue,
                "Orchestra Front",
                "SEC-SYM-ORCH",
                "PREMIUM",
                "#F59E0B",
                5,
                10,
                50,
                "M 260 200 L 740 200 L 700 360 L 300 360 Z"
        );

        Section mezzanine = new Section(
                venue,
                "Mezzanine Royal Tier",
                "SEC-SYM-MEZZ",
                "VIP",
                "#EC4899",
                4,
                12,
                48,
                "M 200 380 L 800 380 L 840 500 L 160 500 Z"
        );

        Section gallery = new Section(
                venue,
                "Grand Gallery",
                "SEC-SYM-GALL",
                "STANDARD",
                "#3B82F6",
                4,
                12,
                48,
                "M 160 520 L 840 520 L 880 630 L 120 630 Z"
        );

        sections.add(sectionRepository.save(orchestra));
        sections.add(sectionRepository.save(mezzanine));
        sections.add(sectionRepository.save(gallery));

        generateSeatsForSection(orchestra, 5, 10, 290, 230, 44, 26, "PREMIUM");
        generateSeatsForSection(mezzanine, 4, 12, 240, 400, 46, 28, "VIP");
        generateSeatsForSection(gallery, 4, 12, 240, 540, 46, 26, "STANDARD");

        return sections;
    }

    private void createSectionsAndSeatsForAmphitheater(Venue venue) {
        Section reservedBowl = new Section(
                venue,
                "Reserved Front Bowl",
                "SEC-AMP-RES",
                "PREMIUM",
                "#8B5CF6",
                4,
                12,
                48,
                ""
        );
        Section generalLawn = new Section(
                venue,
                "Red Rock Lawn General",
                "SEC-AMP-LAWN",
                "STANDARD",
                "#10B981",
                4,
                14,
                56,
                ""
        );
        sectionRepository.save(reservedBowl);
        sectionRepository.save(generalLawn);
        generateSeatsForSection(reservedBowl, 4, 12, 240, 240, 45, 28, "PREMIUM");
        generateSeatsForSection(generalLawn, 4, 14, 200, 400, 45, 28, "STANDARD");
    }

    private void createSectionsAndSeatsForTheater(Venue venue) {
        Section mainStalls = new Section(
                venue,
                "Main Stage Stalls",
                "SEC-THTR-STALLS",
                "VIP",
                "#EC4899",
                4,
                10,
                40,
                ""
        );
        Section circle = new Section(
                venue,
                "Dress Circle",
                "SEC-THTR-CIRC",
                "STANDARD",
                "#3B82F6",
                4,
                10,
                40,
                ""
        );
        sectionRepository.save(mainStalls);
        sectionRepository.save(circle);
        generateSeatsForSection(mainStalls, 4, 10, 280, 240, 48, 30, "VIP");
        generateSeatsForSection(circle, 4, 10, 280, 400, 48, 30, "STANDARD");
    }

    private void generateSeatsForSection(Section section, int rows, int seatsPerRow, double startX, double startY, double stepX, double stepY, String seatType) {
        List<Seat> seats = new ArrayList<>();
        char[] rowLabels = "ABCDEFGHJKLM".toCharArray();

        for (int r = 0; r < rows; r++) {
            String rowLabel = String.valueOf(rowLabels[r % rowLabels.length]);
            for (int s = 1; s <= seatsPerRow; s++) {
                String seatLabel = rowLabel + "-" + (s < 10 ? "0" + s : s);
                double x = startX + (s - 1) * stepX;
                double y = startY + r * stepY;

                String type = seatType;
                if (r == rows - 1 && (s == 1 || s == seatsPerRow)) {
                    type = "ACCESSIBLE";
                }

                Seat seat = new Seat(section, rowLabel, s, seatLabel, type, x, y);
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
    }

    private void createTiersForEvent(Event event, List<Section> sections) {
        List<TicketTier> tiers = new ArrayList<>();

        if (event.getSlug().contains("taylor-swift")) {
            tiers.add(new TicketTier(event, sections.get(0), "Diamond VIP Floor Pass", BigDecimal.valueOf(450.00), BigDecimal.valueOf(15.00), BigDecimal.valueOf(0.08), 40, 40, "#EC4899", "Early venue admission, VIP commemorative lanyard, exclusive merchandise pack, crowd-free bar"));
            tiers.add(new TicketTier(event, sections.get(1), "Lower Bowl Platinum", BigDecimal.valueOf(225.00), BigDecimal.valueOf(10.00), BigDecimal.valueOf(0.08), 60, 60, "#8B5CF6", "Prime lower tier sightline, padded seating, fast-track entry gate"));
            tiers.add(new TicketTier(event, sections.get(2), "Balcony Center Reserved", BigDecimal.valueOf(110.00), BigDecimal.valueOf(8.00), BigDecimal.valueOf(0.08), 56, 56, "#3B82F6", "Elevated panoramic arena vista, stadium sound immersion"));
            tiers.add(new TicketTier(event, sections.get(3), "Executive Skybox Suite", BigDecimal.valueOf(650.00), BigDecimal.valueOf(20.00), BigDecimal.valueOf(0.08), 16, 16, "#10B981", "Private luxury suite, champagne service, artisan catering, dedicated hostess"));
        } else if (event.getSlug().contains("hans-zimmer")) {
            tiers.add(new TicketTier(event, sections.get(0), "Premier Orchestra Tier", BigDecimal.valueOf(185.00), BigDecimal.valueOf(9.00), BigDecimal.valueOf(0.08), 50, 50, "#F59E0B", "Front-row acoustic resonance, direct sightline to Hans Zimmer and orchestra"));
            tiers.add(new TicketTier(event, sections.get(1), "Mezzanine Diamond Box", BigDecimal.valueOf(260.00), BigDecimal.valueOf(12.00), BigDecimal.valueOf(0.08), 48, 48, "#EC4899", "Royal tier seating, acoustic sweet spot, complimentary program booklet"));
            tiers.add(new TicketTier(event, sections.get(2), "Grand Gallery Upper", BigDecimal.valueOf(85.00), BigDecimal.valueOf(6.00), BigDecimal.valueOf(0.08), 48, 48, "#3B82F6", "Pristine hall acoustics, elevated overhead stage perspective"));
        } else {
            tiers.add(new TicketTier(event, sections.get(0), "Courtside VIP Floor", BigDecimal.valueOf(550.00), BigDecimal.valueOf(20.00), BigDecimal.valueOf(0.08), 40, 40, "#EC4899", "Courtside VIP credential, lounge access"));
            tiers.add(new TicketTier(event, sections.get(1), "Lower Level Prime", BigDecimal.valueOf(280.00), BigDecimal.valueOf(12.00), BigDecimal.valueOf(0.08), 60, 60, "#8B5CF6", "Center court unobstructed view"));
            tiers.add(new TicketTier(event, sections.get(2), "Upper Bowl Stadium", BigDecimal.valueOf(125.00), BigDecimal.valueOf(8.00), BigDecimal.valueOf(0.08), 56, 56, "#3B82F6", "Standard seat reservation"));
        }

        ticketTierRepository.saveAll(tiers);
    }

    private void seedSampleBookings(Event event, List<Section> sections) {
        List<TicketTier> tiers = ticketTierRepository.findByEventId(event.getId());
        if (tiers.isEmpty()) return;

        // Take some seats in section 0 and section 1
        List<Seat> sec0Seats = seatRepository.findBySectionId(sections.get(0).getId());
        List<Seat> sec1Seats = seatRepository.findBySectionId(sections.get(1).getId());

        // Booking 1: VIP seats
        if (sec0Seats.size() >= 4) {
            createSampleBooking(event, tiers.get(0), List.of(sec0Seats.get(0), sec0Seats.get(1)), "Marcus Vance", "marcus.vance@soundwave.io", "+1 (310) 555-0192", 3);
            createSampleBooking(event, tiers.get(0), List.of(sec0Seats.get(2), sec0Seats.get(3)), "Sophia Chen", "sophia.chen@techfront.com", "+1 (415) 555-8823", 2);
        }

        // Booking 2: Lower Bowl seats
        if (sec1Seats.size() >= 6) {
            createSampleBooking(event, tiers.get(1), List.of(sec1Seats.get(0), sec1Seats.get(1), sec1Seats.get(2)), "Elena Rodriguez", "elena.rodriguez@metromedia.org", "+1 (212) 555-3391", 4);
            createSampleBooking(event, tiers.get(1), List.of(sec1Seats.get(3), sec1Seats.get(4)), "Liam O'Connor", "liam.oconnor@apexevents.com", "+1 (312) 555-7714", 1);
        }
    }

    private void createSampleBooking(Event event, TicketTier tier, List<Seat> seats, String name, String email, String phone, int daysAgo) {
        BigDecimal price = tier.getPrice();
        BigDecimal fees = tier.getBookingFee().multiply(BigDecimal.valueOf(seats.size()));
        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(seats.size()));
        BigDecimal taxes = subtotal.multiply(tier.getTaxRate());
        BigDecimal total = subtotal.add(fees).add(taxes);

        String ref = "VV-" + LocalDateTime.now().getYear() + "-" + (100000 + new Random().nextInt(900000));
        String txn = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Booking booking = new Booking(
                ref,
                event,
                name,
                email,
                phone,
                seats.size(),
                subtotal,
                fees,
                taxes,
                total,
                "CONFIRMED",
                "CREDIT_CARD",
                txn
        );
        booking.setCreatedAt(LocalDateTime.now().minusDays(daysAgo).minusHours(new Random().nextInt(10)));
        Booking saved = bookingRepository.save(booking);

        List<Ticket> tickets = new ArrayList<>();
        for (Seat s : seats) {
            String tktNo = "TKT-" + ref.substring(8) + "-" + s.getSeatLabel().replace("-", "");
            String qr = "VENUEVISTA:" + ref + ":" + tktNo + ":" + event.getId() + ":" + s.getSeatLabel();

            Ticket t = new Ticket(
                    saved,
                    event,
                    tier,
                    s,
                    tktNo,
                    qr,
                    s.getSeatLabel(),
                    s.getSection().getName(),
                    price
            );
            t.setCreatedAt(booking.getCreatedAt());
            tickets.add(ticketRepository.save(t));

            tier.setAvailableSeats(Math.max(0, tier.getAvailableSeats() - 1));
        }
        ticketTierRepository.save(tier);
        saved.setTickets(tickets);

        event.setTicketsSold(event.getTicketsSold() + seats.size());
        event.setTotalRevenue(event.getTotalRevenue().add(total));
        eventRepository.save(event);
    }
}
