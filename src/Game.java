import java.util.*;
import java.util.concurrent.*;

public class Game {
	
	private Deck d;
	private Player[] players;
	
	private boolean attacked;
	private boolean skip;
	
	public Game(Player[] pls) {
		assert pls.length == 5;
		
		shuffle(pls);
		this.d = new Deck();
		this.players = pls;
	}
	
	//adapted from https://stackoverflow.com/a/1520212
	private void shuffle(Player[] ar) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = ar.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			Player a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
	
	public void start() {
		for (int i = 0; i < players.length; i++) {
			Player p = players[i];
			p.setIdx(i);
			p.setHand(d.dealHand());
		}
		this.attacked = false;
		this.skip = false;
		d.startShuffle();
	}
	
	public void play() {
		
	}
	
	public void round() {
		for (int i = 0; i < players.length; i++) {
			int cardsToDraw = attacked ? 2 : 1;
			this.attacked = false;
			this.skip = false;
			Action a = players[i].getAction();
			while (cardsToDraw > 0 && !this.skip) {
				if (a.isTargetted()) {
					playCardAt(i, a.getCard(), a.getTarget());
				} else {
					playCard(i, a.getCard());
				}
			}
		}
	}
	
	private void playCard(int idx, Card c) {
		switch (c.getType()) {
		case Card.ATTACK:
			this.skip = true;
			this.attacked = true;
			break;
		case Card.SKIP:
			this.skip = true;
			break;
		case Card.SHUFFLE:
			d.shuffle();
			break;
		case Card.SEE_THE_FUTURE:
			players[idx].seeFuture(d.seeFuture());
			break;
		default:
			throw new RuntimeException("[GAME] Unplayable card in playCard " + c);
		}
	}
	
	private void playCardAt(int idx, Card c, int tidx) {
		
	}
}

