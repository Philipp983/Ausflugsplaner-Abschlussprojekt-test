package com.brights.fi.backend.repositories;

import com.brights.fi.backend.model.Benutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BenutzerRepo extends JpaRepository<Benutzer, Long> {
	Optional<Benutzer> findByUserEmail(String userEmail);

	@Query("SELECT b FROM Benutzer b WHERE b.userName = :userName")
	Benutzer findByUserName(@Param("userName") String userName);

	@Query("SELECT b FROM Benutzer b WHERE b.userName LIKE %:userName%")
	Set<Benutzer> findByNameContaining(@Param("userName") String userName);

	Optional<Benutzer> findById(Long Id);
}
