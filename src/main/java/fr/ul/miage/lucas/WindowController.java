package fr.ul.miage.lucas;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.sun.javafx.geom.transform.BaseTransform.Degree;
import com.sun.javafx.util.TempState;

import fr.ul.miage.meteo.json.Result;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
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
	private Label labelVent;
	
	@FXML
	private Label labelNuages;
	
	@FXML
	private Label labelTemp;
	
	@FXML
	private Label labelVille;
	
	@FXML
	private Label labelDesc;
	
	@FXML
	private Label timeLabel;
	
	@FXML
	private Label labelVisibilite;
	
	@FXML
	private Label labelHumidite;
	
	@FXML
	private Button modifTime;
	
	@FXML
	private ImageView imageView;

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
		cityField.setText(App.loader.getVille());
		countryField.setText(App.loader.getPays());
		int time = (int) (App.loader.getRefreshTime()/1000);
		updateTimeLabel(App.loader.getRefreshTime());
		int[]tab = MyUtil.convertSeconds(time);
		hourChoice.setValue(tab[0]);
		minuteChoice.setValue(tab[1]);
		secondChoice.setValue(tab[2]);
	}

	
	public void getMeteo() {
		if(App.loader.isRunning()) {
			App.loader.cancel();
			App.loader.reset();
		}
		String ville = cityField.getText();
		String pays = countryField.getText();
		/*
		int minutes = minuteChoice.getValue();
		int hours = hourChoice.getValue();
		int sec = secondChoice.getValue();
		long time = MyUtil.toMiliseconds(hours, minutes, sec);
		App.loader.setVille(ville);
		App.loader.setPays(pays);
		App.loader.setRefreshTime(time);
		updateTimeLabel(time);
		*/
		labelNuages.textProperty().bind(App.loader.getTextClouds());
		labelVent.textProperty().bind(App.loader.getTextWind());
		labelTemp.textProperty().bind(App.loader.getTextTemp());
		labelVille.textProperty().bind(App.loader.getTextVille());
		labelDesc.textProperty().bind(App.loader.getTextDesc());
		imageView.imageProperty().bind(App.loader.getImageProperty());
		labelHumidite.textProperty().bind(App.loader.getTextHumidity());
		labelVisibilite.textProperty().bind(App.loader.getTextVisibility());
		if(updateRefreshTime()) {
			App.loader.start();
		}
	}
	
	public Boolean updateRefreshTime() {
		int minutes = minuteChoice.getValue();
		int hours = hourChoice.getValue();
		int sec = secondChoice.getValue();
		long time = MyUtil.toMiliseconds(hours, minutes, sec);
		if(time!=0) {
			App.loader.setRefreshTime(time);
			updateTimeLabel(time);
			return true;
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Le temps de rafraichissement ne peut pas valoir 0");
			alert.show();
			return false;
		}
	}
	
	public void updateTimeLabel(long time) {
		int tab[] = MyUtil.convertSeconds((int)time/1000);
		String textTime = "Temps de rafraichissement actuel : ";
		if(tab[0]!=0) {
			textTime += tab[0] + " heures ";
		}
		if(tab[1]!=0) {
			textTime += tab[1] + " minutes ";
		}
		textTime += tab[2] + " secondes ";
		timeLabel.setText(textTime);
	}

}
