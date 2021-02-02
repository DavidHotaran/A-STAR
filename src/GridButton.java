import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
/**
 * 
 * @author David Hotaran
 * @date May 8, 2020
 * @info A Star algorithm 
 *
 */


public class GridButton extends JButton implements ActionListener, Comparable<GridButton> {
	private static int count = 1;
	private int x, y, h, g, f; 
	private static int goalNodeX, goalNodeY; //goal node positions
	private static int startNodeX, startNodeY; //start node positions
	public GridButton parent; //pointer to parent node
	
	public GridButton(int x, int y) { 
		super();
		this.x = x;
		this.y = y;
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) { 
		if(this.getBackground() == Color.BLUE) {
			JOptionPane.showMessageDialog(null, "Cannot click on obstacle block", "Error", JOptionPane.ERROR_MESSAGE);
		}
		if(count <=2 && this.getBackground() != Color.BLUE) { //only do this twice
			if(count == 1) { //start location
				startNodeX = this.x;
				startNodeY = this.y;
				setBackground(Color.black);
				count++;
			}
			else if(count == 2) { //end location
				goalNodeX = this.x;
				goalNodeY = this.y;	
				setBackground(Color.black);
				count++;
			}
		}
	}
	//getters for start/end positions of goal/start locations and x/y boardPosition
	public static int getGoalNodeX() {
		return goalNodeX;
	}
	public static int getGoalNodeY() {
		return goalNodeY;
	}
	public static int getStartNodeX() {
		return startNodeX;
	}
	public static int getStartNodeY() {
		return startNodeY;
	}
	public int getXPos() {
		return x;
	}
	public int getYPos() {
		return y;
	}
	public static int getCount() { //gets value of count, used in main method to see if A* can begin
		return count;
	}
	public static void reset() { //resets all static variables so new search can be started
		goalNodeX = 0;
		goalNodeY = 0;
		startNodeX = 0;
		startNodeY = 0;
		count = 1;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getF() {
		return f;
	}
	public void setF(int f) {
		this.f = f;
	}
	
	@Override
	public int compareTo(GridButton b) {//compare function might be backwards
		if(this.getH() > b.getH()) {
			return 1;
		}else if(this.getH() < b.getH()) {
			return -1;
		}else {
			return 0;
		}
	}
	public void setP(GridButton b) {
		this.parent = b;
	}
	public GridButton getP() {
		return parent;	
	}

}
