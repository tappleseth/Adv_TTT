package services;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import ttt.Services.MoveRequestValidator;

public class MoveRequestValidatorTest{
	
	@Test
	public void GivenInvalidInputs_IsValidMove_ReturnsFalse() {
		AssertValidation("notAnInt", "notAnInt", false);
	}
	
	@Test
	public void GivenInvalidBoardId_IsValidMove_ReturnsFalse() {
		AssertValidation("notAnInt", "2", false);
	}
	
	@Test
	public void GivenInvalidTileId_IsValidMove_ReturnsFalse() {
		AssertValidation("10", "5.1", false);
	}
	
	@Test
	public void GivenValidInputs_IsValidMove_ReturnsTrue() {
		AssertValidation("1", "2", true);
	}
	
	private void AssertValidation(String boardId, String tileId, Boolean expectedReturn) {
		// arrange
		MoveRequestValidator validator = new MoveRequestValidator();
		
		// act
		Boolean actualReturn = validator.isValidMove(boardId, tileId);
		
		// assert
		Assert.assertEquals(expectedReturn, actualReturn);
	}
}