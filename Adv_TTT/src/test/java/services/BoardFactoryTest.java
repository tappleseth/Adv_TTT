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
		var expectedCellQty = dimension * dimension;
		var expectedArray = new String[expectedCellQty];
		var boardFactory = new BoardFactory();
		for (var i = 0; i < expectedCellQty; i++)
			expectedArray[i] = BoardFactory.EmptyCellCharacter;
		
		// act
		var actualArray = boardFactory.CreateGameBoard(dimension);
		
		// assert
		assertArrayEquals(expectedArray, actualArray);
	}
}