package app;
	
import java.io.IOException;

import controller.LoginController;
import data.Admin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Photos extends Application {
	LoginController lc;
	
	@Override
	public void stop() throws IOException {
		try {
			Admin.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		try {
			Admin.load();
			
//			Admin.resetDefault();
						
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			
			Parent root = loader.load();

			lc = loader.getController();
			lc.start(primaryStage);
			
			Scene scene = new Scene(root);
			primaryStage.setTitle("Photos");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
