package view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginController {
	@FXML TextField Username;

	@FXML Button add;
	
	@FXML GridPane rootPane;
	
	public void start(Stage mainStage) {
		System.out.println("Testing start controller");
	}
	
	public void add() throws IOException {
		if(Username.getText().trim().equalsIgnoreCase("admin")) {
			//This is how you change scenes
			GridPane pane = FXMLLoader.load(getClass().getResource("/view/Admin.fxml"));
			rootPane.getChildren().setAll(pane);
		} else {
			//TODO  Add in a factor that loads a specific user
				//Have in a logs package with a user.txt that contains each user????
			
			//This is how you change scenes
			GridPane pane = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
			rootPane.getChildren().setAll(pane);
		}
	}
}
