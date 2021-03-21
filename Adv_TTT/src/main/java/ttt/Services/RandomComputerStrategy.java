package ttt.Services;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class RandomComputerStrategy implements ComputerStrategy{
	@Override
	public int getNextTileNumber(String[] board) {
		ArrayList<Integer> remainingMoves = new ArrayList<Integer>();
		for(int i = 0; i < board.length; i++){
			if (board[i].equals("_")) remainingMoves.add(i);
		}
		Random numberGenerator = new Random();
		int nextTileNumber = remainingMoves.get(numberGenerator.nextInt(remainingMoves.size()));
		return nextTileNumber;
	}
}