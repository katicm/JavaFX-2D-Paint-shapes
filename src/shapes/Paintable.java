package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Paintable extends Shapes {
	private Color fillColor=Color.WHITE;

	public abstract double surface();
	public abstract double perimeter();
	public abstract void fill(GraphicsContext gc);
	
	public Color getFillColor() {
		return fillColor;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	
	

}
