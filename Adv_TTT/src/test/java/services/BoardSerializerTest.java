package services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import ttt.Services.BoardSerializer;

public class BoardSerializerTest{
	@Test
	public void givenBoard_serialize_returnsExpectedString() {
		// arrange
		var boardSerializer = new BoardSerializer();
		var boardArray = new String[] {"x", "o", "x", "o","o","o","o","x","x"};
		var expectedString = "x,o,x,o,o,o,o,x,x";
		
		// act
		var actualString = boardSerializer.serializeBoard(boardArray);
		
		// assert
		assertEquals(expectedString, actualString);
	}
}