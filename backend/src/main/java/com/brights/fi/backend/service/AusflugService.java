package com.brights.fi.backend.service;

import com.brights.fi.backend.exception.ResourceNotFoundException;
import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.AusflugBild;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.repositories.AusflugRepo;
import com.brights.fi.backend.util.AusflugDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AusflugService {

	private AusflugRepo ausflugRepo;
	private BenutzerService benutzerService;

	@Autowired
	public AusflugService(AusflugRepo ausflugRepo, BenutzerService benutzerService) {
		this.ausflugRepo = ausflugRepo;
		this.benutzerService = benutzerService;
	}

	public List<Ausflug> gibAlleAusfluege() {
		return ausflugRepo.findAll();
	}

	public Ausflug speicherAusflug(Long benutzerId, Ausflug ausflug) {
		Benutzer ersteller = benutzerService.findeBenutzerDurchId(benutzerId);
		AusflugBild bild = new AusflugBild();
		if (ausflug.getHauptBild() == null ||
				(ausflug.getHauptBild().getImageUrl() == null || ausflug.getHauptBild().getImageUrl().isEmpty()) &&
						(ausflug.getHauptBild().getImageData() == null || ausflug.getHauptBild().getImageData().length == 0)) {

			bild.setImageUrl("https://scoutmytrip.com/wp-content/uploads/2020/02/friends-3542235_1280-1024x658.jpg");
			bild.setAusflug(ausflug);
			ausflug.setHauptBild(bild);
		}
		if (!ausflug.getTeilnehmerListe().contains(ersteller)) {
			ausflug.getTeilnehmerListe().add(ersteller);
		}
		ausflug.setErstellerDesAusflugsId(ersteller.getId().toString());
		ausflug.getTeilnehmerListe().add(ersteller);
		return ausflugRepo.save(ausflug);
	}

	public Ausflug hinzufuegeTeilnehmer(Long ausflugId, Long benutzerId) {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug mit ID " + ausflugId + " nicht gefunden"));
		Benutzer benutzer = benutzerService.findeBenutzerDurchId(benutzerId);

		ausflug.getTeilnehmerListe().add(benutzer);
		benutzer.getAusfluege().add(ausflug);

//		benutzerService.speichereBenutzer(benutzer);
		return ausflugRepo.save(ausflug);
	}

	public Artikel hinzufuegeArtikel(Long ausflugId, Artikel artikel) {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug mit ID " + ausflugId + " nicht gefunden"));

		artikel.setAusflug(ausflug);
		ausflug.getSachenZumMitbringen().add(artikel);

		ausflugRepo.save(ausflug);

		return artikel;
	}

	public Ausflug entferneTeilnehmer(Long ausflugId, Long benutzerId) {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug mit ID " + ausflugId + " nicht gefunden"));
		Benutzer benutzer = benutzerService.findeBenutzerDurchId(benutzerId);

		if (!ausflug.getTeilnehmerListe().remove(benutzer)) {
			throw new ResourceNotFoundException("Benutzer mit ID " + benutzerId + " ist kein Teilnehmer dieses Ausflugs");
		}
		benutzer.getAusfluege().remove(ausflug);

//		benutzerService.speichereBenutzer(benutzer);
		return ausflugRepo.save(ausflug);
	}

	public Ausflug gibAusflugDurchId(Long ausflugId) {
		return ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug mit ID " + ausflugId + " nicht gefunden"));
	}

	public Ausflug aktualisiereAusflug(Long ausflugId, AusflugDTO ausflugDTO) {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug mit ID " + ausflugId + " nicht gefunden"));

		ausflug.setTitel(ausflugDTO.getTitel());
		ausflug.setBeschreibung(ausflugDTO.getBeschreibung());
		ausflug.setReiseziel(ausflugDTO.getReiseziel());
		ausflug.setAusflugsdatum(LocalDateTime.parse(ausflugDTO.getAusflugsdatum()));
		ausflug.setEnddatum(LocalDateTime.parse(ausflugDTO.getEnddatum()));
		ausflug.setTeilnehmerAnzahl(ausflugDTO.getTeilnehmerAnzahl());
		ausflug.setEditierdatum(LocalDateTime.now());

		// Update teilnehmerListe
		Set<Benutzer> teilnehmerSet = ausflugDTO.getTeilnehmerListe().stream()
				.map(benutzerService::findeBenutzerDurchUserName)
				.collect(Collectors.toSet());
		ausflug.getTeilnehmerListe().clear();
		ausflug.getTeilnehmerListe().addAll(teilnehmerSet);

		// Update sachenZumMitbringen
		List<Artikel> neueArtikel = ausflugDTO.getSachenZumMitbringen().stream()
				.map(artikelName -> {
					Artikel artikel = new Artikel();
					artikel.setName(artikelName);
					artikel.setAusflug(ausflug);
					return artikel;
				})
				.collect(Collectors.toList());

		List<Artikel> aktuelleArtikel = ausflug.getSachenZumMitbringen();

		// Find and remove articles that are no longer in the updated list
		aktuelleArtikel.removeIf(artikel -> !neueArtikel.stream()
				.anyMatch(neuerArtikel -> neuerArtikel.getName().equals(artikel.getName())));

		// Add new articles that do not already exist in the current list
		for (Artikel neuerArtikel : neueArtikel) {
			if (aktuelleArtikel.stream().noneMatch(artikel -> artikel.getName().equals(neuerArtikel.getName()))) {
				aktuelleArtikel.add(neuerArtikel);
			}
		}

		ausflug.setSachenZumMitbringen(aktuelleArtikel);

		return ausflugRepo.save(ausflug);
	}

	public void loescheAusflug(Long ausflugId) {
		Ausflug ausflugDb = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug mit ID " + ausflugId + " nicht gefunden"));
		ausflugRepo.delete(ausflugDb);
	}

	public Ausflug erstelleAusflug(Long benutzerId, AusflugDTO ausflugDTO) {
		Ausflug ausflug = new Ausflug();
		ausflug.setTitel(ausflugDTO.getTitel());
		ausflug.setBeschreibung(ausflugDTO.getBeschreibung());
		ausflug.setReiseziel(ausflugDTO.getReiseziel());
		ausflug.setAusflugsdatum(LocalDateTime.parse(ausflugDTO.getAusflugsdatum()));
		ausflug.setEnddatum(LocalDateTime.parse(ausflugDTO.getEnddatum()));
		ausflug.setTeilnehmerAnzahl(ausflugDTO.getTeilnehmerAnzahl());
		ausflug.setErstellerDesAusflugsId(ausflugDTO.getErstellerDesAusflugsId());

		// Get the creator
		Benutzer ersteller = benutzerService.findeBenutzerDurchId(benutzerId);

		// Convert teilnehmerListe from String to Benutzer objects and ensure the creator is included
		Set<Benutzer> teilnehmerSet = new HashSet<>();
		teilnehmerSet.add(ersteller); // Always add the creator

		if (ausflugDTO.getTeilnehmerListe() != null && !ausflugDTO.getTeilnehmerListe().isEmpty()) {
			teilnehmerSet.addAll(ausflugDTO.getTeilnehmerListe().stream()
							.filter(userName -> !userName.equals(ersteller.getUserName())) // Exclude the creator
							.map(benutzerService::findeBenutzerDurchUserName)
							.collect(Collectors.toSet()));
		}

		ausflug.setTeilnehmerListe(teilnehmerSet);

		// Convert sachenZumMitbringen from String to Artikel objects
		List<Artikel> artikelList = ausflugDTO.getSachenZumMitbringen().stream()
						.map(artikelName -> {
							Artikel artikel = new Artikel();
							artikel.setName(artikelName);
							artikel.setAusflug(ausflug); // Setting die Ausflug Beziehung
							return artikel;
						})
						.collect(Collectors.toList());
		ausflug.setSachenZumMitbringen(artikelList);

		return speicherAusflug(benutzerId, ausflug);
	}

	public Ausflug erstelleAusflugAlt(Long benutzerId, AusflugDTO ausflugDTO) {
		Ausflug ausflug = new Ausflug();
		ausflug.setTitel(ausflugDTO.getTitel());
		ausflug.setBeschreibung(ausflugDTO.getBeschreibung());
		ausflug.setReiseziel(ausflugDTO.getReiseziel());
		ausflug.setAusflugsdatum(LocalDateTime.parse(ausflugDTO.getAusflugsdatum()));
		ausflug.setEnddatum(LocalDateTime.parse(ausflugDTO.getEnddatum()));
		ausflug.setTeilnehmerAnzahl(ausflugDTO.getTeilnehmerAnzahl());
		ausflug.setErstellerDesAusflugsId(ausflugDTO.getErstellerDesAusflugsId());

		// Convert teilnehmerListe from String to Benutzer objects
		Set<Benutzer> teilnehmerSet = ausflugDTO.getTeilnehmerListe().stream()
				.map(benutzerService::findeBenutzerDurchUserName)
				.collect(Collectors.toSet());
		ausflug.setTeilnehmerListe(teilnehmerSet);

		// Convert sachenZumMitbringen from String to Artikel objects
		List<Artikel> artikelList = ausflugDTO.getSachenZumMitbringen().stream()
				.map(artikelName -> {
					Artikel artikel = new Artikel();
					artikel.setName(artikelName);
					artikel.setAusflug(ausflug); // Setting die Ausflug Beziehung
					return artikel;
				})
				.collect(Collectors.toList());
		ausflug.setSachenZumMitbringen(artikelList);

		return speicherAusflug(benutzerId, ausflug);
	}
}
