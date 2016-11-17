package plotter.d3;

import java.awt.Color;

public class CartesianVector extends LineVector{

    //Vector with defined color
    public CartesianVector(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        double[] spherical = tools.VectorConversions.toSpherical(x1, y1, z1);
        r1 		= spherical[0];
        theta1 	= spherical[1];
        phi1 	= spherical[2];
        spherical = tools.VectorConversions.toSpherical(x2, y2, z2);
        r2 		= spherical[0];
        theta2 	= spherical[1];
        phi2 	= spherical[2];
        this.color = color;
    }
 		    
    //Vector with defined color
    public CartesianVector(double x1, double y1, double z1, double x2, double y2, double z2) {
    	this(x1, y1, z1, x2, y2, z2, DEFAULT_COLOR);
    }

}
