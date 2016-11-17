package plotter.d3;

public class Vector3D {
	//Cartesian Coordinates
    double x=0; 
    double y=0; 
    double z=0;
    
    public Vector3D (double x, double y, double z){
    	this.x = x;
        this.y = y;
        this.z = z; 	
    }
    
    public Vector3D (double[] xyz){
    	this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2]; 	
    }
    
    public double getX(){return this.x;}
    public double getY(){return this.y;}
    public double getZ(){return this.z;}
    
    public Vector3D getUnitVector(){
    	double r = Math.pow(x*x+y*y+z*z, 1/2d);
    	return new Vector3D(x/r, y/r, z/r);
    }
    
    public void scale(double a){
    	this.x *= a;
    	this.y *= a;
    	this.z *= a;
    }
    
    public static Vector3D add(Vector3D v1, Vector3D v2){
    	return new Vector3D(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z);
    }
    
    public static double dotProduct(Vector3D v1, Vector3D v2){
    	return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
    }
    
    public static Vector3D crossProduct(Vector3D v1, Vector3D v2){
    	return new Vector3D(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y*v2.x);
    }
}
