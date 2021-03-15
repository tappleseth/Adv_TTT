package ttt.Services;

import org.springframework.stereotype.Service;

import ttt.Model.GameState;

@Service
public class GameStateFactory{
	
	public static final String DefaultComputerAvatar = "O";
	
	private final ComputerStrategy _computerStrategy;
	private final GameScoreKeeper _scoreKeeper;
	
	public GameStateFactory(
			ComputerStrategy computerStrategy,
			GameScoreKeeper scoreKeeper) {
		_computerStrategy = computerStrategy;
		_scoreKeeper = scoreKeeper;
	}
	
	public GameState getNextGameState(String[] gameBoard, int nextTile, String currentPlayer) {
		gameBoard[nextTile] = currentPlayer;
		var gameOver = noMovesLeft(gameBoard) || _scoreKeeper.hasWinner(gameBoard);
		var lastAvatar = currentPlayer;
		var lastTile = nextTile;
		if (!gameOver) {
			var computerTileMove = _computerStrategy.getNextTileNumber(gameBoard);
			gameBoard[computerTileMove] = DefaultComputerAvatar;
			lastAvatar = DefaultComputerAvatar;
			lastTile = computerTileMove;
		}
		var winningAvatar = "";
		var hasWinner = _scoreKeeper.hasWinner(gameBoard);
		gameOver = noMovesLeft(gameBoard) || hasWinner;
		if (hasWinner)
			winningAvatar = lastAvatar;
		return new GameState(lastTile, lastAvatar, gameOver, winningAvatar, gameBoard);
	}
	
	private boolean noMovesLeft(String[] gameBoard) {
		for (var i = 0; i < gameBoard.length; i++) {
			if (gameBoard[i].equals(BoardFactory.EmptyCellCharacter))
				return false;
		}
		return true;
	}
}