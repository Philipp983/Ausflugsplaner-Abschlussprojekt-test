package com.brights.fi.backend.service;

import com.brights.fi.backend.exception.ResourceNotFoundException;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.model.ProfilBild;
import com.brights.fi.backend.repositories.BenutzerRepo;
import com.brights.fi.backend.repositories.ProfilBildRepo;
import com.brights.fi.backend.util.BildUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProfilBildService {

	private final BenutzerRepo benutzerRepo;
	private ProfilBildRepo profilBildRepo;

	@Autowired
	public ProfilBildService(ProfilBildRepo profilBildRepo, BenutzerRepo benutzerRepo) {
		this.profilBildRepo = profilBildRepo;
		this.benutzerRepo = benutzerRepo;
	}

	@Transactional
	public ProfilBild upsertProfilBild(Long benutzerId, MultipartFile file) throws IOException {
		Benutzer benutzer = benutzerRepo.findById(benutzerId)
				.orElseThrow(() -> new ResourceNotFoundException("Benutzer konnte nicht gefunden werden"));

		ProfilBild profilBild = benutzer.getProfilBild();
		if (profilBild == null) {
			profilBild = new ProfilBild();
			profilBild.setBenutzer(benutzer);
		}

		profilBild.setContentType(file.getContentType());
		profilBild.setImageType(ProfilBild.ImageType.FILE);
		profilBild.setImageData(BildUtil.compressImage(file.getBytes()));
		profilBild.setImageUrl(null);

		benutzer.setProfilBild(profilBild);
		benutzerRepo.save(benutzer);
		return profilBildRepo.save(profilBild);

//		return "File uploaded successfully : " + file.getOriginalFilename();
	}

	@Transactional
	public ProfilBild upsertProfilBildUrl(Long benutzerId, String imageUrl) {
		Benutzer benutzer = benutzerRepo.findById(benutzerId)
				.orElseThrow(() -> new ResourceNotFoundException("Benutzer konnte nicht gefunden werden"));

		ProfilBild profilBild = benutzer.getProfilBild();
		if (profilBild == null) {
			profilBild = new ProfilBild();
			profilBild.setBenutzer(benutzer);
		}

		profilBild.setImageType(ProfilBild.ImageType.URL);
		profilBild.setImageUrl(imageUrl);
		profilBild.setImageData(null);

		benutzer.setProfilBild(profilBild);
		benutzerRepo.save(benutzer);
		return profilBildRepo.save(profilBild);

//		return "Image URL saved successfully";
	}

	public byte[] getProfilBildData(Long benutzerId) {
		Benutzer benutzer = benutzerRepo.findById(benutzerId)
				.orElseThrow(() -> new ResourceNotFoundException("Benutzer konnte nicht gefunden werden"));

		ProfilBild profilBild = benutzer.getProfilBild();
		if (profilBild == null || profilBild.getImageType() == ProfilBild.ImageType.URL) {
			return null;
		}

		return BildUtil.decompressImage(profilBild.getImageData());
	}

	public String getProfilBildUrl(Long benutzerId) {
		Benutzer benutzer = benutzerRepo.findById(benutzerId)
				.orElseThrow(() -> new ResourceNotFoundException("Benutzer konnte nicht gefunden werden"));

		ProfilBild profilBild = benutzer.getProfilBild();
		if (profilBild == null || profilBild.getImageType() == ProfilBild.ImageType.FILE) {
			return null;
		}

		return profilBild.getImageUrl();
	}

	public ProfilBild getProfilBild(Long benutzerId) {
		Optional<ProfilBild> byId = profilBildRepo.findById(benutzerId);
		return byId.orElse(null);
	}
}
