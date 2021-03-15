package services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ttt.Services.MoveValidator;

public class MoveValidatorTest{
	@ParameterizedTest
	@ValueSource(ints = {-1, 9})
    public void givenOutOfBoundsTileNumber_isValidMove_returnsFalse(int tileIndex) {
    	// arrange
		var board = new String[9];
		var validator = new MoveValidator();
		
		// act
		var isValidMove = validator.isValidMove(board, tileIndex);
		
		// assert
		assertFalse(isValidMove);
    }
	
	@ParameterizedTest
	@ValueSource(ints = {3, 2, 8})
	public void givenValidTileNumber_isValidMove_returnsTrue(int tileIndex) {
		// arrange
		var board = createTestBoard();
		var validator = new MoveValidator();
		
		// act
		var isValidMove = validator.isValidMove(board, tileIndex);
		
		// assert
		assertTrue(isValidMove);
	}
	
	@Test
	public void givenOccupiedTileNumber_isValidMove_returnsFalse() {
		// arrange
		var board = createTestBoard();
		var validator = new MoveValidator();
		var occupiedIndex = 0;
		
		// act
		var isValidMove = validator.isValidMove(board, occupiedIndex);
		
		// assert
		assertFalse(isValidMove);
	}
	
	private String[] createTestBoard() {
		return new String[] {"X","_","_","_","_","_","_","_","_"};
	}
}