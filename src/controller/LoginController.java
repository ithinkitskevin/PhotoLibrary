package controller;

import java.io.IOException;

import data.Admin;
import data.Context;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginController extends Controller{
	@FXML private TextField Username;

	@FXML private Button login;
	
	@FXML private GridPane rootPane;
	
	public void start(Stage primaryStage) {
		primaryStage.centerOnScreen();
	}
	
	public void login(ActionEvent event) throws IOException {
		String user = Username.getText().trim();
		if(user.equalsIgnoreCase("admin")) {
			newScene(event,"/view/Admin.fxml");
			return;
		} else {
			if(Admin.getAllUser() == null) {
				System.out.println("Empty...");
				return;
			}
			for(int i = 0; i < Admin.getAllUser().size(); i++) {
				String s = Admin.getAllUser().get(i).toString();
				if(s.equals(user)) {
					Context.getInstance().setUser(Admin.getUser().get(i));
					newScene(event,"/view/Dashboard.fxml");
					return;
				}
			}
		}
		
		Alert alert = new Alert(AlertType.ERROR, "Username Failed", ButtonType.OK);
		alert.showAndWait();
		return;
	}
}
