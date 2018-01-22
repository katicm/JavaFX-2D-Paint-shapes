package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import shapes.Circle;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.Shapes;
import shapes.Square;
import shapes.Triangle;

public class PaintController implements Initializable{

	@FXML
    private MenuItem close;
	@FXML
    private Label lbStatusbar;
	@FXML
    private Label lbStatusmode;
	@FXML
    private ImageView point;
	@FXML
    private ImageView line;
	@FXML
    private ImageView circle;
	@FXML
    private ImageView triangle;
	@FXML
    private ImageView rectangle;
	@FXML
    private ImageView square;
	@FXML
    private JFXButton btSelect;
	@FXML
    private JFXColorPicker strokecolor;
	@FXML
    private JFXButton btDelete;
	@FXML
    private JFXColorPicker fillcolor;
	@FXML
    private ImageView spray;
	@FXML
    private JFXSlider slider;
	@FXML
    private JFXButton btEdit;
	@FXML
    private StackPane stackcanvas;
	@FXML
    private Canvas canvas;
	
	private GraphicsContext gc;
	private Color strokeColor=Color.BLACK;
	private Color fillColor=Color.WHITE;
	private String mode;
	private Line oldLine=new Line(new Point(-1,-1),new Point(-1,-1));
	private ArrayList<Shapes> list=new ArrayList<Shapes>();
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gc=canvas.getGraphicsContext2D();
		point();
		canvas.setOnMouseClicked(mouseHandlerI);  
		canvas.setOnMouseMoved(mouseHandlerII); 
		canvas.heightProperty().bind(stackcanvas.heightProperty());
		canvas.widthProperty().bind(stackcanvas.widthProperty());
		canvas.widthProperty().addListener(evt -> reduceCanvas());
		stackcanvas.heightProperty().addListener(evt -> reduceCanvas()); 
	}
	EventHandler<MouseEvent> mouseHandlerI = new EventHandler<MouseEvent>() {
		 
        @Override
        public void handle(MouseEvent mouseEvent) {
        	Point pointClicked=new Point((int)mouseEvent.getX(),(int)mouseEvent.getY());
            if(mode=="select" || mode=="edit" || mode=="delete" ) 
            {
            	shouldSelectObject(pointClicked);
            }else if(mode=="spray")
            {
            	canvas.setOnMouseDragged(mouseHandlerIII);
        	}else
            	whatToDraw(pointClicked);
        }
    };
    
    EventHandler<MouseEvent> mouseHandlerII = new EventHandler<MouseEvent>() {
		 
        @Override
        public void handle(MouseEvent mouseEvent1) {
            lbStatusbar.setText("X:"+Integer.toString((int)mouseEvent1.getX())+"   Y:"+Integer.toString((int)mouseEvent1.getY()));
        }
    };
    
    EventHandler<MouseEvent> mouseHandlerIII = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent2) {
            gc.setFill(getFillColor());
            gc.fillOval(mouseEvent2.getX()-slider.getValue()/2, mouseEvent2.getY()-slider.getValue()/2, slider.getValue(), slider.getValue());
        }
    };
    
    public void selectclicked()
    {
    	reset();
    	setMode("select");
    }
    public void editclicked()
    {
    	reset();
    	setMode("edit");
    	for(int i=list.size()-1;i>=0;i--)
    	{
    		if(list.get(i).isSelected()==true)
    		{
    			Edit(list.get(i),i);
    			list.get(i).setSelected(true);
    			list.get(i).select(gc); //posle editovanja ostaje selektovan
    			setMode("select");
    		}
    	}
    	if(getMode()=="edit")
		{
			Warning();
			setMode("select");
		}
    }
    public void deleteclicked()
    {
    	reset();
    	setMode("delete");
    	for(int i=0;i<list.size();i++)
    	{
    		if(list.get(i).isSelected()==true)
    		{
    			if(dialogDelete()==true)
				{
					list.remove(i);
					resetCanvas();
				}
    			setMode("select");
    		}
    	}
    	if(getMode()=="delete")
		{
			Warning(); 
			setMode("select");
		}
    }
    private void shouldSelectObject(Point pc)
    {
    	try {
    	int i;
    	for(i=list.size()-1;i>=0;i--)
    	{
    		Shapes temp=list.get(i);
    		if(temp.contains(pc))
    		{
    			resetCanvas();
    			temp.paint(gc);
    			temp.select(gc);
    			temp.setSelected(true);
    			break; //poslednji nacrtani selektuje se
    		}
    	}
    	if(i==-1) //unselect
    	{
    		for(i=i+1;i<list.size()-1;i++)
    		{
    			list.get(i).setSelected(false);
    		}
    		resetCanvas();
    	}
    	}
    	catch (Exception IndexOutOfBounds)
    	{
    		
    	}
    }
    
    private void whatToDraw(Point pc)
    {
    	switch(mode)
    	{
    	case "point":
    	{
    		Point temp=new Point(pc.getX(),pc.getY(),this.getStrokeColor());
    		temp.paint(gc);
    		list.add(temp);
    		break;
    	}
    	case "line":
    	{
    		if(oldLine.getStart().getX()>=0)
    		{
    			Line temp=new Line(oldLine.getStart(),pc,this.getStrokeColor());
    			temp.paint(gc);
    			resetOldLine();
    			list.add(temp);
    		}else
    			oldLine.setStart(pc);
    		
    		break;
    	}
    	case "circle":
    	{
    		Circle temp=new Circle(pc,loadDialogCircle(),this.getStrokeColor(),this.getFillColor());
    		temp.paint(gc);
    		list.add(temp);
    		break;
    	}
    	case "square":
    	{
    		Square temp=new Square(pc,loadDialogSquare(),this.getStrokeColor(),this.getFillColor());
    		temp.paint(gc);
    		list.add(temp);
    		break;
    	}
    	case "rectangle":
    	{
    		loadDialogRectangle(pc); 
    		break;
    	}
    	case "triangle":
    	{
    		if(oldLine.getStart().getX()>=0)
    		{
    			if(oldLine.getEnd().getY()>=0)
    			{
    				Triangle temp=new Triangle(oldLine.getStart(),oldLine.getEnd(),pc,this.getStrokeColor(),this.getFillColor());
    				temp.paint(gc);
    				list.add(temp);
    				resetOldLine();
    			}
    			else
    				oldLine.setEnd(pc);
    		}
    		else
    			oldLine.setStart(pc);
    		break;
    	}
    	default:
    	{
    		System.out.print("Pls man select shape!");
    	}
    	}
    	
    }
    public void Edit(Shapes temp,int i)
    {
    	if(temp instanceof Point)
		{
			Point point=(Point)temp;
			customDialogPoint(point,i);
		}else if(temp instanceof Line)
		{
			Line line=(Line)temp;
			customDialogLine(line,i);
		}else if(temp instanceof Circle)
		{
			Circle circle=(Circle)temp;
			customDialogCircle(circle,i);
		}else if(temp instanceof Rectangle)
		{
			Rectangle rectangle=(Rectangle)temp;
			customDialogRectangle(rectangle,i);	
		}else if(temp instanceof Square)
		{ 
			Square square=(Square)temp;
			customDialogSquare(square,i); 
		
		}else if(temp instanceof Triangle)
		{
			Triangle triangle=(Triangle)temp;
			customDialogTriangle(triangle,i);
		}else 
		{
			System.out.print("ERROR! Never gonna happen!");
		}
		
    }
    public void resetCanvas()
    {
    	Color color=Color.web("#FFFFFF");
		gc.setFill(color);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(int y=0;y<list.size();y++)
		{
			list.get(y).setSelected(false);
			list.get(y).paint(gc);
		}
    }
    public void strokecolorchange()
    {
    	setStrokeColor(strokecolor.getValue());
    }
    public void fillcolorchange()
    {
    	setFillColor(fillcolor.getValue());
    }
	public Color getStrokeColor() {
		return strokeColor;
	}
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor=strokeColor;
	}
	public Color getFillColor() {
		return fillColor;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor=fillColor;
	}
	public void setMode(String mode)
	{
		this.mode=mode;
		lbStatusmode.setText("mode: "+getMode());
	}
	public String getMode()
	{
		return this.mode;
	}
	public void point()
	{
		reset();
		point.setImage(new Image("images/pointp.png"));
		setMode("point");
	}
	public void line()
	{
		reset();
		line.setImage(new Image("images/linep.png"));
		setMode("line");
	}
	public void circle()
	{
		reset();
		circle.setImage(new Image("images/circlep.png"));
		setMode("circle");
	}
	public void square()
	{
		reset();
		square.setImage(new Image("images/squarep.png"));
		setMode("square");
	}
	public void rectangle()
	{
		reset();
		rectangle.setImage(new Image("images/rectanglep.png"));
		setMode("rectangle");
	}
	public void triangle()
	{
		reset();
		triangle.setImage(new Image("images/trianglep.png"));
		setMode("triangle");
	}
	public void spray ()
	{
		reset();
		spray.setImage(new Image("images/sprayp.png"));
		setMode("spray");
	} 
	private void reduceCanvas()
	{
		stackcanvas.setPrefHeight(stackcanvas.getHeight());
		stackcanvas.setPrefWidth(stackcanvas.getWidth());
		resetCanvas();
	}
    private void reset()
	{
		Image pressed0=new Image("images/point.png");
		point.setImage(pressed0);
		Image pressed1=new Image("images/line.png");
		line.setImage(pressed1);
		Image pressed2=new Image("images/circle.png"); 
		circle.setImage(pressed2);
		Image pressed3=new Image("images/square.png");
		square.setImage(pressed3);
		Image pressed4=new Image("images/rectangle.png");
		rectangle.setImage(pressed4);
		Image pressed5=new Image("images/triangle.png");
		triangle.setImage(pressed5);
		Image pressed6=new Image("images/spray.png");
		spray.setImage(pressed6);
		setMode("reset");
		canvas.setOnMouseDragged(null);
		resetOldLine();
	}
	private void resetOldLine()
	{
		oldLine.setStart(new Point(-1,-1));
		oldLine.setEnd(new Point(-1,-1));
	}
	public void Warning()
    {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Warning");
    	alert.setHeaderText("Nothing is selected!");
    	alert.setContentText("Please select some shape! ");
    	alert.showAndWait();
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
	private boolean dialogDelete()
    {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirm your Request");
    	alert.setHeaderText("Delete an object.");
    	alert.setContentText("Are you sure you want to delete? This action cannot be undone. :(");
    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
    		return true;
    	} else 
    		return false;
    	
    }
	
    public void dialogExit()
    {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setGraphic(new ImageView(this.getClass().getResource("/images/sad.png").toString()));
    	alert.setTitle("Confirmation");
    	alert.setHeaderText("Confirm Exit ");
    	alert.setContentText("Are you sure you want to Exit?"); 
    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
    		try {
    		Platform.exit();
    		}
    		catch(Exception e)
    		{
    			
    		}
		}
	}
    private int loadDialogCircle()
    {
    	TextInputDialog dialog = new TextInputDialog("50");
    	dialog.setTitle("Circle");
    	dialog.setHeaderText("Let's make circle together! :D");
    	dialog.setContentText("Please enter the radius:");
    	Optional<String> result = dialog.showAndWait(); //moze da ima null vrednost
    	if(result.isPresent()) {
    	try {
        int length=Integer.parseInt(String.valueOf(result.get()));
        if(length>0)
        {
        	return length;
        }else
        	WarningNumberN();
        
        }catch(NumberFormatException e)
        {
        	WarningNumber();
        }
    	}
    	return 0;
    } 
    private int loadDialogSquare()
    { 
    	TextInputDialog dialog = new TextInputDialog("100");
    	dialog.setTitle("Square");
    	dialog.setHeaderText("Let's make square together! :D");
    	dialog.setContentText("Please enter the length:"); 
    	Optional<String> result = dialog.showAndWait(); 
    	if(result.isPresent()) { 
    	try { 
    	int length=Integer.parseInt(String.valueOf(result.get()));
    	if(length<=0)
    	{
    		WarningNumberN();
    	}else
    	return length;
    	
    	}catch(NumberFormatException e)
    	{
    		WarningNumber();
    	}
    	}
		return 0;
	}
    private void loadDialogRectangle(Point pc) 
    {
    	Dialog<ButtonType>dialog=new Dialog<>();
    	dialog.setTitle("Rectangle");
    	dialog.setHeaderText("Let's make rectangle together! :D");
    	ButtonType done=new ButtonType("OK",ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(done,ButtonType.CANCEL);
    	GridPane grid=new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20,150,10,10));
    	TextField length=new TextField("100");
    	TextField width=new TextField("50");
    	grid.add(new Label("Length: "), 0, 0); 
    	grid.add(length, 1, 0);
    	grid.add(new Label("Width: "), 0, 1);
    	grid.add(width, 1, 1);
    	dialog.getDialogPane().setContent(grid); 
    	Platform.runLater(()->length.requestFocus());
    	Optional<ButtonType>result=dialog.showAndWait();
    	if(result.get()==done) { 
	    	try {
	    		 int len=Integer.parseInt(length.getText());
	    		 int wid=Integer.parseInt(width.getText());
	    		    if(len<=0 || wid<=0)
	    		        {
	    		        	WarningNumberN();
	    		        }else
	    		        { 
	    		        	Rectangle temp=new Rectangle(pc,len,wid,this.getStrokeColor(),this.getFillColor());temp.paint(gc);list.add(temp);
	    		        }
	    		         
	    	 }catch(NumberFormatException e)
	    		 {
	    		    	WarningNumber();
	    		 }
	    }
    }
    private void customDialogPoint(Point point,int i)
    {
    	
    	Dialog<ButtonType>dialog=new Dialog<>();
    	dialog.setTitle("Edit Point");
    	dialog.setHeaderText("Let's make some changes!");
    	ButtonType done=new ButtonType("Change",ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(done,ButtonType.CANCEL);
    	String sAx=Integer.toString(point.getX());
    	String sAy=Integer.toString(point.getY());
    	GridPane grid=new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20,150,10,10));
    	TextField Ax=new TextField(sAx);
    	TextField Ay=new TextField(sAy);
    	JFXColorPicker stroke=new JFXColorPicker();
    	stroke.setValue(point.getColor());
    	grid.add(new Label("X-axis: "), 0, 0);
    	grid.add(Ax, 1, 0);
    	grid.add(new Label("Y-axis: "), 0, 1);
    	grid.add(Ay, 1, 1);
    	grid.add(new Label("Stroke color: "), 0, 4);
    	grid.add(stroke, 1, 4);
    	dialog.getDialogPane().setContent(grid);
    	Platform.runLater(()->Ax.requestFocus());
    	Optional<ButtonType>result=dialog.showAndWait();
    	if(result.get()==done) {
	    	try {
	    		if(Integer.parseInt(Ay.getText())<0 || Integer.parseInt(Ax.getText())<0)
	    			WarningNumberN();
	    		else {
	    			Point poi=new Point(Integer.parseInt(Ax.getText()),(Integer.parseInt(Ay.getText())),stroke.getValue());
	    			list.remove(i); 
	    			list.add(i, poi);
	    			resetCanvas();
	    		}
	    	}catch(Exception e)
	    	{
	    		WarningNumber();
	    	}
    	}
    }
    private void customDialogLine(Line line,int i)
    {
    	 
    	Dialog<ButtonType>dialog=new Dialog<>();
    	dialog.setTitle("Edit Line");
    	dialog.setHeaderText("Let's make some changes!");
    	ButtonType done=new ButtonType("Change",ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(done,ButtonType.CANCEL);
    	String sAx=Integer.toString(line.getStart().getX());
    	String sAy=Integer.toString(line.getStart().getY());
    	String sBx=Integer.toString(line.getEnd().getX());
    	String sBy=Integer.toString(line.getEnd().getY());
    	GridPane grid=new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20,150,10,10));
    	TextField Ax=new TextField(sAx);
    	TextField Ay=new TextField(sAy);
    	TextField Bx=new TextField(sBx);
    	TextField By=new TextField(sBy);
    	JFXColorPicker stroke=new JFXColorPicker();
    	stroke.setValue(line.getColor());
    	grid.add(new Label("X-axis start:"), 0, 0);
    	grid.add(Ax, 1, 0);
    	grid.add(new Label("Y-axis start:"), 0, 1);
    	grid.add(Ay, 1, 1);
    	grid.add(new Label("X-axis end:"), 0, 2);
    	grid.add(Bx, 1, 2);
    	grid.add(new Label("Y-axis end:"), 0, 3);
    	grid.add(By, 1, 3);
    	grid.add(new Label("Stroke color: "), 0, 4);
    	grid.add(stroke, 1, 4);
    	dialog.getDialogPane().setContent(grid);
    	Platform.runLater(()->Ax.requestFocus());
    	Optional<ButtonType>result=dialog.showAndWait();
    	if(result.get()==done) {
	    	try {
	    		if(Integer.parseInt(Ax.getText())<0||Integer.parseInt(Ay.getText())<0||Integer.parseInt(Bx.getText())<0||Integer.parseInt(By.getText())<0)
	    			WarningNumberN();
	    		else {
	    			Line lin=new Line(new Point(Integer.parseInt(Ax.getText()),Integer.parseInt(Ay.getText())),new Point(Integer.parseInt(Bx.getText()),Integer.parseInt(By.getText())),stroke.getValue());
	    			list.remove(i);
	    			list.add(i, lin);
	    			resetCanvas();
	    		}
	    	}catch(Exception e)
	    	{
	    		WarningNumber();
	    	}
    	}
    	
    }
    private void customDialogTriangle(Triangle triangle,int i)
    {
    	
    	Dialog<ButtonType>dialog=new Dialog<>();
    	dialog.setTitle("Edit Triangle");
    	dialog.setHeaderText("Let's make some changes!");
    	ButtonType done=new ButtonType("Change",ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(done,ButtonType.CANCEL);
    	String sAx=Integer.toString(triangle.getVertexA().getX());
    	String sAy=Integer.toString(triangle.getVertexA().getY());
    	String sBx=Integer.toString(triangle.getVertexB().getX());
    	String sBy=Integer.toString(triangle.getVertexB().getY());
    	String sCx=Integer.toString(triangle.getVertexC().getX());
    	String sCy=Integer.toString(triangle.getVertexC().getY());
    	GridPane grid=new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20,150,10,10));
    	TextField Ax=new TextField(sAx);
    	TextField Ay=new TextField(sAy);
    	TextField Bx=new TextField(sBx);
    	TextField By=new TextField(sBy);
    	TextField Cx=new TextField(sCx);
    	TextField Cy=new TextField(sCy); 
    	JFXColorPicker stroke=new JFXColorPicker();
    	stroke.setValue(triangle.getColor());
    	JFXColorPicker fill=new JFXColorPicker();
    	fill.setValue(triangle.getFillColor());
    	grid.add(new Label("X-axis A corner:"), 0, 0);
    	grid.add(Ax, 1, 0);
    	grid.add(new Label("Y-axis A corner:"), 0, 1);
    	grid.add(Ay, 1, 1);
    	grid.add(new Label("X-axis B corner:"), 0, 2);
    	grid.add(Bx, 1, 2);
    	grid.add(new Label("Y-axis B corner:"), 0, 3);
    	grid.add(By, 1, 3);
    	grid.add(new Label("X-axis C corner:"), 0, 4);
    	grid.add(Cx, 1, 4);
    	grid.add(new Label("Y-axis C corner:"), 0, 5);
    	grid.add(Cy, 1, 5);
    	grid.add(new Label("Stroke color: "), 0, 6);
    	grid.add(stroke, 1, 6);
    	grid.add(new Label("Fill color: "), 0, 7);
    	grid.add(fill, 1, 7);
    	dialog.getDialogPane().setContent(grid);
    	Platform.runLater(()->Ax.requestFocus());
    	Optional<ButtonType>result=dialog.showAndWait();
    	if(result.get()==done) {
	    	try {
	    		if(Integer.parseInt(Ax.getText())<0||Integer.parseInt(Ay.getText())<0||Integer.parseInt(Bx.getText())<0||Integer.parseInt(By.getText())<0||Integer.parseInt(Cx.getText())<0||Integer.parseInt(Cy.getText())<0)
	    			WarningNumberN();
	    		else {
	    			Triangle tri=new Triangle(new Point(Integer.parseInt(Ax.getText()),Integer.parseInt(Ay.getText())),new Point(Integer.parseInt(Bx.getText()),Integer.parseInt(By.getText())),new Point(Integer.parseInt(Cx.getText()),Integer.parseInt(Cy.getText())),stroke.getValue(),fill.getValue(),true);
	    			list.remove(i);
	    			list.add(i, tri);
	    			resetCanvas();
	    		}
	    	}catch(Exception e)
	    	{
	    		WarningNumber();
	    	}
    	}
    }
    private void customDialogSquare(Square square,int i)
    {
    	Dialog<ButtonType>dialog=new Dialog<>();
    	dialog.setTitle("Edit");
    	dialog.setHeaderText("Let's make some changes!");
    	ButtonType done=new ButtonType("Change",ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(done,ButtonType.CANCEL);
    	String x=Integer.toString(square.getCorner().getX());
    	String y=Integer.toString(square.getCorner().getY());
    	String len=Integer.toString(square.getLength());
    	GridPane grid=new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20,150,10,10));
    	TextField X=new TextField(x);
    	TextField Y=new TextField(y);
    	TextField length=new TextField(len);
    	JFXColorPicker stroke=new JFXColorPicker();
    	stroke.setValue(getStrokeColor());
    	JFXColorPicker fill=new JFXColorPicker();
    	fill.setValue(getFillColor());
    	grid.add(new Label("X-axis corner:"), 0, 0);
    	grid.add(X, 1, 0);
    	grid.add(new Label("Y-axis corner:"), 0, 1);
    	grid.add(Y, 1, 1);
    	grid.add(new Label("Lenght:"), 0, 2);
    	grid.add(length, 1, 2);
    	grid.add(new Label("Stroke color: "), 0, 3);
    	grid.add(stroke, 1, 3);
    	grid.add(new Label("Fill color: "), 0, 4);
    	grid.add(fill, 1, 4);
    	dialog.getDialogPane().setContent(grid);
    	Platform.runLater(()->X.requestFocus());
    	Optional<ButtonType>result=dialog.showAndWait();
    	if(result.get()==done) {
	    	try {
	    		if(Integer.parseInt(X.getText())<0||Integer.parseInt(Y.getText())<0||Integer.parseInt(length.getText())<=0)
	    			WarningNumberN();
	    		else {
	    			Square rec=new Square(new Point(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText())),Integer.parseInt(length.getText()),stroke.getValue(),fill.getValue()); 
	    			list.remove(i);
	    			list.add(i, rec);
	    			resetCanvas();
	    		}
	    	}catch(Exception e)
	    	{
	    		WarningNumber();
	    	}
    	}
    }
    private void customDialogRectangle(Rectangle rectangle,int i)
    {
    	Dialog<ButtonType>dialog=new Dialog<>();
    	dialog.setTitle("Edit Rectangle");
    	dialog.setHeaderText("Let's make some changes!");
    	ButtonType done=new ButtonType("Change",ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(done,ButtonType.CANCEL);
    	String x=Integer.toString(rectangle.getCorner().getX());
    	String y=Integer.toString(rectangle.getCorner().getY());
    	String len=Integer.toString(rectangle.getLength());
    	String wid=Integer.toString(rectangle.getWidth());
    	GridPane grid=new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20,150,10,10));
    	TextField X=new TextField(x);
    	TextField Y=new TextField(y); 
    	TextField length=new TextField(len);
    	TextField width=new TextField(wid);
    	JFXColorPicker stroke=new JFXColorPicker();
    	stroke.setValue(getStrokeColor());
    	JFXColorPicker fill=new JFXColorPicker();
    	fill.setValue(getFillColor());
    	grid.add(new Label("X-axis center:"), 0, 0);
    	grid.add(X, 1, 0);
    	grid.add(new Label("Y-axis center:"), 0, 1);
    	grid.add(Y, 1, 1);
    	grid.add(new Label("Length: "), 0, 2);
    	grid.add(length, 1, 2);
    	grid.add(new Label("Width: "), 0, 3);
    	grid.add(width, 1, 3);
    	grid.add(new Label("Stroke color: "), 0, 4);
    	grid.add(stroke, 1, 4);
    	grid.add(new Label("Fill color: "), 0, 5);
    	grid.add(fill, 1, 5);
    	dialog.getDialogPane().setContent(grid);
    	Platform.runLater(()->X.requestFocus());
    	Optional<ButtonType>result=dialog.showAndWait();
    	if(result.get()==done) {
	    	try {
	    		if(Integer.parseInt(X.getText())<0||Integer.parseInt(Y.getText())<0||Integer.parseInt(length.getText())<=0||Integer.parseInt(width.getText())<=0)
	    			WarningNumberN();
	    		else {
	    			Rectangle rec=new Rectangle(new Point(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText())),Integer.parseInt(length.getText()),Integer.parseInt(width.getText()),stroke.getValue(),fill.getValue()); 
	    			list.remove(i);
	    			list.add(i, rec);
	    			resetCanvas();
	    		}
	    	}catch(Exception e)
	    	{
	    		WarningNumber();
	    	}
    	}
    }
    private void customDialogCircle(Circle circle,int i)
    {
    	
    	Dialog<ButtonType>dialog=new Dialog<>();
    	dialog.setTitle("Edit");
    	dialog.setHeaderText("Let's make some changes!");
    	ButtonType done=new ButtonType("Change",ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(done,ButtonType.CANCEL);
    	String x=Integer.toString(circle.getCenter().getX());
    	String y=Integer.toString(circle.getCenter().getY());
    	String rad=Integer.toString(circle.getRadius());
    	GridPane grid=new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20,150,10,10));
    	TextField X=new TextField(x);
    	TextField Y=new TextField(y);
    	TextField length=new TextField(rad);
    	JFXColorPicker stroke=new JFXColorPicker();
    	stroke.setValue(circle.getColor());
    	JFXColorPicker fill=new JFXColorPicker();
    	fill.setValue(circle.getFillColor());
    	grid.add(new Label("X-axis center:"), 0, 0); 
    	grid.add(X, 1, 0);
    	grid.add(new Label("Y-axis center:"), 0, 1);
    	grid.add(Y, 1, 1);
    	grid.add(new Label("Radius:"), 0, 2);
    	grid.add(length, 1, 2);
    	grid.add(new Label("Stroke color: "), 0, 3);
    	grid.add(stroke, 1, 3);
    	grid.add(new Label("Fill color: "), 0, 4);
    	grid.add(fill, 1, 4);
    	dialog.getDialogPane().setContent(grid);
    	Platform.runLater(()->X.requestFocus());
    	Optional<ButtonType>result=dialog.showAndWait();
		if(result.get()==done) {
			try {
	    		if(Integer.parseInt(X.getText())<0||Integer.parseInt(Y.getText())<0||Integer.parseInt(length.getText())<=0)
	    			WarningNumberN();
	    		else {
	    			Circle cir=new Circle(new Point(Integer.parseInt(X.getText()),Integer.parseInt(Y.getText())),Integer.parseInt(length.getText()),stroke.getValue(),fill.getValue()); 
	    			list.remove(i);
	    			list.add(i, cir);
	    			resetCanvas();
	    		}
	    	}catch(Exception e)
	    	{
	    		WarningNumber();
	    	}
		}
    }

}
