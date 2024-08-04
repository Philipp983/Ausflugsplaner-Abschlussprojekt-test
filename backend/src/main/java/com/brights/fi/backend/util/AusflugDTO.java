package com.brights.fi.backend.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
public class AusflugDTO {

  private Long id;
  private String titel;
  private String beschreibung;
  private String reiseziel;
  private String ausflugsdatum;
  private String enddatum;
  private Integer teilnehmerAnzahl;
  private String erstellerDesAusflugsId;
  private Set<String> teilnehmerListe;
  private List<String> sachenZumMitbringen;
}
