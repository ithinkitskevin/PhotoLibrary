package controller;

import java.io.IOException;
import java.util.ArrayList;

import data.Album;
import data.Context;
import data.Photo;
import data.Tag;
import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ImageController extends Controller{
	@FXML private ListView<String> listOfTags;
	
	@FXML private Button Delete;
	@FXML private Button retToAlbum;
	@FXML private Button Add;
	
	@FXML private Button EditCaption;
	@FXML private Button Move;
	@FXML private Button Copy;
	
	@FXML private Button Prev;
	@FXML private Button Next;
	
	@FXML private TextField Caption;
	@FXML private TextField Type;
	@FXML private TextField Value;
	
	@FXML private Text Time;
	
	@FXML private ImageView Image;
	
	@FXML private ComboBox<String> AlbumBox;
	
	private ArrayList<Photo> photos = null;

	private int currInd = 0;
	private ObservableList<String> obsList;	
	
	private User u = null;
	private Album album = null;
	private Photo photo = null;
	
	@FXML
	public void initialize() {		
		currInd = Context.getInstance().getCurrInd();

		u = Context.getInstance().getUser();
		album = Context.getInstance().getAlbum();
		photo = Context.getInstance().getPhoto();
		
		photos = album.getPhotos();
				
		obsList = FXCollections.observableArrayList(photo.getTagString());		
		
		// Commit the list
		listOfTags.setItems(obsList);
		listOfTags.getSelectionModel().select(0);
		
		AlbumBox.getItems().addAll(u.getAlbumName());

		Image.setImage(new Image(photo.getURL()));
		Time.setText(photo.getTime());
		Caption.setText(photo.getCaption());
	}
	
	public void addTag() {
		String type = Type.getText().trim();
		String value = Value.getText().trim();
					
		if(type.equals("") || value.equals("")) {
			Alert alert = new Alert(AlertType.ERROR, "Type/Value Uninitialized.", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		ObservableList<String> tmp = controllerAddRetObsList((type + "=" + value), obsList);
		
		if(tmp == null) {
			return;
		} else {
			obsList = tmp;
		}
		
		listOfTags.setItems(obsList);
		
		photo.setTag(new Tag(type,value));
		
		Type.setText("");
		Value.setText("");

		if(obsList.size() == 1) {
			listOfTags.getSelectionModel().select(0);
		}	
	}
	
	public void editCaption() {
		String caption = Caption.getText().trim();

		for(int i = 0; i < photos.size(); i++) {
			if(photos.get(i).getCaption().equals(caption)) {
				Alert alert = new Alert(AlertType.ERROR, "Caption Already Exists.", ButtonType.OK);
				alert.showAndWait();
				return;
			}
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "Edit " + caption + "?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.NO)
		   return;
		
		photo.setCaption(caption);
	}
	
	private boolean helpMoveCopy(boolean isCopy) {
		if(AlbumBox.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR, "Did not choose your Destination.", ButtonType.OK);
			alert.showAndWait();
			return false;
		}
		
		String albumName = AlbumBox.getValue().toString().trim();
		
		if(albumName.equals(album.toString())) {
			Alert alert = new Alert(AlertType.ERROR, "Source is same as Destination", ButtonType.OK);
			alert.showAndWait();
			return false;
		}
		
		if(isCopy) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Copy " + albumName + "?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.NO)
				return false;
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Move " + albumName + "?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.NO)
				return false;
		}
		
		return true;
		
	}
	
	public void move() {
		//Move		
		if(!helpMoveCopy(false)) {
			return;
		}
		String albumName = AlbumBox.getValue().toString().trim();

		Album pnt = null;
		
		for(Album a : u.getAlbum()) {
			if(a.toString().equals(albumName)) {
				pnt = a;
			}
		}
				
		album.getPhotos().remove(currInd);	
		
		Photo np = new Photo(photo);
		np.setArrTags(photo.getTag());
		pnt.addPhoto(np);
	}
	
	public void copy() {
		//Copy		
		if(!helpMoveCopy(true)) {
			return;
		}
		String albumName = AlbumBox.getValue().toString().trim();
		
		Album pnt = null;
		
		for(Album a : u.getAlbum()) {
			if(a.toString().equals(albumName)) {
				pnt = a;
			}
		}
		Photo np = new Photo(photo);
		np.setArrTags(photo.getTag());
		pnt.addPhoto(np);
	}
	
	public void deleteTag() {
		if(obsList.size() < 1) {
			Alert alert = new Alert(AlertType.ERROR, "No Tags to delete.", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		// Get the Tags
		String s = listOfTags.getSelectionModel().getSelectedItems().get(0);
		
	    String array1[]= s.split("=");
	    
		// Get confirmation
		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + s + "?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.NO)
		   return;
		
		if(!photo.removeTag(array1[0], array1[1])) {
			Alert alert1 = new Alert(AlertType.CONFIRMATION, "Photo " + s + " doesn't exist", ButtonType.OK);
			alert1.showAndWait();
			return;
		}
				
		obsList.remove(s);

		// Commit the list
		listOfTags.setItems(obsList);

		// Pre-Select and display info for an item if one exists
		if(obsList.size() > 0) {
			listOfTags.getSelectionModel().select(0);
		} 
	}
	
	public void retToAlbum(ActionEvent event) throws IOException {	
		newScene(event,"/view/Album.fxml");
	}
	
	public void prevPhoto() {
		int tmp = currInd-1;
		if(currInd-1 < 0) {
			return;
		}
		photo = photos.get(tmp);
		
		currInd = currInd-1;
		
	    Image.setImage(new Image(photo.getURL()));
	    Context.getInstance().setCurrInd(currInd);
		obsList = FXCollections.observableArrayList(photo.getTagString());	
		Time.setText(photo.getTime());
		Caption.setText(photo.getCaption());

		listOfTags.setItems(obsList);
	}
	
	public void nextPhoto() {
		int tmp = currInd+1;
		if(tmp > (photos.size()-1)) {
			return;
		}
		photo = photos.get(tmp);

		currInd = currInd+1;
		
	    Image.setImage(new Image(photo.getURL()));
	    Context.getInstance().setCurrInd(currInd);
	    obsList = FXCollections.observableArrayList(photo.getTagString());		
		Time.setText(photo.getTime());
		Caption.setText(photo.getCaption());

		listOfTags.setItems(obsList);
	}
	
}
