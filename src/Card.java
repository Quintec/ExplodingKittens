
public class Card {
	
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
	
	private int type;
	
	public Card(int type) {
		this.type = type;
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
