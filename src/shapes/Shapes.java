package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Shapes implements Movable {
	private Color color=Color.BLACK;
	private boolean isSelected=false;
	
	public Shapes(Color color)
	{
		this.color=color; 
	}
	public Shapes()
	{
		
	}
	public abstract void paint(GraphicsContext gc);
	
	public abstract void select(GraphicsContext gc);
	
	public abstract boolean contains(Point pc);
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}

