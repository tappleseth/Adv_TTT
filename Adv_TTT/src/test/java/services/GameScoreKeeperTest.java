package services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ttt.Services.GameScoreKeeper;

public class GameScoreKeeperTest{
	@Test
	public void givenDiagonalVictory1_hasWinner_returnsTrue() {
		// arrange
		var board = new String[] {"X", "O", "O", "_", "X", "O", "_", "_", "X"};
		var scoreKeeper = new GameScoreKeeper();
		
		// act
		var hasWinner = scoreKeeper.hasWinner(board);
		
		// assert
		assertTrue(hasWinner);
	}
	
	@Test
	public void givenDiagonalVictory2_hasWinner_returnsTrue() {
		// arrange
		var board = new String[] {"X", "O", "O", "X", "O", "X", "O", "_", "X"};
		var scoreKeeper = new GameScoreKeeper();

		// act
		var hasWinner = scoreKeeper.hasWinner(board);

		// assert
		assertTrue(hasWinner);
	}
	
	@Test
	public void givenVerticalVictory_hasWinner_returnsTrue() {
		// arrange
		var board = new String[] {"X", "_", "_", "X", "_", "_", "X", "_", "_"};
		var scoreKeeper = new GameScoreKeeper();

		// act
		var hasWinner = scoreKeeper.hasWinner(board);

		// assert
		assertTrue(hasWinner);
	}
	
	@Test
	public void givenHorizontalVictory_hasWinner_returnsTrue() {
		// arrange
		var board = new String[] {"O", "O", "O", "_", "_", "_", "_", "_", "_"};
		var scoreKeeper = new GameScoreKeeper();

		// act
		var hasWinner = scoreKeeper.hasWinner(board);

		// assert
		assertTrue(hasWinner);
	}
	
	@Test
	public void givenStalemate_hasWinner_returnsFalse() {
		// arrange
		var board = new String[] {"O", "X", "O", "X", "X", "O", "X", "O", "X"};
		var scoreKeeper = new GameScoreKeeper();

		// act
		var hasWinner = scoreKeeper.hasWinner(board);

		// assert
		assertFalse(hasWinner);
	}
	
	@Test
	public void givenEmptyBoard_hasWinner_returnsFalse() {
		// arrange
		var board = new String[] {"_", "_", "_", "_", "_", "_", "_", "_", "_"};
		var scoreKeeper = new GameScoreKeeper();

		// act
		var hasWinner = scoreKeeper.hasWinner(board);

		// assert
		assertFalse(hasWinner);
	}
}