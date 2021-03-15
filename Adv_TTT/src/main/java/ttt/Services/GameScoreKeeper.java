package ttt.Services;

import org.springframework.stereotype.Service;

@Service
public class GameScoreKeeper {
	
	private class GameScore{
		public int[] rowScore;
		public int[] colScore;
		public int diag1;
		public int diag2;
		
		private final int _boardDimension;
		
		public GameScore(int boardDimension) {
			_boardDimension = boardDimension;
			rowScore = new int[boardDimension];
			colScore = new int[boardDimension];
			diag1 = 0;
			diag2 = 0;
		}
		
		public boolean isGameWon() {
			if (Math.abs(diag1) == _boardDimension || Math.abs(diag2) == _boardDimension)
				return true;
			for (var i = 0; i < _boardDimension; i++)
			{
				if (Math.abs(rowScore[i]) == _boardDimension || Math.abs(colScore[i]) == _boardDimension)
					return true;
			}
			return false;
		}
	}
	
	public boolean hasWinner(String[] board) {
		var boardDimension = (int)Math.sqrt(board.length);
		var gameScore = new GameScore(boardDimension);
		for (int tile = 0; tile < board.length; tile++)
			updateScore(board, gameScore, tile, boardDimension);
		return gameScore.isGameWon();
	}
	
	private void updateScore(
			String[] board,
			GameScore gameScore,
			int tile,
			int boardDimension){
		if (board[tile].equals(BoardFactory.EmptyCellCharacter))
			return;
		int col = tile / boardDimension;
		int row = tile % boardDimension;
		int pt = board[tile].equals("X") ? 1 : -1;
		gameScore.rowScore[row] += pt;
		gameScore.colScore[col] += pt;
		if (row == col || (row + col == boardDimension-1)){
			if ((row <= boardDimension/2 && col <= boardDimension/2)||
			(row >= boardDimension/2 && col >= boardDimension/2)){
				gameScore.diag1 += pt;
			}
			if ((row >= boardDimension/2 && col <= boardDimension/2) || (row <= boardDimension/2 && col >= boardDimension/2)){
				gameScore.diag2 += pt;
			}
		}
	}
}