package ttt.Services;

import org.springframework.stereotype.Service;

@Service
public class BoardFactory {
	
	public static final int DefaultDimension = 3;
	public static final String EmptyCellCharacter = "_";
	
	public String[] CreateGameBoard() {
		return CreateGameBoard(DefaultDimension);
	}
	
	public String[] CreateGameBoard(int dimension) {
		int gameCells = dimension * dimension;
		String[] gameBoard = new String[gameCells];
        for (int i = 0; i < gameCells; i++){
        	gameBoard[i] = EmptyCellCharacter;
        }
        return gameBoard;
	}
}