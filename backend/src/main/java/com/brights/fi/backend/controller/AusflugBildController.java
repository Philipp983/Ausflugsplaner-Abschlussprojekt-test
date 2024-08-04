package com.brights.fi.backend.controller;


import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.AusflugBild;
import com.brights.fi.backend.service.AusflugBildService;
import com.brights.fi.backend.util.BildUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@RestController
@RequestMapping("/api/ausflugbild")
public class AusflugBildController {

	private final AusflugBildService ausflugBildService;

	public AusflugBildController(AusflugBildService ausflugBildService) {
		this.ausflugBildService = ausflugBildService;
	}

	@GetMapping("/ausflug/{ausflugId}")
	public ResponseEntity<?> herunterladeAusflugBildDurchAusflugId(
			@PathVariable Long ausflugId) {
		AusflugBild ausflugBild = ausflugBildService.bekommeAusflugBild(ausflugId);
		if (ausflugBild == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kein Ausflugbild gefunden");
		}
		if (ausflugBild.getImageType() == AusflugBild.ImageType.URL) {
			return ResponseEntity.status(HttpStatus.OK).body(ausflugBild.getImageUrl());
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.parseMediaType(ausflugBild.getContentType()))
					.body(BildUtil.decompressImage(ausflugBild.getImageData()));
		}
	}

	@GetMapping("/proxy-image")
	public ResponseEntity<byte[]> proxyImage(@RequestParam String url) throws IOException {
		URL imageUrl = new URL(url);
		try (InputStream in = imageUrl.openStream()) {
			byte[] imageBytes = in.readAllBytes();

			// Try to determine the content type
			String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
			if (contentType == null) {
				// If we can't determine the type, default to a generic binary type
				contentType = "application/octet-stream";
			}

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.body(imageBytes);
		}
	}

	@PostMapping("/ausflug/{ausflugId}/upload")
	public ResponseEntity<?> upsertProfilBild(
			@PathVariable Long ausflugId,
			@RequestParam("image") MultipartFile file) throws IOException {
		Ausflug result = ausflugBildService.upsertAusflugBild(ausflugId, file);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PostMapping("/ausflug/{ausflugId}/url")
	public ResponseEntity<?> upsertProfilBildUrl(
			@PathVariable Long ausflugId,
			@RequestParam("imageUrl") String imageUrl) {
		Ausflug result = ausflugBildService.upsertAusflugBildUrl(ausflugId, imageUrl);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}



//	Alte Variante, getrennt put post

//	@PostMapping("/ausflug/{ausflugId}/upload")
//	public ResponseEntity<?> hochladeAusflugBild(@PathVariable Long ausflugId, @RequestParam("image") MultipartFile file) throws IOException {
//		String uploadImage = ausflugBildService.hochladeAusflugBild(ausflugId, file);
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(uploadImage);
//	}
//
//	@PostMapping("/ausflug/{ausflugId}/url")
//	public ResponseEntity<?> speichereAusflugBildUrl(@PathVariable Long ausflugId, @RequestParam("imageUrl") String imageUrl) {
//		String result = ausflugBildService.speichereAusflugBildUrl(ausflugId, imageUrl);
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(result);
//	}
//
//	@PutMapping("/ausflug/{ausflugId}/update")
//	public ResponseEntity<?> anpasseAusflugBildDurchUpload(@PathVariable Long ausflugId, @RequestParam("image") MultipartFile file) throws IOException {
//		String uploadImage = ausflugBildService.updateAusflugBildMitUpload(ausflugId, file);
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(uploadImage);
//	}
//
//	@PutMapping("/ausflug/{ausflugId}/url")
//	public ResponseEntity<?> anpasseAusflugBildDurchUrl(@PathVariable Long ausflugId, @RequestParam("imageUrl") String imageUrl) throws IOException {
//		String result = ausflugBildService.updateAusflugBildDurchUrl(ausflugId, imageUrl);
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(result);
//	}

	//	@GetMapping("/{fileName}")
//	public ResponseEntity<?> herunterladeAusflugBild(@PathVariable String fileName) {
//		byte[] imageData = ausflugBildService.herunterladeAusflugBild(fileName);
//		return ResponseEntity.status(HttpStatus.OK)
//				.contentType(MediaType.valueOf("image/png"))
//				.body(imageData);
//	}

	//	@GetMapping("/ausflug/{ausflugId}")
//	public ResponseEntity<?> herunterladeAusflugBildDurchAusflugId(@PathVariable Long ausflugId) {
//		Object imageData = ausflugBildService.herunterladeAusflugBildDurchAusflugId(ausflugId);
//		if (imageData instanceof String) {
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(imageData);
//		} else if (imageData instanceof byte[]) {
//			return ResponseEntity.status(HttpStatus.OK)
//					.contentType(MediaType.valueOf("image/png"))
//					.body(imageData);
//		} else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No image found");
//		}
//	}


}
