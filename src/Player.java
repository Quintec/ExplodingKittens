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
	
	public ArrayList<Card> getHand() {
		return hand;
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
	
	public void removeNope() {
		//System.out.println("before removal " + hand);
		Iterator<Card> it = hand.iterator();
		while (it.hasNext()) {
			Card c = it.next();
			if (c.getType() == Card.NOPE) {
				it.remove();
				//System.out.println("after removal " + hand);
				return;
			}
		}
		throw new RuntimeException("[GAME] Tried to remove non existent nope card");
	}
	
	public void setHand(Card[] h) {
		//System.out.println(Arrays.toString(h));
		this.hand.addAll(Arrays.asList(h));
		this.bot.setHand(h);
	}
	
	public void init(int i, GameView gv) {
		this.idx = i;
		this.bot.init(i, gv);
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
			if (!this.hand.contains(a.getCard())) {
				System.err.println("Bot " + bot.getClass().getCanonicalName() + " tried to play card not in hand " + a.getCard().toString());
				return Action.draw();
			}
			if (a.getCard().getType() < 10) {
				System.err.println("Bot " + bot.getClass().getCanonicalName() + " tried to play unplayable card " + a.getCard().toString());
				return Action.draw();
			}
			int t = a.getTarget();
			if (a.isTargetted() && (t < 0 || t > 4)) {
				System.err.println("Bot " + bot.getClass().getCanonicalName() + " chose invalid target " + t + " for card " + a.getCard());
				return Action.draw();
			}
			this.hand.remove(a.getCard());
		}
		return a;
	}
	
	public Card giveFavor(int pidx) {
		Card c = this.bot.giveFavor(pidx);
		if (!this.hand.contains(c)) {
			System.err.println("Bot " + bot.getClass().getCanonicalName() + " tried to give favor of card not in hand " + c);
			Card t = this.hand.remove(0);
			return t;
		}
		this.hand.remove(c);
		return c;
	}
	
	public void receiveFavor(int pidx, Card c) {
		if (c != null) {
			this.hand.add(c);
			this.bot.receiveFavor(pidx, c);
		}
	}
	
	@Override
	public String toString() {
		return this.bot.getClass().getCanonicalName();
	}
}
