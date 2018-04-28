package controller;

import java.io.IOException;
import java.util.ArrayList;

import data.Context;
import data.Photo;
import data.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class SImageController extends Controller{
	@FXML private ListView<String> listOfTags; 
	
	@FXML private Button Delete;
	@FXML private Button retToSearch;
	@FXML private Button Add;
	
	@FXML private Button EditCaption;
	
	@FXML private Button Prev;
	@FXML private Button Next;
	
	@FXML private TextField Caption;
	@FXML private TextField Type;
	@FXML private TextField Value;
	
	@FXML private Text Time;	
	
	@FXML private ImageView Image;
	
	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private ArrayList<String> photoNames = new ArrayList<String>();
	private Photo currPhoto = null;
	private int currInd = 0;
	
	private ObservableList<String> obsList;	
	
	@FXML
	public void initialize() {
		photos = Context.getInstance().getPhotos();
		currInd = Context.getInstance().getCurrInd();
				
		currPhoto = photos.get(currInd);
		
		for(Photo p: photos) {
			photoNames.add(p.getCaption());
		}
				
		obsList = FXCollections.observableArrayList(photos.get(currInd).getTagString());		
		Image.setImage(new Image(photos.get(currInd).getURL()));
		// Commit the list
		Time.setText(photos.get(currInd).getTime());
		Caption.setText(photos.get(currInd).getCaption());

		listOfTags.setItems(obsList);
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
		
		if(!currPhoto.removeTag(array1[0], array1[1])) {
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
		
		currPhoto.setTag(new Tag(type,value));
		
		Type.setText("");
		Value.setText("");

		if(obsList.size() == 1) {
			listOfTags.getSelectionModel().select(0);
		}	
	}
	
	public void retToSView(ActionEvent event) throws IOException {	
		Context.getInstance().setComeFromSSelector(false);
		newScene(event,"/view/SView.fxml");
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
		
		currPhoto.setCaption(caption);
	}
	
	public void prevPhoto() {
		int tmp = currInd-1;
		if(currInd-1 < 0) {
			return;
		}
		currPhoto = photos.get(tmp);
		
	    Image.setImage(new Image(currPhoto.getURL()));
	    
		obsList = FXCollections.observableArrayList(currPhoto.getTagString());	
		Time.setText(currPhoto.getTime());
		Caption.setText(currPhoto.getCaption());

		listOfTags.setItems(obsList);
		currInd = currInd-1;
	}
	
	public void nextPhoto() {
		int tmp = currInd+1;
		if(tmp > (photos.size()-1)) {
			return;
		}
		currPhoto = photos.get(tmp);

	    Image.setImage(new Image(currPhoto.getURL()));
	    
	    obsList = FXCollections.observableArrayList(currPhoto.getTagString());		
		Time.setText(currPhoto.getTime()); 
		Caption.setText(currPhoto.getCaption());

		listOfTags.setItems(obsList);
		
		currInd = currInd+1;
	}
	
}
