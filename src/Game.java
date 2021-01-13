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
		while (numPlaying() > 1)
			round();
	}
	
	public int numPlaying() {
		int ans = 0;
		for (Player p : players)
			ans += p.isPlaying() ? 1 : 0;
		return ans;
	}
	
	public void round() {
		for (int i = 0; i < players.length; i++) {
			if (!players[i].isPlaying())
				continue;
			
			int cardsToDraw = attacked ? 2 : 1;
			this.attacked = false;
			this.skip = false;
			Action a = players[i].getAction();
			while (cardsToDraw > 0 && !this.skip) {
				if (a.getType() == Action.DRAW_CARD) {
					Card next = d.drawCard();
					if (next.getType() == Card.EXPLODING_KITTEN) {
						if (players[i].hasDefuse()) {
							players[i].removeDefuse();
							int eidx = players[i].getBot().selfExplodingKittenDrawn();
							d.insert(next, eidx);
							for (int j = 0; j < players.length; j++) {
								if (i == j)
									continue;
								players[j].getBot().explodingKittenDrawn(i);
								players[j].getBot().explodingKittenReplaced(i);
							}
						} else {
							players[i].getBot().selfExplodingKittenDrawn();
							players[i].lose();
							for (int j = 0; j < players.length; j++) {
								if (i == j)
									continue;
								players[j].getBot().explodingKittenDrawn(i);
								players[j].getBot().playerExploded(i);
							}
							continue;
						}
					} else {
						players[i].drawCard(d.drawCard());//DEAL WITH EXPLODING KITTEN DREW
						cardsToDraw--;
						for (int j = 0; j < players.length; j++) {
							if (i == j)
								continue;
							players[j].getBot().cardDrawn(i);
						}
						continue;
					}
				}
				boolean play = prepCard(i, a.getCard(), a.getTarget());
				if (play) {
					if (a.isTargetted()) {
						playCardAt(i, a.getCard(), a.getTarget());
					} else {
						playCard(i, a.getCard());
					}
				}
			}
		}
	}
	
	private boolean prepCard(int i, Card toPlay, int tidx) {
		for (int j = 0; j < players.length; j++) {
			if (i == j)
				continue;
			Card against = players[j].getBot().cardPlayed(i, toPlay, tidx);
			if (against != null) {
				if (against.getType() != Card.NOPE)
					throw new RuntimeException("Bot " + players[j].getBot().getClass() + " tried to counter card with " + against);
				return !prepCard(j, against, i);
			}
		}
		return true;
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
		switch (c.getType()) {
		case Card.FAVOR:
			Card card = players[tidx].giveFavor(idx);
			players[idx].receiveFavor(tidx, card);
			break;
		}
	}
}

