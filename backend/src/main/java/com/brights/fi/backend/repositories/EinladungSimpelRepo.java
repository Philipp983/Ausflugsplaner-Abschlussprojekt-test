package com.brights.fi.backend.repositories;

import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.model.EinladungSimpel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EinladungSimpelRepo extends JpaRepository<EinladungSimpel, Long> {
	boolean existsByBenutzerAndAusflugId(Benutzer benutzer, Long ausflugId);
}
