package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import ttt.Controllers.TTT_Controller;
import ttt.Services.*;

public class TTT_ControllerTest{

	private final int _gameId = 1;
	
	@Test
	public void GivenFinalPlayerMove_MoveResponse_IsPlayerWins() {
		// arrange
		var board = "O,X,O,O,X,O,_,_,_";
		var controller = GetControllerForBoard(board);
		
		// act
		var response = controller.board(String.valueOf(_gameId), "7", 'X');
		
		// assert
		assertEquals("X", response.getWinner());
		assertTrue(response.getGameOver());
		assertTrue(response.getIsValid());
	}
	
	@Test
	public void GivenFinalOpponentMove_MoveResponse_IsComputerWins() {
		// arrange
		var board = "O,X,X,O,X,O,_,O,_";
		var controller = GetControllerForBoard(board);
		
		// act
		var response = controller.board(String.valueOf(_gameId), "8", 'X');
		
		// assert
		assertEquals("O", response.getWinner());
		assertTrue(response.getGameOver());
		assertTrue(response.getIsValid());
	}
	
	@Test
	public void GivenOpenMove_MoveResponse_IsIndecisive() {
		// arrange
		var board = "O,_,_,_,_,_,_,_,_";
		var controller = GetControllerForBoard(board);
		
		// act
		var response = controller.board(String.valueOf(_gameId), "3", 'X');
		
		// assert
		assertEquals("_", response.getWinner());
		assertFalse(response.getGameOver());
		assertTrue(response.getIsValid());
	}
	
	@Test
	public void GivenInvalidMove_MoveReponse_IsRecognizedAsInvalid() {
		// arrange
		var board = "O,_,_,_,_,_,_,_,_";
		var controller = GetControllerForBoard(board);
				
		// act
		var response = controller.board(String.valueOf(_gameId), "19", 'X');
				
		// assert
		assertEquals("_", response.getWinner());
		assertFalse(response.getGameOver());
		assertFalse(response.getIsValid());
	}
	
	private TTT_Controller GetControllerForBoard(String board) {
		var dataService = mock(AmazonDataService.class);
		when(dataService.addBoard(Mockito.any(String.class))).thenReturn(_gameId);
		when(dataService.getBoardStatus(_gameId)).thenReturn(board);
		var serializer = new BoardSerializer();
		return new TTT_Controller(
				new MoveRequestValidator(),
				new BoardSaver(dataService, serializer),
				new BoardUpdater(dataService, serializer),
				new BoardGetter(dataService, new BoardDeserializer()),
				new BoardFactory());
	}
}