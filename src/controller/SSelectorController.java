package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

public class SSelectorController extends Controller {
	
	@FXML private ListView<String> listOfSearch;
	@FXML private ListView<String> listOfPossible;

	private ObservableList<String> srchList;
	private ObservableList<String> pssbList;

	@FXML private DatePicker fromDate;
	@FXML private DatePicker toDate;
	
	@FXML private Button dateButton;
	@FXML private Button LeftButton;
	@FXML private Button RightButton;
	@FXML private Button tagButton;
	@FXML private Button returnToDashboard;
		
	private User u = null;
	
	private ArrayList<String> plausTags =  new ArrayList<String>();
	private ArrayList<String> srchTags = new ArrayList<String>();
	private ArrayList<Photo> allPhotos = new ArrayList<Photo>();
	private ArrayList<Photo> newPhoto = new ArrayList<Photo>();

	@FXML
	public void initialize() {	
		u = Context.getInstance().getUser();
		
		plausTags.clear();
		allPhotos.clear();
		srchTags.clear();
		newPhoto.clear();
		
		ArrayList<Album>alb = u.getAlbum();
		
		//Will switch to Dictionary after seeing rather or not everything works
		for(int a = 0; a < alb.size(); a++) {
			allPhotos.addAll(alb.get(a).getPhotos());
			for(int p = 0; p < alb.get(a).getSize(); p++) {
				for(int t = 0; t < alb.get(a).getPhotos().get(p).getSize(); t++) {
					if(!plausTags.contains(alb.get(a).getPhotos().get(p).getTag().get(t).getString())) {
						plausTags.add(alb.get(a).getPhotos().get(p).getTag().get(t).getString());
					}
				}
			}
		}
				
		pssbList = FXCollections.observableArrayList(plausTags);		
		srchList = FXCollections.observableArrayList(srchTags);		
				
		listOfPossible.setItems(pssbList);
		listOfSearch.setItems(srchList);
	}
	
	public void searchDate(ActionEvent event) throws IOException {	
		LocalDate from = fromDate.getValue();
		LocalDate to = toDate.getValue();
		
		if(from == null || to == null) {
			Alert alert = new Alert(AlertType.ERROR, "From/To Date Not Initialized.", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		if(to.isBefore(from)) {
			Alert alert = new Alert(AlertType.ERROR, "To Date is before From Date", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		
		sortDate(from, to);

		Context.getInstance().setArray(newPhoto);
		Context.getInstance().setComeFromSSelector(true);
		
		newScene(event,"/view/SView.fxml");
	}
	
	private void sortTag(boolean isAll) {
		//srchTag
		for(Photo p: allPhotos) {
			if(isAll) {
				if(p.getTagString().containsAll(srchTags)) {
					newPhoto.add(p);
				} 
			} else {
				if(!Collections.disjoint(p.getTagString(), srchTags)) {
					newPhoto.add(p);
				}				
			}
		}
	}
	
	private void sortDate(LocalDate newFrom, LocalDate newTo) {
		LocalDate from = newFrom;
		LocalDate to = newTo;
		
		for(Photo p: allPhotos) {
			LocalDate d = p.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if(d.isAfter(from) && d.isBefore(to) || d.isEqual(from) || d.isEqual(to)) {
				newPhoto.add(p);
			}
		}
	}
	
	public void searchTag(ActionEvent event) throws IOException {	
		if(srchList.isEmpty()) {
			return;
		}
		
		if(srchList.size() == 1) {
			sortTag(true);
			
			Context.getInstance().setArray(newPhoto);
			Context.getInstance().setComeFromSSelector(true);
					
			newScene(event,"/view/SView.fxml");
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Options");
		alert.setHeaderText("Search for All Photos with All of the Tags or Any One of the Tags");

		ButtonType buttonTypeOne = new ButtonType("All of the Tags");
		ButtonType buttonTypeTwo = new ButtonType("One of the Tags");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne){
			//All of the Tags
			sortTag(true);
		} else if (result.get() == buttonTypeTwo) {
			//One of the Tags
			sortTag(false);
		} else {
			return;
		}
		
		Context.getInstance().setArray(newPhoto);
		Context.getInstance().setComeFromSSelector(true);
				
		newScene(event,"/view/SView.fxml");
	}
	
	public void moveLeft() {
		String search = handleMouseSearch();
		if(search.equals("")) {
			return;
		}
				
		ObservableList<String> tmp = controllerAddRetObsList((search), pssbList);
		
		if(tmp == null) {
			return;
		} else {
			pssbList = tmp;
		}
		
		for(int i = 0; i < srchList.size(); i++) {	
			System.out.println(srchList.get(i).toString() + " vs " + search);
			if(srchList.get(i).toString().equals(search)) {
				srchList.remove(i);
				srchTags.remove(i);
				listOfSearch.setItems(srchList);
			}
		}
		
		listOfPossible.setItems(pssbList);
		
	}
	
	public void moveRight() {
		String search = handleMousePossible();
		if(search.equals("")) {
			return;
		}
				
		ObservableList<String> tmp = controllerAddRetObsList((search), srchList);
		
		if(tmp == null) {
			return;
		} else {
			srchList = tmp;
		}
		
		for(int i = 0; i < pssbList.size(); i++) {	
			if(pssbList.get(i).toString().equals(search)) {
				pssbList.remove(i);
				listOfPossible.setItems(pssbList);
			}
		}
		
		srchTags.add(search);
		listOfSearch.setItems(srchList);
				
	}
	
	public String handleMouseSearch(){
		String search = (String) listOfSearch.getSelectionModel().getSelectedItems().get(0);
		if(search == null) {
			return "";
		}
		return search.trim();
	}
	
	public String handleMousePossible(){
		String possible = (String) listOfPossible.getSelectionModel().getSelectedItems().get(0);
		if(possible == null) {
			return "";
		}
		
		return possible.trim();
	}
	
	public void returnToDashboard(ActionEvent event) throws IOException {	
		newScene(event,"/view/Dashboard.fxml");
	}
}
