package services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ttt.Model.GameState;
import ttt.Model.MoveResponse;
import ttt.Services.MoveResponseFactory;

public class MoveResponseFactoryTest{
	@ParameterizedTest
	@ValueSource(strings = {"example1", "example2"})
	public void givenErrorMessage_fromError_yieldsResponse(String errorMessage) {
		// arrange
		MoveResponseFactory factory = new MoveResponseFactory();
		
		// act
		MoveResponse response = factory.fromError(errorMessage);
		
		// assert
		assertFalse(response.getIsValid());
		assertEquals(errorMessage, response.getMessage());
	}
	
	@Test
	public void givenGameState_fromGateState_yieldsResponse() {
		// arrange
		MoveResponseFactory factory = new MoveResponseFactory();
		int lastTileNumber = 7;
		String lastAvatar = "X";
		String[] board = new String[] {"O","X","O","X","X","O","O","X","X"};
		GameState gameState = new GameState(7, lastAvatar, true, lastAvatar, board);
				
		// act
		MoveResponse response = factory.fromGameState(gameState);
		
		// assert
		assertTrue(response.getIsValid());
		assertTrue(response.getGameOver());
		assertEquals(lastAvatar, response.getWinner());
		assertEquals(lastTileNumber, response.getTileNumber());
		assertEquals(lastAvatar, response.getAvatar());
		assertArrayEquals(board, response.getTiles());
		assertEquals("", response.getMessage());
	}
}