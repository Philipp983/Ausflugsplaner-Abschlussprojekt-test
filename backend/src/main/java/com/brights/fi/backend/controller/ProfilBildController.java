package com.brights.fi.backend.controller;

import com.brights.fi.backend.model.ProfilBild;
import com.brights.fi.backend.service.ProfilBildService;
import com.brights.fi.backend.util.BildUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profilbild")
public class ProfilBildController {


	private final ProfilBildService profilBildService;

	public ProfilBildController(ProfilBildService profilBildService) {
		this.profilBildService = profilBildService;
	}

	@PostMapping("/benutzer/{benutzerId}/upload")
	public ResponseEntity<?> upsertProfilBild(
			@PathVariable Long benutzerId,
			@RequestParam("image") MultipartFile file) throws IOException {
		ProfilBild result = profilBildService.upsertProfilBild(benutzerId, file);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PostMapping("/benutzer/{benutzerId}/url")
	public ResponseEntity<?> upsertProfilBildUrl(
			@PathVariable Long benutzerId,
			@RequestParam("imageUrl") String imageUrl) {
		ProfilBild result = profilBildService.upsertProfilBildUrl(benutzerId, imageUrl);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
// more "flexible" approach

	@GetMapping("/benutzer/{benutzerId}")
	public ResponseEntity<?> getProfilBild(
			@PathVariable Long benutzerId) {
		ProfilBild profilBild = profilBildService.getProfilBild(benutzerId);
		if (profilBild == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kein Profilbild gefunden");
		}

		if (profilBild.getImageType() == ProfilBild.ImageType.URL) {
			return ResponseEntity.status(HttpStatus.OK).body(profilBild.getImageUrl());
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.parseMediaType(profilBild.getContentType()))
					.body(BildUtil.decompressImage(profilBild.getImageData()));
		}
	}


//	@GetMapping("/benutzer/{benutzerId}")
//	public ResponseEntity<?> getProfilBild(@PathVariable Long benutzerId) {
//		byte[] imageData = profilBildService.getProfilBildData(benutzerId);
//		if (imageData != null) {
//			return ResponseEntity.status(HttpStatus.OK)
//					.contentType(MediaType.IMAGE_JPEG)
//					.body(imageData);
//		} else {
//			String imageUrl = profilBildService.getProfilBildUrl(benutzerId);
//			if (imageUrl != null) {
//				return ResponseEntity.status(HttpStatus.OK).body(imageUrl);
//			} else {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No profile image found");
//			}
//		}
//	}
}
