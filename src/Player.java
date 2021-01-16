import java.util.*;
public class Player {
	
	private Bot bot;
	
	private ArrayList<Card> hand;
	private int idx;
	private boolean playing;
	
	public Player(Bot b) {
		this.bot = b;
		this.hand = new ArrayList<Card>();
		this.playing = true;
	}
	
	public int getIdx() {
		return idx;
	}
	
	public Bot getBot() {
		return bot;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public void lose() {
		playing = false;
	}
	
	public boolean hasDefuse() {
		for (Card c : hand) {
			if (c.getType() == Card.DEFUSE)
				return true;
		}
		return false;
	}
	
	public void removeDefuse() {
		//System.out.println("before removal " + hand);
		Iterator<Card> it = hand.iterator();
		while (it.hasNext()) {
			Card c = it.next();
			if (c.getType() == Card.DEFUSE) {
				it.remove();
				//System.out.println("after removal " + hand);
				return;
			}
		}
		throw new RuntimeException("[GAME] Tried to remove non existent defuse card");
	}
	
	public void setHand(Card[] h) {
		System.out.println(Arrays.toString(h));
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
	
	public void drawCard(Card c) {
		this.hand.add(c);
		this.bot.selfCardDrawn(c);
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
				throw new RuntimeException("Bot " + bot.getClass().getCanonicalName() + " chose invalid target " + t + " for card " + a.getCard());
			this.hand.remove(a.getCard());
		}
		return a;
	}
	
	public Card giveFavor(int pidx) {
		Card c = this.bot.giveFavor(pidx);
		if (!this.hand.contains(c))
			throw new RuntimeException("Bot " + bot.getClass().getCanonicalName() + " tried to give favor of card not in hand " + c);
		this.hand.remove(c);
		return c;
	}
	
	public void receiveFavor(int pidx, Card c) {
		this.hand.add(c);
		this.bot.receiveFavor(pidx, c);
	}
}
