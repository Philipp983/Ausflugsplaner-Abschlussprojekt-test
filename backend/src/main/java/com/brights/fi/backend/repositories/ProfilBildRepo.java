package com.brights.fi.backend.repositories;

import com.brights.fi.backend.model.ProfilBild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfilBildRepo extends JpaRepository<ProfilBild, Long> {

}
