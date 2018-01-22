package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Paint_KaticMarkoIT16_2016 extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent loader=FXMLLoader.load(getClass().getResource("MainStage.fxml")); //poziva initialize metodu
		Scene scene=new Scene(loader);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("IT16-2016 KATIC MARKO");
		primaryStage.setMinWidth(916);
		primaryStage.setMinHeight(640);
		primaryStage.setMaxHeight(640);
		primaryStage.show();
		
		PaintController cont=new PaintController();
		primaryStage.setOnCloseRequest(e->{
			e.consume();
			cont.dialogExit();
		});
	}
	public static void main(String[] args) {
		launch(args);
	}
}

