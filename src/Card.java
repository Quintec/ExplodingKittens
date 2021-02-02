import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;


public class Card {
	
	public static final int WIDTH = 96;
	public static final int HEIGHT = 128;
	
	//normal cats
	public static final int CAT0 = 0;
	public static final int CAT1 = 1;
	public static final int CAT2 = 2;
	public static final int CAT3 = 3;
	public static final int CAT4 = 4;
	
	//action cats
	public static final int DEFUSE = 10;
	public static final int NOPE = 11;
	public static final int SKIP = 12;
	public static final int SHUFFLE = 13;
	public static final int SEE_THE_FUTURE = 14;
	public static final int FAVOR = 15;
	public static final int ATTACK = 16;
	public static final int EXPLODING_KITTEN = 17;
	
	//IGNORE THIS (card back image)
	public static final int CARD_BACK = 18;
	
	private static Image[] images = new Image[19];
	
	static {
		try {
			images[CAT0] = getImage("tacocat.png");
			images[CAT1] = getImage("rainbowcat.png");
			images[CAT2] = getImage("beardcat.png");
			images[CAT3] = getImage("cattermelon.png");
			images[CAT4] = getImage("potatocat.png");
			
			images[DEFUSE] = getImage("defuse.png");
			images[NOPE] = getImage("nope.png");
			images[SKIP] = getImage("skip.png");
			images[SHUFFLE] = getImage("shuffle.png");
			images[SEE_THE_FUTURE] = getImage("seethefuture.png");
			images[FAVOR] = getImage("favor.png");
			images[ATTACK] = getImage("attack.png");
			images[EXPLODING_KITTEN] = getImage("explodingkitten.jpg");
			images[CARD_BACK] =  getImage("cardback.png");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
		}
	}
	
	private int type;
	
	public Card(int type) {
		this.type = type;
	}
	
	private static Image getImage(String fileName) throws IOException {
		return ImageIO.read(Card.class.getResource(fileName))/*.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT)*/;
	}
	
	public static Image getCardBack() {
		return images[CARD_BACK];
	}
	
	public Image getImage() {
		return images[type];
	}
	
	public int getType() {
		return type;
	}
	
	public String toString() {
		switch(type) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				return "<Normal cat " + type + ">";
			case DEFUSE:
				return "<Defuse cat>";
			case NOPE:
				return "<Nope cat>";
			case SKIP:
				return "<Skip cat>";
			case SHUFFLE:
				return "<Shuffle cat>";
			case SEE_THE_FUTURE:
				return "<See the future cat>";
			case FAVOR:
				return "<Favor cat>";
			case ATTACK:
				return "<Attack cat>";
			case EXPLODING_KITTEN:
				return "<Exploding kitten>";
			default:
				throw new RuntimeException("Unknown card encountered: type " + type);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (type != other.type)
			return false;
		return true;
	}
}
