package ttt.Services;

import org.springframework.stereotype.Service;

@Service
public class BoardGetter{
	
	private final AmazonDataService _dataService;
	private final BoardDeserializer _deserializer;
	
	public BoardGetter(AmazonDataService dataService, BoardDeserializer deserializer) {
		_dataService = dataService;
		_deserializer = deserializer;
	}
	
	public String[] getBoardFromDataService(int boardId) {
		String boardString = _dataService.getBoardStatus(boardId);
    	String[] board = _deserializer.deserializeBoard(boardString);
    	return board;
	}
}