package com.brights.fi.backend.controller;

import com.brights.fi.backend.DTO.EinladungAntwortDTO;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.model.Einladung;
import com.brights.fi.backend.model.EinladungSimpel;
import com.brights.fi.backend.service.AusflugService;
import com.brights.fi.backend.service.EinladungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/einladungen")
public class EinladungController {

	private final EinladungService einladungService;
	private final AusflugService ausflugService;

	@Autowired
	public EinladungController(EinladungService einladungService, AusflugService ausflugService) {
		this.einladungService = einladungService;
		this.ausflugService = ausflugService;
	}

	@PostMapping("/simpel/{ausflugId}/{benutzerId}")
	public ResponseEntity<Benutzer> erstelleEinladungSimpel(
			@PathVariable Long ausflugId,
			@PathVariable Long benutzerId) {
		Benutzer benutzerNachEinladung = einladungService.erstelleEinladungSimpelAnfrage(ausflugId, benutzerId);
		return ResponseEntity.ok(benutzerNachEinladung);
	}

	@PostMapping("/simpel/bulk")
	public ResponseEntity<List<Benutzer>> erstelleEinladungenSimpel(@RequestParam Long ausflugId, @RequestBody List<String> benutzerUsernames) {
		List<Benutzer> eingeladeneBenutzer = einladungService.erstelleEinladungenSimpel(ausflugId, benutzerUsernames);
		return ResponseEntity.ok(eingeladeneBenutzer);
	}

	@PutMapping("/{einladungId}/annehmen")
	public ResponseEntity<EinladungAntwortDTO> annehmeEinladungSimpel(
			@PathVariable Long einladungId) {
		EinladungSimpel einladung = einladungService.getEinladungSimpel(einladungId);
		Ausflug updatedAusflug = ausflugService.hinzufuegeTeilnehmer(einladung.getAusflugId(), einladung.getHinzuzufuegenderTeilnehmerId());
		Benutzer updatedBenutzer = einladungService.entferneEinladungSimpel(einladungId);

		EinladungAntwortDTO antwort = new EinladungAntwortDTO(updatedBenutzer, updatedAusflug);

		return ResponseEntity.ok(antwort);
	}

	@GetMapping("/benutzer/{benutzerId}")
	public ResponseEntity<?> gebeAlleEinladungenFuerBenutzer(
			@PathVariable Long benutzerId
	) {
		Set<EinladungSimpel> einladungSimpels = einladungService.gebeAlleEinladungenFuerBenutzer(benutzerId);

		return ResponseEntity.ok(einladungSimpels);
	}

	@DeleteMapping("/{einladungId}/ablehnen")
	public ResponseEntity<Benutzer> ablehneEinladungSimpel(
			@PathVariable Long einladungId) {
		Benutzer updatedBenutzer = einladungService.entferneEinladungSimpel(einladungId);
		return ResponseEntity.ok(updatedBenutzer);
	}

	//	@PostMapping
//	public ResponseEntity<Einladung> erstelleEinladung(@RequestParam Long ausflugId, @RequestParam Long benutzerId) {
//		Einladung einladung = einladungService.erstelleEinladung(ausflugId, benutzerId);
//		return ResponseEntity.ok(einladung);
//	}

//	@PutMapping("/{einladungId}/annehmen")
//	public ResponseEntity<Ausflug> annehmeEinladung(@PathVariable Long einladungId) {
//		Einladung einladung = einladungService.getEinladung(einladungId);
//		Ausflug updatedAusflug = ausflugService.hinzufuegeTeilnehmer(einladung.getAusflug().getId(), einladung.getBenutzer().getId());
//		einladungService.entferneEinladung(einladungId);
//		return ResponseEntity.ok(updatedAusflug);
//	}
//
//	@DeleteMapping("/{einladungId}/ablehnen")
//	public ResponseEntity<?> ablehneEinladung(@PathVariable Long einladungId) {
//		einladungService.entferneEinladung(einladungId);
//		return ResponseEntity.ok("Einladung abgelehnt");
//	}
	// Einladungen Status
	// ändern, statt löschen:
//	@PutMapping("/{einladungId}/annehmen")
//	public ResponseEntity<Ausflug> annehmeEinladung(@PathVariable Long einladungId) {
//		Einladung einladung = einladungService.annehmeEinladung(einladungId);
//		Ausflug updatedAusflug = ausflugService.hinzufuegeTeilnehmer(einladung.getAusflug().getId(), einladung.getBenutzer().getId());
//		return ResponseEntity.ok(updatedAusflug);
//	}
//
//	@PutMapping("/{einladungId}/ablehnen")
//	public ResponseEntity<Void> ablehneEinladung(@PathVariable Long einladungId) {
//		einladungService.ablehneEinladung(einladungId);
//		return ResponseEntity.noContent().build();
//	}



}
