package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Square {
	private int width;
	
	public Rectangle(Point corner,int length,int width,Color color,Color fillColor)
	{
		this(corner,length,width);
		setColor(color);setFillColor(fillColor);
	}
	public Rectangle(Point corner,int length,int width,Color color)
	{
		this(corner,length,width);
		setColor(color);
	}
	public Rectangle(Point corner,int length,int width)
	{
		this.corner=corner;this.length=length;this.width=width;
	}
	public Rectangle()
	{
		
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public double surface()
	{
		return length*width;
	}
	public double perimeter()
	{
		return 2*length+2*width;
	}
	public String toString()
	{
		return "UpLeftCorner"+corner.toString()+" Lenght="+length+" Width="+width;
	}
	public Line diagonal()
	{
		return new Line(corner,new Point(corner.getX()+getLength(),corner.getY()+getLength()));
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof Rectangle)
		{
			Rectangle temp=(Rectangle)obj;
			if(this.corner.equals(temp.getCorner())&&
					this.length==temp.getLength()&&
					this.width==temp.getWidth())
				return true;
			else
				return false;
		}
		return false;
	}
	@Override
	public void fill(GraphicsContext gc) {
		gc.setFill(getFillColor());
		gc.fillRect(this.getCorner().getX(),this.getCorner().getY(), this.getLength(), this.getWidth());
	}
	@Override
	public void paint(GraphicsContext gc) {
		fill(gc);
		gc.setStroke(getColor());
		gc.strokeRect(this.getCorner().getX(),this.getCorner().getY(), this.getLength(), this.getWidth());
	}
	@Override
	public void select(GraphicsContext gc) {
		gc.setStroke(Color.BLUE);
		new Line(new Point(this.corner.getX(),this.getCorner().getY()),new Point(this.getCorner().getX()+this.getLength(),this.getCorner().getY())).select(gc);
		new Line(new Point(this.corner.getX()+this.getLength(),this.getCorner().getY()),new Point(this.getCorner().getX()+this.getLength(),this.getCorner().getY()+this.getWidth())).select(gc);
		new Line(new Point(this.corner.getX()+this.getLength(),this.getCorner().getY()+this.getWidth()),new Point(this.corner.getX(),this.getCorner().getY()+this.getWidth())).select(gc);
		new Line(new Point(this.corner.getX(),this.getCorner().getY()+this.getWidth()),new Point(this.corner.getX(),this.getCorner().getY())).select(gc);
	}
	@Override
	public boolean contains(Point pc) {
		if(this.corner.getX()<=pc.getX()&&this.corner.getX()+this.getLength()>=pc.getX()&&this.corner.getY()<=pc.getY()&&this.corner.getY()+this.getWidth()>=pc.getY())
		{
			return true;
		}
		return false;
	}
	

}
