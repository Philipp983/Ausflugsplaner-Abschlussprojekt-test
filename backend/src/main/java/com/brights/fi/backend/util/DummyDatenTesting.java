//package com.brights.fi.backend.util;
//
//import com.brights.fi.backend.model.Ausflug;
//import com.brights.fi.backend.model.AusflugBild;
//import com.brights.fi.backend.model.Benutzer;
//import com.brights.fi.backend.model.EinladungSimpel;
//import com.brights.fi.backend.repositories.EinladungSimpelRepo;
//import com.brights.fi.backend.service.AusflugService;
//import com.brights.fi.backend.service.BenutzerService;
//import com.github.javafaker.Faker;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Random;
//
//@Component
//public class DummyDatenTesting {
//
//	private final Faker faker = new Faker(new Locale("de"));
//	private final Random random = new Random();
//
//	@Autowired
//	private BenutzerService benutzerService;
//
//	@Autowired
//	private AusflugService ausflugService;
//
//	@Autowired
//	private EinladungSimpelRepo einladungSimpelRepo;
//
//	@Bean
//	public CommandLineRunner initDatabase() {
//		return args -> {
//			// Create admin user
//			Benutzer adminUser = new Benutzer();
//			adminUser.setUserName("Admin");
//			adminUser.setUserEmail("admin@admin.de");
//			adminUser.setPasswort("admin");
//			adminUser.setBeschreibung("Admin user");
//			benutzerService.speichereBenutzer(adminUser);
//
//			// Create 3 hardcoded users
//			List<Benutzer> hardcodedUsers = createHardcodedUsers();
//
//			// Create some regular users and Ausflüge
//			List<Benutzer> allUsers = new ArrayList<>(hardcodedUsers);
//			List<Ausflug> allAusfluege = new ArrayList<>();
//
//			for (int i = 0; i < 10; i++) {
//				Benutzer benutzer = createRandomUser();
//				allUsers.add(benutzer);
//
//				if (i % 3 == 0) {
//					Ausflug ausflug = createRandomAusflug(benutzer);
//					allAusfluege.add(ausflug);
//				}
//			}
//
//			// Create EinladungSimpel objects
//			createEinladungen(hardcodedUsers, allAusfluege);
//		};
//	}
//
//	private List<Benutzer> createHardcodedUsers() {
//		List<Benutzer> users = new ArrayList<>();
//		for (int i = 1; i <= 3; i++) {
//			Benutzer user = new Benutzer();
//			user.setUserName("User " + i);
//			user.setUserEmail("email" + i + "@email.com");
//			user.setPasswort("123");
//			user.setBeschreibung("Hardcoded user " + i);
//			users.add(benutzerService.speichereBenutzer(user));
//		}
//		return users;
//	}
//
//	private Benutzer createRandomUser() {
//		Benutzer benutzer = new Benutzer();
//		benutzer.setUserName(faker.name().fullName());
//		benutzer.setUserEmail(faker.internet().emailAddress());
//		benutzer.setPasswort(faker.internet().password());
//		benutzer.setBeschreibung(faker.lorem().paragraph());
//		return benutzerService.speichereBenutzer(benutzer);
//	}
//
//	private Ausflug createRandomAusflug(Benutzer creator) {
//		Ausflug ausflug = new Ausflug();
//		ausflug.setTitel("Ausflug nach " + faker.address().city());
//		ausflug.setBeschreibung(faker.lorem().sentence());
//		ausflug.setReiseziel(faker.address().city());
//		ausflug.setAusflugsdatum(LocalDateTime.now().plusDays(random.nextInt(30)));
//		ausflug.setEnddatum(ausflug.getAusflugsdatum().plusDays(random.nextInt(7)));
//		ausflug.setTeilnehmerAnzahl(random.nextInt(10) + 2);
//		ausflug.setErstellerDesAusflugsId(creator.getId().toString());
//		ausflug.getTeilnehmerListe().add(creator);
//
//		AusflugBild ausflugBild = new AusflugBild();
//		ausflugBild.setImageUrl(getRandomImageUrl());
//		ausflugBild.setAusflug(ausflug);
//		ausflug.setHauptBild(ausflugBild);
//
//		return ausflugService.speicherAusflug(creator.getId(), ausflug);
//	}
//
//	private void createEinladungen(List<Benutzer> users, List<Ausflug> ausfluege) {
//		for (Benutzer user : users) {
//			for (int i = 0; i < 5; i++) {
//				EinladungSimpel einladung = new EinladungSimpel();
//				einladung.setBenutzer(user);
//				einladung.setErstellungsdatum(LocalDateTime.now().minusDays(random.nextInt(30)));
//
//				if (random.nextBoolean()) {
//					// EINLADUNG
//					Ausflug ausflug = ausfluege.get(random.nextInt(ausfluege.size()));
//					einladung.setAusflugId(ausflug.getId());
//					einladung.setArt(EinladungSimpel.ArtDerEinladung.EINLADUNG);
//					einladung.setNameDesAusflugs(ausflug.getTitel());
//					Benutzer invitedUser = users.get(random.nextInt(users.size()));
//					einladung.setHinzuzufuegenderTeilnehmerId(invitedUser.getId());
//					einladung.setNameDesTeilnehmers(invitedUser.getUserName());
//				} else {
//					// ANFRAGE
//					Ausflug ausflug = ausfluege.get(random.nextInt(ausfluege.size()));
//					einladung.setAusflugId(ausflug.getId());
//					einladung.setArt(EinladungSimpel.ArtDerEinladung.ANFRAGE);
//					einladung.setNameDesAusflugs(ausflug.getTitel());
//					Benutzer requester = users.get(random.nextInt(users.size()));
//					einladung.setHinzuzufuegenderTeilnehmerId(requester.getId());
//					einladung.setNameDesTeilnehmers(requester.getUserName());
//				}
//
//				einladungSimpelRepo.save(einladung);
//			}
//		}
//	}
//
//	private String getRandomImageUrl() {
//		int width = 300 + random.nextInt(200);
//		int height = 200 + random.nextInt(200);
//		return String.format("https://picsum.photos/%d/%d", width, height);
//	}
//}
