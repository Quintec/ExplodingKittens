import java.util.*;
public class Main {
	public static void main(String[] args) {
		//there MUST be exactly 5 players
		Bot[] bots = new Bot[] {new PlayAllBot(), new PlayAllBot(), new PlayAllBot(), new PlayAllBot(), new PlayAllBot()};
		
		playGames(bots, 100000);
	}
	
	public static void playGames(Bot[] bots, int num) {
		assert bots.length == 5;
		Game g = new Game(bots, false, false);//first boolean: display or not
											//second boolean: print debug statements or not
		for (int i = 0; i < num; i++) {
			g.start();
			g.play();
		}
		System.out.println(Arrays.toString(g.getResults()));
	}
}
