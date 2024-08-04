package com.brights.fi.backend.DTO;

public class ArtikelUpdateAntwortDTO {

	private Long artikelId;
	private String name;
	private Long benutzerId;
	private Long ausflugId;

	public ArtikelUpdateAntwortDTO() {
	}

	public ArtikelUpdateAntwortDTO(Long artikelId, String text, Long benutzerId, Long ausflugId) {
		this.artikelId = artikelId;
		this.name = text;
		this.benutzerId = benutzerId;
		this.ausflugId = ausflugId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getArtikelId() {
		return artikelId;
	}

	public void setArtikelId(Long artikelId) {
		this.artikelId = artikelId;
	}

	public Long getAusflugId() {
		return ausflugId;
	}

	public void setAusflugId(Long ausflugId) {
		this.ausflugId = ausflugId;
	}

	public Long getBenutzerId() {
		return benutzerId;
	}

	public void setBenutzerId(Long benutzerId) {
		this.benutzerId = benutzerId;
	}
}
