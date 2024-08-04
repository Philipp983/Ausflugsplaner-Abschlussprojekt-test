package com.brights.fi.backend.service;


import com.brights.fi.backend.exception.ResourceNotFoundException;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.AusflugBild;
import com.brights.fi.backend.model.Benutzer;
import com.brights.fi.backend.model.ProfilBild;
import com.brights.fi.backend.repositories.AusflugBildRepo;
import com.brights.fi.backend.repositories.AusflugRepo;
import com.brights.fi.backend.util.BildUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class AusflugBildService {

	private final AusflugRepo ausflugRepo;
	private final AusflugBildRepo ausflugBildRepo;

	@Autowired
	public AusflugBildService(AusflugRepo ausflugRepo, AusflugBildRepo ausflugBildRepo) {
		this.ausflugRepo = ausflugRepo;
		this.ausflugBildRepo = ausflugBildRepo;
	}

	@Transactional
	public Ausflug upsertAusflugBild(Long ausflugId, MultipartFile file) throws IOException {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));

		AusflugBild ausflugBild = ausflug.getHauptBild();
		boolean posted = false;

		if (ausflugBild == null) {
			posted = true;
			ausflugBild = new AusflugBild();
			ausflugBild.setAusflug(ausflug);
		}

		ausflugBild.setContentType(file.getContentType());
		ausflugBild.setImageType(AusflugBild.ImageType.FILE);
		ausflugBild.setImageData(BildUtil.compressImage(file.getBytes()));
		ausflugBild.setImageUrl(null);

		ausflugBildRepo.save(ausflugBild);
		ausflug.setHauptBild(ausflugBild);
		return ausflugRepo.save(ausflug);

//		return 	posted ? "Datei erfolgreich hochgeladen : " + file.getOriginalFilename()
//				: "Datei erfolgreich geändert :" + file.getOriginalFilename();
	}

	@Transactional
	public Ausflug upsertAusflugBildUrl(Long ausflugId, String imageUrl) {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));

		AusflugBild ausflugBild = ausflug.getHauptBild();
		boolean posted = false;

		if (ausflugBild == null) {
			posted = true;
			ausflugBild = new AusflugBild();
			ausflugBild.setAusflug(ausflug);
		}

		ausflugBild.setImageType(AusflugBild.ImageType.URL);
		ausflugBild.setContentType("text/plain");
		ausflugBild.setImageUrl(imageUrl);
		ausflugBild.setImageData(null);

		ausflugBildRepo.save(ausflugBild);
		ausflug.setHauptBild(ausflugBild);
		return ausflugRepo.save(ausflug);

//		return 	posted ? "Bild Url erfolgreich hinzugefügt"
//				: "Bild Url erfolgreich geändert";
	}

	@Transactional
	public String hochladeAusflugBild(Long ausflugId, MultipartFile file) throws IOException {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));

		if (ausflug.getHauptBild() != null) {
			throw new IllegalStateException("Ausflug hat bereits ein Bild. Bitte verwenden Sie die Update-Methode.");
		}

		AusflugBild ausflugBild = AusflugBild.builder()
				.contentType(file.getContentType())
				.name(file.getOriginalFilename())
				.type(file.getContentType())
				.imageData(BildUtil.compressImage(file.getBytes()))
				.ausflug(ausflug)
				.build();

		ausflugBild = ausflugBildRepo.save(ausflugBild);
		ausflug.setHauptBild(ausflugBild);
		ausflugRepo.save(ausflug);

		return "Bild erfolgreich hochgeladen: " + file.getOriginalFilename();
	}

	@Transactional
	public String speichereAusflugBildUrl(Long ausflugId, String imageUrl) {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));

		if (ausflug.getHauptBild() != null) {
			throw new IllegalStateException("Ausflug hat bereits ein Bild. Bitte verwenden Sie die Update-Methode.");
		}

		AusflugBild ausflugBild = AusflugBild.builder()
				.imageUrl(imageUrl)
				.ausflug(ausflug)
				.build();
		ausflug.setHauptBild(ausflugBild);

		ausflugBildRepo.save(ausflugBild);
		ausflugRepo.save(ausflug);

		return "Bild-URL erfolgreich gespeichert";
	}

//	public byte[] herunterladeAusflugBild(String fileName) {
//		Optional<AusflugBild> dbAusflugBild = ausflugBildRepo.findByName(fileName);
//		return BildUtil.decompressImage(dbAusflugBild.get().getImageData());
//	}

	public Object herunterladeAusflugBildDurchAusflugId(Long ausflugId) {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));
		if (ausflug.getHauptBild() == null) {
			throw new ResourceNotFoundException("Kein Bild für diesen Ausflug gefunden");
		}
		if (ausflug.getHauptBild().isUrlImage()) {
			return ausflug.getHauptBild().getImageUrl();
		} else {
			return BildUtil.decompressImage(ausflug.getHauptBild().getImageData());
		}
	}

	@Transactional
	public String updateAusflugBildMitUpload(Long ausflugId, MultipartFile file) throws IOException {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));

		AusflugBild ausflugBild;
		if (ausflug.getHauptBild() != null) {
			ausflugBild = ausflug.getHauptBild();
		} else {
			ausflugBild = new AusflugBild();
			ausflugBild.setAusflug(ausflug);
		}

		ausflugBild.setContentType(file.getContentType());
		ausflugBild.setName(file.getOriginalFilename());
		ausflugBild.setType(file.getContentType());
		ausflugBild.setImageData(BildUtil.compressImage(file.getBytes()));
		ausflugBild.setImageUrl(null);

		ausflugBildRepo.save(ausflugBild);
		ausflug.setHauptBild(ausflugBild);
		ausflugRepo.save(ausflug);

		return "Bild erfolgreich hochgeladen : " + file.getOriginalFilename();
	}

	@Transactional
	public String updateAusflugBildDurchUrl(Long ausflugId, String imageUrl) {
		Ausflug ausflug = ausflugRepo.findById(ausflugId)
				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));

		AusflugBild ausflugBild;
		if (ausflug.getHauptBild() != null) {
			// Update existing AusflugBild
			ausflugBild = ausflug.getHauptBild();
			ausflugBild.setImageUrl(imageUrl);
			ausflugBild.setImageData(null);  // Clear any existing image data
			ausflugBild.setName(null);
			ausflugBild.setType(null);
		} else {
			// Create new AusflugBild
			ausflugBild = AusflugBild.builder()
					.imageUrl(imageUrl)
					.ausflug(ausflug)
					.build();
			ausflug.setHauptBild(ausflugBild);
		}

		ausflugBildRepo.save(ausflugBild);
		ausflugRepo.save(ausflug);

		return "Bild URL erfolgreich geändert";
	}

	public AusflugBild bekommeAusflugBild(Long ausflugId) {
		Optional<AusflugBild> byId = ausflugBildRepo.findById(ausflugId);
		return byId.orElse(null);

	}


//  Maybe using upsert approach? if not, clear distinction of put and post, then put needs this check:
//	@Transactional
//	public String updateAusflugBildMitUpload(Long ausflugId, MultipartFile file) throws IOException {
//		Ausflug ausflug = ausflugRepo.findById(ausflugId)
//				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));
//
//		if (ausflug.getHauptBild() == null) {
//			throw new IllegalStateException("Kein bestehendes Bild zum Aktualisieren. Bitte verwenden Sie die Hochlade-Methode.");
//		}
//
//		AusflugBild ausflugBild = ausflug.getHauptBild();
//		ausflugBild.setName(file.getOriginalFilename());
//		ausflugBild.setType(file.getContentType());
//		ausflugBild.setImageData(BildUtil.compressImage(file.getBytes()));
//		ausflugBild.setImageUrl(null);
//
//		ausflugBildRepo.save(ausflugBild);
//
//		return "Bestehendes Bild aktualisiert: " + file.getOriginalFilename();
//	}

// Upsert for file
//	@Transactional
//	public String upsertAusflugBildMitUpload(Long ausflugId, MultipartFile file) throws IOException {
//		Ausflug ausflug = ausflugRepo.findById(ausflugId)
//				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));
//
//		AusflugBild ausflugBild;
//		boolean isNewImage = false;
//
//		if (ausflug.getHauptBild() == null) {
//			ausflugBild = new AusflugBild();
//			ausflugBild.setAusflug(ausflug);
//			isNewImage = true;
//		} else {
//			ausflugBild = ausflug.getHauptBild();
//		}
//
//		ausflugBild.setName(file.getOriginalFilename());
//		ausflugBild.setType(file.getContentType());
//		ausflugBild.setImageData(BildUtil.compressImage(file.getBytes()));
//		ausflugBild.setImageUrl(null); // Clear any existing URL
//
//		ausflugBildRepo.save(ausflugBild);
//		ausflug.setHauptBild(ausflugBild);
//		ausflugRepo.save(ausflug);
//
//		return isNewImage
//				? "Neues Bild hochgeladen: " + file.getOriginalFilename()
//				: "Bestehendes Bild aktualisiert: " + file.getOriginalFilename();
//	}

	// Upsert for url
//	@Transactional
//	public String upsertAusflugBildDurchUrl(Long ausflugId, String imageUrl) {
//		Ausflug ausflug = ausflugRepo.findById(ausflugId)
//				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));
//
//		AusflugBild ausflugBild;
//		boolean isNewImage = false;
//
//		if (ausflug.getHauptBild() == null) {
//			ausflugBild = new AusflugBild();
//			ausflugBild.setAusflug(ausflug);
//			isNewImage = true;
//		} else {
//			ausflugBild = ausflug.getHauptBild();
//		}
//
//		ausflugBild.setImageUrl(imageUrl);
//		ausflugBild.setImageData(null);  // Clear any existing image data
//		ausflugBild.setName(null);
//		ausflugBild.setType(null);
//
//		ausflugBildRepo.save(ausflugBild);
//		ausflug.setHauptBild(ausflugBild);
//		ausflugRepo.save(ausflug);
//
//		return isNewImage
//				? "Neue Bild-URL gespeichert: " + imageUrl
//				: "Bestehende Bild-URL aktualisiert: " + imageUrl;
//	}

	//	@Transactional
//	public String updateAusflugBildMitUpload(Long ausflugId, MultipartFile file) throws IOException {
//		Ausflug ausflug = ausflugRepo.findById(ausflugId)
//				.orElseThrow(() -> new ResourceNotFoundException("Ausflug konnte nicht gefunden werden"));
//
//		if (ausflug.getHauptBild() != null) {
//			ausflugBildRepo.delete(ausflug.getHauptBild());
//		}
//
//		AusflugBild ausflugBild = AusflugBild.builder()
//				.name(file.getOriginalFilename())
//				.type(file.getContentType())
//				.imageData(BildUtil.compressImage(file.getBytes()))
//				.ausflug(ausflug)
//				.build();
//
//		ausflugBild = ausflugBildRepo.save(ausflugBild);
//		ausflug.setHauptBild(ausflugBild);
//		ausflugRepo.save(ausflug);
//
//		return "Bild erfolgreich hochgeladen : " + file.getOriginalFilename();
//	}
}
