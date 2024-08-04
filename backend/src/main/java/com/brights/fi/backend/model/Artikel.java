package com.brights.fi.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
// alternative zu @Data
@NoArgsConstructor
@Table
public class Artikel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	@NotNull(message = "Artikelname muss angegeben werden")
	@NotBlank(message ="Artikelname muss angegeben werden")
//	@Max(value = 255, message = "Artikelname zu lange")
	private String name;


	@ManyToOne
	@JoinColumn(name = "benutzer_id", nullable = true)
	@OnDelete(action = OnDeleteAction.SET_NULL)
	@JsonIgnoreProperties("sachenZumMitbringen")
	private Benutzer benutzer;

	@ManyToOne
	@JoinColumn(name = "ausflug_id")
	@JsonIgnoreProperties({"sachenZumMitbringen","ausfluege"})
	private Ausflug ausflug;
}
