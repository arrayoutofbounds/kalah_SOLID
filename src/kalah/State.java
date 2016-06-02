package kalah;

public class State {

	private int initialSeeds;
	private int numberOfHouses;

	private int[] houses1;
	private int[] houses2;

	private Player opposing;

	private int store1;

	private int store2;

	private Player currentPlayer;

	private Player one;
	private Player two;

	public State(Player one, Player two,int numberOfHouses,int initialSeeds) {
		this.initialSeeds = initialSeeds;
		this.numberOfHouses = numberOfHouses;
		this.one = one;
		this.two = two;
		initialise();
	}


	private void initialise() {
		houses1 = new int[numberOfHouses];
		for(int i = 0; i < houses1.length; i++) {
			houses1[i] = initialSeeds;
		}
		houses2 = new int[numberOfHouses];
		for(int i = 0; i < houses2.length; i++) {
			houses2[i] = initialSeeds;
		}
	}

	public int[] getHousesToPrintHouses1() {
		return this.houses1;
	}

	public int[] getHousesToPrintHouses2() {
		return this.houses2;
	}


	public int[] getHouses(boolean b) {
		if(b) {
			if(currentPlayer.getId() == 1) {
				return houses1;
			}else {
				return houses2;
			}
		}else {
			if(currentPlayer.getId() == 1) {
				return houses2;
			}else {
				return houses1;
			}
		}

	}

	public void setOpposingPlayer(kalah.Player p) {
		opposing = p;
	}

	public Player getOpposingPlayer() {
		return this.opposing;
	}
	
	public int getStore1(){
		return this.store1;
	}
	
	public int getStore2(){
		return this.store2;
	}

	public int getStore(int id) {
		if(id == 1) {
			return this.store1;
		}else {
			return this.store2;
		}

	}

	public void setStore(int s,int id) {
		if(id == 1) {
			store1 = s;
		}else {
			store2 = s;
		}

	}

	public Player swapPlayer() {
		Player temp = currentPlayer;
		currentPlayer = opposing;
		opposing = temp;
		return currentPlayer;
	}

	public void setCurrentPlayer(Player one) {
		currentPlayer = one;
	}


	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

}
