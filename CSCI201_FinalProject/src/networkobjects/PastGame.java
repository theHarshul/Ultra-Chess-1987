package networkobjects;

public class PastGame {
	private int iWon;
	private String opponentName,chessNotation, win;
	
	public PastGame(String win, String opponentName, String chessNotation){
		this.win = win;
		this.opponentName = opponentName;
		this.chessNotation = chessNotation;
	}

	public int getiWon() {
		return iWon;
	}

	public void setiWon(int iWon) {
		this.iWon = iWon;
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public String getChessNotation() {
		return chessNotation;
	}

	public void setChessNotation(String chessNotation) {
		this.chessNotation = chessNotation;
	}

	public String getWin() {
		return win;
	}

	public void setWin(String win) {
		this.win = win;
	}

}
