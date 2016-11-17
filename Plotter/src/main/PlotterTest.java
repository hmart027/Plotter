package main;

import javax.swing.JFrame;

import plotter.GraphPanel;

public class PlotterTest extends JFrame {

	private static final long serialVersionUID = -141245183353600048L;

	public PlotterTest(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 800);
		GraphPanel pane = new GraphPanel();
		
		setContentPane(pane);
		setVisible(true);
	}

	public static void main(String[] args) {
		new PlotterTest();
	}

}
