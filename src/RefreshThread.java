import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class RefreshThread implements Runnable{
	
	Game g;
	Display d;
	
	public RefreshThread(Game g, Display d) {
		this.g = g;
		this.d = d;
	}
	public void run() {
		while(g.is_running()) {
			BufferedImage img = g.render_frame();
			d.display_image(img);
			try {Thread.sleep(2);}catch(Exception e) {}
		}
    }
}
