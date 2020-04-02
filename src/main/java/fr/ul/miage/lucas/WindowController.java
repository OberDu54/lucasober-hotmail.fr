package fr.ul.miage.lucas;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.sun.javafx.geom.transform.BaseTransform.Degree;

import fr.ul.miage.meteo.json.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;

public class WindowController implements Initializable{
	
	private static final Logger LOG = Logger.getLogger(WindowController.class.getName());
	
	@FXML
	private TextField cityField;
	
	@FXML
	private Button searchButton;
	
	@FXML
	private ChoiceBox<Integer> hourChoice;
	
	@FXML
	private ChoiceBox<Integer> minuteChoice;
	
	@FXML
	private Button timeButton;
	
	@FXML
	private Button refreshButton;
	
	@FXML
	private Label displayer;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cityField.setText("Entrez un nom de ville pour la recherche");
		hourChoice.setValue(0);
		minuteChoice.setValue(0);
		ObservableList<Integer> hourList = FXCollections.observableArrayList();
		for(int i = 0; i<=24; i++) {
			hourList.add(i);
		}
		hourChoice.setItems(hourList);
		ObservableList<Integer> minuteList = FXCollections.observableArrayList();
		for(int i = 0; i<=60; i++) {
			minuteList.add(i);
		}
	}
	
	public void getMeteo() {
		String pays = "fr";
		String ville = cityField.getText();
		MeteoClient cl = new MeteoClient(ville, pays);
		Result res = cl.getWeatherByCityName();
		if (res != null) {
			String v = res.getName();
			float t = res.getMain().getTemp();
			float celsius = t-273.15f;
			displayer.setText("Il fait "+ celsius +" à "+v);
		} else {
			System.out.println("Impossible de trouver la température");
		}
	}
	
	
	
}
