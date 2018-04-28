package controller;

import data.Admin;
import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class AdminController extends Controller{
	@FXML private Text text;

	@FXML private Button delete;
	@FXML private Button login;
	
	@FXML private TextField addUser;
	
	@FXML private ListView<String> listOfUser;
	
	private ObservableList<String> obsList;
	
	@FXML
	public void initialize() {
		// create an ObservableList
		obsList = FXCollections.observableArrayList(Admin.getAllUser());		
			
		// Commit the list
		listOfUser.setItems(obsList);

		// Pre-Select and display info for an item if one exists
		if(obsList.size() > 0) {
			listOfUser.getSelectionModel().select(0);
		}
	}

	public void add() {
		String tmpUser = addUser.getText().trim();
		
		ObservableList<String> tmp = controllerAddRetObsList(tmpUser, obsList);
		
		if(tmp == null) {
			return;
		} else {
			obsList = tmp;
		}
		
		listOfUser.setItems(obsList);
		Admin.addUser(new User(tmpUser));
		
		addUser.setText("");

		if(obsList.size() == 1) {
			listOfUser.getSelectionModel().select(0);
		}
	}
	
	public void delete() {
		// Check if they can delete anything
		if(obsList.size() < 2) {
			Alert alert = new Alert(AlertType.ERROR, "No User to delete.", ButtonType.OK);
			alert.showAndWait();
			return;
		}

		// Get the User
		String s = listOfUser.getSelectionModel().getSelectedItems().get(0);
		
		if(s.equalsIgnoreCase("stock")) {
			Alert alert = new Alert(AlertType.ERROR, "Delete: Can't delete Stock", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		// Get confirmation
		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + s.toString() + "?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.NO)
		   return;
				
		if(!Admin.rmvUser(s)) {
			Alert alert1 = new Alert(AlertType.CONFIRMATION, "User " + s + " doesn't exist", ButtonType.OK);
			alert1.showAndWait();
			return;
		}
		obsList.remove(s);

		// Commit the list
		listOfUser.setItems(obsList);

		// Pre-Select and display info for an item if one exists
		if(obsList.size() > 0) {
			listOfUser.getSelectionModel().select(0);
		} else {
			addUser.setText("");
		}
	}
}
