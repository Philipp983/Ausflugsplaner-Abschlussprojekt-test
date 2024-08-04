package com.brights.fi.backend.controller;

import com.brights.fi.backend.DTO.ArtikelSimpelDTO;
import com.brights.fi.backend.DTO.ArtikelUpdateAntwortDTO;
import com.brights.fi.backend.DTO.ArtikelUpdateDTO;
import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.service.ArtikelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artikel")
public class ArtikelController {
    private ArtikelService artikelService;

    @Autowired
    public ArtikelController(ArtikelService artikelService) {
        this.artikelService = artikelService;
    }

    @GetMapping
    public ResponseEntity<List<Artikel>> gibAlleArtikel() {
        List<Artikel> artikel = artikelService.gibAlleArtikel();
        return ResponseEntity.ok(artikel);
    }

    @PutMapping("/{artikelId}/benutzer/{benutzerId}")
    public ResponseEntity<ArtikelUpdateAntwortDTO> hinzufuegeBenutzerZuArtikel(
            @PathVariable Long artikelId,
            @PathVariable Long benutzerId) {
        Artikel artikel = artikelService.hinzufuegeBenutzerZuArtikel(artikelId, benutzerId);
        ArtikelUpdateAntwortDTO antwort = new ArtikelUpdateAntwortDTO(
                artikel.getId(),
                artikel.getName(),
                artikel.getBenutzer() == null ? null : artikel.getBenutzer().getId(),
                artikel.getAusflug() == null ? null : artikel.getAusflug().getId());
        return ResponseEntity.ok(antwort);
    }


    @GetMapping("/{artikelId}")
    public ResponseEntity<Artikel> gibArtikelDurchId(
            @PathVariable Long artikelId) {
        Artikel artikel = artikelService.gibArtikelDurchId(artikelId);
        return ResponseEntity.ok(artikel);
    }

    @GetMapping("/{artikelId}/simpel")
    public ResponseEntity<ArtikelSimpelDTO> gibArtikelDTODurchId(
            @PathVariable Long artikelId) {
        Artikel artikel = artikelService.gibArtikelDurchId(artikelId);
        ArtikelSimpelDTO antwort = new ArtikelSimpelDTO(
                artikel.getId(),
                artikel.getName(),
                artikel.getBenutzer() != null ? artikel.getBenutzer().getId() : null,
                artikel.getAusflug() != null ? artikel.getAusflug().getId() : null
        );
        return ResponseEntity.ok(antwort);
    }

    @PostMapping("/ausflug/{ausflugId}")
    public ResponseEntity<Ausflug> speichereArtikel(
            @PathVariable Long ausflugId,
            @RequestBody Artikel artikel) {
        Ausflug ausflug = artikelService.speichereArtikel(ausflugId, artikel);
        return ResponseEntity.status(HttpStatus.CREATED).body(ausflug);
    }

    @DeleteMapping("/{artikelId}/entferneArtikel")
    public ResponseEntity<Void> loescheArtikel(
            @PathVariable Long artikelId) {
        artikelService.loescheArtikel(artikelId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{artikelId}/update")
    public ResponseEntity<ArtikelUpdateAntwortDTO> aktualisiereArtikel(
            @PathVariable Long artikelId,
            @RequestBody ArtikelUpdateDTO artikelUpdateDTO) {
        Artikel aktualisierterArtikel = artikelService.aktualisiereArtikel(artikelId, artikelUpdateDTO);
        ArtikelUpdateAntwortDTO antwort = new ArtikelUpdateAntwortDTO(
                aktualisierterArtikel.getId(),
                aktualisierterArtikel.getName(),
                aktualisierterArtikel.getBenutzer() == null ? null : aktualisierterArtikel.getBenutzer().getId(),
                aktualisierterArtikel.getAusflug() == null ? null : aktualisierterArtikel.getAusflug().getId());
        return ResponseEntity.ok(antwort);
    }

    @PutMapping("/{artikelId}/update/volleAntwort")
    public ResponseEntity<Artikel> aktualisiereArtikelVolleAntwort(
            @PathVariable Long artikelId,
            @RequestBody ArtikelUpdateDTO artikelUpdateDTO) {
        Artikel aktualisierterArtikel = artikelService.aktualisiereArtikel(artikelId, artikelUpdateDTO);
        return ResponseEntity.ok(aktualisierterArtikel);
    }

    @PutMapping("/{artikelId}/entferne")
    public ResponseEntity<Void> entferneBenutzerVonArtikel(
            @PathVariable Long artikelId) {
        artikelService.entferneBenutzerVonArtikel(artikelId);
        return ResponseEntity.noContent().build();
    }


    // ??? macht iwie keinen sinn
//    @PutMapping("/{artikelId}")
//    public ResponseEntity<Artikel> aktualisiereArtikel(@PathVariable Long artikelId, @RequestBody Artikel artikel) {
//        Artikel aktualisierterArtikel = artikelService.aktualisiereArtikel(artikelId, artikel);
//        return ResponseEntity.ok(aktualisierterArtikel);
//    }


}
