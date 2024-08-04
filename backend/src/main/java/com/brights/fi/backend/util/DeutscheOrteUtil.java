package com.brights.fi.backend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class DeutscheOrteUtil {
	private static final List<String> deutscheOrte = new ArrayList<>(Arrays.asList(
			"Berlin", "Hamburg", "München", "Köln", "Frankfurt", "Essen", "Dortmund", "Stuttgart",
			"Düsseldorf", "Bremen", "Hannover", "Duisburg", "Nürnberg", "Leipzig", "Dresden",
			"Bochum", "Wuppertal", "Bielefeld", "Bonn", "Mannheim", "Karlsruhe", "Gelsenkirchen",
			"Wiesbaden", "Münster", "Mönchengladbach", "Chemnitz", "Augsburg", "Braunschweig",
			"Aachen", "Krefeld", "Halle", "Kiel", "Magdeburg", "Oberhausen", "Lübeck", "Freiburg",
			"Hagen", "Erfurt", "Kassel", "Rostock", "Mainz", "Hamm", "Saarbrücken", "Herne",
			"Mülheim", "Solingen", "Osnabrück", "Ludwigshafen", "Leverkusen", "Oldenburg", "Neuss",
			"Paderborn", "Heidelberg", "Darmstadt", "Potsdam", "Würzburg", "Göttingen", "Regensburg",
			"Recklinghausen", "Bottrop", "Wolfsburg", "Heilbronn", "Ingolstadt", "Ulm", "Remscheid",
			"Pforzheim", "Bremerhaven", "Offenbach", "Fürth", "Reutlingen", "Salzgitter", "Siegen",
			"Gera", "Koblenz", "Moers", "Bergisch Gladbach", "Cottbus", "Hildesheim", "Witten",
			"Zwickau", "Erlangen", "Iserlohn", "Trier", "Kaiserslautern", "Jena", "Schwerin",
			"Gütersloh", "Marl", "Lünen", "Esslingen", "Velbert", "Ratingen", "Düren", "Ludwigsburg",
			"Wilhelmshaven", "Hanau", "Minden", "Flensburg", "Dessau", "Villingen-Schwenningen"
	));

	private static List<Integer> indexe;
	private static final Random random = new Random();

	static {
		erneureIndexe();
	}

	private static void erneureIndexe() {
		indexe = new ArrayList<>(IntStream.range(0, deutscheOrte.size()).boxed().toList());
	}

	public static String gebeNeuenOrt() {
		if (indexe.isEmpty()) {
			erneureIndexe();
		}
		int index = indexe.remove(random.nextInt(indexe.size()));
		return deutscheOrte.get(index);
	}
}
