package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Point extends Shapes {
	
	private int x;
	private int y;
	
	public Point(int x,int y,Color color)
	{
		this(x,y);
		setColor(color);
	}
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Point() {
		
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void moveTo(int x,int y)
	{
		this.x=x;
		setY(y);
	}
	public void moveFor(int x,int y)
	{
		this.x=this.x+x;
		setY(getY()+y);
	}
	public double distance(Point p)
	{
		return Math.sqrt((this.getX()-p.getX())*(this.getX()-p.getX())+(this.getY()-p.getY())*(this.getY()-p.getY()));
	}
	public String toString()
	{
		return "(x="+this.getX()+" y="+this.getY()+")";
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof Point)
		{
			Point temp=(Point)obj;
			if(this.x==temp.getX()&&
					this.y==temp.getY())
				return true;
			else {
				return false;
			}
		}
		return false;
	}
	public int compareTo(Object obj)
	{
		if(obj instanceof Point)
		{
			Point temp=(Point)obj;
			return (int)(this.distance(new Point(0,0))-temp.distance(new Point(0,0)));
		}else
			return 0;
	}
	public void paint(GraphicsContext gc)
	{
		gc.setFill(getColor());
		gc.fillOval(this.x-2, this.y-2, 4, 4);
	}
	public void select(GraphicsContext gc) {
		gc.setStroke(Color.BLUE);
		gc.strokeRect(this.getX()-2, this.getY()-2, 4, 4); 
	}
	@Override
	public boolean contains(Point pc) {
		if(this.distance(pc)<=3)
		{
			return true;
		}else
			return false;
	}
	

}
