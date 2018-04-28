package controller;

import java.io.IOException;
import java.util.ArrayList;

import data.Album;
import data.Context;
import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;

public class DashboardController extends Controller {	
	@FXML private Button delete;
	@FXML private Button open;
	@FXML private Button rename;
	@FXML private Button create;
	@FXML private Button logOut;
	@FXML private Button search;
	
	@FXML private TextField editText;
	@FXML private TextField createText;
	
	@FXML private Text early;
	@FXML private Text late;
	@FXML private Text numOfPhoto;
		
	@FXML private ListView<String> listOfAlbum;
	
	private ObservableList<String> obsList;
	
	private User u = null;
	
	@FXML
	public void initialize() {
		// create an ObservableList
		u = Context.getInstance().getUser();
		
		obsList = FXCollections.observableArrayList(u.getAlbumName());		
		
		// Commit the list
		listOfAlbum.setItems(obsList);

		// Pre-Select and display info for an item if one exists
		if(obsList.size() > 0) {
			listOfAlbum.getSelectionModel().select(0);
			editText.setText(u.getAlbum().get(0).toString());
			numOfPhoto.setText(" " + u.getAlbum().get(0).getSize() + " ");
			early.setText(u.getAlbum().get(0).getSortDate(true));
			late.setText(u.getAlbum().get(0).getSortDate(false));
		}
	}
	
	public void deleteAlbum() {
		if(obsList.size() < 1) {
			Alert alert = new Alert(AlertType.ERROR, "No User to delete.", ButtonType.OK);
			alert.showAndWait();
			return;
		}

		String s = listOfAlbum.getSelectionModel().getSelectedItems().get(0);
		
		// Get confirmation
		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + s + "?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.NO)
		   return;
		
		if(!u.rmvUser(s)) {
			Alert alert1 = new Alert(AlertType.CONFIRMATION, "User " + s + " doesn't exist", ButtonType.OK);
			alert1.showAndWait();
			return;
		}
				
		obsList.remove(s);

		// Commit the list
		listOfAlbum.setItems(obsList);

		// Pre-Select and display info for an item if one exists
		if(obsList.size() > 0) {
			listOfAlbum.getSelectionModel().select(0);
		} 
	}
	
	public void renameAlbum() {
		String newAlbumName = editText.getText().trim();
		String oldAlbumName = listOfAlbum.getSelectionModel().getSelectedItems().get(0);
		
		for(int i = 0; i < u.getAlbum().size(); i++) {
			if(u.getAlbum().get(i).toString().equals(newAlbumName)) {
				Alert alert = new Alert(AlertType.ERROR, "Album Name Already Exists.", ButtonType.OK);
				alert.showAndWait();
				return;
			}
		}
		
		if(listOfAlbum.getSelectionModel().getSelectedItems() == null) {
			Alert alert = new Alert(AlertType.ERROR, "Rename: Invalid Target", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		if(newAlbumName.equals("")) {
			Alert alert = new Alert(AlertType.ERROR, "Rename: Invalid Name", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		int index = u.findIndex(oldAlbumName);
		if(index == -1) {
			return;
		} else {
			obsList.set(index, newAlbumName);
		}
		u.editUser(oldAlbumName, newAlbumName);
		
		listOfAlbum.setItems(obsList);
		
		editText.setText("");
		
		return;
	}
	
	public void createAlbum() {
		String newAlbumName = createText.getText().trim();
		
		ObservableList<String> tmp = controllerAddRetObsList(newAlbumName, obsList);
		
		if(tmp == null) {
			return;
		} else {
			obsList = tmp;
		}
				
		// Add the song to the list
		u.addAlbum(new Album(newAlbumName));
				
		listOfAlbum.setItems(obsList);

		createText.setText("");

		if(obsList.size() == 1) {
			listOfAlbum.getSelectionModel().select(0);
		}
	}
	
	public void openAlbum(ActionEvent event) throws IOException {
		if(obsList.size() < 1) {
			Alert alert = new Alert(AlertType.ERROR, "No User to Open.", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		Context.getInstance().setAlbum(u.getAlbum().get(listOfAlbum.getSelectionModel().getSelectedIndex()));
	
		newScene(event,"/view/Album.fxml");
	}
	
	public void handleMouseClick() {
		if(listOfAlbum.getSelectionModel().getSelectedItem() == null) {
			return;
		}
			
		ArrayList<Album> a = u.getAlbum();
		Album chosenA = a.get(listOfAlbum.getSelectionModel().getSelectedIndex());
		if(chosenA != null) {
			editText.setText(chosenA.toString());
			numOfPhoto.setText(" " + chosenA.getSize() + " ");
			early.setText(chosenA.getSortDate(true));
			late.setText(chosenA.getSortDate(false));
		}
	}
	
	public void retToSearch(ActionEvent event) throws IOException {	
		newScene(event,"/view/SSelector.fxml");
	}
}
