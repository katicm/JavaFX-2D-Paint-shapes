package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Shapes {
	private Point start;
	private Point end;
	public Line(Point start,Point end,Color color)
	{
		this(start,end);
		setColor(color);
	}
	public Line(Point start,Point end)
	{
		this.start=start;this.end=end;
	}
	public Point getStart() {
		return start;
	}
	public void setStart(Point start) {
		this.start = start;
	}
	public Point getEnd() {
		return end;
	}
	public void setEnd(Point end) {
		this.end = end;
	}
	public void moveTo(int x,int y)
	{
		int x1=x-start.getX();
		int y1=y-start.getY();
		start.moveTo(x, y);
		end.moveFor(x1,y1);
	}
	public void moveFor(int x,int y)
	{
		this.start.moveFor(x,y);
		this.end.moveFor(x, y);
	}
	public double distance()
	{
		return start.distance(end);
	}
	public Point middle()
	{
		return new Point((start.getX()+end.getX())/2,(start.getY()+end.getY())/2);
	}
	public String toString()
	{
		return "start("+start.toString()+") end("+end.toString()+")";
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof Line)
		{
			Line temp=(Line)obj;
			if(this.start.equals(temp.start)&&
			this.end.equals(temp.end))
				return true;
			else
				return false;
		}
		return false;
	}
	public int compareTo(Object obj) {
		if(obj instanceof Line)
		{
			Line temp=(Line)obj;
			return (int)(this.distance()-temp.distance());
		}else
			return 0;
	}
	public void paint(GraphicsContext gc) {
		gc.setStroke(getColor());
		gc.strokeLine(this.start.getX(), this.start.getY(), this.end.getX(), this.end.getY());
	}
	public void select(GraphicsContext gc) {
		this.getStart().select(gc); 
		this.getEnd().select(gc); 
		this.middle().select(gc);
	}
	@Override
	public boolean contains(Point pc) {
		if(this.start.distance(pc)+this.end.distance(pc)<=0.05+this.start.distance(end))
		{
			return true;
		}else
			return false;
	}
	

}

