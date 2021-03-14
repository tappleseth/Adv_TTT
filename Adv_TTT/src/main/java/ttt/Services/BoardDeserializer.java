package ttt.Services;

import org.springframework.stereotype.Service;

@Service
public class BoardDeserializer{
	public String[] deserializeBoard(String boardString) {
		return boardString.split(",");
	}
}