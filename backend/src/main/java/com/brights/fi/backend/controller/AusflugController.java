package com.brights.fi.backend.controller;

import com.brights.fi.backend.DTO.AusflugAntwortDTO;
import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.service.AusflugService;
import com.brights.fi.backend.service.BenutzerService;
import com.brights.fi.backend.util.AusflugDTO;
import com.brights.fi.backend.util.AusflugDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ausfluege")
public class AusflugController {

	private AusflugService ausflugService;
	private BenutzerService benutzerService;

	@Autowired
	public AusflugController(AusflugService ausflugService, BenutzerService benutzerService) {
		this.ausflugService = ausflugService;
		this.benutzerService = benutzerService;
	}

	@GetMapping
	public ResponseEntity<List<Ausflug>> gibAlleAusfluege() {
		List<Ausflug> ausfluege = ausflugService.gibAlleAusfluege();
		return ResponseEntity.ok(ausfluege);
	}


	@GetMapping("/{ausflugId}")
	public ResponseEntity<AusflugAntwortDTO> gibAusflugDurchId(@PathVariable Long ausflugId) {
		Ausflug ausflug = ausflugService.gibAusflugDurchId(ausflugId);
		AusflugAntwortDTO ausflugDTO = AusflugDTOMapper.toDTO(ausflug);
		// Ensure the Ersteller is always in the teilnehmerIds
		if (!ausflugDTO.getTeilnehmerIds().contains(Long.parseLong(ausflugDTO.getErstellerDesAusflugsId()))) {
			ausflugDTO.getTeilnehmerIds().add(Long.parseLong(ausflugDTO.getErstellerDesAusflugsId()));
		}
		return ResponseEntity.ok(ausflugDTO);
	}



	@PostMapping("/{ausflugId}/teilnehmer/{benutzerId}")
	public ResponseEntity<Ausflug> hinzufuegeTeilnehmer(@PathVariable Long ausflugId, @PathVariable Long benutzerId) {
		Ausflug aktualisierterAusflug = ausflugService.hinzufuegeTeilnehmer(ausflugId, benutzerId);
		return ResponseEntity.ok(aktualisierterAusflug);
	}

	@PostMapping("/{benutzerId}")
	public ResponseEntity<Ausflug> speicherAusflug(@PathVariable Long benutzerId, @RequestBody AusflugDTO ausflugDTO) {
		ausflugDTO.setErstellerDesAusflugsId(benutzerId.toString());
		Ausflug gespeicherterAusflug = ausflugService.erstelleAusflug(benutzerId, ausflugDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(gespeicherterAusflug);
	}

	@DeleteMapping("/{ausflugId}/teilnehmer/{benutzerId}")
	public ResponseEntity<Ausflug> entferneTeilnehmer(@PathVariable Long ausflugId, @PathVariable Long benutzerId) {
		Ausflug aktualisierterAusflug = ausflugService.entferneTeilnehmer(ausflugId, benutzerId);
		return ResponseEntity.ok(aktualisierterAusflug);
	}

	@PutMapping("/{ausflugId}")
	public ResponseEntity<Ausflug> aktualisiereAusflug(@PathVariable Long ausflugId, @RequestBody AusflugDTO ausflugDTO) {
		Ausflug aktualisierterAusflug = ausflugService.aktualisiereAusflug(ausflugId, ausflugDTO);
		return ResponseEntity.ok(aktualisierterAusflug);
	}

	@DeleteMapping("/{ausflugId}")
	public ResponseEntity<Void> loescheAusflug(@PathVariable Long ausflugId) {
		ausflugService.loescheAusflug(ausflugId);
		return ResponseEntity.noContent().build();
	}

	//	@GetMapping("/{ausflugId}")
//	public ResponseEntity<Ausflug> gibAusflugDurchId(@PathVariable Long ausflugId) {
//		Ausflug ausflug = ausflugService.gibAusflugDurchId(ausflugId);
//		return ResponseEntity.ok(ausflug);
//	}

	//	@PostMapping("/{benutzerId}")
//	public ResponseEntity<Ausflug> speicherAusflug(@PathVariable Long benutzerId, @RequestBody Ausflug ausflug) {
//		Ausflug gespeicherterAusflug = ausflugService.speicherAusflug(benutzerId, ausflug);
//		return ResponseEntity.status(HttpStatus.CREATED).body(gespeicherterAusflug);
//	}

	/* TODO
	1.: Controller und Service für Artikel schreiben
	1.1.: delete und update Artikel als Endpoint, Artikel erstellen, getArtikelById
	2.: Im Ausflugkontroller: delete und update Ausflug, getAusflugById
	3.: Update Benutzer, delete Benutzer, findAllBenutzer

	 */

}
