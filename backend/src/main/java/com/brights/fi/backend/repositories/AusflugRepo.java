package com.brights.fi.backend.repositories;

import com.brights.fi.backend.model.Ausflug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AusflugRepo extends JpaRepository<Ausflug, Long> {
}
