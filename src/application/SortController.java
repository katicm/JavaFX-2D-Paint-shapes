package application;

import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXListView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import shapes.Point;
import shapes.Square;

public class SortController implements Initializable {
	
	@FXML
    private JFXButton btAdd;

    @FXML
    private JFXListView<Square> lvlist;
    
    private ObservableList<Square> squares = FXCollections.observableArrayList();
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lvlist.setItems(squares);
		btAdd.setOnAction(e->addSquare());
	}
     
	Comparator<Square> comparator = new Comparator<Square>() {
        @Override
        public int compare(Square o, Square o1) {
            return o.getLength()-o1.getLength();
        }
    };
    
	public void addSquare()
    {
    	
    	Dialog<ButtonType>dialog=new Dialog<>();
    	dialog.setTitle("ADD");
    	dialog.setHeaderText("Let's add some squares!");
    	ButtonType done=new ButtonType("ADD",ButtonData.OK_DONE); 
    	dialog.getDialogPane().getButtonTypes().addAll(done,ButtonType.CANCEL);
    	GridPane grid=new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20,150,10,10));
    	TextField X=new TextField();
    	TextField Y=new TextField();
    	TextField length=new TextField(); 
    	JFXColorPicker stroke=new JFXColorPicker();
    	JFXColorPicker fill=new JFXColorPicker();
    	grid.add(new Label("X-axis corner:"), 0, 0);
    	grid.add(X, 1, 0);
    	grid.add(new Label("Y-axis corner:"), 0, 1);
    	grid.add(Y, 1, 1);
    	grid.add(new Label("Lenght:"), 0, 2);
    	grid.add(length, 1, 2);
    	grid.add(new Label("Line color: "), 0, 3);
    	grid.add(stroke, 1, 3);
    	grid.add(new Label("Fill color: "), 0, 4);
    	grid.add(fill, 1, 4);
    	dialog.getDialogPane().setContent(grid); 
    	Platform.runLater(()->X.requestFocus());
    	Optional<ButtonType> result = dialog.showAndWait();
    	if(result.get()==done) {
    	try {
    		if(Integer.parseInt(X.getText())<0||Integer.parseInt(Y.getText())<0||Integer.parseInt(length.getText())<=0)
    			WarningNumberN();
    		else {
    			Square rec=new Square(new Point(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText())),Integer.parseInt(length.getText()),stroke.getValue(),fill.getValue()); 
    			squares.add(0, rec);
    			FXCollections.sort(squares,comparator);
    		}
    	}catch(Exception e)
    	{
    		WarningNumber();
    	}
    	}
    }
	

	public void WarningNumber()
	    {
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Warning");
	    	alert.setHeaderText("Number Format Exception");
	    	alert.setContentText("Sorry I couldn't understand you, Please input Integers!");

	    	alert.showAndWait();
	    }
	public void WarningNumberN()
	    {
	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Warning");
	    	alert.setHeaderText("Negative Integers input");
	    	alert.setContentText("Sorry I couldn't understand you, Please input positive Integers!");

	    	alert.showAndWait();
	    }


	
	
	
	
    




}
