package com.brights.fi.backend.DTO;

public class ArtikelSimpelDTO {

	private Long id;
	private String name;
	private Long benutzerId;
	private Long ausflugID;

	public ArtikelSimpelDTO() {
	}

	public ArtikelSimpelDTO(Long id, String name, Long benutzerId, Long ausflugID) {
		this.id = id;
		this.name = name;
		this.benutzerId = benutzerId;
		this.ausflugID = ausflugID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBenutzerId() {
		return benutzerId;
	}

	public void setBenutzerId(Long benutzerId) {
		this.benutzerId = benutzerId;
	}

	public Long getAusflugID() {
		return ausflugID;
	}

	public void setAusflugID(Long ausflugID) {
		this.ausflugID = ausflugID;
	}
}
