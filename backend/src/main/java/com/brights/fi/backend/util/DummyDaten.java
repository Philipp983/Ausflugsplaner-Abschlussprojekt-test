//package com.brights.fi.backend.util;
//
//
//import com.brights.fi.backend.model.Artikel;
//import com.brights.fi.backend.model.Ausflug;
//import com.brights.fi.backend.model.Benutzer;
//import com.brights.fi.backend.model.EinladungSimpel;
//import com.brights.fi.backend.repositories.EinladungSimpelRepo;
//import com.brights.fi.backend.service.AusflugBildService;
//import com.brights.fi.backend.service.AusflugService;
//import com.brights.fi.backend.service.BenutzerService;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.List;
//
//@Configuration
//public class DummyDaten {
//    private final BenutzerService benutzerService;
//    private final AusflugService ausflugService;
//    private final AusflugBildService ausflugBildService;
//    private final EinladungSimpelRepo einladungSimpelRepo;
//
//
//    @Autowired
//    public DummyDaten(BenutzerService benutzerService, AusflugService ausflugService, AusflugBildService ausflugBildService, EinladungSimpelRepo einladungSimpelRepo) {
//        this.benutzerService = benutzerService;
//        this.ausflugService = ausflugService;
//        this.ausflugBildService = ausflugBildService;
//        this.einladungSimpelRepo = einladungSimpelRepo;
//    }
//
//    @PostConstruct
//    public void loadData() {
//        // Creating sample Benutzer
//        Benutzer benutzer1 = new Benutzer();
//        benutzer1.setUserName("John Doe");
//        benutzer1.setUserEmail("john.doe@example.com");
//        benutzer1.setPasswort("password");
//        benutzerService.speichereBenutzer(benutzer1);
//
//        Benutzer benutzer2 = new Benutzer();
//        benutzer2.setUserName("Jane Smith");
//        benutzer2.setUserEmail("jane.smith@example.com");
//        benutzer2.setPasswort("password");
//        benutzerService.speichereBenutzer(benutzer2);
//
//        Benutzer benutzer3 = new Benutzer();
//        benutzer3.setUserName("Alice Johnson");
//        benutzer3.setUserEmail("alice.johnson@example.com");
//        benutzer3.setPasswort("password");
//        benutzerService.speichereBenutzer(benutzer3);
//
//        Benutzer benutzer4 = new Benutzer();
//        benutzer4.setUserName("Bob Brown");
//        benutzer4.setUserEmail("bob.brown@example.com");
//        benutzer4.setPasswort("password");
//        benutzerService.speichereBenutzer(benutzer4);
//
//        Ausflug ausflug1 = new Ausflug();
//        ausflug1.setTitel("Ausflug nach München");
//        ausflug1.setBeschreibung("Besuch des Oktoberfestes");
//        ausflug1.setReiseziel("München");
//        ausflug1.setAusflugsdatum(LocalDateTime.now());
//        ausflug1.setEnddatum(LocalDateTime.now());
//        ausflug1.setTeilnehmerAnzahl(5);
//        ausflug1.getTeilnehmerListe().add(benutzer3);
//        ausflug1.getTeilnehmerListe().add(benutzer2);
//        ausflugService.speicherAusflug(1L,ausflug1);
//
//        Ausflug ausflug2 = new Ausflug();
//        ausflug2.setTitel("Ausflug nach Berlin");
//        ausflug2.setBeschreibung("Besuch des Brandenburger Tors");
//        ausflug2.setReiseziel("Berlin");
//        ausflug2.setAusflugsdatum(LocalDateTime.now().plusDays(2));
//        ausflug2.setEnddatum(LocalDateTime.now().plusDays(3));
//        ausflug2.setTeilnehmerAnzahl(10);
//        ausflug2.getTeilnehmerListe().add(benutzer4);
//        ausflug2.getTeilnehmerListe().add(benutzer1);
//        ausflugService.speicherAusflug(2L, ausflug2);
//
//        Ausflug ausflug3 = new Ausflug();
//        ausflug3.setTitel("Ausflug nach Hamburg");
//        ausflug3.setBeschreibung("Besuch des Hafens");
//        ausflug3.setReiseziel("Hamburg");
//        ausflug3.setAusflugsdatum(LocalDateTime.now().plusDays(4));
//        ausflug3.setEnddatum(LocalDateTime.now().plusDays(5));
//        ausflug3.setTeilnehmerAnzahl(8);
//        ausflug3.getTeilnehmerListe().add(benutzer4);
//        ausflug3.getTeilnehmerListe().add(benutzer1);
//        ausflugService.speicherAusflug(3L, ausflug3);
//
//        Artikel artikel1 = new Artikel();
//        artikel1.setName("Reiseführer");
//        artikel1.setBenutzer(benutzer1);
//        artikel1.setAusflug(ausflug1);
//        ausflugService.hinzufuegeArtikel(1L,artikel1);
//
//        Artikel artikel7 = new Artikel();
//        artikel7.setName("Reiseführer");
//        artikel7.setBenutzer(benutzer1);
//        artikel7.setAusflug(ausflug1);
//        ausflugService.hinzufuegeArtikel(1L,artikel1);
//
//        Artikel artikel2 = new Artikel();
//        artikel2.setName("Sonnencreme");
//        artikel2.setBenutzer(benutzer2);
//        artikel2.setAusflug(ausflug1);
//        ausflugService.hinzufuegeArtikel(1L,artikel2);
//
//        Artikel artikel3 = new Artikel();
//        artikel3.setName("Kamera");
//        artikel3.setBenutzer(benutzer3);
//        artikel3.setAusflug(ausflug2);
//        ausflugService.hinzufuegeArtikel(2L, artikel3);
//
//        Artikel artikel4 = new Artikel();
//        artikel4.setName("Stadtplan");
//        artikel4.setBenutzer(benutzer4);
//        artikel4.setAusflug(ausflug2);
//        ausflugService.hinzufuegeArtikel(2L, artikel4);
//
//        Artikel artikel5 = new Artikel();
//        artikel5.setName("Regenjacke");
//        artikel5.setBenutzer(benutzer1);
//        artikel5.setAusflug(ausflug3);
//        ausflugService.hinzufuegeArtikel(3L, artikel5);
//
//        Artikel artikel6 = new Artikel();
//        artikel6.setName("Wasserflasche");
//        artikel6.setBenutzer(benutzer2);
//        artikel6.setAusflug(ausflug3);
//        ausflugService.hinzufuegeArtikel(3L, artikel6);
//
//        EinladungSimpel einladungSimpel1 = new EinladungSimpel();
//        einladungSimpel1.setBenutzer(benutzer1);
//        einladungSimpel1.setAusflugId(1L);
//        einladungSimpel1.setErstellungsdatum(LocalDateTime.now().minusYears(3));
//        einladungSimpelRepo.save(einladungSimpel1);
//
//        EinladungSimpel einladungSimpel2 = new EinladungSimpel();
//        einladungSimpel2.setBenutzer(benutzer2);
//        einladungSimpel2.setAusflugId(2L);
//        einladungSimpel2.setErstellungsdatum(LocalDateTime.now().minusYears(3));
//        einladungSimpel2.setHinzuzufuegenderTeilnehmerId(3L);
//        einladungSimpel2.setArt(EinladungSimpel.ArtDerEinladung.ANFRAGE);
//        einladungSimpel2.setNameDesAusflugs("Ausflug nach Berlin");
//        einladungSimpel2.setNameDesTeilnehmers("Alice Johnson");
//        einladungSimpelRepo.save(einladungSimpel2);
//
//        EinladungSimpel einladungSimpel3 = new EinladungSimpel();
//        einladungSimpel3.setBenutzer(benutzer2);
//        einladungSimpel3.setAusflugId(3L);
//        einladungSimpel3.setErstellungsdatum(LocalDateTime.now().minusYears(3));
//        einladungSimpel3.setHinzuzufuegenderTeilnehmerId(2L);
//        einladungSimpel3.setArt(EinladungSimpel.ArtDerEinladung.EINLADUNG);
//        einladungSimpel3.setNameDesAusflugs("Ausflug nach Hamburg");
//        einladungSimpel3.setNameDesTeilnehmers("John Doe");
//        einladungSimpelRepo.save(einladungSimpel3);
//
////        EinladungSimpel einladungSimpel3 = new EinladungSimpel();
////        einladungSimpel1.setBenutzer(benutzer1);
////        einladungSimpel1.setAusflugId(1L);
////        einladungSimpel1.setErstellungsdatum(LocalDateTime.now().minusYears(3));
////        einladungSimpelRepo.save(einladungSimpel3);
////
////        EinladungSimpel einladungSimpel4 = new EinladungSimpel();
////        einladungSimpel2.setBenutzer(benutzer3);
////        einladungSimpel2.setAusflugId(2L);
////        einladungSimpel2.setErstellungsdatum(LocalDateTime.now().minusYears(3));
////        einladungSimpelRepo.save(einladungSimpel4);
//
//
//		// List of image URLs
//		List<String> imageUrls = List.of(
//				"https://cdn.pixabay.com/photo/2016/03/27/18/53/drinks-1283608_1280.jpg",
//				"https://cdn.pixabay.com/photo/2024/04/08/03/53/people-8682585_1280.jpg",
//				"https://cdn.pixabay.com/photo/2017/12/08/11/53/event-party-3005668_960_720.jpg"
//		);
//
//		// Create and save AusflugBild
//		ausflugBildService.updateAusflugBildDurchUrl(ausflug1.getId(), imageUrls.getFirst());
//		ausflugBildService.updateAusflugBildDurchUrl(ausflug2.getId(), imageUrls.get(1));
////		ausflugBildService.speichereAusflugBildUrl(ausflug3.getId(), imageUrls.get(2));
//    }
//}
