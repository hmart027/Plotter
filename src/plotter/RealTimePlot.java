package plotter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class RealTimePlot extends Plot{
	
	protected List<Double> xPlot;
	protected List<Double> yPlot;
	
	public RealTimePlot(double x, double y){
		this(x, y, 0, 0, Color.BLACK);
	}
	
	public RealTimePlot(double x, double y, Color c){
		this(x, y, 0, 0, c);
	}
	
	public RealTimePlot(double x, double y, double xO, double yO, Color c){
		this.xPlot = new ArrayList<>();
		this.xPlot.add(x);
		this.yPlot = new ArrayList<>();
		this.yPlot.add(y);
		this.xOffset = xO;
		this.yOffset = yO;
		this.color = c;

		minX = x;
		maxX = x;
		minY = y;
		maxY = y;
	}
	
	public RealTimePlot(double[] x, double[] y, double xO, double yO, Color c){
		this.xPlot = toDoubleArray(x);
		this.yPlot = toDoubleArray(y);
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
	
	private ArrayList<Double> toDoubleArray(double[] x){
		ArrayList<Double> out = new ArrayList<>();
		for(double d: x){
			out.add(d);
		}
		return out;
	}
	
	public RealTimePlot(double[] x, double[] y, Color c){
		this(x,y,0,0,c);
	}
	
	public void addPoint(double x, double y){
		lastX = x;
		lastY = y;
		xPlot.add(x);
		yPlot.add(y);
		if(xPlot.size()>1){
			if(x<minX) minX = x;
			if(x>maxX) maxX = x;
			if(y<minY) minY = y;
			if(y>maxY) maxY = y;
		}
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

	public void clear(){
		this.xPlot = new ArrayList<>();
		this.yPlot = new ArrayList<>();
		this.xOffset = 0;
		this.yOffset = 0;
		this.color = Color.BLACK;
		minX = 0;
		maxX = 0;
		minY = 0;
		maxY = 0;
	}
}
