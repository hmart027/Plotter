package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JPanel;

//import com.protocol.Protocol;

import msg.CheckSum;
import msg.MAVLink;
import msg.MAVLinkTypes.MAV_MESSAGE;
import plotter.GraphPanel;
import plotter.d3.Vector3D;
import tools.VectorConversions;
import bytetools.ByteTools;


public class OcilloscopeMain{		
	
//	static Protocol msgHandler = new Protocol();

	private static Window window; 			// The application window
	static GraphPanel pane1 = null; 		// Pane containing filled rectangles
	static GraphPanel pane2 = null; 		// Pane containing filled rectangles
	
	static double Vvalues[]=new double[101];
    
    static int counter=0;
    
    public double xCenter = 0;
    public double yCenter = 0;
    

	Vector3D xAxis = new Vector3D(15, 0, 0);
	Vector3D yAxis = new Vector3D(0, 15, 0);
	Vector3D zAxis = new Vector3D(0, 0, 25);
    
	private int mouseX = 0;
	private int mouseY = 0;
    private boolean cntlrPressed = false;
    private boolean rotating = false;
    
    public double theta = 45;
    public double phi1 	= 45;
    public double phi2 	= 45;
    
    public boolean loadIntoArray(byte[] src, byte[] dst, int offset){
    	if(offset < 0) return false;
    	if(offset+src.length > dst.length) return false;
    	for(int i = 0; i < src.length; i++)
    		dst[offset+i] = src[i];
    	return true;
    }
    
    public OcilloscopeMain(){
    	
//    	int[] msg = new int[] {0x30, //lengt
//    			0,
//    			0x1,
//    			0x1,
//    			0x1,
//    			0, 			// payload 0
//    			0,
//    			0,
//    			0xe0,
//    			0xf5,
//    			0xb9,
//    			0xcd,
//    			0x3f,
//    			0,
//    			0,
//    			0, 			// payload 10
//    			0xc0,
//    			0x2c,
//    			0xfd,
//    			0xdb,
//    			0xbf,
//    			0,
//    			0,
//    			0,
//    			0xa0,
//    			0xa5, 			// payload 20
//    			0xfd,
//    			0x22,
//    			0x40,
//    			0,
//    			0,
//    			0,
//    			0xa0,
//    			0x7d,
//    			0xbd,
//    			0xac, 			// payload 30
//    			0xbf,
//    			0,
//    			0,
//    			0,
//    			0x40,
//    			0x1a,
//    			0x1a,
//    			0xc7,
//    			0xbf,
//    			0, 			// payload 40
//    			0,
//    			0,
//    			0,
//    			0xea,
//    			0x12,
//    			0xac,
//    			0xbf};
//    	
//    	char crc = CheckSum.crc_init();
//		for(int i = 0; i<msg.length; i++)
//			crc = CheckSum.crc_accumulate((byte)msg[i], crc);
//		
//		MAV_MESSAGE msg2 = new MAV_MESSAGE();
//		msg2.setSystemID(1);
//		msg2.setComponentID(1);
//		msg2.setMessageID(1);
//		msg2.setLength(48);
//		for(int i = 5; i<msg.length; i++)
//			msg2.setPayloadByte(i-5, (byte) msg[i]);
//		byte[] t3 = msg2.pack();
//		
//		for(byte b: t3)
//			System.out.println(Integer.toHexString((b) & 0x0FF));
//		
//		System.out.println(Integer.toHexString((crc>>8) & 0x0FF) + " " +Integer.toHexString((crc) & 0x0FF));
//		System.out.println(Integer.toHexString((t3[t3.length-1]) & 0x0FF) + " " +Integer.toHexString((t3[t3.length-2]) & 0x0FF));
//		
//		MAVLink parser = new MAVLink();
//		
//		for(byte b: t3)
//			System.out.println(parser.parseChar(b));
		
    	
    	this.creatGUI();
    	
    	pane2.setMaxX(10);
    	pane2.setMinX(0);
    	pane2.setdeltaX(0.5);
    	pane2.setMaxY(16);
    	pane2.setMinY(-16);
    	pane2.setdeltaY(2);
    	
    	pane1.setMaxX(10);
    	pane1.setMinX(0);
    	pane1.setdeltaX(0.5);
    	pane1.setMaxY(4);
    	pane1.setMinY(-4);
    	pane1.setdeltaY(.5);
    	
//    	new SerialListener().start();
    	
//    	this.creatGUI();
//    	
//    	pane2.setMaxX(10);
//    	pane2.setMinX(-1);
//    	pane2.setdeltaX(0.5);
//    	pane2.setMaxY(1.3);
//    	pane2.setMinY(-1.3);
//    	pane2.setdeltaY(0.1);
//    	
//    	pane1.setMaxX(10);
//    	pane1.setMinX(-1);
//    	pane1.setdeltaX(Math.PI/3);
//    	pane1.setMaxY(1.3);
//    	pane1.setMinY(-1.3);
//    	pane1.setdeltaY(0.1);
//    	
//    	double lx = 0;
//    	double ly = Math.sin(lx);
//    	for(double x = 0; x<10;x+=0.1){
//    		pane1.drawLine(lx, ly, x, Math.sin(x));
//    		lx = x;
//    		ly = Math.sin(x);
//    	}
//    	pane1.showGrid(true);
//    	
//    	new SineThread().start();
    }
	
	// Method to create the application GUI
	private void creatGUI() {
		
		window = new Window("Ocilloscope"); 			// Create the app window
		Toolkit theKit = window.getToolkit(); 		// Get the window toolkit
		Dimension wndSize = theKit.getScreenSize(); // Get screen size
		
		int width = wndSize.width;
		int height = wndSize.height;
		
		int sizeFactor = 90;
		
		if(width>height){
			width = height;
			height = height-50;
		}
		if(height>width){
			height = width;
		}
		
		Dimension prefdim = new Dimension(width,height);//Preffered dimensions
		
		// Set the position & size of window
		window.setBounds(0, 0, 	// Position
				width*sizeFactor/100, height*sizeFactor/100); 				// Size
		window.setPreferredSize(prefdim);

		window.setVisible(true);		// Shows window
		window.pack();					// Packs window
		
		//Sends the size to the class
		Dimension dim = window.getContentPane().getSize();
		pane1 = new GraphPanel(dim.width,dim.height/2-2); // Pane containing filled rectangles
		pane2 = new GraphPanel(dim.width,dim.height/2-2); // Pane containing filled rectangles
		
		//Adds the background picture for the first time
		JPanel pane = new JPanel();
		pane.setPreferredSize(dim);
		pane.add(pane1);
		pane.add(pane2);
		window.getContentPane().add(pane); 
		
		window.setResizable(false);		// Prevents resizing
				
//		System.out.println("Width:  "+pane.dim.width);
//		System.out.println("Heigth: "+pane.dim.height);

	}
	
	private void draw(){
		Vector3D cv = new Vector3D(VectorConversions.toCartesian(1, Math.toRadians(theta), Math.toRadians(phi1)));//new Vector3D(VectorConversions.toCartesian(1, Math.toRadians(45), Math.toRadians(45)));
		Vector3D yv = new Vector3D(VectorConversions.toCartesian(1, Math.toRadians(theta-180), Math.toRadians(phi2)));//new Vector3D(VectorConversions.toCartesian(1, Math.toRadians(-135), Math.toRadians(45)));
		Vector3D xv = Vector3D.crossProduct(cv, yv);
		
//		double x = Vector3D.dotProduct(xAxis, xv);
//		double y = Vector3D.dotProduct(xAxis, yv);
//		
//		System.out.println(xv.getX());
//		System.out.println(xv.getY());
//		System.out.println(xv.getZ());
		
		Vector3D v = new Vector3D(3, 2.5, 5);
		
		pane2.clearLines();
		
		pane2.drawLine(0, 0, -Vector3D.dotProduct(xAxis, xv), Vector3D.dotProduct(xAxis, yv), Color.RED);
		pane2.drawLine(0, 0, -Vector3D.dotProduct(yAxis, xv), Vector3D.dotProduct(yAxis, yv), Color.GREEN);
		pane2.drawLine(0, 0, -Vector3D.dotProduct(zAxis, xv), Vector3D.dotProduct(zAxis, yv), Color.YELLOW);
		
		pane2.drawLine(0, 0, -Vector3D.dotProduct(v, xv), Vector3D.dotProduct(v, yv), Color.BLUE);
		
		pane2.repaint();
	}
	
	private class KeyListener implements java.awt.event.KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			switch(e.getKeyChar()){
			case '+':
				theta++;
				draw();
				break;
			case '-':
				theta--;
				draw();
				break;
			case '8':
				phi1++;
				phi2--;
				draw();
				break;
			case '2':
				phi1--;
				phi2++;
				draw();
				break;
			}
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.isControlDown())
				cntlrPressed = true;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(cntlrPressed && !e.isControlDown()){
				cntlrPressed = false;
				rotating = false;
			}
		}
		
	}
	
	private class MouseListener implements java.awt.event.MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton()==1 && cntlrPressed){
				mouseX = e.getX();
				mouseY = e.getY();
				rotating=true;
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getButton()==1){
				rotating=false;
			}
						
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class MousePositionListener implements MouseMotionListener{
		
		@Override
		public void mouseDragged(MouseEvent e) {
//			System.out.println("Dragging: "+e.getX()+", "+e.getY());
			if(rotating){
				int x = e.getX();
				int y = e.getY();
				if(x>mouseX){
					theta-=0.5;
				}
				if(x<mouseX){
					theta+=0.5;
				}
				if(y>mouseY){
					phi1-=0.5;
					phi2+=0.5;
				}
				if(y<mouseY){
					phi1+=0.5;
					phi2-=0.5;
				}
				draw();
				mouseX = x;
				mouseY = y;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
//			System.out.println("Moving: "+e.getX()+", "+e.getY());
		}
	
	}	
		
/*	public class SerialListener extends Thread{
		
		public void run(){
			try{
		    	ServerSocket server = new ServerSocket(30);
		    	System.out.println("Waiting");
		    	Socket s = server.accept();
		    	System.out.println("Connected");
		    	InputStream input = s.getInputStream();
		    			    	
		    	long t0 	= System.nanoTime();
		    	double t 	=0;
		    	double lt 	= 0;
		    	double dt;
		    	
		    	double ax = 0;
				double ay = 0;
				double az = 0;
				double gx = 0;
				double gy = 0;
				double gz = 0;
		    	
		    	double lax = 0;
		    	double lay = 0;
		    	double laz = 0;
		    	
		    	double lgx = 0;
		    	double lgy = 0;
		    	double lgz = 0;
		    	
		    	long t1 = System.currentTimeMillis();
		    	    	
		    	while(true){
		    		while(input.available()>0){
		    			if(msgHandler.parseChar((char)input.read())){
							double maxX = pane2.getMaxX();
							double minX = pane2.getMinX();
							byte[] in = msgHandler.getPayload();
							
							System.out.println("Got Msg: "+(System.currentTimeMillis() - t1));
							t1 = System.currentTimeMillis();
							
							int index = 0;
			    			ax = ByteTools.byteArray2Float(index, in);
							index+=4;
			    			ay = ByteTools.byteArray2Float(index, in);
							index+=4;
			    			az = ByteTools.byteArray2Float(index, in);
							index+=4;
			    			gx = ByteTools.byteArray2Float(index, in);
							index+=4;
			    			gy = ByteTools.byteArray2Float(index, in);
			    			index+=4;
			    			gz = ByteTools.byteArray2Float(index, in);
			    			
			    			t = (System.nanoTime()-t0)/1000000000d;
			    			dt = (t-lt);
			    			
			    			pane2.drawLine(lt, lax, t, ax,Color.GREEN);
			    			pane2.drawLine(lt, lay, t, ay,Color.BLUE);
			    			pane2.drawLine(lt, laz, t, az,Color.yellow);
	
			    			pane1.drawLine(lt, lgx, t, gx,Color.GREEN);
			    			pane1.drawLine(lt, lgy, t, gy,Color.BLUE);
			    			pane1.drawLine(lt, lgz, t, gz,Color.yellow);
	
	//		    			System.out.println(gx);
	//		    			System.out.println(Math.pow(ax*ax+ay*ay+az*az, 0.5));
	//		    			System.out.println(System.nanoTime());
			    			
			    			lt=t;
			    			lax=ax;
			    			lay=ay;
			    			laz=az;
			    			lgx=gx;
			    			lgy=gy;
			    			lgz=gz;
			    			pane1.autoscale(true, false);
			    			pane2.autoscale(true, false);
			    			pane2.repaint();
			    			pane1.repaint();
			    		}
		    		}
		    	}
		    	
		    	}catch(IOException e){
		    		e.printStackTrace();
		    	}
		}
	}*/
}