import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
public class PlayAllBot extends Bot {
	
	private ArrayList<Card> hand;
	private int idx;

	@Override
	public void init(int myIndex) {
		hand = new ArrayList<Card>();
		idx = myIndex;
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
			}
		}
		return 0;
	}

	@Override
	public void selfCardDrawn(Card card) {
		hand.add(card);
	}

	@Override
	public Action takeTurn() {
		for (Card c : hand) {
			int type = c.getType();
			if (type > 11 && type != 15) {
				hand.remove(c);
				return Action.play(c);
			} else if (type == Card.FAVOR) {
				int t = idx == 0 ? 1 : 0;
				hand.remove(c);
				return Action.target(c, t);
			}
		}
		return Action.draw();
	}

	@Override
	public Card giveFavor(int playerIndex) {
		return hand.remove(0);
	}

	@Override
	public void seeTheFuture(Card[] future) {
		
	}

	@Override
	public void receiveFavor(int playerIndex, Card card) {
		hand.add(card);
	}

	@Override
	public void cardDrawn(int playerIndex) {
		
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