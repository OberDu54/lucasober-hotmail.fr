package fr.ul.miage.lucas;

import javafx.scene.control.Label;

import fr.ul.miage.meteo.json.Result;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MeteoLoader extends Service<Void> {
	
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
	
	public StringProperty text;
		
	public MeteoLoader(MeteoClient client, long time) {
		super();
		this.client = client;
		this.refreshTime = time;
		this.text = new SimpleStringProperty("");
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				while(!isCancelled()) {
					Result res = client.getWeatherByCityName();
					if (res != null) {
						String v = res.getName();
						float t = res.getMain().getTemp();
						float celsius = t-273.15f;
						System.out.println("Il fait "+ celsius +" à "+v);
						Platform.runLater(
							()->{
								text.set("Il fait "+ celsius +" à "+v);
							}
						);
						System.out.println("valeur de text"+text.getValueSafe());
						text.notifyAll();
					} else {
						System.out.println("Impossible de trouver la température");
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
	
	
	
	

}