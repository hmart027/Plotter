package plotter;

import java.awt.Color;

public class GraphPanelLabel {

	private String label;
	private String val;
	private int x, y;
	private Color c = Color.RED;
	
	public GraphPanelLabel(String l, String v, int x, int y){
		this.label = l;
		this.val = v;
		this.x=x;
		this.y=y;
	}
	
	public String getLabel(){
		return label;
	}
	
	public String getValue(){
		return val;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Color getColor(){
		return c;
	}
	
	public void setLabel(String l){
		this.label = l;
	}
	
	public void setValue(String v){
		this.val = v;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setColor(Color c){
		this.c = c;
	}
}
