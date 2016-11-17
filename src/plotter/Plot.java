package plotter;

import java.awt.Color;

public class Plot {
	protected double[] xPlot;
	protected double[] yPlot;
	protected double   xOffset;
	protected double   yOffset;
	protected Color    color;
	
	public Plot(double[] x, double[] y, double xO, double yO, Color c){
		this.xPlot = x;
		this.yPlot = y;
		this.xOffset = xO;
		this.yOffset = yO;
		this.color = c;
	}
	
	public Plot(double[] x, double[] y, Color c){
		this(x,y,0,0,c);
	}
}
