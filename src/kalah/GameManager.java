package kalah;

public class GameManager {

	//private Player one;
	//private Player two;
	private Printing print;
	
	private int input;
	private int index; // derived from input
	private boolean initialDone = false;

	private int increment;
	private int opposingIncrement;
	private boolean oppositionHousesRemaining;

	private int wrapIndex;

	private int seedsRemaining;

	private Player currentPlayer;
	private AbstractState currentState;

	public GameManager(AbstractState s,Printing p) {
		currentState = s;
		print = p;
	}

	public void setCurrentPlayer(Player p) {
		currentPlayer = p;
	}
	
	public void setPrinting(Printing p) {
		print = p;
	}

	public void run() {

		// check if the current player has any moves left. if not, then the game finishes
		boolean movesLeft = checkIfMovesLeft();
		if(movesLeft==false) {
			print.noMovesLeft(currentState);
		}else {

			if(initialDone == false) {
				input = print.printInitial(currentState);
				initialDone=true;
			}else {
				input = print.getInput(currentPlayer);
			}
			// input is already in correct format
			if(input==-1) {
				print.print(true,currentState);
			}else {
				// they did not press quit 

				// check if the house they selected has seeds in it or not
				// if not then printAndRun() again
				if(currentState.getHouses(true)[input-1] == 0) {
					print.printHouseEmpty();
					printAndRunAgain();
				}else {
					// so a house has been picked
					// break the board into 4 sections
					// 1. current players side of the board
					// 2. current players store
					// 3. opp players side of the board
					// 4. back around to this player 
					// step 1
					houseSelected();
				}
			}
		}
	}

	private boolean checkIfMovesLeft() {
		boolean movesLeft = false;
		for(int i =0;i<currentState.getHouses(true).length;i++) {
			if(currentState.getHouses(true)[i]>0) {
				movesLeft = true;
				break;
			}
		}
		return movesLeft;
	}

	private void houseSelected() {
		// seeds that are in the chosen house of the current player
		index = input-1;
		seedsRemaining = currentState.getHouses(true)[index];
		increment = 1;
		while((seedsRemaining>0)&&((index+increment)<=(currentState.getHouses(true).length-1))) {
			placeSeedsOnCurrentPlayerSide((index+increment));
		}
		// if there were remaining then they were put in the store after reaching the end
		if(seedsRemaining>0) {
			putInStoreOfCurrentPlayer();
		}
		// if still remaining then put on the opposite players side
		opposingIncrement = 0;
		oppositionHousesRemaining = true;
		while((seedsRemaining>0) && (oppositionHousesRemaining == true) && (opposingIncrement<6)) {
			putSeedsInOppositionHouses();
		}
		// if still remaining then wrap around to the current player side again till the original index reached
		wrapIndex=0;
		while((wrapIndex<=index) && (seedsRemaining>0)) {
			wrapAround();
		}
	}

	private void wrapAround() {
		seedsRemaining--;
		if(seedsRemaining==0) {
			int houseLastPutIn = currentState.getHouses(true)[wrapIndex];
			if(houseLastPutIn != 0) {

				if((currentState.getHouses(true)[index] ==1)&&(index==wrapIndex)) {
					int oppHouseIndex = getOppHouseIndex(wrapIndex);
					int oppHouseSeeds = currentState.getHouses(false)[oppHouseIndex];

					if(oppHouseSeeds ==0) {
						//NO CAPTURE
						currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
						currentState.getHouses(true)[wrapIndex]++; // the house gets add a seed
						wrapIndex = 0;
						swapCurrentPlayer(); // as this player no longer has the turn
						printAndRunAgain();
					}else {
						// CAPTURE
						// remove all seeds from opp house
						currentState.getHouses(false)[oppHouseIndex] = 0;
						// original house down by one
						currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
						//no need to add to the current index as it goes to the store directly
						currentState.setStore(currentState.getStore(currentPlayer.getId()) + oppHouseSeeds + 1,currentPlayer.getId());
						// set increment back to 1
						increment = 1;
						swapCurrentPlayer(); // as this player no longer has the turn
						printAndRunAgain();
					}
				}else {
					//System.out.println(currentPlayer.getHouses()[index]);
					currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
					currentState.getHouses(true)[wrapIndex]++; // the house gets add a seed
					wrapIndex = 0;
					swapCurrentPlayer(); // as this player no longer has the turn
					printAndRunAgain();
				}
			}

			if(houseLastPutIn == 0) {
				// if opp house has 0 seeds then no capture
				// else capture
				int oppHouseIndex = getOppHouseIndex(wrapIndex);
				int oppHouseSeeds = currentState.getHouses(false)[oppHouseIndex];
				if(oppHouseSeeds ==0) {
					//NO CAPTURE
					currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
					currentState.getHouses(true)[wrapIndex]++; // the house gets add a seed
					wrapIndex = 0;
					swapCurrentPlayer(); // as this player no longer has the turn
					printAndRunAgain();
				}else {
					// CAPTURE
					// remove all seeds from opp house
					currentState.getHouses(false)[oppHouseIndex] = 0;
					// original house down by one
					currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
					//no need to add to the current index as it goes to the store directly
					currentState.setStore(currentState.getStore(currentPlayer.getId()) + oppHouseSeeds + 1,currentPlayer.getId());
					// set increment back to 1
					increment = 1;
					swapCurrentPlayer(); // as this player no longer has the turn
					printAndRunAgain();
				}
			}
		}else {
			// the house that had the seeds has one taken away from it
			currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
			currentState.getHouses(true)[wrapIndex]++; // the house gets add a seed
			wrapIndex++;
		}
	}
	/**
	 * This method prints and then get the input by running the run method again
	 */
	private void printAndRunAgain() {
		print.print(false,currentState);
		run();
	}

	private void putSeedsInOppositionHouses() {
		if(opposingIncrement>5) {
			oppositionHousesRemaining = false;
			opposingIncrement = 0;
		}else {
			//decrement the seeds
			seedsRemaining--;
			if(seedsRemaining==0) {
				// minus from original house
				currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
				// add to opposite players house
				currentState.getHouses(false)[opposingIncrement] = currentState.getHouses(false)[opposingIncrement] + 1;
				opposingIncrement = 0;
				oppositionHousesRemaining = true;
				// ending on opp player means its the other players turn
				swapCurrentPlayer();
				printAndRunAgain();
			}else {
				// minus from original house
				currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
				// add to opposite players house
				currentState.getHouses(false)[opposingIncrement] = currentState.getHouses(false)[opposingIncrement] + 1;
				// increment to next house of opposition
				opposingIncrement++;
				oppositionHousesRemaining = true;
			}
		}
	}

	private void putInStoreOfCurrentPlayer() {
		seedsRemaining--;
		if(seedsRemaining==0) {
			currentState.setStore(currentState.getStore(currentPlayer.getId()) + 1,currentPlayer.getId());
			// take away one from chosen house
			currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
			// no swapping players as getting it in the store last
			printAndRunAgain();
		}else {
			// add to store
			currentState.setStore(currentState.getStore(currentPlayer.getId()) + 1,currentPlayer.getId());
			// take away one from chosen house
			currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
			// not 0 yet so more seeds hence no print
		}
	}

	private void placeSeedsOnCurrentPlayerSide(int house) {
		seedsRemaining--;
		if(seedsRemaining==0) {
			int houseLastPutIn = currentState.getHouses(true)[house];
			if(houseLastPutIn != 0) {
				currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
				currentState.getHouses(true)[house]++; // the house gets add a seed
				increment = 1;
				swapCurrentPlayer(); // as this player no longer has the turn
				printAndRunAgain();
			}
			if(houseLastPutIn == 0) {
				// if opp house has 0 seeds then no capture
				// else capture
				int oppHouseIndex = getOppHouseIndex(house);
				int oppHouseSeeds = currentState.getHouses(false)[oppHouseIndex];
				if(oppHouseSeeds ==0) {
					//NO CAPTURE
					currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
					currentState.getHouses(true)[house]++; // the house gets add a seed
					increment = 1;
					swapCurrentPlayer(); // as this player no longer has the turn
					printAndRunAgain();
				}else {
					// CAPTURE
					// remove all seeds from opp house
					currentState.getHouses(false)[oppHouseIndex] = 0;
					// original house down by one
					currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
					//no need to add to the current index as it goes to the store directly
					currentState.setStore( currentState.getStore(currentPlayer.getId()) + oppHouseSeeds + 1,currentPlayer.getId());
					// set increment back to 1
					increment = 1;
					swapCurrentPlayer(); // as this player no longer has the turn
					printAndRunAgain();
				}
			}
		}else {
			// the house that had the seeds has one taken away from it
			currentState.getHouses(true)[index] = currentState.getHouses(true)[index] -1; 
			currentState.getHouses(true)[house]++; // the house gets add a seed
			increment++;
		}
	}

	private int getOppHouseIndex(int currentHouseIndex) {
		if(currentHouseIndex == 0) {
			return 5;
		}else if(currentHouseIndex == 1) {
			return 4;
		}else if(currentHouseIndex == 2) {
			return 3;
		}else if(currentHouseIndex == 3) {
			return 2;
		}else if(currentHouseIndex == 4) {
			return 1;
		}else if(currentHouseIndex == 5) {
			return 0;
		}else {
			System.out.println("Sorry you cannot have a index beyong 0 and 5");
			return -1;
		}
	}

	private void swapCurrentPlayer() {
		// p1 has id 1
		// p2 has id 2
		Player newCurrent = currentState.swapPlayer();
		currentPlayer = newCurrent;
	}
}
