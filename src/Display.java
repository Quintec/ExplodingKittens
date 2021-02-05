import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Display {
	private JFrame frame;
	private Card deckTop;
	
	private JFrame log;
	
	private Player[] players;
	private boolean viewAll;
	private int idx;
	
	public Display(boolean va) {
		viewAll = va;
		deckTop = null;
		
		frame = new JFrame("Exploding Kittens");
		frame.add(new GamePanel());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		log = new JFrame("Event log");
		JTextArea logger = new JTextArea();
		FrameLogger fl = new FrameLogger(logger, "Log");
		System.setOut(new PrintStream(fl));
		log.add(logger);
		log.setSize(300, frame.getHeight());
		log.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		log.setLocation(frame.getX() + frame.getWidth(), frame.getY());
		log.setVisible(true);
	}
	
	public void setPlayers(Player[] pls) {
		this.players = pls;
	}
	
	public void setIdx(int i) {
		this.idx = i;
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
			if (players == null)
				return;
			for (int i = 0; i < players.length; i++) {
				Player p = players[i];
				if (idx == i)
					g.setColor(Color.RED);
				g.drawString(p.getBot().getClass().getCanonicalName(), 10, 10 + i * (Card.HEIGHT + 10));
				g.setColor(Color.BLACK);
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

	private class FrameLogger extends OutputStream {

	   private final JTextArea textArea;
	   private final StringBuilder sb = new StringBuilder();
	   private String title;

	   public FrameLogger(final JTextArea textArea, String title) {
	      this.textArea = textArea;
	      this.title = title;
	      sb.append(title + "> ");
	   }

	   @Override
	   public void flush() {
	   }

	   @Override
	   public void close() {
	   }

	   @Override
	   public void write(int b) throws IOException {

	      if (b == '\r')
	         return;

	      if (b == '\n') {
	         final String text = sb.toString() + "\n";
	         SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	               textArea.append(text);
	               log.repaint();
	            }
	         });
	         sb.setLength(0);
	         sb.append(title + "> ");
	         return;
	      }

	      sb.append((char) b);
	   }
	}
}
