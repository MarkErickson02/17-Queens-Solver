//Mark Erickson

import java.util.*;

/* This program solves the 17-Queens problem using the steepest hill climb and min-conflicts algorithms.
 * 
 */
public class Queens 
{
	static int steps = 0;
	
	/* The steepestHillClimb method creates a random board and improves on each column by placing it in the
	 * row with the least attacking queens. 
	 */
	public boolean steepestHillClimb()
	{
		PriorityQueue<Node> successors = new PriorityQueue<Node>(17, new Comparator<Node>(){ //This priority queue holds nodes ordered by their fitness cost. 
			public int compare(Node a, Node b)
			{
				return a.getFitness() - b.getFitness();
			}
		});
		Node current = new Node();
		int[] queens = new int[17];
		Random rng = new Random();
		for (int i=0; i<17; i++) //Creating new 17-Queens problem.
		{
			queens[i] = rng.nextInt(17);
		}
		current.setBoard(queens);
		current.setFitness(findFitness(queens));
		
		Node successor = current;
		
		while (current.getFitness() != 0) //Loop until there is zero conflicts.
		{
			for (int i=0; i<17; i++) //Loop through all columns. 
			{
				for (int j=0; j<17; j++) //Loop through all rows.
				{
					int[] currentBoard = successor.getBoard();
					Node bestRowSuccessor = new Node();
					if (j == currentBoard[i])
					{
						continue;
					}
					else
					{
						currentBoard[i] = j; //Replace queen in column i with other rows.
						bestRowSuccessor.setBoard(currentBoard);
						bestRowSuccessor.setFitness(findFitness(currentBoard));
						successors.add(bestRowSuccessor); //Adds the succcessor to priority queue to find least conflicting state.
					}
				}
				successor = successors.remove(); //successor now holds the best row for a given column. 
				successors.clear();
				
			}
			steps++;
		
			if (successor.getFitness() >= current.getFitness()) //If successor does not improve on current state then we are at a local maxima.
			{
				return false;
			}
			current = successor;
			successor = current;
			
		}
		current.printBoard(current);
		return true;
	}
	
	/* This method finds the number of queens that can attack other queens.
	 * An attacking queen can be on the same row or on the two diagonals.
	 * The calculated fitness is between all queens so the pairs of attacking queens is one half fitness.
	 */
	
	public int findFitness(int[] board)
	{
		int fitness = 0;
		int firstQueen;
		int secondQueen;
		
		for (int i=0; i<17; i++) // Loop through all pairs of queens.
		{
		    firstQueen = board[i];
			
			for (int j=0; j<17; j++)
			{
				secondQueen = board[j];
				if (i == j)
				{
					continue;
				}
				else
				{
					int rowDifference = Math.abs(firstQueen-secondQueen); //The difference between two rows
					int colDifference = Math.abs(i-j); //The difference between two columns 
					if (firstQueen == secondQueen || rowDifference == colDifference) //If the difference between two columns is equal the two queens are attacking
					{
						fitness++;
					}
				}
				
			}
		}
		return fitness/2;
	}
	
	/* The minConlficts algorithm selects a queen at random then finds the row with the lowest number of conflicting queens.
	 * Returns true if board can be solved and false if it cannot.
	 */
	public boolean minConflicts()
	{
		PriorityQueue<Node> minConflictingSuccessors = new PriorityQueue<Node>(17, new Comparator<Node>(){ //This priority queue determines the best row to put a random queen.
			public int compare(Node a, Node b)
			{
				return a.getFitness() - b.getFitness();
			}
		});
		Node current = new Node();
		int[] queens = new int[17];
		Random rng = new Random();
		for (int i=0; i<17; i++)
		{
			queens[i] = rng.nextInt(17);
		}
		current.setBoard(queens);
		current.setFitness(findFitness(queens));
		
		for (int maxSteps=0; maxSteps<289; maxSteps++) //Allow for 289 random column selections
		{
			if (current.getFitness() == 0)
			{
				current.printBoard(current);
				return true;
			}
			int randomCol = rng.nextInt(17);
			int[] currentBoard = current.getBoard();
			for (int row=0; row<17; row++) //This for loop searches through each row to find the location with the least number of attacking queens.
			{
				Node successor = new Node();
				currentBoard[randomCol] = row;
				successor.setBoard(currentBoard);
				successor.setFitness(findFitness(currentBoard));
				minConflictingSuccessors.add(successor);
			}
			current = minConflictingSuccessors.remove();
			minConflictingSuccessors.clear();
		}
		return false;
	}
	
	public void geneticAlgorithm()
	{
		Random rng = new Random();
		int fitnessWeight = 0;
		ArrayList<Node> geneticList = new ArrayList<Node>(); //Hold nodes in population
		PriorityQueue<Node> fitnessPriority = new PriorityQueue<Node>(17, new Comparator<Node>(){ //Order the nodes in population by their fitness function.
			public int compare(Node a, Node b)
			{
				return b.getFitness() - a.getFitness();
			}
		});
		for (int population = 0; population <10; population++)
		{
			Node pop = new Node(); //Creating new 17-queens problem.
			int[] queens = new int[17];
			for (int i=0; i<17; i++)
			{
				queens[i] = rng.nextInt(17);
			}
			pop.setBoard(queens);
			int fitness = 136 - findFitness(queens);
			fitnessWeight += fitness;
			pop.setFitness(fitness);
			geneticList.add(pop);
		}
		for (int i=0; i<10; i++)
		{
			Node normalizeNode = geneticList.get(i);
			int attackingQueens = normalizeNode.getFitness();
			normalizeNode.setFitness(attackingQueens/fitnessWeight);
			fitnessPriority.add(normalizeNode);
		}
		Random rand = new Random();
		while (fitnessPriority.size() > 6) //Selection
		{
			int selection = rand.nextInt(1);
			for (int i=0; i<10; i++)
			{
				if (fitnessPriority.peek().getFitness() > selection)
				{
					fitnessPriority.remove();
				}
			}
		}
		
		
	}
	
	public static void main(String args[])
	{
		Queens queen = new Queens();
		int solved = 0;
		final long startTime = System.nanoTime();
		for (int i=0; i<1000; i++)
		{
			if (queen.steepestHillClimb() == true)
			{
				solved++;
			}
		}
		final long endTime = System.nanoTime() - startTime;
		System.out.println("Number of solved 17-Queens problem with steepest hill climbing: " + solved);
		double successRate = (((double)solved)/1000)*100;
		System.out.println("Percentage solved: " + successRate + "%");
		System.out.println("Total elapsed time for 1000 trials: " + endTime * Math.pow(10, -9) + " seconds");
		System.out.println("Average run-time per trial: " + endTime/1000 * Math.pow(10, -9) + " seconds");
		System.out.println("The average number of steps per 1000 trials: " + steps/1000);
		
		solved = 0;
		final long conflictsStartTime = System.nanoTime();
		for (int j=0; j<1000; j++)
		{
			if (queen.minConflicts() == true)
			{
				solved++;
			}
		}
		final long endConflictsTime = System.nanoTime() - conflictsStartTime;
		System.out.println("Number of solved 17-queens problems with min conflicts: " + solved);
		successRate = (((double)solved)/1000)*100;
		System.out.println("Percentage of solved: " + successRate + "%");
		System.out.println("Total elapsed time for 1000 trials: " + endConflictsTime * Math.pow(10, -9) + " seconds");
		System.out.println("Average run-time per trial: " + endConflictsTime/1000 * Math.pow(10, -9) + " seconds");
	}
}
