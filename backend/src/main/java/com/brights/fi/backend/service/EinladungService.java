package com.brights.fi.backend.service;

import com.brights.fi.backend.exception.BusinessLogicException;
import com.brights.fi.backend.exception.ResourceNotFoundException;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.model.Einladung;
import com.brights.fi.backend.model.EinladungSimpel;
import com.brights.fi.backend.repositories.AusflugRepo;
import com.brights.fi.backend.repositories.BenutzerRepo;
import com.brights.fi.backend.repositories.EinladungRepo;
import com.brights.fi.backend.repositories.EinladungSimpelRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EinladungService {


	private final BenutzerRepo benutzerRepo;
	private final AusflugRepo ausflugRepo;
	private final EinladungRepo einladungRepo;
	private final EinladungSimpelRepo einladungSimpelRepo;

	public EinladungService(BenutzerRepo benutzerRepo, AusflugRepo ausflugRepo, EinladungRepo einladungRepo, EinladungSimpelRepo einladungSimpelRepo) {
		this.benutzerRepo = benutzerRepo;
		this.ausflugRepo = ausflugRepo;
		this.einladungRepo = einladungRepo;
		this.einladungSimpelRepo = einladungSimpelRepo;
	}

	public Benutzer erstelleEinladungSimpel(Long ausflugId, Long benutzerId) {
		Benutzer benutzer = benutzerRepo.findById(benutzerId).orElseThrow(() -> new ResourceNotFoundException("Benutzer konnte nicht gefunden werden"));
		Ausflug ausflug = ausflugRepo.findById(ausflugId).orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));

		if (einladungSimpelRepo.existsByBenutzerAndAusflugId(benutzer, ausflugId)) {
			throw new BusinessLogicException("Eine Einladung für diesen Benutzer zu diesem Ausflug existiert bereits.");
		}
		if (ausflug.getTeilnehmerListe() != null) {
			for (Benutzer benutzer1 : ausflug.getTeilnehmerListe()) {
				if (benutzer.getId() == benutzerId) {
					throw new BusinessLogicException("Dieser Benutzer ist bereits ein Teilnehmer");
				}
			}
		}

		EinladungSimpel einladungSimpel = new EinladungSimpel();
		einladungSimpel.setAusflugId(ausflug.getId());
		einladungSimpel.setBenutzer(benutzer);
		einladungSimpel.setErstellungsdatum(LocalDateTime.now());

		benutzer.getEinladungenSimpel().add(einladungSimpel);
//
		return benutzerRepo.save(benutzer);

//		return einladungSimpelRepo.save(einladungSimpel);
	}

	public Benutzer entferneEinladungSimpel(Long einladungId) {
		EinladungSimpel einladung = einladungSimpelRepo.findById(einladungId)
				.orElseThrow(() -> new ResourceNotFoundException("Einladung nicht gefunden"));
//		Einladung einladung = getEinladung(einladungId);
		Benutzer benutzer = einladung.getBenutzer();
		benutzer.getEinladungenSimpel().remove(einladung);
		einladungSimpelRepo.deleteById(einladungId);
		return benutzerRepo.save(benutzer);
	}



	public EinladungSimpel getEinladungSimpel(Long einladungId) {
		return einladungSimpelRepo.findById(einladungId)
				.orElseThrow(() -> new ResourceNotFoundException("Einladung nicht gefunden"));
	}


//	public Einladung erstelleEinladung(Long ausflugId, Long benutzerId) {
//		Benutzer benutzer = benutzerRepo.findById(benutzerId).orElseThrow(() -> new ResourceNotFoundException("Benutzer konnte nicht gefunden werden"));
//		Ausflug ausflug = ausflugRepo.findById(ausflugId).orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));
//
//		if (einladungRepo.existsByBenutzerAndAusflug(benutzer, ausflug)) {
//			throw new BusinessLogicException("Eine Einladung für diesen Benutzer zu diesem Ausflug existiert bereits.");
//		}
//
//		Einladung einladung = new Einladung();
//		einladung.setAusflug(ausflug);
//		einladung.setBenutzer(benutzer);
//		einladung.setStatus(Einladung.EinladungStatus.AUSSTEHEND);
//		einladung.setErstellungsdatum(LocalDateTime.now());
//
//		benutzer.getEinladungen().add(einladung);
////
////		benutzerRepo.save(benutzer);
//		return einladungRepo.save(einladung);
//
//	}



	public Einladung annehmeEinladung(Long einladungId) {
		Einladung einladung = einladungRepo.findById(einladungId)
				.orElseThrow(() -> new ResourceNotFoundException("Einladung nicht gefunden"));
		einladung.setStatus(Einladung.EinladungStatus.ANGENOMMEN);
		return einladungRepo.save(einladung);
	}

	public void ablehneEinladung(Long einladungId) {
		Einladung einladung = einladungRepo.findById(einladungId)
				.orElseThrow(() -> new ResourceNotFoundException("Einladung nicht gefunden"));
		einladung.setStatus(Einladung.EinladungStatus.ABGELEHNT);
		einladungRepo.save(einladung);
	}

	public Einladung getEinladung(Long einladungId) {
		return einladungRepo.findById(einladungId)
				.orElseThrow(() -> new ResourceNotFoundException("Einladung nicht gefunden"));
	}

	public List<Benutzer> erstelleEinladungenSimpel(Long ausflugId, List<String> benutzerUsernames) {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
						.orElseThrow(() -> new ResourceNotFoundException("Ausflug nicht gefunden"));
		String erstellerUsername = ausflug.getErstellerDesAusflugsId();
		List<Benutzer> eingeladeneBenutzer = new ArrayList<>();

		Benutzer ersteller = benutzerRepo.findById(Long.valueOf(ausflug.getErstellerDesAusflugsId()))
						.orElseThrow(() -> new ResourceNotFoundException("Ersteller konnte nicht gefunden werden"));

		for (String username : benutzerUsernames) {
			Benutzer benutzer = benutzerRepo.findByUserName(username);
			if (benutzer == null) {
				throw new ResourceNotFoundException("Benutzer nicht gefunden: " + username);
			}

			if (!erstellerUsername.equals(username) &&
							!einladungSimpelRepo.existsByBenutzerAndAusflugId(benutzer, ausflugId) &&
							!ausflug.getTeilnehmerListe().contains(benutzer)) {

				EinladungSimpel einladungSimpel = new EinladungSimpel();
				einladungSimpel.setAusflugId(ausflugId);
				einladungSimpel.setBenutzer(benutzer);
				einladungSimpel.setErstellungsdatum(LocalDateTime.now());
				einladungSimpel.setArt(EinladungSimpel.ArtDerEinladung.EINLADUNG);
				einladungSimpel.setHinzuzufuegenderTeilnehmerId(benutzer.getId());
				einladungSimpel.setNameDesAusflugs(ausflug.getTitel());
				einladungSimpel.setNameDesTeilnehmers(ersteller.getUserName());

				benutzer.getEinladungenSimpel().add(einladungSimpel);
				eingeladeneBenutzer.add(benutzerRepo.save(benutzer));
			}
		}

		return eingeladeneBenutzer;
	}

	public Set<EinladungSimpel> gebeAlleEinladungenFuerBenutzer(Long benutzerId) {
		Benutzer benutzer = benutzerRepo.findById(benutzerId)
				.orElseThrow(() -> new ResourceNotFoundException("Benutzer nicht gefunden"));

		return benutzer.getEinladungenSimpel();
	}

	public Benutzer erstelleEinladungSimpelAnfrage(Long ausflugId, Long benutzerId) {
		Benutzer hinzuzufuegenderTeilnehmer = benutzerRepo.findById(benutzerId)
				.orElseThrow(() -> new ResourceNotFoundException("Benutzer konnte nicht gefunden werden"));
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));
		Benutzer erstellerDesAusfluges = benutzerRepo.findById(Long.valueOf(ausflug.getErstellerDesAusflugsId()))
				.orElseThrow(() -> new ResourceNotFoundException("Ersteller des Ausflugs konnte nicht gefunden werden"));

		// Check if a similar request already exists
		boolean anfrageExistiert = erstellerDesAusfluges.getEinladungenSimpel().stream()
				.anyMatch(einladung -> einladung.getAusflugId().equals(ausflugId) &&
						einladung.getHinzuzufuegenderTeilnehmerId().equals(benutzerId) &&
						einladung.getArt() == EinladungSimpel.ArtDerEinladung.ANFRAGE);

		if (anfrageExistiert) {
			throw new BusinessLogicException("Eine Anfrage für diesen Benutzer zu diesem Ausflug existiert bereits. Bitte warte, bis der Ersteller deine Anfrage beantwortet.");
		}

		boolean isAlreadyParticipant = ausflug.getTeilnehmerListe().stream()
				.anyMatch(teilnehmer -> teilnehmer.getId().equals(benutzerId));

		if (isAlreadyParticipant) {
			throw new BusinessLogicException("Der Benutzer ist bereits Teilnehmer dieses Ausflugs.");
		}

		EinladungSimpel einladung = new EinladungSimpel();
		einladung.setBenutzer(erstellerDesAusfluges);
		einladung.setAusflugId(ausflugId);
		einladung.setErstellungsdatum(LocalDateTime.now());
		einladung.setHinzuzufuegenderTeilnehmerId(hinzuzufuegenderTeilnehmer.getId());
		einladung.setArt(EinladungSimpel.ArtDerEinladung.ANFRAGE);
		einladung.setNameDesAusflugs(ausflug.getTitel());
		einladung.setNameDesTeilnehmers(hinzuzufuegenderTeilnehmer.getUserName());

		erstellerDesAusfluges.getEinladungenSimpel().add(einladung);
		return benutzerRepo.save(erstellerDesAusfluges);
	}
}
