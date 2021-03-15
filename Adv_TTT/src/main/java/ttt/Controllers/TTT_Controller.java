package ttt.Controllers;

import ttt.Model.*;
import ttt.Services.BoardFactory;
import ttt.Services.BoardGetter;
import ttt.Services.BoardSaver;
import ttt.Services.BoardUpdater;
import ttt.Services.GameStateFactory;
import ttt.Services.MoveRequestValidator;
import ttt.Services.MoveResponseFactory;
import ttt.Services.MoveValidator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class TTT_Controller {

	private final MoveRequestValidator _moveRequestValidator;
	private final MoveValidator _moveValidator;
	private final BoardSaver _boardSaver;
	private final BoardUpdater _boardUpdater;
	private final BoardGetter _boardGetter;
	private final BoardFactory _boardFactory;
	private final GameStateFactory _gameStateFactory;
	private final MoveResponseFactory _moveResponseFactory;
	
	public TTT_Controller(
			MoveRequestValidator moveRequestValidator,
			MoveValidator moveValidator,
			BoardSaver boardSaver,
			BoardUpdater boardUpdater,
			BoardGetter boardGetter,
			BoardFactory boardFactory,
			GameStateFactory gameStateFactory,
			MoveResponseFactory moveResponseFactory) {
	    _moveRequestValidator = moveRequestValidator;
	    _moveValidator = moveValidator;
	    _boardSaver = boardSaver;
	    _boardUpdater = boardUpdater;
	    _boardGetter = boardGetter;
	    _boardFactory = boardFactory;
	    _gameStateFactory = gameStateFactory;
	    _moveResponseFactory = moveResponseFactory;
	}
	
	@CrossOrigin
	@GetMapping("/board")
	public BoardResponse board(@RequestParam(name="boardID",defaultValue="-1") String boardID) {
		var boardId = Integer.parseInt(boardID);
		var currentBoard = _boardGetter.getBoardFromDataService(boardId);
		var humanGoesFirst = true;
    	return new BoardResponse(
    			boardId,
    			(int)Math.sqrt(currentBoard.length),
    			humanGoesFirst,
    			currentBoard);
	}
	
	@CrossOrigin
    @PostMapping("/board")
    public BoardResponse board(
    		@RequestParam(name="boardLength", defaultValue="3") String boardLength,
    		@RequestParam(name="humanGoesFirst",defaultValue="true") String humanGoesFirst) {
        var boardDimension = Integer.parseInt(boardLength);
        var playerGoesFirst = Boolean.parseBoolean(humanGoesFirst.toLowerCase());
        var newBoard = _boardFactory.CreateGameBoard(boardDimension);
        var newBoardId = _boardSaver.saveBoardToDataService(newBoard, "X");
		return new BoardResponse(
				newBoardId,
				boardDimension,
				playerGoesFirst,
				newBoard);
    }
	
	@CrossOrigin
	@PutMapping("/board")
	public MoveResponse board(
			@RequestParam(name = "boardID", defaultValue="-1") String boardID,
			@RequestParam(name="tileNumber", defaultValue="-1") String tileNumber,
			@RequestParam(name="avatar",defaultValue="_") Character avatar){
		var isValid = _moveRequestValidator.isValidRequest(boardID, tileNumber);
		if (!isValid)
			return _moveResponseFactory.fromError("Invalid request format");
		var boardId = Integer.parseInt(boardID);
		var playerTileNumber = Integer.parseInt(tileNumber);
		var oldBoard = _boardGetter.getBoardFromDataService(boardId);
		if (oldBoard == null)
			return _moveResponseFactory.fromError("Board not found on server");
		if (!_moveValidator.isValidMove(oldBoard, playerTileNumber))
			return _moveResponseFactory.fromError("Missing or occupied tile");
		var playerAvatar = Character.toString(Character.toUpperCase(avatar));
		var gameState = _gameStateFactory.getNextGameState(oldBoard, playerTileNumber, playerAvatar);
		_boardUpdater.updateBoard(boardId, gameState.getCurrentBoard());
		return _moveResponseFactory.fromGameState(gameState);
	}
}