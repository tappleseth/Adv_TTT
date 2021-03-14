package ttt.Controllers;


import ttt.DTO.*;
import ttt.Services.BoardGetter;
import ttt.Services.BoardSaver;
import ttt.Services.BoardUpdater;
import ttt.Services.MoveRequestValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@CrossOrigin
public class TTT_Controller {

	private final MoveRequestValidator _moveValidator;
	private final BoardSaver _boardSaver;
	private final BoardUpdater _boardUpdater;
	private final BoardGetter _boardGetter;
	
	public TTT_Controller(
			MoveRequestValidator moveValidator,
			BoardSaver boardSaver,
			BoardUpdater boardUpdater,
			BoardGetter boardGetter) {
	    _moveValidator = moveValidator;
	    _boardSaver = boardSaver;
	    _boardUpdater = boardUpdater;
	    _boardGetter = boardGetter;
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
    public BoardResponse board(@RequestParam(name="boardLength", defaultValue="3") String boardLength,
                             @RequestParam(name="humanGoesFirst",defaultValue="true") String humanGoesFirst) {
        var boardDimension = Integer.parseInt(boardLength);
        var playerGoesFirst = Boolean.parseBoolean(humanGoesFirst.toLowerCase());
        var newBoard = makeBoard(boardDimension);
        var newBoardId = _boardSaver.saveBoardToDataService(newBoard, "X");
		return new BoardResponse(
				newBoardId,
				boardDimension,
				playerGoesFirst,
				newBoard);
    }
	
	@CrossOrigin
	@PutMapping("/board")
	public MoveResponse board(@RequestParam(name = "boardID", defaultValue="-1") String boardID,
			@RequestParam(name="tileNumber", defaultValue="-1") String tileNumber,
		@RequestParam(name="avatar",defaultValue="_") Character avatar){
		int bId = -1;
		int nextTile = -1;
		boolean isValid = _moveValidator.isValidMove(boardID, tileNumber);
		if (isValid) {
			bId = Integer.parseInt(boardID);
			nextTile = Integer.parseInt(tileNumber);
		}
		
		String[] oldBoard = _boardGetter.getBoardFromDataService(bId);
		if (oldBoard == null){
			isValid = false;
		}
		
		
		avatar = Character.toUpperCase(avatar);
		
		if (isValid) isValid = tryUpdateBoard(oldBoard, nextTile, avatar.toString());
		
		if (isValid){
		GameOutcome state = CheckGameStatus(oldBoard, nextTile,avatar.toString());
		
		boolean gameOver = false;
		String winnerChar = "_";
		int opponentTile = -1;
		
		if (state == GameOutcome.EveryoneLoses){
			gameOver = true;
		}
		else if (state == GameOutcome.SomebodyWins){
			gameOver = true;
			winnerChar = avatar.toString();
		}
		else {
			opponentTile = getFoeMove(oldBoard, "O");
			GameOutcome gs2 = CheckGameStatus(oldBoard, opponentTile,"O");
			if (gs2 != GameOutcome.Null){
				winnerChar = "O";
				gameOver = true;
			}
		}
			_boardUpdater.updateBoard(bId,oldBoard);
			return new MoveResponse(opponentTile,"O", isValid,oldBoard, gameOver, winnerChar);
		}
		return new MoveResponse(-1,"_", false,oldBoard, false, "_");
	}
	
	private int getFoeMove(String[] board, String avatar){
		ArrayList<Integer> remainingMoves = new ArrayList<Integer>();
		for(int i = 0; i < board.length; i++){
			if (board[i].equals("_")) remainingMoves.add(i);
		}
		
		Random rand = new Random();
		int foeMove = remainingMoves.get(rand.nextInt(remainingMoves.size()));
		tryUpdateBoard(board, foeMove,avatar);
		return foeMove;
	}
	
	

    private String[] makeBoard(int boardLength){
        String[] board = new String[boardLength*boardLength];
        for (int i = 0; i < boardLength*boardLength; i++){
            board[i] = "_";
        }
        return board;
    }
	
	private boolean tryUpdateBoard(String[] board, int tile, String avatar){
		if (tile < 0 || tile >= board.length) return false;
		board[tile] = avatar;
		return true;
	}
	
	public class GameState {
		public int[] rowScore = null;
		public int[] colScore = null;
		public int diag1 = 0;
		public int diag2 = 0;
		public int remainingMoves = 0;
		
		public GameState(String[] board) {
			
			remainingMoves = board.length;
			int bl = (int)Math.sqrt(board.length);
			rowScore = new int[bl];
			colScore = new int[bl];
			for (int tile = 0; tile < board.length; tile++) {
				updateScore(board,tile,bl);
			}
		}
		
		private void updateScore(String[] board, int tile, int bl){
			
			if (board[tile].equals("_")) return;
			
			//resolve tile to row/col
			
			//for example, tile 4
			int col = tile / bl;
			int row = tile % bl;
			
			int pt = board[tile].equals("X") ? 1 : -1;
			
			rowScore[row] += pt;
			colScore[col] += pt;
			
			if (row == col || (row + col == bl-1)){
				if ((row <= bl/2 && col <= bl/2)||
				(row >= bl/2 && col >= bl/2)){
					diag1 += pt;
				}
				if ((row >= bl/2 && col <= bl/2) || (row <= bl/2 && col >= bl/2)){
					diag2 += pt;
				}
			}
		
			remainingMoves--;
		}
	}
	
	
	
	private GameOutcome CheckGameStatus(String[] board, int tile, String avatar){
		
		int bl = (int)Math.sqrt(board.length);
		int col = tile / bl;
		int row = tile % bl;
		
		GameState gs = new GameState(board);
		
		int pt = avatar.equals("X") ? 1 : -1;
		
		int target = bl*pt;
		
		//if winner, reset x5 score members
		if (gs.rowScore[row] == target || gs.colScore[col] == target || gs.diag1 == target || gs.diag2 == target){
			return GameOutcome.SomebodyWins;
		}
		if (gs.remainingMoves <= 0) return GameOutcome.EveryoneLoses;
		return GameOutcome.Null;
	}


	public enum GameOutcome {
		Null,SomebodyWins,EveryoneLoses;
	}
}