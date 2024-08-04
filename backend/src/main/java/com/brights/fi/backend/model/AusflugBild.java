package com.brights.fi.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AusflugBild")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AusflugBild {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String type;

	@Enumerated(EnumType.STRING)
	private ImageType imageType;

	private String contentType;


	@Column(name = "image_url")
	private String imageUrl;

	@Lob
	@Column(name = "imagedata", length = 1000000000)
	@JsonIgnore
	private byte[] imageData;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ausflug_id", nullable = false)
	@JsonIgnore
	private Ausflug ausflug;

	@Transient
	public boolean isUrlImage() {
		return imageUrl != null && !imageUrl.isEmpty();
	}

	public enum ImageType {
		URL, FILE
	}
}
