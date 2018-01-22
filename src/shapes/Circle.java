package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends Paintable {
	private Point center;
	private int radius;
	public Circle(Point center,int radius,Color color,Color fillColor)
	{
		this(center,radius);
		setColor(color);setFillColor(fillColor);
	}
	public Circle(Point center,int radius,Color color)
	{
		this(center,radius);
		setColor(color);
	}
	public Circle(Point center, int radius) {
		this.center = center;
		this.radius = radius;
	}
	public Circle() {
		
	}
	public Point getCenter() {
		return center;
	}
	public void setCenter(Point center) {
		this.center = center;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public double surface()
	{
		return radius*radius*Math.PI;
	}
	public double perimeter()
	{
		return 2*radius*Math.PI;
	}
	public void moveTo(int x, int y) {
		this.center.moveTo(x,y);
	}
	public void moveFor(int x, int y) {
		this.center.moveFor(x, y);
	}
	public String toString()
	{
		return "Center("+center.toString()+") Radius="+radius;
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof Circle)
		{
			Circle temp=(Circle)obj;
			if(this.center.equals(temp.getCenter())&&
					this.radius==temp.getRadius())
				return true;
			else
				return false;
		}
		return false;
	}
	public int compareTo(Object obj) {
		if(obj instanceof Circle)
		{
			Circle temp=(Circle)obj;
			return (int)(this.surface()-temp.surface());
		}else
			return 0;
	}
	@Override
	public void fill(GraphicsContext gc) {
		gc.setFill(getFillColor());
		gc.fillOval(this.getCenter().getX()-this.getRadius(), this.getCenter().getY()-this.getRadius(), this.getRadius()*2, this.getRadius()*2);
	}
	@Override
	public void paint(GraphicsContext gc) {
		fill(gc);
		gc.setStroke(getColor());
		gc.strokeOval(this.getCenter().getX()-this.getRadius(), this.getCenter().getY()-this.getRadius(), this.getRadius()*2, this.getRadius()*2);
	}
	@Override
	public void select(GraphicsContext gc) {
		gc.setStroke(Color.BLUE);
		new Line(new Point(this.getCenter().getX(),this.getCenter().getY()-this.getRadius()),new Point(this.getCenter().getX(),this.getCenter().getY()+this.getRadius())).select(gc);
		new Line(new Point(this.getCenter().getX()-this.getRadius(),this.getCenter().getY()),new Point(this.getCenter().getX()+this.getRadius(),this.getCenter().getY())).select(gc);
	}
	@Override
	public boolean contains(Point pc) {
		if(this.getCenter().distance(pc)<=this.getRadius())
		{
			return true;
		}else
			return false;
	}
	

}
