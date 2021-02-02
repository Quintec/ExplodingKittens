/**
 * A limited view of the game that each Bot has access to for informational purposes.
 */
public class GameView {
	private Game game;
	
	public GameView(Game g) {
		this.game = g;
	}
	
	/**
	 * Method that returns whether the player at a particular index is still alive (not exploded).
	 * @param idx the index of a player
	 * @return whether that player has exploded or not
	 */
	public boolean isAlive(int idx) {
		return game.getPlayers()[idx].isPlaying();
	}
	
	/**
	 * Method that returns the number of cards in the hand of a player at a particular index.
	 * @param idx the index of a player
	 * @return the number of cards in their hand
	 */
	public int getNumCards(int idx) {
		return game.getPlayers()[idx].getHand().size();
	}
}

