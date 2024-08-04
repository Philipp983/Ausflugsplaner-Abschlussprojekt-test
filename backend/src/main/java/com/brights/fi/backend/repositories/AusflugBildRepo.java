package com.brights.fi.backend.repositories;


import com.brights.fi.backend.model.AusflugBild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AusflugBildRepo extends JpaRepository<AusflugBild, Long> {
	Optional<AusflugBild> findByName(String fileName);
}
