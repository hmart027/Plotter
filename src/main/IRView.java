package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.javafx.geom.Arc2D;

public class IRView extends JPanel {

	private static final long serialVersionUID = 5972794662246986041L;

	private float maxDistance = 10;
	private int numberOfSensors = 16;
	private float distances[];
	
	public IRView() {
		this.setPreferredSize(new Dimension(500, 500));
		distances = new float[numberOfSensors];
		distances[0] = 0.5f;
		distances[1] = 0.25f;
		distances[2] = 0.5f;
	}
	
	public void setBounds(int x, int y, int width, int height){
		if(width<height)
			super.setBounds(x, y, width, width);
		else
			super.setBounds(x, y, height, height);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(java.awt.Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(java.awt.Color.BLUE);
		g.fillOval(0, 0, this.getWidth(), this.getHeight());
		int xC = this.getWidth()/2;
		int yC = this.getHeight()/2;
		
		float inc = 360.0f/(float)numberOfSensors;
		float ang;
		int x, y, r;
		r = this.getHeight()/2;

		g.setColor(java.awt.Color.GRAY);
		for(int i=0; i<numberOfSensors; i++){
			ang = (float) Math.toRadians(inc*i);	
			int z = (int) (r-r*distances[i]);
			g.fillArc(xC-z, yC-z, 2*(z), 2*(z), (int)Math.toDegrees(ang), (int)Math.round(inc));
		}
		
		g.setColor(java.awt.Color.GREEN);
		for(int i=0; i<numberOfSensors; i++){
			ang = (float) Math.toRadians(inc*i);
			x = (int) (r*Math.sin(ang));
			y = (int) (r*Math.cos(ang));
			g.drawLine(xC, yC, xC+x, yC+y);
		}		

		g.setColor(java.awt.Color.RED);
		g.drawLine(xC, yC-5, xC, yC+5);
		g.drawLine(xC+1, yC-5, xC+1, yC+5);
		g.drawLine(xC-1, yC-5, xC-1, yC+5);
		g.drawLine(xC-5, yC, xC+5, yC);
		g.drawLine(xC-5, yC+1, xC+5, yC+1);
		g.drawLine(xC-5, yC-1, xC+5, yC-1);
		
		
	}
	
	public static void main(String[] args){
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(new IRView());
		f.setVisible(true);
		f.pack();
	}

}
