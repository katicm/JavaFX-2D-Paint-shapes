package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Sort_KaticMarkoIT16_2016 extends Application {

	
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent loader=FXMLLoader.load(getClass().getResource("Sort.fxml")); //poziva initialize metodu
		Scene scene=new Scene(loader);
		primaryStage.setScene(scene);
		primaryStage.setTitle("IT16-2016 KATIC MARKO");
		primaryStage.show();
		
	}

}