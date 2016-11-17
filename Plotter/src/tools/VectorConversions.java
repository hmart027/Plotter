package tools;

public class VectorConversions {

	public static double[] toSpherical(double x, double y, double z){
		double[] out = new double[3];
		out[0] = Math.pow(x*x+y*y+z*z, 1/2d);
		out[1] = Math.atan2(y, x);
		out[2] = Math.acos(z/out[0]);
		return out;
	}
	
	public static double[] toCartesian(double r, double theta, double phi){
		double[] out = new double[3];
		out[0] = r*Math.cos(theta)*Math.sin(phi);
		out[1] = r*Math.sin(theta)*Math.sin(phi);
		out[2] = r*Math.cos(phi);
		return out;
	}
}
