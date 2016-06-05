package kalah;

public interface Printing {
	public int printInitial(AbstractState s);

	public void print(boolean gameOver,AbstractState state);
	
	public void noMovesLeft(AbstractState state);
	
	public void printHouseEmpty();

	public String convertToString(int h) ;

	public int getInput(Player p) ;
}
