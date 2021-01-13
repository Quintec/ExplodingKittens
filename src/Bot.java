public abstract class Bot {
	
	public abstract void init(int myIndex);
	public abstract void setHand(Card[] hand);
	
	public abstract Card cardPlayed(int playerIndex, Card played, int targetIndex);
	public abstract int selfExplodingKittenDrawn();
	public abstract void selfCardDrawn(Card card);
	public abstract Action takeTurn();
	public abstract Card giveFavor(int playerIndex);
	public abstract void seeTheFuture(Card[] future);
	
	public abstract void receiveFavor(int playerIndex, Card card);
	public abstract void cardDrawn(int playerIndex);
	public abstract void explodingKittenDrawn(int playerIndex);
	public abstract void explodingKittenReplaced(int playerIndex);
	public abstract void playerExploded(int playerIndex);
	
}
