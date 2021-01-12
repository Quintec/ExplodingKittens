public abstract class Bot {
	
	public abstract void init(int myIndex);
	public abstract void setHand(Card[] hand);
	
	public abstract Card cardPlayed(int playerIndex, Card played);
	public abstract int selfExplodingKittenDrawn(int newIndex);
	public abstract void selfCardDrawn(Card c);
	public abstract Action takeTurn();
	public abstract Card giveFavor(int playerIndex);
	public abstract void seeTheFuture(Card[] future);
	
	public abstract void cardDrawn(int playerIndex);
	public abstract void explodingKittenDrawn(int playerIndex);
	
}
