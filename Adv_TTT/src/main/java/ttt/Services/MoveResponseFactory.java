package ttt.Services;

import org.springframework.stereotype.Service;

import ttt.Model.GameState;
import ttt.Model.MoveResponse;

@Service
public class MoveResponseFactory{
	public MoveResponse fromGameState(GameState gameState) {
		return new MoveResponse(
				gameState.getLastPlayedTileNumber(),
				gameState.getLastPlayedAvatar(),
				true,
				gameState.getCurrentBoard(),
				gameState.getIsGameOver(),
				gameState.getWinningAvatar(),
				"");
	}
	
	public MoveResponse fromError(String errorMessage) {
		return new MoveResponse(0, "", false, null, false, "", errorMessage);
	}
}