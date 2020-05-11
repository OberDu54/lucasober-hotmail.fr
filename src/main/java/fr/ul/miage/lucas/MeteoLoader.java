package fr.ul.miage.lucas;

import javafx.scene.image.Image;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;

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

/**
 * Service utilisant un meteoClient et qui met à jour l'interface utilisateur
 * 
 * @author lucas
 *
 */
public class MeteoLoader extends Service<Void> {
	
	/**
	 * Logger
	 */
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
	 * Texte affichant les erreurs
	 */
	private StringProperty textError;
	
	/**
	 * Texte decrivant le temps d'une manière générale
	 */
	private StringProperty textDesc;
	
	/**
	 * Texte decrivant l'humidité
	 */
	private StringProperty textHumidity;
	
	/**
	 * Texte decrivant la visibilité
	 */
	private StringProperty textVisibility;

	
	/**
	 * Objet correspondant à l'icone météo
	 */
	private ObjectProperty<Image> imageProperty;

	/**
	 * Constructeur 
	 * @param client MeteoClient utilisé
	 * @param time Temps de rafraichissement
	 */
	public MeteoLoader(MeteoClient client, long time) {
		super();
		this.client = client;
		this.refreshTime = time;
		this.textError = new SimpleStringProperty("");
		this.textClouds = new SimpleStringProperty("");
		this.textWind = new SimpleStringProperty("");
		this.textVille = new SimpleStringProperty("");
		this.textTemp = new SimpleStringProperty("");
		this.textDesc = new SimpleStringProperty("");
		this.textHumidity = new SimpleStringProperty("");
		this.textVisibility = new SimpleStringProperty("");
		this.imageProperty = new SimpleObjectProperty<Image>();
	}

	@Override
	/**
	 * Méthode appellée lorsque le service est lancé
	 */
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
						int humidity = res.getMain().getHumidity();
						int visibility = res.getVisibility()/1000;
						Platform.runLater(
							()->{
								textVille.set(v+", "+client.getCountry());
								textTemp.set(temp+"°C	Min : "+min+"°C	  Max : "+max+"°C");
								textClouds.set(""+clouds.getAll()+"%");
								textWind.set("Vitesse : "+wind.getSpeed()+"m/s Degré : "+wind.getDeg());
								textDesc.set(desc);
								textVisibility.set(visibility+"km");
								textHumidity.set(humidity+"%");
								imageProperty.set(new Image("http://openweathermap.org/img/w/"+ iconCode +".png"));
							}
						);
					} else {
						LOG.severe("Impossible de trouver les informations pour cette ville");
						Platform.runLater(
							()->{
								textError.set("Impossible de trouver les informations pour cette ville");
								textVille.set("");
								textTemp.set("");
								textClouds.set("");
								textWind.set("");
								textDesc.set("");
								textVisibility.set("");
								textHumidity.set("");
								imageProperty.set(null);
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
	public StringProperty getTextError() {
		return textError;
	}

	public void setText(StringProperty text) {
		this.textError = text;
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

	public StringProperty getTextHumidity() {
		return textHumidity;
	}

	public StringProperty getTextVisibility() {
		return textVisibility;
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
