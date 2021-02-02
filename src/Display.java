import javax.swing.*;
import java.awt.*;

public class Display {
	private JFrame frame;
	private Card deckTop;
	
	private Player[] players;
	private boolean viewAll;
	
	public Display(Player[] pls, boolean va) {
		players = pls;
		viewAll = va;
		deckTop = null;
		
		frame = new JFrame("Exploding Kittens");
		frame.add(new GamePanel());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void setTop(Card c) {
		this.deckTop = c;
	}
	
	public void refresh() {
		frame.repaint();
	}
	
	private class GamePanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			for (int i = 0; i < players.length; i++) {
				Player p = players[i];
				g.drawString(p.getBot().getClass().getCanonicalName(), 10, 10 + i * (Card.HEIGHT + 10));
				if (p.isPlaying()) {
					int j = 0;
					for (Card c : p.getHand()) {
						Image img;
						if (viewAll)
							img = c.getImage();
						else
							img = Card.getCardBack();
						g.drawImage(img, 80 + j * (Card.WIDTH / 3), 10 + i * (Card.HEIGHT + 10), null);
						j++;
					}
				} else {
					g.drawImage(new Card(Card.EXPLODING_KITTEN).getImage(), 80, 10 + i * (Card.HEIGHT + 10), null);
				}
			}
			
			if (deckTop != null) {
				g.drawImage(deckTop.getImage(), 800 - Card.WIDTH - 50, (800 - Card.HEIGHT) / 2, null);
			}
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(800, 800);
		}
	}
}
