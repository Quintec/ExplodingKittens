import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
public class PlayAllBot extends Bot {
	
	private ArrayList<Card> hand;
	private int idx;
	private int[] cards;
	
	private GameView gv;

	@Override
	public void init(int myIndex, GameView gv) {
		hand = new ArrayList<Card>();
		idx = myIndex;
		cards = new int[5];
		Arrays.fill(cards, 5);
		this.gv = gv;
	}

	@Override
	public void setHand(Card[] cards) {
		hand.addAll(Arrays.asList(cards));
	}

	@Override
	public Card cardPlayed(int playerIndex, Card played, int targetIndex) {
		Iterator<Card> it = hand.iterator();
		while (it.hasNext()) {
			Card c = it.next();
			if (c.getType() == Card.NOPE) {
				//System.out.println("player " + idx  + " playing nope");
				it.remove();
				return c;
			}
		}
		return null;
	}

	@Override
	public int selfExplodingKittenDrawn() {
		//System.out.println("kitten drawn - hand " + hand);
		Iterator<Card> it = hand.iterator();
		while (it.hasNext()) {
			Card c = it.next();
			if (c.getType() == Card.DEFUSE) {
				it.remove();
				//System.out.println("player " + idx  + " playing defuse");
			}
		}
		return 0;
	}

	@Override
	public void selfCardDrawn(Card card) {
		//System.out.println("getting card: "+ card);
		hand.add(card);
		//System.out.println("hand: "+ hand);
	}

	@Override
	public Action takeTurn() {
		for (Card c : hand) {
			int type = c.getType();
			if (type > 11 && type != 15) {
				hand.remove(c);
				//System.out.println("player " + idx  + " playing " + c);
				return Action.play(c);
			} else if (type == Card.FAVOR) {
				for (int i = 0; i < 5; i++) {
					if (i == idx)
						continue;
					if (gv.isAlive(i) && gv.getNumCards(i) > 0) {
						hand.remove(c);
						//System.out.println("player " + idx  + " playing " + c);
						return Action.target(c, i);
					}
				}
				
			}
		}
		return Action.draw();
	}

	@Override
	public Card giveFavor(int playerIndex) {
		//System.out.println("faovr with: " + hand);
		return hand.remove(0);
	}

	@Override
	public void seeTheFuture(Card[] future) {
		
	}

	@Override
	public void receiveFavor(int playerIndex, Card card) {
		if (card != null) {
			hand.add(card);
			cards[playerIndex]--;
		}
	}

	@Override
	public void cardDrawn(int playerIndex) {
		cards[playerIndex]++;
	}

	@Override
	public void explodingKittenDrawn(int playerIndex) {
		
	}

	@Override
	public void explodingKittenReplaced(int playerIndex) {
		
	}

	@Override
	public void playerExploded(int playerIndex) {
		
	}

}
