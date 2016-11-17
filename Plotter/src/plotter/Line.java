package plotter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

//Object that defines lines
public class Line{ 
	public static final BasicStroke BASIC = new BasicStroke();
	public static final BasicStroke DASHED = new BasicStroke(1.0f,
		        BasicStroke.CAP_BUTT,
		        BasicStroke.JOIN_MITER,
		        10.0f, new float[]{10.0f}, 0.0f);
	
    final double x1; 
    final double y1;
    final double x2;
    final double y2;   
    final Color color;
    final Stroke stroke;
    
   //Lines with defined color
   public Line(double x1, double y1, double x2, double y2, Color color, Stroke stroke) {
       this.x1 		= x1;
       this.y1 		= y1;
       this.x2 		= x2;
       this.y2 		= y2;
       this.color 	= color;
       this.stroke 	= stroke;
   }
	
   public Line(double x1, double y1, double x2, double y2, Color color) {
	   this(x1,y1,x2,y2,color, BASIC);
   }
   
   //Lines with defined color
   public Line(double x1, double y1, double x2, double y2) {
	   this(x1,y1,x2,y2,Color.WHITE, null);
   }
		    
}
