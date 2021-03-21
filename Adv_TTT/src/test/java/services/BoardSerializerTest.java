package services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import ttt.Services.BoardSerializer;

public class BoardSerializerTest{
	@Test
	public void givenBoard_serialize_returnsExpectedString() {
		// arrange
		BoardSerializer boardSerializer = new BoardSerializer();
		String[] boardArray = new String[] {"x", "o", "x", "o","o","o","o","x","x"};
		String expectedString = "x,o,x,o,o,o,o,x,x";
		
		// act
		String actualString = boardSerializer.serializeBoard(boardArray);
		
		// assert
		assertEquals(expectedString, actualString);
	}
}