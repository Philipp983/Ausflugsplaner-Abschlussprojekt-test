package com.brights.fi.backend.service;

import com.brights.fi.backend.DTO.ArtikelUpdateAntwortDTO;
import com.brights.fi.backend.DTO.ArtikelUpdateDTO;
import com.brights.fi.backend.exception.ResourceNotFoundException;
import com.brights.fi.backend.model.Artikel;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.repositories.ArtikelRepo;
import com.brights.fi.backend.repositories.AusflugRepo;
import com.brights.fi.backend.repositories.BenutzerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtikelService {
    private final AusflugRepo ausflugRepo;
    private final BenutzerService benutzerService;
    private final BenutzerRepo benutzerRepo;
    private ArtikelRepo artikelRepo;

    @Autowired
    public ArtikelService(ArtikelRepo artikelRepo, AusflugRepo ausflugRepo, BenutzerService benutzerService, BenutzerRepo benutzerRepo) {
        this.artikelRepo = artikelRepo;
        this.ausflugRepo = ausflugRepo;
        this.benutzerService = benutzerService;
        this.benutzerRepo = benutzerRepo;
    }

    public List<Artikel> gibAlleArtikel() {
        return artikelRepo.findAll();
    }

    public Ausflug speichereArtikel(Long ausflugId, Artikel artikel) {
        Ausflug ausflug = ausflugRepo.findById(ausflugId)
                .orElseThrow(() -> new ResourceNotFoundException("Ausflug mit Id " + ausflugId + " wurde nicht gefunden"));
        artikel.setAusflug(ausflug);
        ausflug.getSachenZumMitbringen().add(artikel);
        return ausflugRepo.save(ausflug);
    }

    @Transactional
    public void loescheArtikel(Long artikelId) {
        Artikel artikel = artikelRepo.findById(artikelId)
                .orElseThrow(() -> new ResourceNotFoundException("Artikel mit Id " + artikelId + " wurde nicht gefunden"));

        // Remove the artikel from its associated Ausflug
        if (artikel.getAusflug() != null) {
            Ausflug ausflug = artikel.getAusflug();
            ausflug.getSachenZumMitbringen().remove(artikel);
            ausflugRepo.save(ausflug);
        }

        // Now delete the artikel
        artikelRepo.delete(artikel);
    }

    @Transactional
    public Artikel aktualisiereArtikel(Long artikelId, ArtikelUpdateDTO artikel) {

        Artikel existingArtikel = artikelRepo.findById(artikelId)
                .orElseThrow(() -> new ResourceNotFoundException("Artikel mit Id " + artikelId + " wurde nicht gefunden"));

        existingArtikel.setName(artikel.getName());

        return artikelRepo.save(existingArtikel);
    }


    public Artikel entferneBenutzerVonArtikel(Long artikelId) {
        Artikel existingArtikel = artikelRepo.findById(artikelId)
                .orElseThrow(() -> new ResourceNotFoundException("Artikel mit Id " + artikelId + " wurde nicht gefunden"));

        existingArtikel.setBenutzer(null);

        return artikelRepo.save(existingArtikel);
    }
    // alte version
//    public Artikel aktualisiereArtikel(Long artikelId, Artikel artikel) {
//        return artikelRepo.findById(artikelId)
//                .map(artikel1 -> {
//                    artikel1.setName(artikel.getName());
//                    artikel1.setBenutzer(artikel.getBenutzer());
//                    artikel1.setAusflug(artikel.getAusflug());
//                    return artikelRepo.save(artikel1);
//                })
//                .orElseThrow(() -> new ResourceNotFoundException("Artikel mit Id " + artikelId + " wurde nicht gefunden"));
//    }

    public Artikel gibArtikelDurchId(Long artikelId) {
        return artikelRepo.findById(artikelId)
                .orElseThrow(() -> new ResourceNotFoundException("Artikel mit Id " + artikelId + " wurde nicht gefunden"));
    }

    public Artikel hinzufuegeBenutzerZuArtikel(Long artikelId, Long benutzerId) {
        Benutzer benutzer = benutzerRepo.findById(benutzerId)
                .orElseThrow(() -> new ResourceNotFoundException("Benutzer nicht gefunden"));

        Artikel existingArtikel = artikelRepo.findById(artikelId)
                .orElseThrow(() -> new ResourceNotFoundException("Artikel mit Id " + artikelId + " wurde nicht gefunden"));

        existingArtikel.setBenutzer(benutzer);
        return artikelRepo.save(existingArtikel);
    }
}
