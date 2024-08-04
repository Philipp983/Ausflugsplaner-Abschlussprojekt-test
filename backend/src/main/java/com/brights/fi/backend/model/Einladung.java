package com.brights.fi.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Einladung {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "benutzer_id")
	@JsonIgnoreProperties("einladungen")
	private Benutzer benutzer;

	@ManyToOne
	@JoinColumn(name = "ausflug_id")
	@JsonIgnoreProperties({"teilnehmerListe", "sachenZumMitbringen", "hauptBild"})
	private Ausflug ausflug;

	//Vielleicht kein Ausflug, sondern direkt ausflugId
//	@Column(name = "ausflug_id")
//	private Long ausflugId;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private EinladungStatus status;

	@Column(name = "erstellungsdatum")
	private LocalDateTime erstellungsdatum;

	public enum EinladungStatus {
		AUSSTEHEND, ANGENOMMEN, ABGELEHNT
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Einladung)) return false;
		Einladung einladung = (Einladung) o;
		return Objects.equals(benutzer.getId(), einladung.benutzer.getId()) &&
				Objects.equals(ausflug.getId(), einladung.ausflug.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(benutzer.getId(), ausflug.getId());
	}
}
