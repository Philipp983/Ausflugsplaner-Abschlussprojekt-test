package com.brights.fi.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.lang.annotation.After;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
// alternative zu @Data
@NoArgsConstructor
@Table
public class Ausflug {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "titel")
	@NotNull(message = "Titel darf nicht leer sein")
	@Size(min = 3, max = 50, message = "Titel muss zwischen 3 und 50 Zeichen lang sein")
	private String titel;

	@Column(name = "beschreibung")
	@NotNull(message = "Beschreibung darf nicht leer sein")
	@Size(min = 10, max = 255, message = "Beschreibung muss zwischen 10 und 255 Zeichen lang sein")
	private String beschreibung;

	@Column(name = "reiseziel")
	@NotNull(message = "Reiseziel darf nicht leer sein")
	@Size(min = 3, max = 50, message = "Reiseziel muss zwischen 3 und 50 Zeichen lang sein")
	private String reiseziel;

	@Column(name = "ausflugsdatum")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@NotNull(message = "Ausflugsdatum darf nicht leer sein")
//	@Future(message = "Ausflugsdatum muss in der Vergangenheit oder Gegenwart liegen")
	private LocalDateTime ausflugsdatum;

	@Column(name = "enddatum")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@NotNull(message = "Enddatum darf nicht leer sein")
//	@Future(message = "Enddatum muss in der Zukunft liegen")
	private LocalDateTime enddatum;

	@Column(name = "editierdatum")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime editierdatum;

	@Column(name = "teilnehmer_anzahl")
	@NotNull(message = "Teilnehmeranzahl darf nicht leer sein")
	@Min(value = 1, message = "Mindestens 1 Teilnehmer muss angegeben werden")
	@Max(value = 20, message = "Maximale Teilnehmeranzahl ist 20")
	private Integer teilnehmerAnzahl;

	@ManyToMany
	@JoinTable(
			name = "ausflug_teilnehmer",
			joinColumns = @JoinColumn(name = "ausflug_id"),
			inverseJoinColumns = @JoinColumn(name = "benutzer_id")
	)
	@JsonIgnoreProperties({"ausfluege","sachenZumMitbringen"})
	private Set<Benutzer> teilnehmerListe = new HashSet<>();

	@Column(name = "ersteller_des_ausflugs")
	// TODO eventuell besser als Long definieren
	private String erstellerDesAusflugsId;

	@OneToMany(mappedBy = "ausflug", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"ausfluege","benutzer"})
	private List<Artikel> sachenZumMitbringen = new ArrayList<>();

	//AusflugBilder
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "ausflug")
	private AusflugBild hauptBild;

}
