package services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import ttt.Services.BoardFactory;

public class BoardFactoryTest{
	
	@ParameterizedTest
	@ValueSource(ints = {1,BoardFactory.DefaultDimension,4,5,9})
	public void GivenDimensions_CreateGameBoard_YieldsExpectedArray(int dimension) {
		// arrange
		int expectedCellQty = dimension * dimension;
		String[] expectedArray = new String[expectedCellQty];
		BoardFactory boardFactory = new BoardFactory();
		for (int i = 0; i < expectedCellQty; i++)
			expectedArray[i] = BoardFactory.EmptyCellCharacter;
		
		// act
		String[] actualArray = boardFactory.CreateGameBoard(dimension);
		
		// assert
		assertArrayEquals(expectedArray, actualArray);
	}
}