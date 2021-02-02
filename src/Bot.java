/**
 * The base class for you to base your AI on.
 */
public abstract class Bot {
	
	/**
	 * Method called at the start of the game.
	 * @param myIndex your player's index in the list of players
	 */
	public abstract void init(int myIndex, GameView view);
	
	/**
	 * Method called at the start of the game to deal the starting hand.
	 * @param hand your starting hand (defuse card at index 0)
	 */
	public abstract void setHand(Card[] hand);
	
	/**
	 * Method called when another player plays a card.
	 * @param playerIndex the index of the player
	 * @param played the card they have played
	 * @param targetIndex the target of the card if the card targets someone, else -1
	 * @return a reaction card if you want to play one (i.e. nope card), otherwise null
	 */
	public abstract Card cardPlayed(int playerIndex, Card played, int targetIndex);
	
	/**
	 * Method called when you draw an exploding kitten.
	 * If you have one, a defuse card is automatically removed from your hand.
	 * @return an index where you wish to replace the kitten in the deck.
	 * If you do not have a defuse card, you are out of the game, and you may return anything.
	 * It will be ignored.
	 */
	public abstract int selfExplodingKittenDrawn();
	
	/**
	 * Method called when you draw a card.
	 * @param card the card you drew.
	 */
	public abstract void selfCardDrawn(Card card);
	
	/**
	 * Method called when it is your turn.
	 * This method will repeatedly be called until you draw a card (2 if you have been attacked)
	 * or if you end your turn in other ways (ex. skip, attack, dead to exploding kitten)
	 * @return the action you will take.
	 * @see Action
	 */
	public abstract Action takeTurn();
	
	/**
	 * Method called when someone plays a favor card targeting you.
	 * @param playerIndex the index of the player playing the favor card.
	 * @return the card you will give them.
	 */
	public abstract Card giveFavor(int playerIndex);
	
	/**
	 * Method called when you play a see the future card.
	 * @param future The top 3 cards in the deck. The top of the deck is at index 0.
	 */
	public abstract void seeTheFuture(Card[] future);
	
	
	/**
	 * Method called when you play a favor card.
	 * @param playerIndex The player you targeted with your favor card.
	 * @param card The card that player chose to give you.
	 */
	public abstract void receiveFavor(int playerIndex, Card card);
	
	/**
	 * Method called when someone else draws a card.
	 * @param playerIndex The index of the player that drew a card.
	 */
	public abstract void cardDrawn(int playerIndex);
	
	/**
	 * Method called when someone else draws an exploding kitten.
	 * @param playerIndex The index of the player that drew an exploding kitten.
	 */
	public abstract void explodingKittenDrawn(int playerIndex);
	
	/**
	 * Method called when someone else puts an exploding kitten back into the deck.
	 * @param playerIndex The index of the player that put the kitten back.
	 */
	public abstract void explodingKittenReplaced(int playerIndex);
	
	/**
	 * Method called when someone loses to an exploding kitten.
	 * @param playerIndex The index of the player that exploded.
	 */
	public abstract void playerExploded(int playerIndex);
	
}
