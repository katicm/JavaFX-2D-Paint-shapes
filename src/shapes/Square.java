package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Square extends Paintable {
	protected int length;
	protected Point corner;
	
	public Square(Point corner,int length,Color color,Color fillColor)
	{
		this(corner,length);
		setColor(color);setFillColor(fillColor);
	}
	public Square(Point corner,int length,Color color)
	{
		this(corner,length);
		setColor(color);
	}
	public Square(Point corner,int length) {
		this.length = length;
		this.corner = corner;
	}
	public Square()
	{
		
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length; 
	}
	public Point getCorner() {
		return corner;
	}
	public void setCorner(Point corner) {
		this.corner = corner;
	}
	public void moveFor(int x,int y)
	{
		corner.moveFor(x, y);
	}
	public void moveTo(int x,int y)
	{
		corner.moveTo(x, y);
	}
	public String toString()
	{
		return "Upper Left Corner "+this.corner.toString()+" Lenght="+this.length+" Line Color ("+this.getColor()+") Fill Color ("+this.getFillColor()+")";
	}
	public double surface()
	{
		return length*length;
	}
	public double perimeter()
	{
		return 4*length;
	}
	public Line diagonal()
	{
		return new Line(corner,new Point(corner.getX()+getLength(),corner.getY()+getLength()));
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof Square)
		{
			Square temp=(Square)obj;
			if(this.corner.equals(temp.getCorner())&&
					this.length==temp.getLength())
				return true;
			else
				return false;
		}
		return false;
	}
	public int compareTo(Object obj) {
		if(obj instanceof Square)
		{
			Square temp=(Square)obj;
			return (int)(this.surface()-temp.surface());
		}else
			return 0;
	}
	@Override
	public void fill(GraphicsContext gc) {
		gc.setFill(getFillColor());
		gc.fillRect(this.getCorner().getX(), this.getCorner().getY(), this.getLength(), this.getLength());
	}
	@Override
	public void paint(GraphicsContext gc) {
		fill(gc);
		gc.setStroke(getColor());
		gc.strokeRect(this.getCorner().getX(),this.getCorner().getY(), this.getLength(), this.getLength());
	}
	@Override
	public void select(GraphicsContext gc) {
		gc.setStroke(Color.BLUE);
		new Line(new Point(this.corner.getX(),this.getCorner().getY()),new Point(this.getCorner().getX()+this.getLength(),this.getCorner().getY())).select(gc);
		new Line(new Point(this.corner.getX()+this.getLength(),this.getCorner().getY()),new Point(this.getCorner().getX()+this.getLength(),this.getCorner().getY()+this.getLength())).select(gc);
		new Line(new Point(this.corner.getX()+this.getLength(),this.getCorner().getY()+this.getLength()),new Point(this.corner.getX(),this.getCorner().getY()+this.getLength())).select(gc);
		new Line(new Point(this.corner.getX(),this.getCorner().getY()+this.getLength()),new Point(this.corner.getX(),this.getCorner().getY())).select(gc);
	}
	@Override
	public boolean contains(Point pc) {
		if(this.corner.getX()<=pc.getX()&&this.corner.getX()+this.getLength()>=pc.getX()&&this.corner.getY()<=pc.getY()&&this.corner.getY()+this.getLength()>=pc.getY())
		{
			return true;
		}
		return false;
	}
	
}

