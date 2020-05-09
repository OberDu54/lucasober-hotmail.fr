package fr.ul.miage.lucas;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.NewCookie;

import fr.ul.miage.meteo.json.Clouds;
import fr.ul.miage.meteo.json.Result;
import fr.ul.miage.meteo.json.Weather;
import fr.ul.miage.meteo.json.Wind;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
	
	/**
	 * Texte decrivant le temps d'une manière générale
	 */
	private StringProperty textDesc;
	
	private ObjectProperty<Image> imageProperty;

	
	public MeteoLoader(MeteoClient client, long time) {
		super();
		this.client = client;
		this.refreshTime = time;
		this.textClouds = new SimpleStringProperty("");
		this.textWind = new SimpleStringProperty("");
		this.textVille = new SimpleStringProperty("");
		this.textTemp = new SimpleStringProperty("");
		this.textDesc = new SimpleStringProperty("");
		this.imageProperty = new SimpleObjectProperty<Image>();
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
						float max = res.getMain().getTempMax()-273.15f;
						float min = res.getMain().getTempMin()-273.15f;
						DecimalFormat df = new DecimalFormat("##.#");
						String temp = df.format(celsius);
						Result result = JSONProcessor.simpleDeserialize(jsonString);
						Clouds clouds = result.getClouds();
						Wind wind = result.getWind();
						List<Weather> weather = result.getWeather();
						String desc = weather.get(0).getDescription();
						String iconCode = weather.get(0).getIcon();
						Platform.runLater(
							()->{
								textVille.set(v+", "+client.getCountry());
								textTemp.set(temp+"°C	Min : "+min+"°C	  Max : "+max+"°C");
								textClouds.set(""+clouds.getAll()+"%");
								textWind.set("Vitesse : "+wind.getSpeed()+"m/s Degré : "+wind.getDeg());
								textDesc.set(desc);
								imageProperty.set(new Image("http://openweathermap.org/img/w/"+ iconCode +".png"));
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
	
	public StringProperty getTextDesc() {
		return textDesc;
	}

	public ObjectProperty<Image> getImageProperty() {
		return imageProperty;
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
