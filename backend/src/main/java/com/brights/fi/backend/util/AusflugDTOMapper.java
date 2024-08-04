package com.brights.fi.backend.util;

import com.brights.fi.backend.DTO.ArtikelSimpelDTO;
import com.brights.fi.backend.DTO.AusflugAntwortDTO;
import com.brights.fi.backend.model.Ausflug;
import com.brights.fi.backend.model.Benutzer;

import java.util.stream.Collectors;

public class AusflugDTOMapper {
	public static AusflugAntwortDTO toDTO(Ausflug ausflug) {
		AusflugAntwortDTO dto = new AusflugAntwortDTO();
		dto.setId(ausflug.getId());
		dto.setTitel(ausflug.getTitel());
		dto.setBeschreibung(ausflug.getBeschreibung());
		dto.setReiseziel(ausflug.getReiseziel());
		dto.setAusflugsdatum(ausflug.getAusflugsdatum());
		dto.setEnddatum(ausflug.getEnddatum());
		dto.setEditierdatum(ausflug.getEditierdatum());
		dto.setTeilnehmerAnzahl(ausflug.getTeilnehmerAnzahl());
		dto.setTeilnehmerIds(ausflug.getTeilnehmerListe().stream()
				.map(Benutzer::getId)
				.collect(Collectors.toList()));
		dto.setErstellerDesAusflugsId(ausflug.getErstellerDesAusflugsId());
		dto.setNameDerTeilnehmer(ausflug.getTeilnehmerListe().stream()
				.map(Benutzer::getUserName)
				.collect(Collectors.toList())
		);

		dto.setSachenZumMitbringen(ausflug.getSachenZumMitbringen().stream()
				.map(artikel -> {
					ArtikelSimpelDTO artikelDTO = new ArtikelSimpelDTO();
					artikelDTO.setId(artikel.getId());
					artikelDTO.setName(artikel.getName());
					artikelDTO.setBenutzerId(artikel.getBenutzer() != null ? artikel.getBenutzer().getId() : null);
					return artikelDTO;
				})
				.collect(Collectors.toList()));

		if (ausflug.getHauptBild() != null) {
			dto.setImageUrl(ausflug.getHauptBild().getImageUrl());
			dto.setImageType(ausflug.getHauptBild().getImageType());
		}

		return dto;
	}
}
