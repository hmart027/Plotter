package plotter;

import java.awt.Color;

public class Plot {
	protected double[] xPlot;
	protected double[] yPlot;
	protected double   minX, maxX, minY, maxY, lastX, lastY;
	protected double   xOffset;
	protected double   yOffset;
	protected Color    color;
	
	public Plot(double[] x, double[] y, double xO, double yO, Color c){
		this.xPlot = x;
		this.yPlot = y;
		this.xOffset = xO;
		this.yOffset = yO;
		this.color = c;

		minX = x[0];
		maxX = x[0];
		minY = y[0];
		maxY = y[0];
		for(int i=0; i<x.length; i++){
			double xV = x[i];
			double yV = y[i];
			if(xV<minX) minX = xV;
			if(xV>maxX) maxX = xV;
			if(yV<minY) minY = yV;
			if(yV>maxY) maxY = yV;
		}
	}
	
	public Plot(double[] x, double[] y, Color c){
		this(x,y,0,0,c);
	}
	
	protected Plot(){}
		
	public int getSize(){
		return yPlot.length;
	}
	
	public double getMaxX(){
		return maxX;
	}
	
	public double getMinX(){
		return minX;
	}
	
	public double getMaxY(){
		return maxY;
	}
	
	public double getMinY(){
		return minY;
	}
	
	public double getLastX(){
		return lastX;
	}
	
	public double getLastY(){
		return lastY;
	}
	
}
