package ttt.Services;

import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

@Service
public class AmazonDataService{
	
	private final Table _configTable;
	private final Table _boardTable;
	
	public AmazonDataService() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
    			.withRegion(Regions.US_EAST_1).build();
	    DynamoDB dynamoDB = new DynamoDB(client);
	    _configTable = dynamoDB.getTable("Records");
	    _boardTable = dynamoDB.getTable("CheapTTT");
	}
	
	public int addBoard(String gameBoard) {
	
    	GetItemSpec tableQueryCommand = new GetItemSpec().withPrimaryKey("ConfigID",1);
    	Item tableCell = _configTable.getItem(tableQueryCommand);
    	int nextBoardId = 1 + tableCell.getInt("GamesPlayed");
    	
    	UpdateItemSpec uSpec = new UpdateItemSpec().withPrimaryKey("ConfigID",1)
    			.withUpdateExpression("set GamesPlayed = :a")
    			.withValueMap(new ValueMap().withNumber(":a",nextBoardId));
    	_configTable.updateItem(uSpec);
    	
    	_boardTable.putItem(new Item().withPrimaryKey("BoardID",nextBoardId)
    			.withString("BoardStatus", gameBoard)
    			.withBoolean("GameOver",false)
    			.withString("PlayerAvatar","X")
    			.withString("Winner","_"));
    	
    	return nextBoardId;
	}
	
	public void updateBoard(int boardId, String gameBoard) {
    	UpdateItemSpec updateItemCommand = new UpdateItemSpec().withPrimaryKey("BoardID", boardId)
    			.withUpdateExpression("set BoardStatus = :a")
    			.withValueMap(new ValueMap().withString(":a", gameBoard));
    	_boardTable.updateItem(updateItemCommand);
	}
	
	public String getBoardStatus(int boardId) {
		GetItemSpec boardQuery = new GetItemSpec().withPrimaryKey("BoardID", boardId);
    	Item boardItem = _boardTable.getItem(boardQuery);
    	return boardItem.getString("BoardStatus");
	}
}