package com.brights.fi.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class EinladungSimpel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "benutzer_id")
	private Benutzer benutzer;

	@Column(name = "ausflug_id")
	private Long ausflugId;

	@Column(name = "erstellungsdatum")
	private LocalDateTime erstellungsdatum;

	private Long hinzuzufuegenderTeilnehmerId;

	@Enumerated(EnumType.STRING)
	private ArtDerEinladung art;

	private String nameDesAusflugs;

	private String nameDesTeilnehmers;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EinladungSimpel)) return false;
		EinladungSimpel einladung = (EinladungSimpel) o;
		return Objects.equals(benutzer.getId(), einladung.benutzer.getId()) &&
				Objects.equals(ausflugId, einladung.ausflugId) &&
				Objects.equals(hinzuzufuegenderTeilnehmerId, einladung.hinzuzufuegenderTeilnehmerId) &&
				art == einladung.art;
	}

	@Override
	public int hashCode() {
		return Objects.hash(benutzer.getId(), ausflugId, hinzuzufuegenderTeilnehmerId, art);
	}

	public enum ArtDerEinladung{
		EINLADUNG, ANFRAGE
	}
}
