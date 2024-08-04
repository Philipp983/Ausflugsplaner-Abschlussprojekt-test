package com.brights.fi.backend.DTO;

import com.brights.fi.backend.model.AusflugBild;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class AusflugAntwortDTO {
	private Long id;
	private String titel;
	private String beschreibung;
	private String reiseziel;
	private LocalDateTime ausflugsdatum;
	private LocalDateTime enddatum;
	private LocalDateTime editierdatum;
	private Integer teilnehmerAnzahl;
	private List<Long> teilnehmerIds;
	private List<String> nameDerTeilnehmer;
	private String erstellerDesAusflugsId;
	private List<ArtikelSimpelDTO> sachenZumMitbringen;
	private String imageUrl;
	private AusflugBild.ImageType imageType;

	public AusflugAntwortDTO() {
	}

	public AusflugAntwortDTO(Long id, String titel, String beschreibung, String reiseziel, LocalDateTime ausflugsdatum, LocalDateTime enddatum, LocalDateTime editierdatum, Integer teilnehmerAnzahl, List<Long> teilnehmerIds, List<String> nameDerTeilnehmer, String erstellerDesAusflugsId, List<ArtikelSimpelDTO> sachenZumMitbringen, String imageUrl, AusflugBild.ImageType imageType) {
		this.id = id;
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.reiseziel = reiseziel;
		this.ausflugsdatum = ausflugsdatum;
		this.enddatum = enddatum;
		this.editierdatum = editierdatum;
		this.teilnehmerAnzahl = teilnehmerAnzahl;
		this.teilnehmerIds = teilnehmerIds;
		this.nameDerTeilnehmer = nameDerTeilnehmer;
		this.erstellerDesAusflugsId = erstellerDesAusflugsId;
		this.sachenZumMitbringen = sachenZumMitbringen;
		this.imageUrl = imageUrl;
		this.imageType = imageType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getReiseziel() {
		return reiseziel;
	}

	public void setReiseziel(String reiseziel) {
		this.reiseziel = reiseziel;
	}

	public LocalDateTime getAusflugsdatum() {
		return ausflugsdatum;
	}

	public void setAusflugsdatum(LocalDateTime ausflugsdatum) {
		this.ausflugsdatum = ausflugsdatum;
	}

	public LocalDateTime getEnddatum() {
		return enddatum;
	}

	public void setEnddatum(LocalDateTime enddatum) {
		this.enddatum = enddatum;
	}

	public LocalDateTime getEditierdatum() {
		return editierdatum;
	}

	public void setEditierdatum(LocalDateTime editierdatum) {
		this.editierdatum = editierdatum;
	}

	public Integer getTeilnehmerAnzahl() {
		return teilnehmerAnzahl;
	}

	public void setTeilnehmerAnzahl(Integer teilnehmerAnzahl) {
		this.teilnehmerAnzahl = teilnehmerAnzahl;
	}

	public List<Long> getTeilnehmerIds() {
		return teilnehmerIds;
	}

	public void setTeilnehmerIds(List<Long> teilnehmerIds) {
		this.teilnehmerIds = teilnehmerIds;
	}

	public String getErstellerDesAusflugsId() {
		return erstellerDesAusflugsId;
	}

	public void setErstellerDesAusflugsId(String erstellerDesAusflugsId) {
		this.erstellerDesAusflugsId = erstellerDesAusflugsId;
	}

	public List<ArtikelSimpelDTO> getSachenZumMitbringen() {
		return sachenZumMitbringen;
	}

	public void setSachenZumMitbringen(List<ArtikelSimpelDTO> sachenZumMitbringen) {
		this.sachenZumMitbringen = sachenZumMitbringen;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public AusflugBild.ImageType getImageType() {
		return imageType;
	}

	public void setImageType(AusflugBild.ImageType imageType) {
		this.imageType = imageType;
	}

	public List<String> getNameDerTeilnehmer() {
		return nameDerTeilnehmer;
	}

	public void setNameDerTeilnehmer(List<String> nameDerTeilnehmer) {
		this.nameDerTeilnehmer = nameDerTeilnehmer;
	}
}
