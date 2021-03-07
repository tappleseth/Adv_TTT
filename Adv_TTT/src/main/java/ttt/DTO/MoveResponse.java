package ttt;

public class MoveResponse{
	
	private final String avatar;
	private final int tileNumber;
	private final boolean isValid;
	private final String[] tiles;
	private final boolean gameOver;
	private final String winner;
	
	public MoveResponse(int tileNumber, String avatar, boolean isValid, String[] tiles, boolean gameOver, String winner){
		
		this.avatar = avatar;
		this.tileNumber = tileNumber;
		this.isValid = isValid;
		this.tiles = tiles;
		
		this.gameOver = gameOver;
		this.winner = winner;
	}
	
	public int getTileNumber(){return tileNumber;}
	public boolean getIsValid(){return isValid;}
	public String getAvatar(){ return avatar;}
	public String[] getTiles(){return tiles;}
	public boolean getGameOver(){ return gameOver;}
	public String getWinner(){ return winner;}
	
}