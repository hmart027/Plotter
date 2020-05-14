package plotter;

import java.awt.Dimension;
import java.awt.Toolkit;

import main.Window;
import plotter.GraphPanel;

public class SimplePlotter extends GraphPanel{

	private static final long serialVersionUID = -6607071461968685823L;
	private Window window; 			// The application window
            
    public SimplePlotter(String title){
    	if(title==null)
    		title = "SimplePlotter";
		window = new Window(title); 			// Create the app window
    	this.creatGUI();

    	this.setBackgroundColor(java.awt.Color.WHITE);
    	this.setCursorColor(java.awt.Color.BLACK, true);
    	this.setMaxY(10);
    	this.setMinY(-10);
    	this.setdeltaY(.5);
    	
    	this.setMinX(0);
    	this.setMaxX(20);
    	this.setdeltaX(1);

    }
    
    public void setTitle(String title){
    	this.window.setTitle(title);
    }
    
    public void setResizable(boolean res) {
    	window.setResizable(res);
    }
	
	// Method to create the application GUI
	private void creatGUI() {
		
		Toolkit theKit = window.getToolkit(); 		// Get the window toolkit
		Dimension wndSize = theKit.getScreenSize(); // Get screen size
		
		int width = wndSize.width;
		int height = wndSize.height;
		
		double sizeFactor = 0.30;
		
		height = height-50;
		
		width 	*= sizeFactor;
		height 	*= sizeFactor;
		
		Dimension prefdim = new Dimension(width,height);//Preffered dimensions
		
		// Set the position & size of window
		window.setBounds(0, 0, 	// Position
				width, height); 				// Size
		window.setPreferredSize(prefdim);

		window.setVisible(true);		// Shows window
		window.pack();					// Packs window
		
		//Sends the size to the class
		Dimension dim = window.getContentPane().getSize();
		
		this.init(dim.width,dim.height-2); // Pane containing filled rectangles
		
		window.setContentPane(this);
		window.setResizable(false);		// Prevents resizing
		window.pack();

	}
	
}
