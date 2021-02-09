import java.util.*;
import java.util.concurrent.*;

public class Game {
	
	public static final int SLEEP = 3000;
	
	private Deck d;
	private Player[] players;
	private Display display;
	
	private Bot[] bots;
	
	private boolean attacked;
	private int cardsToDraw;
	private GameView gv;
	
	private boolean disp;
	private boolean print;
	
	private int[] wins;
	
	public Game(Bot[] bots, boolean disp, boolean print) {
		assert bots.length == 5;
		
		this.bots = bots;
		this.gv = new GameView(this);
		
		this.disp = disp;
		this.print = print;
		
		this.wins = new int[bots.length];
		
		if (disp)
			this.display = new Display(true);
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public Deck getDeck() {
		return d;
	}
	
	//adapted from https://stackoverflow.com/a/1520212
	private void shuffle(Player[] ar) {
		Random rnd = new Random(System.currentTimeMillis());
		for (int i = ar.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			Player a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
	
	public void start() {
		this.d = new Deck();
		this.players = new Player[bots.length];
		for (int i = 0; i < bots.length; i++) {
			players[i] = new Player(bots[i]);
		}
		if (disp)
			this.display.setPlayers(players);
		//shuffle(players);
		
		for (int i = 0; i < players.length; i++) {
			Player p = players[i];
			p.init(i, gv);
			p.setHand(d.dealHand());
		}
		this.attacked = false;
		this.cardsToDraw = 1;
		d.startShuffle();
	}
	
	public int[] getResults() {
		return wins;
	}
	
	public void play() {
		while (numPlaying() > 1)
			round();
		for (int i = 0; i < players.length; i++) {
			Player p = players[i];
			if (p.isPlaying()) {
				if (print)
					System.out.println("Player " + i + " (" + p.getBot().getClass().getCanonicalName() + ") wins");
				wins[i]++;
				break;
			}
		}
	}
	
	public int numPlaying() {
		int ans = 0;
		for (Player p : players)
			ans += p.isPlaying() ? 1 : 0;
		return ans;
	}
	
	public void round() {
		int i = (int)(Math.random() * players.length);
		out: for (int count = 0; count < players.length; count++) {
			i = (i + 1) % players.length;
			if (disp) {
				display.setIdx(i);
				display.refresh();
			}
			if (!players[i].isPlaying())
				continue;
			if (numPlaying() == 1)
				return;
			if (print)
				System.out.println("player " + i + " (" + players[i].getBot().getClass().getCanonicalName() + ") turn");
			//for (int a = 0; a < players.length; a++)
			//	System.out.println("player " + a + players[a].isPlaying() + ": " + players[a].getHand());
			cardsToDraw = attacked ? 2 : 1;
			this.attacked = false;
			while (cardsToDraw > 0) {
				if (disp)
					try {Thread.sleep(SLEEP); } catch (InterruptedException e) {};
				Action a = players[i].getAction();
				if (a.getType() == Action.DRAW_CARD) {
					Card next = d.drawCard();
					if (print)
						System.out.println("drawn " + next);
					if (next.getType() == Card.EXPLODING_KITTEN) {
						if (players[i].hasDefuse()) {
							players[i].removeDefuse();
							int eidx = players[i].getBot().selfExplodingKittenDrawn();
							cardsToDraw--;
							if (print)
								System.out.println("replaced " + next);
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
							if (print)
								System.out.println("player exploded");
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
			try {Thread.sleep(SLEEP); } catch (InterruptedException e) {};
		}
		for (int j = 0; j < players.length; j++) {
			if (i == j)
				continue;
			if (!players[j].isPlaying())
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
			Card card;
			if (!players[tidx].isPlaying())
				throw new RuntimeException("player " + idx + " requested favor from dead player");
			if (players[tidx].getHand().isEmpty())
				card = null;
			else {
				card = players[tidx].giveFavor(idx);
			}
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

