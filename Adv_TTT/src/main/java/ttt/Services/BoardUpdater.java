package ttt.Services;

import org.springframework.stereotype.Service;

@Service
public class BoardUpdater{
	private final BoardSerializer _boardSerializer;
	private final AmazonDataService _dataService;
	
	public BoardUpdater(AmazonDataService dataService, BoardSerializer boardSerializer) {
		_boardSerializer = boardSerializer;
		_dataService = dataService;
	}
	
	public void updateBoard(int boardId, String[] gameBoard) {
		String boardString = _boardSerializer.serializeBoard(gameBoard);
		_dataService.updateBoard(boardId, boardString);
	}
}