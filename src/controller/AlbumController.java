package controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import data.Album;
import data.Context;
import data.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AlbumController extends Controller {
	@FXML private Button delete;
	@FXML private Button retToDashboard;
	@FXML private Button view;
	@FXML private Button add;

	@FXML private ImageView thumbnail;
	
	@FXML private TextField caption;
	@FXML private Text filePath;
	
	@FXML private Text title;
	@FXML private Text photoTitle;
	@FXML private Text oldTime;
	@FXML private Text numOfPhotos;
	
	@FXML private ListView<String> listOfPhotos;
	
	private ArrayList<Photo> photos = new ArrayList<Photo>();
	
	private ObservableList<String> obsList;	
	private Album album = null;
	private int currInd = 0;
		
	@FXML
	public void initialize() {
		Context.getInstance().getUser();
		album = Context.getInstance().getAlbum();
				
		photos = album.getPhotos();
		
		title.setText(album.toString());
		
		obsList = FXCollections.observableArrayList(album.getPhotosName());		
				
		// Commit the list
		listOfPhotos.setItems(obsList);

		// Pre-Select and display info for an item if one exists
		if(obsList.size() > 0) {
			listOfPhotos.getSelectionModel().select(0);
			thumbnail.setImage(new Image(album.getPhotos().get(0).getURL()));
			oldTime.setText("Last Modified: " + album.getPhotos().get(0).getTime());
		    numOfPhotos.setText("Number of Photos: " + album.getPhotos().size());
			filePath.setText(album.getPhotos().get(0).getURL());
//			caption.setText(album.getPhotos().get(0).getCaption());
		}
	}
	
	public void retToDashboard(ActionEvent event) throws IOException {	
		newScene(event,"/view/Dashboard.fxml");
	}
	
	public void deletePhoto() {
		if(obsList.size() < 1) {
			Alert alert = new Alert(AlertType.ERROR, "No Photo to delete.", ButtonType.OK);
			alert.showAndWait();
			return;
		}

		// Get the User
		String s = listOfPhotos.getSelectionModel().getSelectedItems().get(0);
				
		// Get confirmation
		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + s + "?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.NO)
		   return;
		
		if(!album.rmvPhoto(s)) {
			Alert alert1 = new Alert(AlertType.CONFIRMATION, "Photo " + s + " doesn't exist", ButtonType.OK);
			alert1.showAndWait();
			return;
		}
				
		obsList.remove(s);

		// Commit the list
		listOfPhotos.setItems(obsList);

		// Pre-Select and display info for an item if one exists
		if(obsList.size() > 0) {
			listOfPhotos.getSelectionModel().select(0);
		} 
	}

	public void addPhoto() {		
		Node node = (Node) filePath;
		
		String newPhoto = caption.getText().trim();
		if(newPhoto.equals("")) {
			Alert alert = new Alert(AlertType.ERROR, "Rename: Invalid Name", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Get File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.gif", "*.png", "*.jpg", "*.jpeg"));
		File selectedFile = fileChooser.showOpenDialog(node.getScene().getWindow());
				
		if(selectedFile == null) {
			return;
		}
				
		ObservableList<String> tmp = controllerAddRetObsList(newPhoto, obsList);
		
		if(tmp == null) {
			return;
		} else {
			obsList = tmp;
		}
				
		album.addPhoto(new Photo(selectedFile.getAbsoluteFile(), newPhoto));
				
		listOfPhotos.setItems(obsList);
		thumbnail.setImage(new Image(album.getPhotos().get(0).getURL()));
		oldTime.setText("Last Modified: " + album.getPhotos().get(0).getTime());
	    numOfPhotos.setText("Number of Photos: " + album.getPhotos().size());
	    filePath.setText(album.getPhotos().get(0).getURL());
	    
	    caption.setText("");
	    
		if(obsList.size() == 1) {
			listOfPhotos.getSelectionModel().select(0);
		}
	}
	
	public void viewPhoto(ActionEvent event) throws IOException {
		if(listOfPhotos.getSelectionModel().getSelectedItems().get(0) == null) {
			return;
		}
		
		String s = listOfPhotos.getSelectionModel().getSelectedItems().get(0);
		if(album.getPhotosName() != null) {
			for(int i = 0; i < photos.size(); i++) {
				if(photos.get(i).toString().equals(s)) {
					Context.getInstance().setPhoto(photos.get(i));
				}
			}
		}
		
		Context.getInstance().setCurrInd(currInd);
		
		newScene(event,"/view/Image.fxml");
	}
	
	public void handleMouseClick(MouseEvent event) throws MalformedURLException {
		int x = listOfPhotos.getSelectionModel().getSelectedIndex();
		if(x < 0) {
			return;
		}
	    thumbnail.setImage(new Image(album.getPhotos().get(x).getURL()));
	    oldTime.setText("Last Modified: " + album.getPhotos().get(x).getTime());
	    numOfPhotos.setText("Number of Photos: " + album.getPhotos().size());
	    filePath.setText(album.getPhotos().get(x).getURL());
//		caption.setText(album.getPhotos().get(x).getCaption());

		currInd = x;
	}
}
