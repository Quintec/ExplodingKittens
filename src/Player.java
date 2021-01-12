import java.util.*;
public class Player {
	
	private Bot bot;
	private ArrayList<Card> hand;
	private int idx;
	
	public Player(Bot b) {
		this.bot = b;
		this.hand = new ArrayList<Card>();
	}
	
	public void setHand(Card[] h) {
		this.hand.addAll(Arrays.asList(h));
		this.bot.setHand(h);
	}
	
	public void setIdx(int i) {
		this.idx = i;
		this.bot.init(i);
	}
	
	public void seeFuture(Card[] future) {
		this.bot.seeTheFuture(future);
	}
	
	public Action getAction() {
		Action a = this.bot.takeTurn();
		if (a.getType() == Action.PLAY_CARD) {
			if (!this.hand.contains(a.getCard()))
				throw new RuntimeException("Bot " + bot.getClass().getCanonicalName() + " tried to play card not in hand " + a.getCard().toString());
			if (a.getCard().getType() < 10)
				throw new RuntimeException("Bot " + bot.getClass().getCanonicalName() + " tried to play unplayable card " + a.getCard().toString());
			int t = a.getTarget();
			if (a.isTargetted() && (t < 0 || t > 4))
				throw new RuntimeException("Bot " + bot.getClass().getCanonicalName() + " chose invalid target " + t);
			this.hand.remove(a.getCard());
		}
		return a;
	}
}
