package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle extends Paintable {

	private Point vertexC;
	private Point vertexB;
	private Point vertexA;
	public Triangle(Point vertexC,Point vertexB, Point vertexA, Color color,Color fillColor,boolean IsSelected)
	{
		this(vertexC,vertexB,vertexA);
		setColor(color);setFillColor(fillColor);setSelected(IsSelected);
	}
	public Triangle(Point vertexC,Point vertexB, Point vertexA, Color color,Color fillColor)
	{
		this(vertexC,vertexB,vertexA);
		setColor(color);setFillColor(fillColor);
	}
	public Triangle(Point vertexC,Point vertexB, Point vertexA, Color color)
	{
		this(vertexC,vertexB,vertexA);
		setColor(color);
	}
	public Triangle(Point vertexC,Point vertexB, Point vertexA) {
		this.vertexC = vertexC;
		this.vertexB = vertexB;
		this.vertexA = vertexA;
	}
	public Triangle()
	{
		
	}
	public Point getVertexB() {
		return vertexB;
	}
	public void setVertexB(Point vertexB) {
		this.vertexB = vertexB;
	}
	@Override
	public void moveTo(int x, int y) {
		int rx = x - vertexA.getX();
		int ry = y - vertexA.getY();
		vertexA.moveTo(x,y);
		vertexB.moveFor(rx, ry);
		vertexC.moveFor(rx, ry);
	}
	public Point getVertexC() {
		return vertexC;
	}
	public void setVertexC(Point vertexC) {
		this.vertexC = vertexC;
	}
	public Point getVertexA() {
		return vertexA;
	}
	public void setVertexA(Point vertexA) {
		this.vertexA = vertexA;
	}
	@Override
	public void moveFor(int x, int y) {
		vertexA.moveFor(x,y);
		vertexB.moveFor(x,y);
		vertexC.moveFor(x, y);
	}
	@Override
	public double surface() {
		return Math.abs(vertexA.getX()*(vertexB.getY()-vertexC.getY()+vertexB.getX()*(vertexC.getY()-vertexC.getY())+vertexC.getY()*(vertexA.getY()-vertexB.getY())))/2;
	}
	@Override
	public double perimeter() {
		return (vertexA.distance(vertexB)+vertexB.distance(vertexC)+vertexC.distance(vertexA));
	}
	@Override
	public void fill(GraphicsContext gc) {
		gc.setFill(getFillColor());
		double[]xniz=new double[] {vertexA.getX(),vertexB.getX(),vertexC.getX()};
		double[]yniz=new double[] {vertexA.getY(),vertexB.getY(),vertexC.getY()};
		gc.fillPolygon(xniz, yniz, 3);
	}
	@Override
	public void paint(GraphicsContext gc) {
		fill(gc);
		gc.setStroke(getColor());
		double[]xniz=new double[] {vertexA.getX(),vertexB.getX(),vertexC.getX()};
		double[]yniz=new double[] {vertexA.getY(),vertexB.getY(),vertexC.getY()};
		gc.strokePolygon(xniz, yniz, 3);
		/*new Line(vertexA,vertexB,getColor()).paint(gc);
		new Line(vertexB,vertexC,getColor()).paint(gc);
		new Line(vertexC,vertexA,getColor()).paint(gc);*/
	}
	@Override
	public void select(GraphicsContext gc) {
		gc.setStroke(Color.BLUE);
		new Line(vertexA,vertexB).select(gc);
		new Line(vertexB,vertexC).select(gc);
		new Line(vertexC,vertexA).select(gc);
	}
	@Override
	public boolean contains(Point pc) { 
		return PointInTriangle(pc,vertexA,vertexB,vertexC);
	}
	float sign (Point p1, Point p2, Point p3)
	{
	    return (p1.getX() - p3.getX()) * (p2.getY() - p3.getY()) - (p2.getX() - p3.getX()) * (p1.getY() - p3.getY());
	}

	boolean PointInTriangle (Point pt, Point v1, Point v2, Point v3)
	{
	    boolean b1, b2, b3;

	    b1 = sign(pt, v1, v2) < 0.0f;
	    b2 = sign(pt, v2, v3) < 0.0f;
	    b3 = sign(pt, v3, v1) < 0.0f;
	    
	    return ((b1 == b2) && (b2 == b3));
	}
}
