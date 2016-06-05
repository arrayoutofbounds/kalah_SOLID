package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

/**
 * This class is the starting point for the Modifiability Assignment.
 */
public class Kalah {
	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}
	public void play(IO io) {
		// Replace what's below with your implementation
		
		int numberOfHouses = 6;
		int initialSeeds = 4;
		
		Player one = new Human();
		one.setId(1);
		Player two = new Human();
		two.setId(2);
		
		AbstractState s = new State(one,two,numberOfHouses,initialSeeds);
		
		s.setCurrentPlayer(one);
		s.setOpposingPlayer(two);
		
		Printing p = new PrintingManager(io);
		
		GameManager gm = new GameManager(s,p);
		gm.setCurrentPlayer(s.getCurrentPlayer());
		gm.run();
	}
}
