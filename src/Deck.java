import java.util.*;
public class Deck {
	
	private ArrayList<Card> cards;
	
	public Deck() {
		cards = new ArrayList<Card>();
		this.init();
	}
	
	public void init() {
		for (int i = 0; i < 5; i++) {
			for (int c = 0; c < 4; c++)
				cards.add(new Card(i));
		}
		
		for (int c = 0; c < 4; c++) {
			cards.add(new Card(Card.ATTACK));
			cards.add(new Card(Card.SKIP));
			cards.add(new Card(Card.FAVOR));
			cards.add(new Card(Card.SHUFFLE));
			cards.add(new Card(Card.NOPE));
		}
		
		cards.add(new Card(Card.NOPE));
		
		this.shuffle();
	}
	
	public void startShuffle() {
		for (int i = 0; i < 4; i++)
			cards.add(new Card(Card.EXPLODING_KITTEN));
		cards.add(new Card(Card.DEFUSE));
		this.shuffle();
	}
	
	public Card[] dealHand() {
		Card[] hand = new Card[5];
		hand[0] = new Card(Card.DEFUSE);
		for (int i = 1; i < 5; i++)
			hand[i] = this.drawCard();
		return hand;
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	public Card drawCard() {
		return cards.remove(0);
	}
	
	public Card[] seeFuture() {
		Card[] future = new Card[3];
		int l = Math.min(3, cards.size());
		for (int i = 0; i < l; i++)
			future[i] = cards.get(i);
		return future;
	}
	
	public void insert(Card c, int idx) {
		cards.add(idx, c);
	}
}
