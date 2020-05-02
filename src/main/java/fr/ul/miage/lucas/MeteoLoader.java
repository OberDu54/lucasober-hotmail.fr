package fr.ul.miage.lucas;

import javafx.scene.control.Label;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import fr.ul.miage.meteo.json.Clouds;
import fr.ul.miage.meteo.json.Result;
import fr.ul.miage.meteo.json.Weather;
import fr.ul.miage.meteo.json.Wind;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MeteoLoader extends Service<Void> {

	private static final Logger LOG = Logger.getLogger(MeteoLoader.class.getName());
	
	/**
	 * Client meteo 
	 */
	private MeteoClient client;
	
	/**
	 * Temps de rafraichissement
	 */
	private long refreshTime;
	
	/**
	 * Label ou afficher les résultats
	 */
	private Label label;

	private StringProperty text;
	
	/**
	 * Texte renseignant les nuages
	 */
	private StringProperty textClouds;
	
	/**
	 * Texte renseignant le vent
	 */
	private StringProperty textWind;
	
	/**
	 * Texte renseignant la ville 
	 */
	private StringProperty textVille;
	
	/**
	 * Texte renseignant la température
	 */
	private StringProperty textTemp;

	
	public MeteoLoader(MeteoClient client, long time) {
		super();
		this.client = client;
		this.refreshTime = time;
		this.textClouds = new SimpleStringProperty("");
		this.textWind = new SimpleStringProperty("");
		this.textVille = new SimpleStringProperty("");
		this.textTemp = new SimpleStringProperty("");
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				while(!isCancelled()) {
					Result res = client.getWeatherByCityName();
					String jsonString = client.getJsonWeatherByCityName();
					
					if (res != null) {
						String v = res.getName();
						float t = res.getMain().getTemp();
						float celsius = t-273.15f;
						DecimalFormat df = new DecimalFormat("##.#");
						String temp = df.format(celsius);
						System.out.println("Il fait "+ celsius +" à "+v);
						System.out.println(jsonString);
						Result result = JSONProcessor.simpleDeserialize(jsonString);
						Clouds clouds = result.getClouds();
						Wind wind = result.getWind();
						Platform.runLater(
							()->{
								textVille.set(v+", "+client.getCountry());
								textTemp.set(temp+"°");
								textClouds.set(""+clouds.getAll()+"%");
								textWind.set("Vitesse : "+wind.getSpeed()+"m/s Degré : "+wind.getDeg());
							}
						);
					} else {
						LOG.severe("Impossible de trouver les informations pour cette ville");
						Platform.runLater(
							()->{
								text.set("Impossible de trouver les informations pour cette ville");
							}
						);
						this.cancel();
					}
					Thread.sleep(refreshTime);
				}
				return null;
			}
			
		};
	}
	
	
	//Getters et Setters
	public StringProperty getText() {
		return text;
	}

	public void setText(StringProperty text) {
		this.text = text;
	}

	public MeteoClient getClient() {
		return client;
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	public StringProperty getTextClouds() {
		return textClouds;
	}

	public StringProperty getTextWind() {
		return textWind;
	}

	public StringProperty getTextVille() {
		return textVille;
	}

	public StringProperty getTextTemp() {
		return textTemp;
	}
	
	public void setVille(String v) {
		this.client.setCity(v);
	}
	
	public String getVille() {
		return this.client.getCity();
	}
	
	public void setPays(String p) {
		this.client.setCountry(p);
	}
	
	public String getPays() {
		return this.client.getCountry();
	}
	
}
