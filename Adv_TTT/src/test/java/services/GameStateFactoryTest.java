package services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ttt.Model.GameState;
import ttt.Services.DebugComputerStrategy;
import ttt.Services.GameScoreKeeper;
import ttt.Services.GameStateFactory;
import ttt.Services.RandomComputerStrategy;

public class GameStateFactoryTest{
	@Test
	public void givenCertainPlayerWin_createGameState_yieldsPlayerWin() {
		// arrange
		String[] board = new String[] {"_"};
		GameStateFactory factory = new GameStateFactory(new RandomComputerStrategy(), new GameScoreKeeper());
		String playerAvatar = "X";
		String[] expectedBoard = new String[] {playerAvatar};
		
		// act
		GameState gameState = factory.getNextGameState(board, 0, playerAvatar);
		
		// assert
		assertTrue(gameState.getIsGameOver());
		assertEquals(playerAvatar, gameState.getLastPlayedAvatar());
		assertEquals(0, gameState.getLastPlayedTileNumber());
		assertArrayEquals(expectedBoard, gameState.getCurrentBoard());
	}
	
	@Test
	public void givenPlayerWinAndComputerWinsOnNextMove_createGameState_yieldsPlayerWin() {
		// arrange
		String[] board = new String[] {"O","X","O","O","X","O","_","_","_"};
		GameStateFactory factory = new GameStateFactory(new RandomComputerStrategy(), new GameScoreKeeper());
		String playerAvatar = "X";
		int playerMove = 7;
		String[] expectedBoard = new String[] {"O","X","O","O","X","O","_","X","_"};

		// act
		GameState gameState = factory.getNextGameState(board, playerMove, playerAvatar);

		// assert
		assertTrue(gameState.getIsGameOver());
		assertEquals(playerAvatar, gameState.getLastPlayedAvatar());
		assertEquals(playerMove, gameState.getLastPlayedTileNumber());
		assertArrayEquals(expectedBoard, gameState.getCurrentBoard());
	}
	
	@Test
	public void givenCertainComputerWin_createGameState_yieldsComputerWin() {
		// arrange
		String[] board = new String[] {"O","X","O","X","X","O","_","_","X"};
		GameStateFactory factory = new GameStateFactory(new RandomComputerStrategy(), new GameScoreKeeper());
		String playerAvatar = "X";
		String[] expectedBoard = new String[] {"O","X","O","X","X","O","X","O","X"};
		
		// act
		GameState gameState = factory.getNextGameState(board, 6, playerAvatar);
        
        // assert
        assertTrue(gameState.getIsGameOver());
        assertEquals("O", gameState.getLastPlayedAvatar());
        assertEquals(7, gameState.getLastPlayedTileNumber());
        assertArrayEquals(expectedBoard, gameState.getCurrentBoard());	
	}
	
	@Test
	public void givenCertainStalemate_createGameState_yieldsStalemate() {
		// arrange
		String[] board = new String[] {"O","X","O","X","X","O","_","_","X"};
		GameStateFactory factory = new GameStateFactory(new RandomComputerStrategy(), new GameScoreKeeper());
		String playerAvatar = "X";
		String[] expectedBoard = new String[] {"O","X","O","X","X","O","X","O","X"};
		int playerTileNumber = 6;
		int expectedComputerTileNumber = 7;

		// act
		GameState gameState = factory.getNextGameState(board, playerTileNumber, playerAvatar);

		// assert
		assertTrue(gameState.getIsGameOver());
		assertEquals("O", gameState.getLastPlayedAvatar());
		assertEquals(expectedComputerTileNumber, gameState.getLastPlayedTileNumber());
		assertArrayEquals(expectedBoard, gameState.getCurrentBoard());	
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2, 3, 5, 6, 7, 8})
	public void givenOpenMatch_createGameState_yieldsExpectedOpenState(int computerTileMove) {
		// arrange
		String[] board = getEmptyBoard();
		DebugComputerStrategy computerStrategy = new DebugComputerStrategy();
		computerStrategy.setNextTileNumber(computerTileMove);
		GameStateFactory factory = new GameStateFactory(computerStrategy, new GameScoreKeeper());
		String playerAvatar = "X";
		String computerAvatar = "O";
		int playerTileMove = 4;
		String[] expectedBoard = getEmptyBoard();
		expectedBoard[computerTileMove] = computerAvatar;
		expectedBoard[playerTileMove] = playerAvatar;
		
		// act
		GameState gameState = factory.getNextGameState(board, playerTileMove, playerAvatar);
		
		// assert
		assertFalse(gameState.getIsGameOver());
		assertEquals(computerAvatar, gameState.getLastPlayedAvatar());
		assertEquals(computerTileMove, gameState.getLastPlayedTileNumber());
		assertArrayEquals(expectedBoard, gameState.getCurrentBoard());	
	}
	
	private String[] getEmptyBoard() {
		return new String[] {"_","_","_","_","_","_", "_", "_", "_"};
	}
}