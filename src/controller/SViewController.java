package controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import data.Album;
import data.Context;
import data.Photo;
import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class SViewController extends Controller{
	@FXML private ListView<String> ListOfPhotos;
	
	@FXML private Button RetToDashboard;
	@FXML private Button Create;
	@FXML private Button View;
	
	@FXML private TextField AlbumText;
	@FXML private ImageView Thumbnail;
	
	@FXML private Text PhotoTitle;
	@FXML private Text OldTime;
	@FXML private Text NumOfPhotos;
	
	private User u = null;
	private ArrayList<Photo> newPhoto = new ArrayList<Photo>();
	private ArrayList<String> newPhotoName = new ArrayList<String>();
	private ArrayList<Album>alb = new ArrayList<Album>();
	private int currInd = 0;
	
	private ObservableList<String> obsList;	
	
	@FXML
	public void initialize() {
		u = Context.getInstance().getUser();
		
		alb = u.getAlbum();
				 
		newPhoto = Context.getInstance().getArray();
		for(Photo p : newPhoto) {
			newPhotoName.add(p.getCaption());
		}
		
//		System.out.println(Arrays.asList(newPhotoName.toArray()));
		 
		obsList = FXCollections.observableArrayList(newPhotoName);		
		
		ListOfPhotos.setItems(obsList);
		
		if(newPhoto.size() > 0) {
			Thumbnail.setImage(new Image(newPhoto.get(0).getURL()));
			OldTime.setText("Last Modified: " + newPhoto.get(0).getTime());
		    NumOfPhotos.setText("Number of Photos: " + newPhoto.size());
			PhotoTitle.setText(newPhoto.get(0).getCaption());
		}
	}
	
	public void handleMouseClick(MouseEvent event) throws MalformedURLException {
		int x = ListOfPhotos.getSelectionModel().getSelectedIndex();
		if(x < 0) {
			return;
		}
		Thumbnail.setImage(new Image(newPhoto.get(x).getURL()));
		OldTime.setText("Last Modified: " + newPhoto.get(x).getTime());
	    NumOfPhotos.setText("Number of Photos: " + newPhoto.size());
		PhotoTitle.setText(newPhoto.get(x).getCaption());
		
		currInd = x;
	}
	
	public void retToDashboard(ActionEvent event) throws IOException {		
		newScene(event,"/view/Dashboard.fxml");
	}
	
	public void createAlbum() {
		if(newPhoto.size() < 1) {
			return;
		}
		
		if(AlbumText.getText().trim().equals("")) {
			Alert alert = new Alert(AlertType.ERROR, "Invalid Name.", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		for(int i = 0; i < alb.size(); i++) {
			if(alb.get(i).toString().equals(AlbumText.getText().trim())) {
				Alert alert = new Alert(AlertType.ERROR, "Album Name Already Exists.", ButtonType.OK);
				alert.showAndWait();
				return;
			}
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Copy or Move");
		alert.setHeaderText("Do you want to Copy the images or Move the images");

		ButtonType buttonTypeOne = new ButtonType("Move");
		ButtonType buttonTypeTwo = new ButtonType("Copy");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne){
			//Move
			String albumName = AlbumText.getText().trim();
			
			Album a = new Album(albumName);
			
			for(Photo p: newPhoto) {
				Photo np = new Photo(p);
				np.setArrTags(p.getTag());
				a.addPhoto(np);
			}
			
			for(int index = 0; index < newPhoto.size(); index++) {
				String s = newPhoto.get(index).getCaption();
				for(int alb = 0; alb < u.getAlbum().size(); alb++) {
					for(int ind = 0; ind < u.getAlbum().get(alb).getPhotos().size(); ind++) {
						if(u.getAlbum().get(alb).getPhotos().get(ind).getCaption().equals(s)) {
							u.getAlbum().get(alb).getPhotos().remove(ind);
						}
					}
				}
			}
			
			u.addAlbum(a);
		} else if (result.get() == buttonTypeTwo) {
			//Copy
			String albumName = AlbumText.getText().trim();
			
			Album a = new Album(albumName);
			
			for(Photo p: newPhoto) {
				Photo np = new Photo(p);
				np.setArrTags(p.getTag());
				a.addPhoto(np);
			}
			
			u.addAlbum(a);
		} else {
			return;
		}
	}
	
	public void viewPhoto(ActionEvent event) throws IOException {	
		Context.getInstance().setPhotos(newPhoto);
		Context.getInstance().setCurrInd(currInd);
		newScene(event,"/view/SImage.fxml");
	}
	
}
