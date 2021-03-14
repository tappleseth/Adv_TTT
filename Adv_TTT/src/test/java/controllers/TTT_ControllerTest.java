package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import ttt.Controllers.TTT_Controller;
import ttt.DTO.MoveResponse;
import ttt.Services.MoveRequestValidator;

public class TTT_ControllerTest{
	
	@Mock
	Table _boardTable;
	
	@Mock
	static Item _item;
	
	@BeforeAll
	public static void CreateMocks() {
		_item = mock(Item.class);
	}
	
	@Test
	public void MoveResponse_ValidTileNumber_IsGameOver() {
		// arrange
		String board = "O,X,O,O,X,O,_,_,_";
		TTT_Controller controller = GetControllerForBoard(board);
		
		// act
		MoveResponse response = controller.board("1", "7", 'X');
		
		// assert
		assertEquals("X", response.getWinner());
	}
	
	private TTT_Controller GetControllerForBoard(String board) {
		doReturn(board).when(_item).getString("BoardStatus");
		when(_boardTable.getItem(Mockito.any(GetItemSpec.class))).thenReturn(_item);
		return new TTT_Controller(new MoveRequestValidator());
	}
}