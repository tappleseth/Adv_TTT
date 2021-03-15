package ttt.Services;

import org.springframework.stereotype.Service;

@Service
public class MoveValidator {
	public boolean isValidMove(String[] board, int tileIndex) {
		return tileIndex >= 0 && tileIndex < board.length && board[tileIndex].equals(BoardFactory.EmptyCellCharacter);
	}
}