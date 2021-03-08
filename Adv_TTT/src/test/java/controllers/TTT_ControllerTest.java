package controllers;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.amazonaws.services.dynamodbv2.document.Table;

public class TTT_ControllerTest{
	
	@Mock
	Table _boardTable;
	@Mock
	Table _configTable;
	
	@Test
	public void MoveResponse_InvalidBoardId() {
	}
	
	@Test
	public void MoveResponse_InvalidTileNumber() {
		
	}
	
	@Test
	public void MoveResponse_ValidTileNumber_IsGameOver() {
		
	}
	
	@Test
	public void MoveResponse_ValidTileNumber_NotGameOver() {
		
	}
}