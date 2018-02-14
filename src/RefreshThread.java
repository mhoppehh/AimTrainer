
import java.awt.image.BufferedImage;

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
