package com.brights.fi.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Benutzer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name")
	@NotNull(message = "Benutzername darf nicht leer sein")
	@Size(min = 3, max = 40, message = "Benutzername muss zwischen 3 und 40 Zeichen lang sein")
	private String userName;

	@Column(name = "user_email")
	@Email(message = "Ungültige E-Mail-Adresse")
	@NotNull(message = "E-Mail-Adresse darf nicht leer sein")
	private String userEmail;

//	@Column(name = "beschreibung")
//	private String beschreibung;

	@Column(name = "passwort")
	@NotNull(message = "Passwort darf nicht leer sein")
	@Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein")
	private String passwort;

	@Column(name = "beschreibung", columnDefinition = "TEXT")
	@Size(max = 2000, message = "Beschreibung darf nicht mehr als 1000 Zeichen lang sein")
	private String beschreibung;

	@ManyToMany(mappedBy = "teilnehmerListe")
	@JsonIgnoreProperties("teilnehmerListe")
	private Set<Ausflug> ausfluege = new HashSet<>();

	@OneToMany(mappedBy = "benutzer", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("benutzer")
	private List<Artikel> sachenZumMitbringen = new ArrayList<>();

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "benutzer")
	private ProfilBild profilBild;

//	@OneToMany(mappedBy = "benutzer", cascade = CascadeType.ALL, orphanRemoval = true)
//	@JsonIgnoreProperties("benutzer")
//	@JsonIgnore
//	private Set<Einladung> einladungen = new HashSet<>();

	@OneToMany(mappedBy = "benutzer", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("benutzer")
	private Set<EinladungSimpel> einladungenSimpel = new HashSet<>();

}
