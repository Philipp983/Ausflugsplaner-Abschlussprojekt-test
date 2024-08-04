package com.brights.fi.backend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GenerischeSaetzeUtil {
	private static final Random random = new Random();
	private static final List<String> sentences = new ArrayList<>(Arrays.asList(
			"Entdecke die atemberaubende Landschaft und tauche ein in die faszinierende Geschichte dieser Region.",
			"Ein unvergessliches Erlebnis für Naturliebhaber und Kulturinteressierte.",
			"Erlebe die lokale Küche, besuche traditionelle Märkte und lerne die einheimische Bevölkerung kennen.",
			"Eine einzigartige Gelegenheit, die Kultur und Traditionen dieses Ortes zu entdecken.",
			"Unternimm eine Wanderung durch unberührte Natur und genieße die Ruhe und Schönheit der Umgebung.",
			"Ein perfekter Ausflug für alle, die dem Alltagsstress entfliehen möchten.",
			"Besuche historische Stätten und erfahre mehr über die Vergangenheit dieser Region.",
			"Eine Reise durch die Zeit und eine Entdeckung der kulturellen Schätze dieser Gegend.",
			"Entdecke die lokale Kunst und Kultur und besuche Galerien und Museen.",
			"Eine inspirierende Reise für Kunstliebhaber und Kulturinteressierte.",
			"Genieße Wassersportarten, entspanne am Strand oder erkunde die Unterwasserwelt.",
			"Ein perfekter Ausflug für alle, die Sonne, Strand und Meer lieben.",
			"Mach eine Radtour durch die malerische Landschaft und entdecke versteckte Winkel.",
			"Eine aktive und erlebnisreiche Art, die Umgebung zu erkunden.",
			"Besuche einen Freizeitpark, genieße Fahrgeschäfte und erlebe unvergessliche Momente.",
			"Ein Ausflug für die ganze Familie und für alle, die Spaß und Abenteuer lieben.",
			"Entdecke die lokale Flora und Fauna und beobachte Tiere in ihrem natürlichen Lebensraum.",
			"Eine unvergessliche Erfahrung für Naturliebhaber und Fotografen.",
			"Genieße die lokale Gastronomie und probiere regionale Spezialitäten.",
			"Eine kulinarische Reise durch die Region und eine Entdeckung neuer Geschmackserlebnisse."
	));

	private static List<Integer> indexe;
	static {
		erneureIndexe();
	}

	private static void erneureIndexe() {
		indexe = new ArrayList<>(IntStream.range(0, sentences.size()).boxed().toList());
	}

	public static String gebeNeuenSatz() {
		if (indexe.isEmpty()) {
			erneureIndexe();
		}
		int index = indexe.remove(random.nextInt(indexe.size()));
		return sentences.get(index);
	}
}
