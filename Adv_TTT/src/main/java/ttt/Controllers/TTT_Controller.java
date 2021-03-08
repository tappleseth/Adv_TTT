package ttt.Controllers;


import com.amazonaws.regions.Regions;
import ttt.DTO.*;
import ttt.Services.MoveRequestValidator;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.*;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.AtomicLong;

import java.util.*;


@RestController
@CrossOrigin
public class TTT_Controller {
	
	AmazonDynamoDB client = null;
	DynamoDB dynamoDB = null;
	Table boardTable = null;
	Table configTable = null;
	
	MoveRequestValidator moveValidator;
	
	public TTT_Controller(MoveRequestValidator moveValidator) {
		client = AmazonDynamoDBClientBuilder.standard()
    			.withRegion(Regions.US_EAST_1).build();
	    dynamoDB = new DynamoDB(client);
	    boardTable = dynamoDB.getTable("CheapTTT");
	    configTable = dynamoDB.getTable("Records");
	    this.moveValidator = moveValidator;
	}
	
	
	private String[] getBoardFromDB(int boardID) {

    	GetItemSpec spec = new GetItemSpec().withPrimaryKey("BoardID",boardID);
    
    	Item test = null;
    	try {
    		System.out.println("Attempting to read god damned item GameID:" + boardID);
    		test = boardTable.getItem(spec);
    		System.out.println("Got item with board val: " + test.getString("BoardStatus"));
    	}
    	catch (Exception ex) {
    		System.out.println("Failed with exception " + ex.getMessage());
    		//SPRING back to your feet and return new board instead
			return makeBoard(3);
    	}
    	
    	if (test == null) return makeBoard(3);
    	
    	String[] savedBoard = makeBoard(test.getString("BoardStatus"));
    	return savedBoard;
	}
	
	@CrossOrigin
	@GetMapping("/board")
	public BoardResponse board(@RequestParam(name="boardID",defaultValue="-1") String boardID) {
		int bId = -1;
		try {
			bId = Integer.parseInt(boardID);
		}
		catch (Exception ex) {}
		
		if (bId == -1) {
			//TODO log
			//SPRING back to your feet and return new board instead
			return board("3","true");
		}
		
		String[] savedBoard = getBoardFromDB(bId);
		
    	return new BoardResponse(bId,(int)Math.sqrt(savedBoard.length),true,savedBoard);
	}
	
	private String[] makeBoard(String deserializeMe) {
		return deserializeMe.split(",");
	}
	
	private String serializeBoard(String[] serializeMe) {
		StringBuilder sb = new StringBuilder();
		for (String s : serializeMe) {
			sb.append(s + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	private void updateBoard(int boardID, String[] board) {
		
		String bs = serializeBoard(board);
    	UpdateItemSpec uSpec = new UpdateItemSpec().withPrimaryKey("BoardID",boardID)
    			.withUpdateExpression("set BoardStatus = :a")
    			.withValueMap(new ValueMap().withString(":a",bs));
    	boardTable.updateItem(uSpec);
	}
	
	private int writeBoard(String[] board, String avatar) {
		
		String boardStr = serializeBoard(board);
		int bID = -1;
		try {
	    	GetItemSpec spec = new GetItemSpec().withPrimaryKey("ConfigID",1);
	    	Item test = configTable.getItem(spec);
	    	int oldID = test.getInt("GamesPlayed");
	    	
	    	//increment
	    	bID = oldID + 1;
	    	
	    	//update count of game played
	    	UpdateItemSpec uSpec = new UpdateItemSpec().withPrimaryKey("ConfigID",1)
	    			.withUpdateExpression("set GamesPlayed = :a")
	    			.withValueMap(new ValueMap().withNumber(":a",bID));
	    	configTable.updateItem(uSpec);
	    	
	    	//save board
	    	boardTable.putItem(new Item().withPrimaryKey("BoardID",bID)
	    			.withString("BoardStatus", boardStr)
	    			.withBoolean("GameOver",false)
	    			.withString("PlayerAvatar","X")
	    			.withString("Winner","_"));
	    	
		} catch (Exception ex) {
			System.out.println("Exception in writeBoard: " + ex.getMessage());
			return -1;
		}
		return bID;
	}
	
	@CrossOrigin
    @PostMapping("/board")
    public BoardResponse board(@RequestParam(name="boardLength", defaultValue="3") String boardLength,
                             @RequestParam(name="humanGoesFirst",defaultValue="true") String humanGoesFirst) {


        int bl =  Integer.parseInt(boardLength);
        boolean hf = Boolean.parseBoolean(humanGoesFirst.toLowerCase());
        String[] newBoard = makeBoard(bl);
        
        int boardID = writeBoard(newBoard,"X");

		return new BoardResponse(boardID,bl,hf,newBoard);
    }
	
	@CrossOrigin
	@PutMapping("/board")
	public MoveResponse board(@RequestParam(name = "boardID", defaultValue="-1") String boardID,
			@RequestParam(name="tileNumber", defaultValue="-1") String tileNumber,
		@RequestParam(name="avatar",defaultValue="_") Character avatar){
		int bId = -1;
		int nextTile = -1;
		boolean isValid = moveValidator.isValidMove(boardID, tileNumber);
		if (isValid) {
			bId = Integer.parseInt(boardID);
			nextTile = Integer.parseInt(tileNumber);
		}
		
		String[] oldBoard = getBoardFromDB(bId);
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
			updateBoard(bId,oldBoard);
			return new MoveResponse(opponentTile,"O", isValid,oldBoard, gameOver, winnerChar);
		}
		return new MoveResponse(-1,"_", false,oldBoard, false, "_");
	}
	
	private int getFoeMove(String[] board, String avatar){
		int bl = (int)Math.sqrt(board.length);
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