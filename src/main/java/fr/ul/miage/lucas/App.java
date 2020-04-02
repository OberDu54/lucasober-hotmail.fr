package fr.ul.miage.lucas;

import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.ul.miage.meteo.json.Result;

public class App {
	private static final Logger LOG = Logger.getLogger(App.class.getName());
	public static void main(String[] args) {
		// paramètres
		String ville = "Nancy";
		String pays = "fr";
		// options
		Options options = new Options();
		Option city = new Option("v", "ville", true, "nom de la ville");
		Option country = new Option("p", "pays", true, "nom du pays");
		options.addOption(city);
		options.addOption(country);
		// parser la ligne de commande
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("v")) {
				ville = line.getOptionValue("v");
			}
			if (line.hasOption("p")) {
				pays = line.getOptionValue("p");
			}
		} catch (ParseException exp) {
			LOG.severe("Erreur dans la ligne de commande");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("meteo", options);
			System.exit(1);
		}
		// traitement
		MeteoClient cl = new MeteoClient(ville, pays);
		Result res = cl.getWeatherByCityName();
		if (res != null) {
			String v = res.getName();
			float t = res.getMain().getTemp();
			System.out.printf("If fait %.1f °C à %s%n", t-273.15f, v);
		} else {
			System.out.println("Impossible de trouver la température");
		}
	}

}