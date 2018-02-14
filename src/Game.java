import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {
	
	Target[] game_targets;
	int num_targets;
	double targets_per_second;
	double targets_per_second_incerment;

	int total_clicks;
	int targets_hit;
	int target_radius;
	int target_lifetime;
	
	int width;
	int height;
	
	long next_target_spawn;
	long next_freq_change;
	
	boolean is_running;
	
	ArrayList<Point2D.Double> click_history;
	
	public Game(int width, int height, double targets_per_second, double targets_per_second_incerment, int target_radius, int target_lifetime) {
		this.width 		= width;
		this.height 	= height;
		
		this.targets_per_second 			= targets_per_second;
		this.targets_per_second_incerment 	= targets_per_second_incerment;
		this.target_radius 					= target_radius;
		this.target_lifetime 				= target_lifetime;
		
		game_targets = new Target[20];
		num_targets		= 0;
		total_clicks 	= 1;
		targets_hit 	= 1;
		last_time_frame = 0;
		
		this.next_target_spawn 	= System.currentTimeMillis() + (long)(1000 / targets_per_second);
		this.next_freq_change 	= System.currentTimeMillis() + 1000;
		
		click_history = new ArrayList<Point2D.Double>();
		
		is_running = true;
	}
	
	public void generate_target() {//private
		Random rand = new Random();
		int x = rand.nextInt(this.width - (2 * target_radius)) + target_radius;
		int y = rand.nextInt(this.height - (2 * target_radius)) + target_radius;
		
		game_targets[num_targets] = new Target(x, y, target_lifetime, target_radius);
		num_targets++;
	}
	
	public boolean is_running() {
		return is_running;
	}
	
	long last_time_frame;
	
	public BufferedImage render_frame() {
		
		update_game();
		
		if(!is_running())
			return render_end_screen();
		
		long current_time = System.currentTimeMillis();
		int duration = (int)(current_time - last_time_frame);
		float fps = 1 / (float)((float)(duration) / 1000);
		last_time_frame = current_time;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setPaint ( new Color ( 255, 255, 255 ) );
        graphics.fillRect ( 0, 0, img.getWidth(), img.getHeight() );
        
        for(int i = 0; i < num_targets;i++) {
        	draw_circle(graphics, new Color(0, 0, 0), 		game_targets[i].get_x(), game_targets[i].get_y(), game_targets[i].tier1_size());
        	draw_circle(graphics, new Color(255, 0, 0), 	game_targets[i].get_x(), game_targets[i].get_y(), game_targets[i].tier2_size());
        	draw_circle(graphics, new Color(255, 255, 0), 	game_targets[i].get_x(), game_targets[i].get_y(), game_targets[i].tier3_size());
        }
        
    	DecimalFormat df = new DecimalFormat(".###");
        
        graphics.setPaint ( new Color ( 0, 0, 0 ) );
        graphics.setFont(new Font("Calibri", Font.PLAIN, 24));
		graphics.drawString("FPS: " + (int)(fps), 10, 20);
		graphics.drawString("Accuracy: " + (int)((targets_hit * 100) / total_clicks), 100, 20);
		graphics.drawString("Targets Per Second: " + df.format(targets_per_second), 300, 20);
				
		return img;
	}
	
	public BufferedImage render_end_screen() {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setPaint ( new Color ( 255, 255, 255 ) );
        graphics.fillRect ( 0, 0, img.getWidth(), img.getHeight() );
        
        draw_circle(graphics, new Color(0, 0, 0), 		width / 2, height / 2, (int)(height * 0.2)			);
    	draw_circle(graphics, new Color(255, 0, 0), 	width / 2, height / 2, (int)(height * 0.2 * 0.6666666)	);
    	draw_circle(graphics, new Color(255, 255, 0), 	width / 2, height / 2, (int)(height * 0.2 * 0.3333333)	);
    	
    	DecimalFormat df = new DecimalFormat(".###");
    	graphics.setPaint ( new Color ( 0, 0, 0 ) );
        graphics.setFont(new Font("Calibri", Font.PLAIN, 24));
		graphics.drawString("Accuracy: " + (int)((targets_hit * 100) / total_clicks), 10, 20);
		graphics.drawString("Targets Per Second: " + df.format(targets_per_second), 210, 20);
    	
    	for(int i = 0 ; i < click_history.size();i++) {
    		double r = click_history.get(i).getX() * (int)(height * 0.2);
    		double radiant = click_history.get(i).getY();
    		int x = (int)(r * Math.cos(radiant));
    		int y = (int)(r * Math.sin(radiant));
    		draw_circle(graphics, new Color(255, 255, 255), (width / 2) + x - 5, (height / 2) + y - 5, 10);
    		draw_circle(graphics, new Color(0, 0, 0), (width / 2) + x - 5, (height / 2) + y - 5, 6);
    		
    	}
        
		return img;
	}
	
	private void draw_circle(Graphics2D graphics, Color c, int x, int y, int r) {
		graphics.setPaint (c);
    	graphics.fillOval(x - r, y - r, r * 2, r * 2);
	}
	
	private void update_game() {
		update_targets();
		
		update_spawn();
	}
	
	private void update_spawn() {
		
		if(System.currentTimeMillis() > next_target_spawn) {
			generate_target();
			next_target_spawn += (long)(1000 / targets_per_second);
		}
		if(System.currentTimeMillis() > next_freq_change) {
			targets_per_second += targets_per_second_incerment;
			next_freq_change += 1000;
		}
		
	}
	
	private void update_targets() {
		for(int i = 0; i < num_targets;i++)
			game_targets[i].update();
		int array_size = num_targets;
		for(int i = 0; i < array_size;i++) {
			if(game_targets[i].is_expired())
				is_running = false;
			if(game_targets[i].is_clicked()) {
				num_targets--;
				game_targets[i] = null;
			}
		}
		for(int j = 0; j < array_size;j++) {
			if(game_targets[j] == null && game_targets[j + 1] != null) {
				game_targets[j] = game_targets[j + 1];
				game_targets[j + 1] = null;
			}
		}
	}
	
	private boolean remove_target(int index) throws Exception {
		game_targets[index] = null;
		return true;
	}
	
	public void click(int x, int y){
		total_clicks++;
		int closest_target = 0;
		for(int i = 0; i < num_targets;i++) {
			if(game_targets[closest_target].get_distance(x, y) > game_targets[i].get_distance(x, y)) {
				closest_target = i;
			}
		}
		Point2D.Double temp = game_targets[closest_target].click(x,y);
		if(temp != null) {
			click_history.add(temp);
			targets_hit++;
		}
	}
	
	
	
}
