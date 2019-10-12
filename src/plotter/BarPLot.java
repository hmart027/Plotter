package plotter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class BarPLot extends GraphPanel {

	private static final long serialVersionUID = 3533034125780095646L;
	
	protected List<BarPlot> bars = new ArrayList<>();

	public BarPLot() {
		
	}
	
	public void init(int w, int h) {
		super.init(w, h);
	}
	
	//Method to paint
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		setD2P();
		setAxisCordinates();
		gHeight = dim.height;
		gWidth = gHeight;
		background(g);

		drawBars(g2);
		drawLines(g2);
		drawPlots(g);
		drawPoints(g);
		drawCursor(g);
		drawLabels(g2);
		drawSelection(g2);

	}
	
	protected void drawBars(Graphics2D g2){
		//Loop to draw all lines in the list
		int tY, bY, lX;
		for (int i = 0; i<bars.size(); i++) {
			 BarPlot b = bars.get(i);
			 for(int xI=0; xI<b.x.length; xI++) {
				 if (!((b.x[xI]-b.w/2)>maxX || ((b.x[xI]+b.w/2)<minX))){
				     g2.setColor(b.color);
				     tY = yVal(b.y[xI]);
				     bY = yVal(0);
				     lX = xVal(b.x[xI]-b.w/2);
					 g2.fillRect(lX,tY,xVal(b.w),bY-tY);
				 }
			 }
		 }
	}
	
	public int addBarPlot(double[] x, double[] y, double barW, Color c) {
		BarPlot b = new BarPlot();
		b.x = x;
		b.y = y; 
		b.w = barW;
		b.color = c;
		int index = bars.size();
		bars.add(b);
		return index;
	}
	
	public void setBarPlotVals(int pIndex, double[] y) {
		if(pIndex>bars.size())
			return;
		BarPlot b = bars.get(pIndex);
		b.y = y;
		refresh = true;
	}
	
	public class BarPlot{
		double y[], x[], w;
		Color color;
	}
		
}
