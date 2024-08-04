package com.brights.fi.backend.service;

import com.brights.fi.backend.exception.BusinessLogicException;
import com.brights.fi.backend.exception.ResourceNotFoundException;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.model.ProfilBild;
import com.brights.fi.backend.repositories.BenutzerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BenutzerService {

	private BenutzerRepo benutzerRepo;

	@Autowired
	public BenutzerService(BenutzerRepo benutzerRepo) {
		this.benutzerRepo = benutzerRepo;
	}

	public Benutzer speichereBenutzer(Benutzer benutzer) {
		if (benutzerRepo.findByUserEmail(benutzer.getUserEmail()).isPresent()) {
			throw new BusinessLogicException("Ein Benutzer mit dieser E-Mail existiert bereits");
		}
		if (benutzer.getProfilBild() == null) {
			benutzer.setProfilBild(erstelleStandardProfilBild());
		} else {
			ProfilBild profilBild = benutzer.getProfilBild();
			if ((profilBild.getImageUrl() == null || profilBild.getImageUrl().isEmpty()) &&
							(profilBild.getImageData() == null || profilBild.getImageData().length == 0)) {
				benutzer.setProfilBild(erstelleStandardProfilBild());
			} else {
				profilBild.setBenutzer(benutzer);
				if (profilBild.getImageUrl() != null && !profilBild.getImageUrl().isEmpty()) {
					profilBild.setImageType(ProfilBild.ImageType.URL);
				} else {
					profilBild.setImageType(ProfilBild.ImageType.FILE);
				}
			}
		}

		return benutzerRepo.save(benutzer);
	}

	public Benutzer speichereBenutzer2(Benutzer benutzer) {
		if (benutzerRepo.findByUserEmail(benutzer.getUserEmail()).isPresent()) {
			throw new BusinessLogicException("Ein Benutzer mit dieser E-Mail existiert bereits");
		}

		ProfilBild profilBild = benutzer.getProfilBild();
		if (profilBild == null) {
			profilBild = erstelleStandardProfilBild();
		} else if ((profilBild.getImageUrl() == null || profilBild.getImageUrl().isEmpty()) &&
						(profilBild.getImageData() == null || profilBild.getImageData().length == 0)) {
			profilBild = erstelleStandardProfilBild();
		} else {
			if (profilBild.getImageUrl() != null && !profilBild.getImageUrl().isEmpty()) {
				profilBild.setImageType(ProfilBild.ImageType.URL);
			} else {
				profilBild.setImageType(ProfilBild.ImageType.FILE);
			}
		}

		profilBild.setBenutzer(benutzer);
		benutzer.setProfilBild(profilBild);

		return benutzerRepo.save(benutzer);
	}

	public Benutzer findeBenutzerDurchId(Long benutzerId) {
		return benutzerRepo.findById(benutzerId)
						.orElseThrow(() -> new ResourceNotFoundException("Benutzer mit ID " + benutzerId + " nicht gefunden"));
	}

	public List<Benutzer> gibAlleBenutzer() {
		return benutzerRepo.findAll();
	}

	public Benutzer aktualisiereBenutzer(Long benutzerId, Benutzer benutzer) {
		return benutzerRepo.findById(benutzerId)
						.map(existingBenutzer -> {
							if (benutzer.getUserName() != null) {
								existingBenutzer.setUserName(benutzer.getUserName());
							}
							if (benutzer.getUserEmail() != null) {
								existingBenutzer.setUserEmail(benutzer.getUserEmail());
							}
							if (benutzer.getPasswort() != null) {
								existingBenutzer.setPasswort(benutzer.getPasswort());
							}
							if (benutzer.getBeschreibung() != null) {
								existingBenutzer.setBeschreibung(benutzer.getBeschreibung());
							}

							return benutzerRepo.save(existingBenutzer);
						})
						.orElseThrow(() -> new ResourceNotFoundException("Benutzer mit ID " + benutzerId + " nicht gefunden"));
	}

	public void loescheBenutzer(Long benutzerId) {
		if (!benutzerRepo.existsById(benutzerId)) {
			throw new ResourceNotFoundException("Benutzer mit ID " + benutzerId + " nicht gefunden");
		}
		benutzerRepo.deleteById(benutzerId);
	}

	public Benutzer login(String userEmail, String passwort) {
		Benutzer benutzer = benutzerRepo.findByUserEmail(userEmail)
						.orElseThrow(() -> new ResourceNotFoundException("Benutzer nicht gefunden"));

		if (!benutzer.getPasswort().equals(passwort)) {
			throw new BusinessLogicException("Falsches Passwort");
		}

		return benutzer;
	}
	public void aktualisierePasswort(Long benutzerId, String currentPassword, String newPassword, String newPasswordConfirm) {
		Benutzer benutzer = benutzerRepo.findById(benutzerId)
						.orElseThrow(() -> new ResourceNotFoundException("Benutzer mit ID " + benutzerId + " nicht gefunden"));

		if (!benutzer.getPasswort().equals(currentPassword)) {
			throw new BusinessLogicException("Falsches Passwort");
		}

		if (!newPassword.equals(newPasswordConfirm)) {
			throw new BusinessLogicException("Neue Passwörter stimmen nicht überein");
		}

		benutzer.setPasswort(newPassword);
		benutzerRepo.save(benutzer);
	}

	// this method to help mapping the string usernName we got from the frontend into Benutzer object
	public Benutzer findeBenutzerDurchUserName(String userName) {
		return benutzerRepo.findByUserName(userName);
	}

	// return a list of Benutzer, matching this userName
	public Set<Benutzer> searchBenutzerByName(String userName) {
		return benutzerRepo.findByNameContaining(userName);
	}

	private ProfilBild erstelleStandardProfilBild() {
		ProfilBild profilBild = new ProfilBild();
		profilBild.setImageUrl("https://images.nightcafe.studio//assets/profile.png");
		profilBild.setImageType(ProfilBild.ImageType.URL);
		profilBild.setContentType("image/png");
		return profilBild;
	}

	public Benutzer findById(String id) {
		return benutzerRepo.findById(Long.valueOf(id))
						.orElseThrow(() -> new BusinessLogicException("Benutzer nicht gefunden"));
	}
}
