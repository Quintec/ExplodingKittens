
public class Action {
	public static final int DRAW_CARD = 0;
	public static final int PLAY_CARD = 1;
	
	private int type;
	private Card c;
	private int targetIndex;
	
	private Action(int type) { this.type = type; this.targetIndex = -1; }
	
	private Action(int type, Card c) { this(type); this.c = c;}
	
	private Action(int type, Card c, int tidx) { this(type, c); this.targetIndex = tidx; }
	
	public static Action draw() {
		return new Action(DRAW_CARD);
	}
	
	public static Action play(Card c) {
		return new Action(PLAY_CARD, c);
	}
	
	public static Action target(Card c, int targetIndex) {
		return new Action(PLAY_CARD, c, targetIndex);
	}
	
	public int getType() {
		return type;
	}
	
	public Card getCard() {
		return c;
	}
	
	public int getTarget() {
		return targetIndex;
	}
	
	public boolean isTargetted() {
		return targetIndex == -1;
	}
}
