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
	private TextField countryField;
	
	@FXML
	private Button searchButton;
	
	@FXML
	private ChoiceBox<Integer> hourChoice;
	
	@FXML
	private ChoiceBox<Integer> minuteChoice;
	
	@FXML
	private ChoiceBox<Integer> secondChoice;
	
	@FXML
	private Button timeButton;
	
	@FXML
	private Button refreshButton;
	
	@FXML
	public Label labelVent;
	
	@FXML
	public Label labelNuages;
	
	@FXML
	public Label labelTemp;
	
	@FXML
	public Label labelVille;
	
	//private MeteoLoader loader;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cityField.setText("Entrez un nom de ville pour la recherche");
		ObservableList<Integer> hourList = FXCollections.observableArrayList();
		for(int i = 0; i<=23; i++) {
			hourList.add(i);
		}
		hourChoice.setItems(hourList);
		ObservableList<Integer> minuteList = FXCollections.observableArrayList();
		for(int i = 0; i<=59; i++) {
			minuteList.add(i);
		}
		minuteChoice.setItems(minuteList);
		ObservableList<Integer> secondList = FXCollections.observableArrayList();
		for(int i = 0; i<=59; i++) {
			secondList.add(i);
		}
		secondChoice.setItems(secondList);
		hourChoice.setValue(0);
		minuteChoice.setValue(0);
		secondChoice.setValue(5);
		cityField.setText(App.loader.getVille());
		countryField.setText(App.loader.getPays());
	}

	
	public void getMeteo() {
		if(App.loader.isRunning()) {
			App.loader.cancel();
			App.loader.reset();
		}
		String ville = cityField.getText();
		String pays = countryField.getText();
		int minutes = minuteChoice.getValue();
		int hours = hourChoice.getValue();
		int sec = secondChoice.getValue();
		long time = MyUtil.toMiliseconds(hours, minutes, sec);
		/*
		MeteoClient client = new MeteoClient(ville,pays);
		MeteoLoader loader = new MeteoLoader(client, time);*/
		App.loader.setVille(ville);
		App.loader.setPays(pays);
		App.loader.setRefreshTime(time);
		labelNuages.textProperty().bind(App.loader.getTextClouds());
		labelVent.textProperty().bind(App.loader.getTextWind());
		labelTemp.textProperty().bind(App.loader.getTextTemp());
		labelVille.textProperty().bind(App.loader.getTextVille());
		App.loader.start();
	}
	
	
	
}
