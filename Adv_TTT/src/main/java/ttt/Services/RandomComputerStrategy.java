package ttt.Services;

import java.util.ArrayList;
import java.util.Random;

public class RandomComputerStrategy implements ComputerStrategy{
	@Override
	public int getNextTileNumber(String[] board) {
		var remainingMoves = new ArrayList<Integer>();
		for(int i = 0; i < board.length; i++){
			if (board[i].equals("_")) remainingMoves.add(i);
		}
		var numberGenerator = new Random();
		var nextTileNumber = remainingMoves.get(numberGenerator.nextInt(remainingMoves.size()));
		return nextTileNumber;
	}
}