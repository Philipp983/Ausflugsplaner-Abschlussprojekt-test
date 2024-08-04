package com.brights.fi.backend.repositories;

import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.model.Einladung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EinladungRepo extends JpaRepository<Einladung, Long> {


	boolean existsByBenutzerAndAusflug(Benutzer benutzer, Ausflug ausflug);
}
