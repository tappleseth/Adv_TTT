package ttt.Model;

public class GameState{
	private final int _lastPlayedTileNumber;
	private final String _lastPlayedAvatar;
	private final boolean _isGameOver;
	private final String[] _currentBoard;
	
	public GameState(
			int lastPlayedTileNumber,
			String lastPlayedAvatar,
			boolean isGameOver,
			String[] currentBoard) {
		_lastPlayedTileNumber = lastPlayedTileNumber;
		_lastPlayedAvatar = lastPlayedAvatar;
		_isGameOver = isGameOver;
		_currentBoard = currentBoard;
	}
	
	public int getLastPlayedTileNumber() {
		return _lastPlayedTileNumber;
	}
	
	public String getLastPlayedAvatar() {
		return _lastPlayedAvatar;
	}
	
	public boolean getIsGameOver() {
		return _isGameOver;
	}
	
	public String[] getCurrentBoard() {
		return _currentBoard;
	}
}