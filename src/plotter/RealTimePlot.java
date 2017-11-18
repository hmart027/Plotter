package plotter;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.lang3.ArrayUtils;

public class RealTimePlot extends Plot{
	
	protected LinkedList<Double> xPlot;
	protected LinkedList<Double> yPlot;
	protected double   minX, maxX, minY, maxY, lastX, lastY;
	protected double   xOffset;
	protected double   yOffset;
	protected Color    color;
	
	public RealTimePlot(double[] x, double[] y, double xO, double yO, Color c){
		this.xPlot = new LinkedList<>(Arrays.asList(ArrayUtils.toObject(x)));
		this.yPlot = new LinkedList<>(Arrays.asList(ArrayUtils.toObject(y)));
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
	
	public RealTimePlot(double[] x, double[] y, Color c){
		this(x,y,0,0,c);
	}
	
	public void addPoint(double x, double y){
		xPlot.add(x);
		yPlot.add(y);
		if(x<minX) minX = x;
		if(x>maxX) maxX = x;
		if(y<minY) minY = y;
		if(y>maxY) maxY = y;
		lastX = x;
		lastY = y;
	}
	
	public int getSize(){
		return yPlot.size();
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
