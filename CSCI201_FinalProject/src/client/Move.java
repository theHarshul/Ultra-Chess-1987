package client;

public class Move {
	
	//We can embed more info in this class if we want
	//but right now, this is all I think we need
	public ChessSquare first, second;
	
	
	Move(ChessSquare f, ChessSquare s){
		first = f;
		second = s;
	}
	
	public boolean equals(Move m){
		if(m == this) return true;
		int xf1 = first.getIndexX();
		int yf1 = first.getIndexY();
		int xs1 = second.getIndexX();
		int ys1 = second.getIndexY();
		
		int xf2 = m.first.getIndexX();
		int yf2 = m.first.getIndexY();
		int xs2 = m.second.getIndexX();
		int ys2 = m.second.getIndexY();
		return (xf1 == xf2 && yf1 == yf2 && xs1 == xs2 && ys1 == ys2);
	}
}
