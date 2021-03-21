package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import ttt.Controllers.TTT_Controller;
import ttt.Model.MoveResponse;
import ttt.Services.*;

public class TTT_ControllerTest{

	private final int _gameId = 1;
	
	@Test
	public void GivenFinalPlayerMove_MoveResponse_IsPlayerWins() {
		// arrange
		String board = "O,X,O,O,X,O,_,_,_";
		TTT_Controller controller = GetControllerForBoard(board);
		
		// act
		MoveResponse response = controller.board(String.valueOf(_gameId), "7", 'X');
		
		// assert
		assertTrue(response.getIsValid());
		assertEquals("X", response.getWinner());
		assertTrue(response.getGameOver());
	}
	
	@Test
	public void GivenFinalOpponentMove_MoveResponse_IsComputerWins() {
		// arrange
		String board = "O,X,X,O,X,O,_,O,_";
		TTT_Controller controller = GetControllerForBoard(board);
		
		// act
		MoveResponse response = controller.board(String.valueOf(_gameId), "8", 'X');
		
		// assert
		assertTrue(response.getIsValid());
		assertEquals("O", response.getWinner());
		assertTrue(response.getGameOver());
	}
	
	@Test
	public void GivenOpenMove_MoveResponse_IsIndecisive() {
		// arrange
		String board = "O,_,_,_,_,_,_,_,_";
		TTT_Controller controller = GetControllerForBoard(board);
		
		// act
		MoveResponse response = controller.board(String.valueOf(_gameId), "3", 'X');
		
		// assert
		assertEquals("", response.getWinner());
		assertFalse(response.getGameOver());
		assertTrue(response.getIsValid());
	}
	
	@Test
	public void GivenInvalidMove_MoveReponse_IsRecognizedAsInvalid() {
		// arrange
		String board = "O,_,_,_,_,_,_,_,_";
		TTT_Controller controller = GetControllerForBoard(board);
				
		// act
		MoveResponse response = controller.board(String.valueOf(_gameId), "19", 'X');
				
		// assert
		assertEquals("", response.getWinner());
		assertFalse(response.getGameOver());
		assertFalse(response.getIsValid());
	}
	
	private TTT_Controller GetControllerForBoard(String board) {
		AmazonDataService dataService = mock(AmazonDataService.class);
		when(dataService.addBoard(Mockito.any(String.class))).thenReturn(_gameId);
		when(dataService.getBoardStatus(_gameId)).thenReturn(board);
		BoardSerializer serializer = new BoardSerializer();
		GameStateFactory gameStateFactory = new GameStateFactory(
				new RandomComputerStrategy(),
				new GameScoreKeeper());
		return new TTT_Controller(
				new MoveRequestValidator(),
				new MoveValidator(),
				new BoardSaver(dataService, serializer),
				new BoardUpdater(dataService, serializer),
				new BoardGetter(dataService, new BoardDeserializer()),
				new BoardFactory(),
				gameStateFactory,
				new MoveResponseFactory());
	}
}