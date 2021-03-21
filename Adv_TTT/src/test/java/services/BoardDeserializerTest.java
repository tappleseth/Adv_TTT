package services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import ttt.Services.BoardDeserializer;

public class BoardDeserializerTest{
	@Test
	public void givenBoardString_deserialize_returnsArray() {
		// arrange
		String boardString = "X,O,O,_,X,_,_,_,X";
		BoardDeserializer deserializer = new BoardDeserializer();
		String[] expectedBoardArray = new String[] {"X","O","O","_","X","_","_","_","X"};
		
		// act
		String[] boardArray = deserializer.deserializeBoard(boardString);
		
		// assert
		assertArrayEquals(expectedBoardArray, boardArray);
	}
}