package ttt.DTO;


public class BoardResponse {

    private final long id;
    private final int boardLength;
    private final boolean humanGoesFirst;
    private final String[] tiles;
	
    public BoardResponse(long id, int boardLength, boolean humanGoesFirst, String[] tiles) {
        this.id = id;
        this.boardLength = boardLength;
        this.humanGoesFirst = humanGoesFirst;
        this.tiles = tiles;
    }
	
	

    public long getId() {
        return id;
    }

    public int getBoardLength() {
        return boardLength;
    }

    public boolean getHumanGoesFirst(){
        return humanGoesFirst;
    }

    public String[] getTiles(){
        return tiles;
    }
}