
public class Node 
{
	private int[] board; //board holds the column location of attacking queens
	private int fitness; //Number of attacking queens
	
	public Node()
	{
		board = new int[17];
	}
	
	public void setBoard(int[] newBoard)
	{
		for (int i=0; i<17; i++)
		{
			board[i] = newBoard[i];
		}
	}
	
	public int[] getBoard()
	{
		return board;
	}
	
	public void setFitness(int f)
	{
		fitness = f; 
	}
	
	public int getFitness()
	{
		return fitness;
	}
	
	public void printBoard(Node n)
	{	
		for (int i=0; i<17; i++)
		{
			System.out.print(board[i] + " ");
		}
		System.out.println();
	}
}
