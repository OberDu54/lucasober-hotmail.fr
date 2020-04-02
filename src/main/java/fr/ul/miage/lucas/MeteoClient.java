package fr.ul.miage.lucas;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import fr.ul.miage.meteo.json.Result;

public class MeteoClient {
	private static final Logger LOG = Logger.getLogger(MeteoClient.class.getName());
	//statics
	public static String WEBSERVICE="https://api.openweathermap.org/data/2.5/";
	//fields
	private String apiKey;
	private String city;
	private String country;
	//constructors
	public MeteoClient() {
		this("75b83b66ff1ca04ec816f27b3e5f2890","Nancy","fr");
	}	
	public MeteoClient(String city) {
		this("75b83b66ff1ca04ec816f27b3e5f2890",city,"fr");
	}	
	public MeteoClient(String city, String country) {
		this("75b83b66ff1ca04ec816f27b3e5f2890",city,country);
	}	
	public MeteoClient(String apiKey, String city, String country) {
		setApiKey(apiKey);
		setCity(city);
		setCountry(country);
	}	
	//methods
	/**
	 * Construire la requête
	 * @return contenant la requete WS
	 */
	public String buildRequest() {
		String request = WEBSERVICE
				+ "weather?"
				+ "q=" + getCity() + "," + getCountry()
				+ "&APPID=" + getApiKey();
		return request;

	}
	/**
	 * Get WS response as a (JSON) String
	 * @return JSON String
	 */
	public String getJsonWeatherByCityName() {
		String res = null;
		//construire la requête
		String request = buildRequest();
		try {
			Client client = Client.create();
			WebResource r = client.resource(request);
			r.accept("application/json");
			ClientResponse response = r.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				LOG.severe("Erreur de requête:" + request 
						+ "(code:"+ response.getStatus() + ")");
				return null;
			}
			res = response.getEntity(String.class);
		  } catch (Exception e) {
				LOG.severe("Erreur de webservice:" + request);
				return null;
		  }
		return res;	
	}
	/**
	 * Get WS Response as Result instance
	 * @return
	 */
	public Result getWeatherByCityName() {
		Result res = null;
		String tmp = getJsonWeatherByCityName();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		res = gson.fromJson(tmp, Result.class);
		return res;
	}

	//setters & getters
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

}
