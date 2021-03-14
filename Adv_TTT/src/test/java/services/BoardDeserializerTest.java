package services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import ttt.Services.BoardDeserializer;

public class BoardDeserializerTest{
	@Test
	public void givenBoardString_deserialize_returnsArray() {
		// arrange
		var boardString = "X,O,O,_,X,_,_,_,X";
		var deserializer = new BoardDeserializer();
		var expectedBoardArray = new String[] {"X","O","O","_","X","_","_","_","X"};
		
		// act
		var boardArray = deserializer.deserializeBoard(boardString);
		
		// assert
		assertArrayEquals(expectedBoardArray, boardArray);
	}
}