package plotter;

import java.awt.Graphics;

public interface VerticalGridDrawer {
	
	public void drawGrid(Graphics g, int i, double d);

	public void drawGrid(Graphics g, double minX, double maxX, double d2pX);

}
