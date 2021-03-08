package ttt.Services;

import org.springframework.stereotype.Service;

@Service
public class MoveRequestValidator{
	
	public boolean isValidMove(String boardId, String tileId) {
		try {
			Integer.parseInt(boardId);
			Integer.parseInt(tileId);
			return true;
		}
		catch (NumberFormatException ex) {
			return false;
		}
	}
}