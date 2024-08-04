package com.brights.fi.backend.DTO;

import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;

public class EinladungAntwortDTO {

	private Benutzer benutzer;
	private Ausflug ausflug;

	public EinladungAntwortDTO(Benutzer benutzer, Ausflug ausflug) {
		this.benutzer = benutzer;
		this.ausflug = ausflug;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	public void setAusflug(Ausflug ausflug) {
		this.ausflug = ausflug;
	}

	public Benutzer getBenutzer() {
		return benutzer;
	}

	public Ausflug getAusflug() {
		return ausflug;
	}
}
