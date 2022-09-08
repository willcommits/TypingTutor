package typingTutor;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
		private AtomicBoolean done ; //REMOVE
		private AtomicBoolean started ; //REMOVE
		private AtomicBoolean won ; //REMOVE

		private FallingWord[] words;
		private int noWords;
		private HungryWord[] hungryWords;
		private final static int borderWidth=25; //appearance - border
		GamePanel(FallingWord[] words,HungryWord[] H, int maxY,
				 AtomicBoolean d, AtomicBoolean s, AtomicBoolean w) {
			this.words=words; //shared word list
			noWords = words.length; //only need to do this once
			done=d; //REMOVE
			started=s; //REMOVE
			won=w; //REMOVE
			hungryWords=H;
		}
		
		public void paintComponent(Graphics g) {
		    int width = getWidth()-borderWidth*2;
		    int height = getHeight()-borderWidth*2;
		    g.clearRect(borderWidth,borderWidth,width,height);//the active space
		    g.setColor(Color.pink); //change colour of pen
		    g.fillRect(borderWidth,height,width,borderWidth); //draw danger zone

		    g.setColor(Color.black);
		    g.setFont(new Font("Arial", Font.PLAIN, 26));
		   //draw the words
		    if (!started.get()) {
		    	g.setFont(new Font("Arial", Font.BOLD, 26));
				g.drawString("Type all the words before they hit the red zone,press enter after each one.",borderWidth*2,height/2);	
		    	
		    }
		    else if (!done.get()) {
				FontMetrics fm = g.getFontMetrics(new Font("Arial", Font.BOLD, 26));

		    	for (int i=0;i<noWords;i++){
					Rectangle2D bounds = fm.getStringBounds(words[i].getWord(), g);
					int Ilength = (int)bounds.getWidth();
					int Iheight = (int)bounds.getHeight();
		    		g.drawString(words[i].getWord(),words[i].getX()+borderWidth,words[i].getY());
					words[i].setWidth(Ilength);
					words[i].setHeight(Iheight);
		    	}
		    	g.setColor(Color.lightGray); //change colour of pen
		    	g.fillRect(borderWidth,0,width,borderWidth);

				for (int i=0;i<noWords-(noWords-1);i++){
					Rectangle2D bounds = fm.getStringBounds(hungryWords[i].getWord(), g);
					int Ilength = (int)bounds.getWidth();
					int Iheight = (int)bounds.getHeight();
					g.setColor(Color.GREEN); //change colour of pen
					g.drawString(hungryWords[i].getWord(),hungryWords[i].getX(),hungryWords[i].getY()+borderWidth);
					hungryWords[i].setWidth(Ilength);
					hungryWords[i].setHeight(Iheight);
				}


		   }
		   else { if (won.get()) {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Well done!",width/3,height/2);	
		   } else {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Game over!",width/2,height/2);	
		   }
		   }
		}
		
		public int getValidXpos() {
			int width = getWidth()-borderWidth*4;
			int x= (int)(Math.random() * width);
			return x;
		}

	public int getValidMiddleYpos() {
		int height = getHeight()-borderWidth*4;
		height=height/2;
		return height;
	}
		
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(10); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
		}

	}


