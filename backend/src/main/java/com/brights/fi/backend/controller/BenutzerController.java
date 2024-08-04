package com.brights.fi.backend.controller;

import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.service.BenutzerService;
import com.brights.fi.backend.util.EinloggenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api/benutzer")
public class BenutzerController {

	private BenutzerService benutzerService;

	@Autowired
	public BenutzerController(BenutzerService benutzerService) {
		this.benutzerService = benutzerService;
	}

	@GetMapping
	public ResponseEntity<List<Benutzer>> gibAlleBenutzer() {
		List<Benutzer> benutzer = benutzerService.gibAlleBenutzer();
		return ResponseEntity.ok(benutzer);
	}

	@GetMapping("/{benutzerId}")
	public ResponseEntity<Benutzer> findeBenutzerDurchId(
					@PathVariable Long benutzerId) {
		Benutzer benutzer = benutzerService.findeBenutzerDurchId(benutzerId);
		return ResponseEntity.ok(benutzer);
	}

	@PostMapping("/login")
	public ResponseEntity<Benutzer> login(
					@RequestBody EinloggenDTO loginRequest) {
		Benutzer benutzer = benutzerService.login(loginRequest.getUserEmail(), loginRequest.getPasswort());
		return ResponseEntity.ok(benutzer);
	}

	@PostMapping
	public ResponseEntity<Benutzer> erstelleBenutzer(
					@RequestBody Benutzer benutzer) {
		Benutzer erstellterBenutzer = benutzerService.speichereBenutzer2(benutzer);

		return ResponseEntity.status(HttpStatus.CREATED).body(erstellterBenutzer);
	}

	@DeleteMapping("/{benutzerId}")
	public ResponseEntity<Void> loescheBenutzer(
					@PathVariable Long benutzerId) {
		benutzerService.loescheBenutzer(benutzerId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{benutzerId}")
	public ResponseEntity<Benutzer> aktualisiereBenutzer(
					@PathVariable Long benutzerId,
					@RequestBody Benutzer benutzer) {
		Benutzer aktualisierterBenutzer = benutzerService.aktualisiereBenutzer(benutzerId, benutzer);
		return ResponseEntity.ok(aktualisierterBenutzer);
	}

	@PutMapping("/{benutzerId}/passwort")
	public ResponseEntity<Void> aktualisierePasswort(
					@PathVariable Long benutzerId,
					@RequestBody Map<String, String> passwordData) {
		String currentPassword = passwordData.get("currentPassword");
		String newPassword = passwordData.get("newPassword");
		String newPasswordConfirm = passwordData.get("newPasswordConfirm");

		benutzerService.aktualisierePasswort(benutzerId, currentPassword, newPassword, newPasswordConfirm);
		return ResponseEntity.noContent().build();
	}
	@GetMapping("/search")
	public ResponseEntity<Set<Benutzer>> searchBenutzer(
					@RequestParam String userName) {
		Set<Benutzer> benutzerList = benutzerService.searchBenutzerByName(userName);
		return ResponseEntity.ok(benutzerList);
	}
}



