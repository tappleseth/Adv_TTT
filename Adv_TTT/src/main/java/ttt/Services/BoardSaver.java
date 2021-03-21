package ttt.Services;

import org.springframework.stereotype.Service;

@Service
public class BoardSaver
{
	private final BoardSerializer _boardSerializer;
	private final AmazonDataService _dataService;
	
	public BoardSaver(AmazonDataService dataService, BoardSerializer boardSerializer) {
		_boardSerializer = boardSerializer;
		_dataService = dataService;
	}
	
	public int saveBoardToDataService(String[] board, String avatar) {
		String boardStr = _boardSerializer.serializeBoard(board);
	    int nextBoardId = _dataService.addBoard(boardStr);
	    return nextBoardId;
	}
}