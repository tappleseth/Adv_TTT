package ttt.Services;

public class DebugComputerStrategy implements ComputerStrategy{

	private int _nextTileNumber = 0;
	
	@Override
	public int getNextTileNumber(String[] board) {
		return _nextTileNumber;
	}
	
	public void setNextTileNumber(int tileNumber) {
		_nextTileNumber = tileNumber;
	}
}