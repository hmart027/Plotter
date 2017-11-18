package plotter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

@SuppressWarnings({"serial","unused"})
public class GraphPanel extends JPanel  implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
		
	private Dimension dim = new Dimension();
	private Color bgColor = Color.BLACK;
	private Color axisColor = Color.RED;
	private Color gridColor = Color.GRAY;
	private Color lineColor = Color.BLUE;

	// Grid Variables
	private boolean grid = false;
	private boolean dashed = true;
	private boolean labels = true;
	private boolean drawAxis = true;
	private boolean drawVerticalAxis = true;
	private boolean drawHorizontalAxis = true;

	private boolean autoRefresh = false;
	private boolean autoXScroll = false;

	int gWidth;
	int gHeight;

	// Cursor Variables
	private boolean cursorInWindow 	   = false;
	private boolean usePermanentCursor = false;
	private boolean useCursor 		   = true;
	private boolean useVCursor 		   = false;
	private boolean useVCursorLabel    = false;
	private double 	vCursorRoundingVal = 1000;
	private boolean useHCursor 		   = true;
	private boolean useHCursorLabel    = true;
	private double 	hCursorRoundingVal = 1000;
	private double cX = 0;
	private double cY = 0;
	private Color cursorColor = Color.WHITE;
	private Labeler xCursorLabler = null;
	private Labeler yCursorLabler = null;

	// Signal/Window Variables
	private boolean matchSignal = false;
	private double maxX = 10;
	private double minX = -10;
	private double dX = 1;
	private double maxY = 10;
	private double minY = -10;
	private double dY = 1;
	private double d2pX = dim.width / (maxX - minX);
	private double d2pY = dim.height / (maxY - minY);
	private int originX = 0;
	private int originY = 0;
	private int axisX = 0;
	private int axisY = 0;
	private Labeler xAxisLabler = null;
	private Labeler yAxisLabler = null;
	private VerticalGridDrawer vGrid = null;

	// Control Keys
	boolean cntlrP, altP, lClick, rClick;

	// List of existing lines
	private final List<Line> lines = Collections.synchronizedList(new ArrayList<Line>());
	private int lineBufferSize = 22000;// 1024;

	// Plots
	private int lastPlot = 0;
	private ArrayList<Plot> plots = new ArrayList<>();

	// Points
	int pointRad = 3;
	boolean fillPoint = true;
	private final ArrayList<Point> points = new ArrayList<>();
	
	// Circle
	private final ArrayList<Point> circles = new ArrayList<>();
		

	public GraphPanel(){
		this(new Dimension(50, 50));
	};
	
	public GraphPanel(int width, int height){
		this(new Dimension(width, height));
	}
	
	public GraphPanel(Dimension dim) {
		this.init(dim);
		this.setFocusable(true);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
	}
	
	/**
	 * Initialize the pane to the given dimension
	 * @param width
	 * @param height
	 */
	protected void init(int width, int height){
		init(new Dimension(width, height));
	}
	
	/**
	 * Initialize the pane to the given dimension
	 * @param dim
	 */
	protected void init(Dimension dim){
		this.dim = dim;
		this.setPreferredSize(dim);
		gHeight = dim.height;
		gWidth = dim.width;
		setAxisCordinates();
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height){
		super.setBounds(x, y, width, height);
		this.init(width, height);
	}
		
		//Method to paint
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		setAxisCordinates();
		setD2P();
		gHeight=dim.height;
		gWidth =gHeight;
		background(g);
		    			
		//Loop to draw all lines in the list
		 for (int i = 0; i<lines.size(); i++) {
			 Line l = lines.get(i);
			 if (!((l.x1>maxX && l.x2>maxX) || (l.x1<minX && l.x2<minX) || 
					 (l.y1>maxY && l.y2>maxY) || (l.y1<minY && l.y2<minY))){
			     g2.setColor(l.color);
			     if(l.stroke!=null)
			    	 g2.setStroke(l.stroke);
				 g2.drawLine(xVal(l.x1),yVal(l.y1),xVal(l.x2),yVal(l.y2));
			 }
		 }
		 g2.setStroke(Line.BASIC);
		 
		//Loop through all plots to draw all lines in the array
		 for(int i=0;i<plots.size(); i++){
			 Plot plot = plots.get(i);
			 if(plot.color==null) plot.color = lineColor;
			 g.setColor(plot.color);
			 int lx=xVal(plot.xPlot[0]+plot.xOffset), ly=yVal(plot.yPlot[0]+plot.yOffset), x, y;
			 double[] yValues = plot.yPlot;
			 double[] xValues = plot.xPlot;
			 for(int p=1; p<plot.getSize(); p++){
				x=xVal(xValues[p]+plot.xOffset);
				y=yVal(yValues[p]+plot.yOffset);
				if(x==lx && y==ly)
					continue;
				if (!((lx > dim.width && x > dim.width) || (lx < 0 && x < 0) || (ly > dim.height && y > dim.height)
						|| (ly < 0 && y < 0))) {
					g.drawLine(lx, ly, x, y);
				}
				lx=x;
				ly=y;
			 }
		 }
		 
		 //Loop through all the points
		 if(fillPoint){
			 for(int i=0; i<points.size(); i++){
				 Point p = points.get(i);
				 g.setColor(p.c);
				 if (!(p.x>maxX || p.x<minX || p.y>maxY || p.y<minY))
					 g.fillOval(xVal(p.x)-pointRad/2, yVal(p.y)-pointRad/2, pointRad, pointRad);
			 }
		 }else{
			 for(int i=0; i<points.size(); i++){
				 Point p = points.get(i);
				 g.setColor(p.c);
				 if (!(p.x>maxX || p.x<minX || p.y>maxY || p.y<minY))
					 g.drawOval(xVal(p.x)-pointRad/2, yVal(p.y)-pointRad/2, pointRad, pointRad);
			 }
		 }
		 
		 //Draw the cursor
		 if(useCursor && (cursorInWindow || usePermanentCursor)){
			 g.setColor(cursorColor);
			 if(useVCursor){
				 g.drawLine(0, (int)cY, gWidth, (int)cY);
			 }
			 if(useHCursor){
				 g.drawLine((int)cX, 0, (int)cX, gHeight);
			 }
			 int hYOffset = 0;
			 if(useVCursorLabel) hYOffset = 12;
			 if(useHCursorLabel){
				 double xVal = Math.round(cX/d2pX*hCursorRoundingVal)/hCursorRoundingVal+minX;
				 String label = "X: "+xVal;
				 if(xCursorLabler!=null) label = xCursorLabler.getLabel(xVal);
				 g.drawString(label, (int)cX+2, (int)cY-hYOffset);
			 }
			 if(useVCursorLabel){
				 double yVal = Math.round(cY/d2pY*vCursorRoundingVal)/vCursorRoundingVal+minY;
				 String label = "Y: "+yVal;
				 if(yCursorLabler!=null) label = yCursorLabler.getLabel(yVal);
				 g.drawString(label, (int)cX+2, (int)cY);
			 }
		 }
		
	}
		
	/**
	 * Method to draw the background image.
	 * @param g
	 */
	private void background(Graphics g) {
		// Background
		g.setColor(bgColor);
		g.fillRect(0, 0, dim.width, dim.height);

		// Drawing center axis
		if (drawAxis)
			drawAxis(g);
		g.setColor(Color.blue);
	}
		
	/**
	 * Draws the marks in the axes
	 * @param g
	 */
	private void drawAxisMarcs(Graphics g) {
		g.setColor(gridColor);
		double current = 0, next = 0, exp = 0;
		int xL = (int) (dim.height * 0.02 / 2);
		if(drawHorizontalAxis){
			// Drawing delta X to +
			next = minX + axisX/d2pX + dX;
			current = 0;
			exp = Math.pow(10, Math.abs(Math.getExponent(dX)));
			for (int c = axisX; c < dim.width - 1; c++) {
				current = (c - originX) / d2pX;
				if (current >= next) {
					if (!(c >= (axisX - 1) && c <= (axisX + 1))) {
						if (grid)
							g.drawLine(c, 0, c, gHeight);
						else
							g.drawLine(c, axisY - xL, c, axisY + xL);
						if (labels) {
							String label = Math.round(current*exp)/exp+"";
							if(xAxisLabler!=null) label = xAxisLabler.getLabel(current);
							if(axisY<=gHeight/2)
								g.drawString(label, c - 10, axisY + xL + 12);
							else
								g.drawString(label, c - 10, axisY - xL - 5);
						}
					}
					next += dX;
				}
			}
			// Drawing delta X to -
			next = minX + axisX/d2pX - dX;		
			current = 0;
			for (int c = axisX; c > 0; c--) {
				current = (c - originX) / d2pX;
				if (current <= next) {
					if (!(c >= (axisX - 1) && c <= (axisX + 1))) {
						if (grid)
							g.drawLine(c, 0, c, gHeight);
						else
							g.drawLine(c, axisY - xL, c, axisY + xL);
						if (labels) {
							String label = Math.round(current*exp)/exp+"";
							if(xAxisLabler!=null) label = xAxisLabler.getLabel(current);
							if(axisY<=gHeight/2)
								g.drawString(label, c - 10, axisY + xL + 12);
							else
								g.drawString(label, c - 10, axisY - xL - 5);
						}
					}
					next -= dX;
				}
			}
		}
		if(drawVerticalAxis){
			// Drawing delta y to +
			next = maxY - axisY/d2pY + dY;
			current = 0;
			exp = Math.pow(10, Math.abs(Math.getExponent(dY)));
			for (int c = axisY; c > 0; c--) {
				current = (originY - c) / d2pY;
				if (current >= next) {
					if (!(c >= (axisY - 1) && c <= (axisY + 1))) {
						if (grid)
							g.drawLine(0, c, dim.width, c);
						else
							g.drawLine(axisX - xL, c, axisX + xL, c);
						if (labels) {
							String label = Math.round(current*exp)/exp+"";
							if(yAxisLabler!=null) label = yAxisLabler.getLabel(current);
							int l = label.length()*5;
							if(axisX<gWidth/2)
								g.drawString(label, axisX + xL + 10, c + 5);
							else
								g.drawString(label, axisX - xL - 10 - l, c + 5);
						}
					}
					next += dY;
				}
			}
			// Drawing delta y to -
			next = maxY - axisY/d2pY - dY;
			current = 0;
			for (int c = axisY; c < dim.height - 1; c++) {
				current = (originY - c) / d2pY;
				if (current <= next) {
					if (!(c >= (axisY - 1) && c <= (axisY + 1))) {
						if (grid)
							g.drawLine(0, c, dim.width, c);
						else
							g.drawLine(axisX - xL, c, axisX + xL, c);
						if (labels) {
							String label = Math.round(current*exp)/exp+"";
							if(yAxisLabler!=null) label = yAxisLabler.getLabel(current);
							int l = label.length()*5;
							if(axisX<gWidth/2)
								g.drawString(label, axisX + xL + 10, c + 5);
							else
								g.drawString(label, axisX - xL - 10 - l, c + 5);
						}
					}
					next -= dY;
				}
			}
		}
		
		if(vGrid!=null){
			double p2dX = 1.0/d2pX;
			for(int i=0; i<dim.width; i++){
				vGrid.drawGrid(g, i, i*p2dX+minX);
			}
		}
	}
		
	private void drawAxis(Graphics g) {
		setAxisCordinates();
		g.setColor(axisColor);
		// Draw X-axis
		g.drawLine(0, axisY - 1, dim.width, axisY - 1);
		g.drawLine(0, axisY, dim.width, axisY);
		g.drawLine(0, axisY + 1, dim.width, axisY + 1);
		// Draw Y-axis
		g.drawLine(axisX - 1, 0, axisX - 1, dim.height);
		g.drawLine(axisX, 0, axisX, dim.height);
		g.drawLine(axisX + 1, 0, axisX + 1, dim.height);
		if(dashed)
			drawAxisMarcs(g);
		if(vGrid!=null){
			vGrid.drawGrid(g, minX, maxX, d2pX);
		}
	}
	
	protected void drawVGrid(Graphics g, int i, double d){
		
	}
	
	protected void drawHGrid(Graphics g, int i, double d){
		
	}
	
	public void setBackgroundColor(Color c) {
		this.bgColor = c;
		repaint();
	}
	
	public void setCursorColor(Color c, boolean repaint) {
		this.cursorColor = c;
		if(repaint) repaint();
	}
		
	public void setVerticalGridDrawer(VerticalGridDrawer v){
		this.vGrid=v;
	}
	
	// Draw a line with a specific color
	public void drawLine(double x1, double y1, double x2, double y2, Color color) {
		// if(lines.size()>=lineBufferSize)
		// lines.remove(0);
		// lines.add(new Line(x1,y1,x2,y2,color));
		addLine(new Line(x1, y1, x2, y2, color), true);
	}

	// Draw line with no specific color
	public void drawLine(double x1, double y1, double x2, double y2) {
		drawLine(x1, y1, x2, y2, Color.blue);
	}

	// Draw line with no specific color
	public void addLine(Line l, boolean repaint) {
		lines.add(l);
		if (repaint)
			this.repaint();
	}

	public Plot getPlot(int plotIndex){
		if(plotIndex<0 || plotIndex>=plots.size())
			return null;
		return plots.get(plotIndex);
	}
	
	public int setPlot(int pIndex, double[] x, double[] y, Color c, boolean resize) {
		return setPlot(pIndex, 0, 0, x, y, c, resize);
	}

	public int setPlot(int pIndex, double xOffset, double yOffset, double[] x, double[] y, Color c, boolean resize) {
		if (y == null || y.length < 2)
			return -1;
		if (x == null || x.length != y.length)
			return -1;
		if (c == null)
			c = lineColor;
		Plot p = new Plot(x, y, xOffset, yOffset, c);
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
		repaint();
		return lastPlot;
	}
	
	public void removePlot(int index){
		if(index<0 || index>=plots.size()) return;
		plots.remove(index);
		repaint();
	}

	public int addPlot(double[] x, double[] y, Color c, boolean resize) {
		return setPlot(plots.size(), x, y, c, resize);
	}

	public int addPlot(double[] x, double[] y, Color c) {
		return setPlot(plots.size(), x, y, c, true);
	}

	public int addPlot(double[] y, Color c) {
		double[] x = new double[y.length];
		for (int i = 0; i < y.length; i++) {
			x[i] = i;
		}
		return setPlot(plots.size(), x, y, c, true);
	}
	
	public void addPoint(double x, double y, Color c) {
		points.add(new Point(x, y, c));
		repaint();
	}

	public void addPoint(double x, double y) {
		points.add(new Point(x, y));
		repaint();
	}

	// Gets the x value in pixel
	private int xVal(double x) {
		return originX + (int) (x * d2pX);
	}

	// Gets the y value in pixel
	private int yVal(double y) {
		return originY - (int) (y * d2pY);
	}

	// Clears all graphs
	public void clearLines() {
		lines.clear();
	}

	public void clearPlots() {
		plots.clear();
		lastPlot = -1;
		repaint();
	}
	
	public void clearPoints() {
		points.clear();
		repaint();
	}
	
	// Clears all graphs
	public void clearAll() {
		plots.clear();
		lastPlot = -1;
		lines.clear();
		points.clear();
		repaint();
	}

	public void fitY() {
		fitY(lastPlot);
	}

	public void fitY(int plotIndex) {
		Plot p = plots.get(plotIndex);
		maxY = p.getMaxY();
		minY = p.getMinY();
		this.dY = (maxY - minY) / 10d;
		repaint();
	}

	public void fitX() {
		fitX(lastPlot);
	}

	public void fitX(int plotIndex) {
		Plot p = plots.get(plotIndex);
		maxX = p.getMaxX();
		minX = p.getMinX();
		this.dX = (maxX - minX) / 10d;
		repaint();
	}
		
	public void autoscale(boolean sx, boolean sy) {
		double maxX = lines.get(0).x1;
		double maxY = lines.get(0).y1;
		double minX = lines.get(0).x2;
		double minY = lines.get(0).y2;
		double dx = 0;
		double dy = 0;

		for (Line l : lines) {
			if (l.x1 > maxX) maxX = l.x1;
			if (l.x2 > maxX) maxX = l.x2;
			if (l.x1 < minX) minX = l.x1;
			if (l.x2 < minX) minX = l.x2;

			if (l.y1 > maxY) maxY = l.y1;
			if (l.y2 > maxY) maxY = l.y2;
			if (l.y1 < minY) minY = l.y1;
			if (l.y2 < minY) minY = l.y2;
		}
		dx = maxX - minX;
		dy = maxY - minY;
		if (sx) {
			this.maxX = maxX + dx * 0.010;
			this.minX = minX - dx * 0.010;
		}
		if (sy) {
			this.maxY = maxY + dy * 0.010;
			this.minY = minY - dy * 0.010;
		}
	}
		
	public void setPointRadius(int rad) {
		if (rad <= 0)
			return;
		this.pointRad = rad;
		repaint();
	}
	
	public void setPointFill(boolean fillPoint) {
		this.fillPoint = fillPoint;
		repaint();
	}
		
	public void setMaxX(double max) {
		setMaxX(max, false);
	}

	public void setMinX(double min) {
		setMinX(min, false);
	}
	
	public void setMaxX(double max, boolean computeDeltaX) {
		if (max <= this.minX)
			return;
		this.maxX = max;
		if(computeDeltaX)
			this.dX = (this.maxX-this.minX)*0.1;
		setAxisCordinates();
	}
	
	public void setMinX(double min, boolean computeDeltaX) {
		if (min >= this.maxX)
			return;
		this.minX = min;
		if(computeDeltaX)
			this.dX = (this.maxX-this.minX)*0.1;
		setAxisCordinates();
	}

	public void setdeltaX(double d) {
		this.dX = Math.abs(d);
		setAxisCordinates();
	}
		
	public void setMaxY(double max) {
		setMaxY(max, false);
	}
	
	public void setMinY(double min) {
		setMinY(min, false);
	}
	
	public void setMaxY(double max, boolean computeDeltaY) {
		if (max <= this.minY)
			return;
		this.maxY = max;
		if(computeDeltaY)
			this.dY = (this.maxY-this.minY)*0.1;
		setAxisCordinates();
	}
	
	public void setMinY(double min, boolean computeDeltaY) {
		if (min >= this.maxY)
			return;
		this.minY = min;
		if(computeDeltaY)
			this.dY = (this.maxY-this.minY)*0.1;
		setAxisCordinates();
	}
		
	public void setdeltaY(double d) {
		this.dY = Math.abs(d);
		setAxisCordinates();
	}
	
	public void setXAxisLabeler(Labeler l){
		this.xAxisLabler = l;
		this.repaint();
	}
	
	public void setYAxisLabeler(Labeler l){
		this.yAxisLabler = l;
		this.repaint();
	}
	
	public void setXCursorLabeler(Labeler l){
		this.xCursorLabler = l;
		this.repaint();
	}
	
	public void setYCursorLabeler(Labeler l){
		this.yCursorLabler = l;
		this.repaint();
	}
	
	public void showGrid(boolean grid) {
		this.grid = grid;
		this.repaint();
	}
	
	public void showVerticalAxis(boolean show){
		this.drawVerticalAxis = show;
		repaint();
	}
	
	public void showHorizontalAxis(boolean show){
		this.drawHorizontalAxis = show;
		repaint();
	}
		
	public void setAutoRefresh(boolean autoRefresh){
		this.autoRefresh = autoRefresh;
	}
	
	public void setAutoXScroll(boolean autoScroll){
		this.autoXScroll = autoScroll;
	}
		
	private void setAxisCordinates() {
		setD2P();
		originX = (int) round(-minX * d2pX, 0); // For drawing the Y-Axis
		originY = (int) round(maxY * d2pY, 0); // For drawing the X-Axis
		axisX = originX;
		axisY = originY;
		if (minX > 0 && maxX > 0)
			axisX = 0;
		if (minX < 0 && maxX < 0)
			axisX = dim.width;
		if (minY > 0 && maxY > 0)
			axisY = dim.height;
		if (minY < 0 && maxY < 0)
			axisY = 0;
	}
		
	private void setD2P() {
		d2pX = dim.width / (maxX - minX);
		d2pY = dim.height / (maxY - minY);
	}
		
	public double round(double number, double digits) {
		double n = Math.pow(10, digits + 1);
		double out = (long) (number * n);
		double sD = Math.abs(out % 10);
		if (sD >= 5)
			if (out > 0)
				out += 10;
			else
				out -= 10;
		out = (long) (out / 10);
		out = out / Math.pow(10, digits);
		return out;
	}
		
	public double getMaxX(){
		return this.maxX;
	}
	
	public double getMinX(){
		return this.minX;
	}
	
	public double getMaxY(){
		return this.maxY;
	}
	
	public double getMinY(){
		return this.minY;
	}
	
	public boolean isRefreshing(){
		return this.autoRefresh;
	};
		
	public void drawAxis(boolean drawAxis) {
		this.drawAxis = drawAxis;
	}
	
	public void drawDashes(boolean dashes) {
		this.dashed = dashes;
	}
		
	public boolean removeLine(int index) {
		if (lines.remove(index) != null)
			return true;
		return false;
	}
			
	public void moveRight(){
		double dx = (maxX - minX) * 0.05;
		maxX += dx;
		minX  += dx;
		repaint();
	}
	
	public void moveLeft(){
		double dx = (maxX - minX) * 0.05;
		maxX -= dx;
		minX  -= dx;
		repaint();
	}
	
	public void moveUp(){
		double dy = (maxY - minY) * 0.05;
		maxY += dy;
		minY  += dy;
		repaint();
	}
	
	public void moveDown(){
		double dy = (maxY - minY) * 0.05;
		maxY -= dy;
		minY  -= dy;
		repaint();
	}
	
	/**
	 * Magnifies the plot. The expected values are the magnifications for the x and y axis. Positive 
	 * magnification values will zoom in while negative values will zoom out. A value of 0.1 indicates 
	 * a 10% magnification.
	 * @param zX magnification along the x-axis (i.e. 0.1 is a 10% zoom in). 
	 * @param zY magnification along the y-axis (i.e. 0.1 is a 10% zoom in).
	 */
	public void zoom(double zX, double zY){
		double dx = (maxX - minX) * zX;
		double dy = (maxY - minY) * zY;
		maxX -= dx*0.5;
		minX += dx*0.5;
		maxY -= dy*0.5;
		minY += dy*0.5;
		this.dY = (maxY - minY) / 10d;
		this.dX = (maxX - minX) / 10d;
		repaint();
	}
	
	public void setCursorLocation(int x, int y){
		cX = x;
		cY = y;
		repaint();	
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println("p: "+e.getKeyCode());
		switch (e.getKeyCode()) {
		case 17: {
			cntlrP = true;
			break;
		}
		case 18: {
			altP = true;
			break;
		}
		case 38: { // UP
			moveUp();
			break;
		}
		case 40: { // DOWN
			moveDown();
			break;
		}
		case 39: {
			moveRight();
			break;
		}
		case 37: {
			moveLeft();
			break;
		}

		default:
			break;
		}
	}
		
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case 17: {
			cntlrP = false;
			break;
		}
		case 18: {
			altP = false;
			break;
		}
		}
	}
		
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		cursorInWindow = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		cursorInWindow = false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.requestFocusInWindow();
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
		
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// System.out.println(e.getWheelRotation());
		if (cntlrP) {
			if (e.getWheelRotation() > 0) {
				zoom(-0.1, 0);
			} else {
				zoom(0.1/1.1, 0);
			}
		}
		if (altP) {
			if (e.getWheelRotation() > 0) {
				zoom(0,-0.1);
			} else {
				zoom(0,0.1/1.1);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setCursorLocation(e.getX(), e.getY());
	}
		
	public void useCursor(boolean use) {
		useCursor = use;
	}
	
	public void usePermanentCursor(boolean use){
		usePermanentCursor = use;
	}

	public void useHorizontalCursor(boolean use) {
		useHCursor = use;
	}

	public void useHorizontalCursorLabler(boolean use) {
		useHCursorLabel = use;
	}

	public void useVerticalCursor(boolean use) {
		useVCursor = use;
	}

	public void useVerticalCursorLabler(boolean use) {
		useVCursorLabel = use;
	}
}