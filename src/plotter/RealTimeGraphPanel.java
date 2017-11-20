package plotter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class RealTimeGraphPanel extends GraphPanel {

	private static final long serialVersionUID = -4180320169882516642L;

	private int lastPlot = 0;
	
	public RealTimeGraphPanel(){}
		
	@Override
	protected void drawPlots(Graphics g){
		//Loop through all plots to draw all lines in the array
		for (int i = 0; i < plots.size(); i++) {
			RealTimePlot plot = (RealTimePlot)plots.get(i);
			if(plot==null)
				continue;
			if (plot.color == null)
				plot.color = lineColor;
			g.setColor(plot.color);
			List<Double> yValues = plot.yPlot;
			List<Double> xValues = plot.xPlot;
			int lx = xVal(xValues.get(0) + plot.xOffset), ly = yVal(yValues.get(0) + plot.yOffset), xi, yi;
			double x=0, y=0;
			int pS = plot.getSize();
			for (int p = 1; p < pS; p++) {
				try{
					x = xValues.get(p) + plot.xOffset;
					y = yValues.get(p) + plot.yOffset;
				}catch(NullPointerException | java.lang.IndexOutOfBoundsException e){
					continue;
				}
				if(x<minX || x>maxX)
					continue;
				xi = xVal(x);
				yi = yVal(y);
				if (xi == lx && yi == ly)
					continue;
				if (!((lx > dim.width && xi > dim.width) || (lx < 0 && xi < 0) || (ly > dim.height && yi > dim.height)
						|| (ly < 0 && yi < 0))) {
					g.drawLine(lx, ly, xi, yi);
				}
				lx = xi;
				ly = yi;
			}
		}
	}
	
	public int setPlot(int pIndex, double xOffset, double yOffset, double[] x, double[] y, Color c, boolean resize) {
		if (y == null || y.length < 2)
			return -1;
		if (x == null || x.length != y.length)
			return -1;
		if (c == null)
			c = lineColor;
		RealTimePlot p = new RealTimePlot(x, y, xOffset, yOffset, c);
		if (pIndex < 0 || pIndex >= plots.size()) {
			pIndex = plots.size();
			plots.add(p);
		} else {
			plots.set(pIndex, p);
		}
		double[] xVals = x;
		double[] yVals = y;
		double tmaxX = 0, tminX = 0, tmaxY = 0, tminY = 0;
		if (resize) {
			for (int i = 0; i < y.length; i++) {
				if (xVals[i] > tmaxX)
					tmaxX = xVals[i];
				if (xVals[i] < tminX)
					tminX = xVals[i];
				if (yVals[i] > tmaxY)
					tmaxY = yVals[i];
				if (yVals[i] < tminY)
					tminY = yVals[i];
			}
			this.maxX = tmaxX + xOffset;
			this.minX = tminX + xOffset;
			this.maxY = tmaxY + yOffset;
			this.minY = tminY + yOffset;
			this.dX = (maxX - minX) / 10d;
			this.dY = (maxY - minY) / 10d;
		}
		lastPlot = pIndex;
		refresh = true;
		return lastPlot;
	}
	
	public void addPoint(int plotIndex, double x, double y, boolean autoscroll){
		((RealTimePlot)(plots.get(plotIndex))).addPoint(x, y);
		if(x>minX && x<maxX){
			double dx = x-(maxX-dX);
			if(autoscroll && dx>0){
				minX += dx;
				maxX += dx;
			}
			refresh = true;
		}
	}
	
	public void addPoints(double x, double[] y, boolean autoscroll){
		for(int i=0; i<plots.size(); i++)
			((RealTimePlot)(plots.get(i))).addPoint(x, y[i]);
		if(x>minX && x<maxX){
			double dx = x-(maxX-dX);
			if(autoscroll && dx>0){
				minX += dx;
				maxX += dx;
			}
			refresh = true;
		}
	}
	
	public void gotoMaxX(){
		double max = 0;
		double winSize = maxX-minX;
		for(int i=0; i<plots.size(); i++){
			double t = ((RealTimePlot)(plots.get(i))).getMaxX();
			if(t>max) max = t;
		}
		maxX = max + dX;
		minX = max-winSize;
		refresh = true;
	}
}
