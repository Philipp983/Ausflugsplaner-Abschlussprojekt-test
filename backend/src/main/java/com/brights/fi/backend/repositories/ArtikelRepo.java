package com.brights.fi.backend.repositories;

import com.brights.fi.backend.model.Artikel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtikelRepo extends JpaRepository<Artikel, Long> {
}
