/**
 * A class representing an action in the game.
 * Note that you cannot construct an Action object directly - you must use the factory methods draw(), play(), and target().
 * @see #draw
 * @see #play
 * @see #target
 */
public class Action {
	/**
	 * An action that draws a card.
	 */
	public static final int DRAW_CARD = 0;
	/**
	 * An action that plays a card.
	 */
	public static final int PLAY_CARD = 1;
	
	/**
	 * The action's type (either DRAW_CARD or PLAY_CARD).
	 */
	private int type;
	/**
	 * The card associated with the action.
	 */
	private Card c;
	/**
	 * The target index of the action.
	 */
	private int targetIndex;
	
	private Action(int type) { this.type = type; this.targetIndex = -1; }
	
	private Action(int type, Card c) { this(type); this.c = c;}
	
	private Action(int type, Card c, int tidx) { this(type, c); this.targetIndex = tidx; }
	
	/**
	 * Factory constructor for an action that draws a card.
	 * @return A draw action
	 */
	public static Action draw() {
		return new Action(DRAW_CARD);
	}
	
	/**
	 * Factory constructor for an action that plays a card.
	 * @param c The card to be played
	 * @return An action that plays the card
	 */
	public static Action play(Card c) {
		return new Action(PLAY_CARD, c);
	}
	
	/**
	 * Factory constructor for an action that plays a card targeting someone else.
	 * @param c The card to be played
	 * @param targetIndex The index of the target
	 * @return An action that plays the card targeting the specified person
	 */
	public static Action target(Card c, int targetIndex) {
		return new Action(PLAY_CARD, c, targetIndex);
	}
	
	/**
	 * Gets the type of the action - DRAW_CARD or PLAY_CARD.
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Gets the card associated with the action.
	 * @return the card
	 */
	public Card getCard() {
		return c;
	}
	
	/**
	 * Gets the target of the action.
	 * @return the target
	 */
	public int getTarget() {
		return targetIndex;
	}
	
	/**
	 * Returns whether or not the action targets someone.
	 * @return true if the action targets someone, false if it doesn't
	 */
	public boolean isTargetted() {
		return targetIndex != -1;
	}
}
