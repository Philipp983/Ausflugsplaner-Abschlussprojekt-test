package com.brights.fi.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "ProfilBild")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfilBild {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ImageType imageType;

	private String imageUrl;

	private String contentType;

	@Lob
	@Column(name = "imagedata", length = 1000000000)
	@JsonIgnore
	private byte[] imageData;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="benutzer_id")
	@JsonIgnore
	private Benutzer benutzer;

	public enum ImageType {
		URL, FILE
	}
}
