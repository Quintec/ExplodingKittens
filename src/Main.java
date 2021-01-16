
public class Main {
	public static void main(String[] args) {
		//there MUST be exactly 5 players
		Bot[] bots = new Bot[] {new PlayAllBot(), new PlayAllBot(), new PlayAllBot(), new PlayAllBot(), new PlayAllBot()};
		
		playGame(bots);
	}
	
	public static void playGame(Bot[] bots) {
		assert bots.length == 5;
		Player[] ps = new Player[5];
		for (int i = 0; i < 5; i++)
			ps[i] = new Player(bots[i]);
		Game g = new Game(ps);
		g.start();
		g.play();
	}
}
