package plotter.d3;

import java.awt.Color;

public class SphericalVector extends LineVector{
	
	   //Vector with defined color
    public SphericalVector(double r1, double theta1, double phi1, double r2, double theta2, double phi2, Color color) {
        this.r1 	= x1;
        this.theta1 = y1;
        this.phi1 	= z1;
        this.r2 	= x2;
        this.theta2 = y2;
        this.phi2 	= z2;
        double[] cartesian = tools.VectorConversions.toCartesian(r1, theta1, phi1);
        x1 			= cartesian[0];
        y1 			= cartesian[1];
        z1 			= cartesian[2];
        cartesian = tools.VectorConversions.toCartesian(r2, theta2, phi2);
        x2 			= cartesian[0];
        y2 			= cartesian[1];
        z2 			= cartesian[2];
        this.color = color;
    }
 		    
    //Vector with defined color
    public SphericalVector(double x1, double y1, double z1, double x2, double y2, double z2) {
    	this(x1, y1, z1, x2, y2, z2, DEFAULT_COLOR);
    }

}
