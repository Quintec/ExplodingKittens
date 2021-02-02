import java.util.*;
import java.util.concurrent.*;

public class Game {
	
	private Deck d;
	private Player[] players;
	private Display display;
	
	private boolean attacked;
	private int cardsToDraw;
	private GameView gv;
	
	private boolean disp;
	private boolean print;
	
	public Game(Player[] pls, boolean disp, boolean print) {
		assert pls.length == 5;
		
		shuffle(pls);
		
		this.d = new Deck();
		this.players = pls;
		this.gv = new GameView(this);
		
		this.disp = disp;
		this.print = print;
		
		if (disp)
			this.display = new Display(players, true);
	}
	
	public Player[] getPlayers() {
		return players;
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
			p.init(i, gv);
			p.setHand(d.dealHand());
		}
		this.attacked = false;
		this.cardsToDraw = 1;
		d.startShuffle();
	}
	
	public void play() {
		while (numPlaying() > 1)
			round();
		for (Player p : players) {
			if (p.isPlaying())
				System.out.println("Player " + p.getBot().getClass().getCanonicalName() + p.getIdx() + " wins");//PLACEHOLDER - return player? count wins, do match
		}
	}
	
	public int numPlaying() {
		int ans = 0;
		for (Player p : players)
			ans += p.isPlaying() ? 1 : 0;
		return ans;
	}
	
	public void round() {
		out: for (int i = 0; i < players.length; i++) {
			if (disp)
				display.refresh();
			if (!players[i].isPlaying())
				continue;
			if (numPlaying() == 1)
				return;
			if (disp)
				try {Thread.sleep(1000); } catch (InterruptedException e) {};
			if (print)
				System.out.println("player " + i + " (" + players[i].getBot().getClass().getCanonicalName() + ") turn");
			cardsToDraw = attacked ? 2 : 1;
			this.attacked = false;
			while (cardsToDraw > 0) {
				Action a = players[i].getAction();
				if (a.getType() == Action.DRAW_CARD) {
					Card next = d.drawCard();
					if (print)
						System.out.println("drawn " + next);
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
							continue out;
						}
					} else {
						players[i].drawCard(next);
						cardsToDraw--;
						for (int j = 0; j < players.length; j++) {
							if (i == j)
								continue;
							players[j].getBot().cardDrawn(i);
						}
					}
				} else {
					if (print)
						System.out.println("playing " + a.getCard() + " against " + a.getTarget());
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
	}
	
	private boolean prepCard(int i, Card toPlay, int tidx) {
		if (disp) {
			display.setTop(toPlay);
			display.refresh();
			try {Thread.sleep(1000); } catch (InterruptedException e) {};
		}
		for (int j = 0; j < players.length; j++) {
			if (i == j)
				continue;
			Card against = players[j].getBot().cardPlayed(i, toPlay, tidx);
			if (against != null) {
				if (print)
					System.out.println("player " + j + " played nope");
				if (against.getType() != Card.NOPE)
					throw new RuntimeException("Bot " + players[j].getBot().getClass() + " tried to counter card with " + against);
				players[j].removeNope();
				return !prepCard(j, against, i);
			}
		}
		return true;
	}
	
	private void playCard(int idx, Card c) {
		switch (c.getType()) {
		case Card.ATTACK:
			this.attacked = true;
			this.cardsToDraw = 0;
			break;
		case Card.SKIP:
			this.cardsToDraw--;
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
		if (disp) {
			display.setTop(c);
			display.refresh();
		}
	}
	
	private void playCardAt(int idx, Card c, int tidx) {
		switch (c.getType()) {
		case Card.FAVOR:
			if (print)
				System.out.println("player " + idx + " getting favor");
			Card card = players[tidx].giveFavor(idx);
			players[idx].receiveFavor(tidx, card);
			if (print)
				System.out.println("received " + card);
			break;
		}
		if (disp) {
			display.setTop(c);
			display.refresh();
		}
	}
}

