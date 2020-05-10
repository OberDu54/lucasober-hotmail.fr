package fr.ul.miage.lucas;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.sun.jersey.api.client.Client;

import fr.ul.miage.meteo.json.Result;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale 
 * 
 * @author lucas
 *
 */
public class App extends Application{
	
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(App.class.getName());
	
	/**
	 * Objet meteoLoader statique permettant d'utiliser l'api
	 */
	public static MeteoLoader loader = new MeteoLoader(new MeteoClient("Ville","Pays"), 5000);
	
	/**
	 * Lance l'interface utilisateur
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TP3_Oberhausser");
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("Window.fxml"));
		}catch(IOException e) {
			LOG.severe("Erreur de chargement de l'interface");
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
	}
	
	/**
	 * Lit les éventuels arguments et lance l'application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// paramètres
		String ville = "Nancy";
		String pays = "fr";
		// options
		Options options = new Options();
		Option city = new Option("v", "ville", true, "nom de la ville");
		Option country = new Option("p", "pays", true, "nom du pays");
		Option time = new Option("t", "temps", true, "temps de rafraichissement en secondes");
		options.addOption(city);
		options.addOption(country);
		options.addOption(time);
		// parser la ligne de commande
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("v")) {
				ville = line.getOptionValue("v");
				loader.setVille(ville);
			}
			if (line.hasOption("p")) {
				pays = line.getOptionValue("p");
				loader.setPays(pays);
			}
			if(line.hasOption("t")) {
				long temps = Long.parseLong(line.getOptionValue("t"));
				loader.setRefreshTime(temps*1000);
			}
			launch(args);
		} catch (ParseException exp) {
			LOG.severe("Erreur dans la ligne de commande");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("meteo", options);
			System.exit(1);
		}

	}

}