package kalah;

public interface Printing {
	public int printInitial(State s);

	public void print(boolean gameOver,State state);
	
	public void noMovesLeft(State state);
	
	public void printHouseEmpty();

	public String convertToString(int h) ;

	public int getInput(Player p) ;
}
