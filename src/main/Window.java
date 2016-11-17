package main;
// Frame for the Sketcher application
import javax.swing.*;
import java.awt.Font;

@SuppressWarnings("serial")
public class Window extends JFrame {
	
	public JMenuBar menuBar = new JMenuBar(); // Window menu bar

	// Constructor
	public Window(String title) {
		
		setTitle(title); // Set the window title
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(menuBar); // Add the menu bar to the window
	
}
	
	public JLabel addLabel(String str,int xaling,int yaling){
		
		//Trying to add a label
		Font font = new Font("Times New Roman", Font.PLAIN, 12);
		JLabel textLabel = new JLabel(str);
		textLabel.setFont(font);
		textLabel.setHorizontalAlignment(xaling);
		textLabel.setVerticalAlignment(yaling);
		return textLabel;
		
	}
	

}