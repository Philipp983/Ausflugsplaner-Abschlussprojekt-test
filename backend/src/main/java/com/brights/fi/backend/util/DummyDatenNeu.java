package com.brights.fi.backend.util;

import com.brights.fi.backend.model.*;
import com.brights.fi.backend.repositories.EinladungSimpelRepo;
import com.brights.fi.backend.service.AusflugService;
import com.brights.fi.backend.service.BenutzerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DummyDatenNeu {

	private final Faker faker = new Faker(new Locale("de"));
	private final Random random = new Random();

	private String getRandomImageUrl() {
		int width = 700 + random.nextInt(200);
		int height = 600 + random.nextInt(200);
		return String.format("https://picsum.photos/%d/%d", width, height);
	}

	private String getRandomProfileImageUrl() {
		String randomString = UUID.randomUUID().toString();
		return String.format("https://robohash.org/%s.png?size=200x200&set=set4", randomString);
	}

	@Bean
	public CommandLineRunner initDatabase(BenutzerService benutzerService, AusflugService ausflugService, EinladungSimpelRepo einladungSimpelRepo) {
		return args -> {
			// Create admin user
			Benutzer adminUser = createAdminUser(benutzerService);

			// Create hardcoded users
			List<Benutzer> hardcodedUsers = createHardcodedUsers(benutzerService);

			// Create Ausflüge for hardcoded users
			List<Ausflug> hardcodedAusfluege = createAusfluegeForHardcodedUsers(hardcodedUsers, ausflugService);

			// Create random users and Ausflüge
			List<Ausflug> randomAusfluege = createRandomUsersAndAusfluege(benutzerService, ausflugService, 50);

//			DeutscheOrteUtil.erneuerOrte();

			// Combine all Ausflüge
			List<Ausflug> allAusfluege = new ArrayList<>(hardcodedAusfluege);
			allAusfluege.addAll(randomAusfluege);

			// Create EinladungSimpel objects for hardcoded users
			createEinladungen(hardcodedUsers, allAusfluege, einladungSimpelRepo, benutzerService);
		};
	}

	private Benutzer createAdminUser(BenutzerService benutzerService) {
		Benutzer adminUser = new Benutzer();
		adminUser.setUserName("Admin");
		adminUser.setUserEmail("admin@admin.de");
		adminUser.setPasswort("admin123");
		adminUser.setBeschreibung("Admin user");
		ProfilBild profilBild = new ProfilBild();
		profilBild.setImageUrl(getRandomProfileImageUrl());
		adminUser.setProfilBild(profilBild);
		return benutzerService.speichereBenutzer(adminUser);
	}

	private List<Benutzer> createHardcodedUsers(BenutzerService benutzerService) {
		List<Benutzer> users = new ArrayList<>();
		String[] names = {"Eva", "Osama", "Mazlum","Lars","Medede","Sezgin", "Philipp"};
		for (int i = 1; i <= 7; i++) {
			Benutzer user = new Benutzer();
			user.setUserName(names[i - 1]);
			user.setUserEmail("email" + i + "@email.com");
			user.setPasswort("12345678");
			user.setBeschreibung("Hallo Leute! Mein Name ist " + user.getUserName() + " und ich freu mich schon rießig," +
					" gemeinsam mit euch Ausflüge zu erleben!");
			ProfilBild profilBild = new ProfilBild();
			profilBild.setImageUrl(getRandomProfileImageUrl());
			user.setProfilBild(profilBild);
			users.add(benutzerService.speichereBenutzer(user));
		}
		return users;
	}


	private List<Ausflug> createAusfluegeForHardcodedUsers(List<Benutzer> hardcodedUsers, AusflugService ausflugService) {
		List<Ausflug> hardcodedAusfluege = new ArrayList<>();
		for (Benutzer user : hardcodedUsers) {
			for (int i = 0; i < 3; i++) {
				Ausflug ausflug = createRandomAusflug(user);
				ausflug = ausflugService.speicherAusflug(user.getId(), ausflug);

				// Add 4-5 Artikel to each Ausflug
				for (int j = 0; j < random.nextInt(2) + 4; j++) {
					Artikel artikel = createRandomArtikel(user, ausflug);
					ausflugService.hinzufuegeArtikel(ausflug.getId(), artikel);
				}

				hardcodedAusfluege.add(ausflug);
			}
		}
		return hardcodedAusfluege;
	}

	private List<Ausflug> createRandomUsersAndAusfluege(BenutzerService benutzerService, AusflugService ausflugService, int count) {
		List<Ausflug> allAusfluege = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			Benutzer benutzer = createRandomUser(benutzerService);

			if (i % 2 == 0) {
				Ausflug ausflug = createRandomAusflug(benutzer);
				ausflug = ausflugService.speicherAusflug(benutzer.getId(), ausflug);
				allAusfluege.add(ausflug);

				// Add some Artikel to the Ausflug
				for (int j = 0; j < random.nextInt(5) + 1; j++) {
					Artikel artikel = createRandomArtikel(benutzer, ausflug);
					ausflugService.hinzufuegeArtikel(ausflug.getId(), artikel);
				}
			}
		}
		return allAusfluege;
	}

	private Benutzer createRandomUser(BenutzerService benutzerService) {
		Benutzer benutzer = new Benutzer();
		benutzer.setUserName(faker.name().fullName());
		benutzer.setUserEmail(faker.internet().emailAddress());
		benutzer.setPasswort(faker.internet().password(8, 20));
		benutzer.setBeschreibung(faker.lorem().paragraph());
		ProfilBild profilBild = new ProfilBild();
		profilBild.setImageUrl(getRandomProfileImageUrl());
		benutzer.setProfilBild(profilBild);
		return benutzerService.speichereBenutzer(benutzer);
	}

	private Ausflug createRandomAusflug(Benutzer creator) {
		Ausflug ausflug = new Ausflug();
		String reiseOrt = DeutscheOrteUtil.gebeNeuenOrt();
		ausflug.setTitel("Ausflug nach " + faker.address().city());
		ausflug.setBeschreibung(GenerischeSaetzeUtil.gebeNeuenSatz());
		ausflug.setReiseziel(reiseOrt);
		ausflug.setAusflugsdatum(LocalDateTime.now().plusDays(random.nextInt(30)));
		ausflug.setEnddatum(ausflug.getAusflugsdatum().plusDays(random.nextInt(7)));
		ausflug.setTeilnehmerAnzahl(random.nextInt(10) + 2);
		ausflug.setErstellerDesAusflugsId(creator.getId().toString());

		AusflugBild ausflugBild = new AusflugBild();
		ausflugBild.setImageUrl(getRandomImageUrl());
		ausflugBild.setImageType(AusflugBild.ImageType.URL);
		ausflugBild.setContentType("URL");
		ausflugBild.setAusflug(ausflug);
		ausflug.setHauptBild(ausflugBild);

		return ausflug;
	}

	private Artikel createRandomArtikel(Benutzer benutzer, Ausflug ausflug) {
		Artikel artikel = new Artikel();
		artikel.setName(faker.commerce().productName());
//		artikel.setBenutzer(benutzer);
		artikel.setAusflug(ausflug);
		return artikel;
	}

	private void createEinladungen(List<Benutzer> users, List<Ausflug> ausfluege, EinladungSimpelRepo einladungSimpelRepo, BenutzerService benutzerService) {
		for (Benutzer user : users) {
			// Nur noch zwei Einladungen pro Benutzer
			for (int i = 0; i < 2; i++) {
				EinladungSimpel einladung = new EinladungSimpel();
				einladung.setBenutzer(user);
				einladung.setErstellungsdatum(LocalDateTime.now().minusDays(random.nextInt(30)));

				// Durch Modulo abwechselnd Einladung und Anfrage
				if (i % 2 == 0) {
					// EINLADUNG
					Ausflug ausflug = getRandomAusflugNotCreatedBy(user, ausfluege);
					if (ausflug != null) {
						einladung.setAusflugId(ausflug.getId());
						einladung.setArt(EinladungSimpel.ArtDerEinladung.EINLADUNG);
						einladung.setNameDesAusflugs(ausflug.getTitel());
						einladung.setHinzuzufuegenderTeilnehmerId(user.getId());

						// Get the creator of the Ausflug
						Benutzer creator = benutzerService.findById(ausflug.getErstellerDesAusflugsId());
						einladung.setNameDesTeilnehmers(creator.getUserName());

						einladungSimpelRepo.save(einladung);
					}
				} else {
					// ANFRAGE
					Ausflug ausflug = getRandomAusflugCreatedBy(user, ausfluege);
					if (ausflug != null) {
						einladung.setAusflugId(ausflug.getId());
						einladung.setArt(EinladungSimpel.ArtDerEinladung.ANFRAGE);
						einladung.setNameDesAusflugs(ausflug.getTitel());
						Benutzer requester = getRandomUserExcept(user, users);
						einladung.setHinzuzufuegenderTeilnehmerId(requester.getId());
						einladung.setNameDesTeilnehmers(requester.getUserName());
						einladungSimpelRepo.save(einladung);
					}
				}
			}
		}
	}

	private Ausflug getRandomAusflugNotCreatedBy(Benutzer user, List<Ausflug> ausfluege) {
		List<Ausflug> eligibleAusfluege = ausfluege.stream()
				.filter(a -> !a.getErstellerDesAusflugsId().equals(user.getId().toString()))
				.collect(Collectors.toList());
		return eligibleAusfluege.isEmpty() ? null : eligibleAusfluege.get(random.nextInt(eligibleAusfluege.size()));
	}

	private Ausflug getRandomAusflugCreatedBy(Benutzer user, List<Ausflug> ausfluege) {
		List<Ausflug> eligibleAusfluege = ausfluege.stream()
				.filter(a -> a.getErstellerDesAusflugsId().equals(user.getId().toString()))
				.collect(Collectors.toList());
		return eligibleAusfluege.isEmpty() ? null : eligibleAusfluege.get(random.nextInt(eligibleAusfluege.size()));
	}

	private Benutzer getRandomUserExcept(Benutzer excludedUser, List<Benutzer> users) {
		List<Benutzer> eligibleUsers = users.stream()
				.filter(u -> !u.getId().equals(excludedUser.getId()))
				.collect(Collectors.toList());
		return eligibleUsers.isEmpty() ? null : eligibleUsers.get(random.nextInt(eligibleUsers.size()));
	}
}
