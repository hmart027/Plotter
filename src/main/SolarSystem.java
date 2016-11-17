package main;

import plotter.d3.CartesianVector;
import plotter.d3.LineVector;
import plotter.d3.SphericalVector;
import plotter.d3.Vector3D;
import tools.VectorConversions;


@SuppressWarnings("unused")
public class SolarSystem {
	
	double scale = 3.0E11;    //7009100d
	
	public SolarSystem(){
		new OcilloscopeMain();
	}
	
	
	public static void main(String[] args){
		new SolarSystem();
//		long t0;
//		LineVector l1;
//		long t1;
//		long totalT;
//				
//		totalT = 0;
//		for(int i = 0; i<1000;i++){
//			t0 = System.currentTimeMillis();
//			l1 = new SphericalVector(1, 3.14, 1, 2, 3.14, 1.5);
//			t1 = System.currentTimeMillis();
//			totalT += t1-t0;
//		}
//		System.out.println(totalT/1000f);
//		
//		totalT = 0;
//		for(int i = 0; i<1000;i++){
//			t0 = System.currentTimeMillis();
//			l1 = new CartesianVector(2, 2, 2, 5, 5, 5);
//			t1 = System.currentTimeMillis();
//			totalT += t1-t0;
//		}
//		System.out.println(totalT/1000f);
		
//		Vector3D v1 =new Vector3D(5, 0, 0);
//		Vector3D cv =new Vector3D(VectorConversions.toCartesian(1, Math.toRadians(45), Math.toRadians(-45)));
//		Vector3D yv =new Vector3D(VectorConversions.toCartesian(1, Math.toRadians(45), Math.toRadians(45)));
//		Vector3D xv = Vector3D.crossProduct(cv, yv);
//		double x = Vector3D.dotProduct(v1, xv);
//		double y = Vector3D.dotProduct(v1, yv);
		
//		System.out.println(v4.getX());
//		System.out.println(v4.getY());
//		System.out.println(v4.getZ());
//		System.out.println(x+","+y);
		
	}

}
