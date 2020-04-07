package fr.ul.miage.lucas;

import fr.ul.miage.meteo.json.Result;
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
		
	public MeteoLoader(MeteoClient client, long time) {
		super();
		this.client = client;
		this.refreshTime = time;
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
					} else {
						System.out.println("Impossible de trouver la température");
					}
					Thread.sleep(refreshTime);
				}
				return null;
			}
			
		};
	}

}
