package controller;

import java.io.IOException;

import data.Admin;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class Controller {	
	public void newScene(ActionEvent event, String filePath) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(filePath));
		
		AnchorPane pane = loader.load();
		Controller controller = loader.getController();
				
		Scene scene = new Scene(pane);
		
		//We need to get the stage...
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(scene);
		window.show();
		window.setResizable(false);
		window.centerOnScreen();
	}
	
	public void goToLogin(ActionEvent event) throws IOException {
		Admin.save();
		newScene(event,"/view/Login.fxml");
	}
	
	public ObservableList<String> controllerAddRetObsList(String text, ObservableList<String> obsList) {		
		if(text.equals("")) {
			Alert alert = new Alert(AlertType.ERROR, "Add: Invalid Name", ButtonType.OK);
			alert.showAndWait();
			return null;
		}		
		
		for(int i = 0; i < obsList.size(); i++) {
			String temp = obsList.get(i).toString();
			if(temp.equals(text)) {
				Alert alert = new Alert(AlertType.ERROR, "Add: Already Exists", ButtonType.OK);
				alert.showAndWait();
				return null;
			}
		}
		// Get confirmation
		Alert alert = new Alert(AlertType.CONFIRMATION, "Add " + text + "?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.NO) {
			return null;
		}
				
		// Add the song to the list
		obsList.add(text);
		
		return obsList;
	}
	
	public void retToSearch(ActionEvent event) throws IOException {	
		newScene(event,"/view/SSelector.fxml");
	}
	
}
