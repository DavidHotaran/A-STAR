import javax.swing.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
/**
 * 
 * @author David Hotaran
 * @date May 8, 2020
 * @info A Star algorithm 
 *
 */
public class Game {

	private static JButton buttons[][] = new JButton[25][25]; // JButtons
	
	public static void main(String[] args) {
		
		try {
			start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	/**
	 * handles the logic for the program display
	 * @throws InterruptedException
	 */
	public static void start() throws InterruptedException  {

		JFrame t = gridLayout();
		JOptionPane.showMessageDialog(t, "Click a start and end location and watch the magic happen");
		int dialogButton = JOptionPane.YES_NO_OPTION;

		while (true) {
			if (GridButton.getCount() == 3) {
				A_STAR();
				int dialogResult = JOptionPane.showConfirmDialog(t, "Click YES to run again, NO to exit", "Go Again?",
						dialogButton);
				if (dialogResult == 0) {
					t.dispose();
					GridButton.reset();
					t = gridLayout();
					JOptionPane.showMessageDialog(t, "Click a start and end location and watch the magic happen");
				} else {
					t.dispose();
					break;
				}
			} else {
				Thread.sleep(100);
			}
		}
	}

	/**
	 * Method that creates a 15x15 grid choosing 10% of spots that will be non
	 * navigable, those are colored blue. Uses JFrame, JPanel, GridLayout() and
	 * button class GridButton.
	 */
	public static JFrame gridLayout() {
		JFrame frame = new JFrame("A* search");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(25, 25));
		panel.setBackground(Color.black);

		for (int x = 0; x <= 24; x++) {
			for (int y = 0; y <= 24; y++) {
				buttons[x][y] = new GridButton(x, y);
				panel.add(buttons[x][y]);
			}
		}

		int randomX = 0, randomY = 0;
		for (int i = 0; i < 63; i++) { // generate 10% nodes that are non navigable (10% of 225 is 23 rounded up)
			randomX = (int) (Math.random() * 24 + 1);
			randomY = (int) (Math.random() * 24 + 1);
			buttons[randomX][randomY].setBackground(Color.BLUE);
		}

		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setSize(1000, 1000);

		return frame;
	}

	/**
	 * Animates the path found
	 * 
	 * @throws InterruptedException
	 */
	public static void animate(GridButton node) throws InterruptedException { //works but is backwards and start node is colored, need to fix
		GridButton b = node;
		Stack<GridButton> list = new Stack<GridButton>();
		while(b.getP() != null) { //while there is a parent add children to stack
			list.add(b);
			b = b.getP();
		}
		while(!list.empty()) { //trace stack and animate the path taken
			b = list.pop();
			buttons[b.getP().getXPos()][b.getP().getYPos()].setBackground(Color.RED);
			Thread.sleep(100);
		}
	}

	/**
	 * Implementation of A* algorithm, if solution is found will call method to animate path,
	 * otherwise message is displayed that solution is not found
	 * @throws InterruptedException 
	 */
	public static void A_STAR() throws InterruptedException {

		PriorityQueue<GridButton> open = new PriorityQueue<GridButton>();
		ArrayList<GridButton> closed = new ArrayList<GridButton>();

		GridButton current = new GridButton(GridButton.getStartNodeX(),GridButton.getStartNodeY());
		current.setH(calculateManhattan(current.getXPos(), current.getYPos()));
		current.setF(current.getH() + current.getG());
		open.add(current);
		
		while(!open.isEmpty()) {
			current = open.poll();
			closed.add(current);
			
			if(atGoal(current.getXPos(), current.getYPos())) { //if we found goal node stop
				animate(current); //draw path
				return;
			}
			ArrayList<GridButton> list = getNeighbors(current.getXPos(), current.getYPos()); //generate neighbor nodes
			
			for(GridButton node : list) {
				if(closed.contains(node)) { //if child is in closed list, skip it
					continue;
				}
				node.setG(current.getG() + 1); //increase distance traveled by 1 each time	
				node.setH(calculateManhattan(node.getXPos(), node.getYPos())); //get Manhattan distance
				node.setF(node.getH() + node.getG()); //set total distance
				node.setP(current);
				
				if(open.contains(node)) {
					if(node.getG() > open.poll().getG()) { 
						continue;
					}
				}
				if(buttons[node.getXPos()][node.getYPos()].getBackground() != Color.BLACK) {
				buttons[node.getXPos()][node.getYPos()].setBackground(Color.YELLOW);
				}
				Thread.sleep(100);
				open.add(node);
			}	
		}
		 JOptionPane.showMessageDialog(null, "solution not found"); //solution not found
	}

	/**
	 * 
	 * @param x X Position of node on board
	 * @param y X Position of node on board
	 * @return True if at goal, False otherwise
	 */
	public static boolean atGoal(int x, int y) {
		if (x == GridButton.getGoalNodeX() && y == GridButton.getGoalNodeY()) {
			return true;
		}
		return false;
	}

	/**
	 * Manhattan distance formula
	 * 
	 * @param x X position on board
	 * @param y Y position on board
	 * @return Manhattan distance
	 */
	public static int calculateManhattan(int x, int y) {
		int goalX = GridButton.getGoalNodeX();
		int goalY = GridButton.getGoalNodeY();
		return Math.abs(x - goalX) + Math.abs(y - goalY);
	}

	/**
	 * Generates all neighbors of current node, if possible. Does not add a neighbor
	 * node if it is blocked or if the path has already been traveled on, indicated
	 * by a Yellow color background.
	 * 
	 * @param x X position on board
	 * @param y Y position on board
	 * @return an ArrayList containing the valid neighbors of current node.
	 */
	public static ArrayList<GridButton> getNeighbors(int x, int y) {
		ArrayList<GridButton> neighbors = new ArrayList<GridButton>(); // ArrayList to hold neighbor buttons

		if ((x - 1) >= 0 && buttons[x - 1][y].getBackground() != Color.BLUE
				&& buttons[x - 1][y].getBackground() != Color.YELLOW) { // check to see if can add left neighbor
			neighbors.add(new GridButton(x - 1, y));
		}
		if ((x + 1) <= 24 && buttons[x + 1][y].getBackground() != Color.BLUE
				&& buttons[x + 1][y].getBackground() != Color.YELLOW) { // check to see if can add right neighbor
			neighbors.add(new GridButton(x + 1, y));
		}
		if ((y - 1) >= 0 && buttons[x][y - 1].getBackground() != Color.BLUE
				&& buttons[x][y - 1].getBackground() != Color.YELLOW) { // check to see if can add top neighbor
			neighbors.add(new GridButton(x, y - 1));
		}
		if ((y + 1) <= 24 && buttons[x][y + 1].getBackground() != Color.BLUE
				&& buttons[x][y + 1].getBackground() != Color.YELLOW) { // check to see if can add bottom neighbor
			neighbors.add(new GridButton(x, y + 1));
		}
		return neighbors;
	}

}
