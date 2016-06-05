package kalah;

public abstract class AbstractState {
	
	protected int initialSeeds;
	protected int numberOfHouses;

	protected int[] houses1;
	protected int[] houses2;

	protected Player opposing;

	protected int store1;

	protected int store2;

	protected Player currentPlayer;

	protected Player one;
	protected Player two;
	
	protected abstract void initialise();
	
	public abstract int[] getHousesToPrintHouses1();
	public abstract int[] getHousesToPrintHouses2();
	
	public abstract int[] getHouses(boolean b);
	public abstract void setOpposingPlayer(kalah.Player p);
	
	public abstract Player getOpposingPlayer();
	
	public abstract int getStore1();
	
	public abstract int getStore2();
	
	
	public abstract int getStore(int id);
	
	public abstract void setStore(int s,int id);
	
	public abstract Player swapPlayer();
	
	public abstract void setCurrentPlayer(Player one);
	
	public abstract Player getCurrentPlayer();
	

}
