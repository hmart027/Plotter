package plotter;

import java.awt.Color;
import java.io.Serializable;

public class Point implements Serializable{
	private static final long serialVersionUID = 1429271601402013852L;
	double x;
	double y;
	Color c = Color.BLUE;
	
	public Point(int x, int y){
		this(x, y, Color.BLUE);
	}
	
	public Point(double x, double y){
		this(x, y, Color.BLUE);
	}
	
	public Point(double x, double y, Color c){
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
}
