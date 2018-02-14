import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.lang.Math;
public class Target
{
    long birth_second;
    
    int age;
    int lifetime;
    
    int x, y;
    int max_size;
    
    boolean expired;
    boolean clicked;
    public Target(int x, int y, int lifetime, int max_size){
        this.x = x;
        this.y = y;
        this.age = 0;
        this.lifetime = lifetime;
        this.max_size = max_size;
        birth_second = System.currentTimeMillis();
        
        this.expired = false;
        this.clicked = false;
        
        update();
    }
    
    public int get_x() {return x;}
    public int get_y() {return y;}
    
    
    public int tier1_size(){
    	if(age > lifetime)
    		return 0;
    	float percent = (float)(age) / (lifetime / 2);
    	if(percent > 1)
    		percent = 1 - (percent  - (int)(percent));
        return (int)(max_size * percent);
    }
    
    public int tier2_size() {
    	return (int)(tier1_size() * (0.66666));
    }
    
    public int tier3_size() {
    	return (int)(tier2_size() * (0.5));
    }
    
    public double get_distance(int x, int y) {
    	return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }
    
    public Point2D.Double click(int x, int y){
    	if(get_distance(x, y) < tier1_size()) {
    		clicked = true;
    		double r     = get_distance(x, y) / tier1_size();
            double theta = Math.atan2(y - this.y, x - this.x);
    		return new Point2D.Double(r,theta);
    	}
    	return null;
    }
    
    public int age() {
    	return age;
    }
    
    public boolean is_expired() {return expired;}
    public boolean is_clicked() {return clicked;}
    
    private boolean benchmark = false;
    
    public void update(){
    	
    	if(!this.expired)
    		this.age = (int)(System.currentTimeMillis() - birth_second);
        if(this.age > lifetime) {
        	if(!benchmark)
        		this.expired = true;
        	else
        		this.clicked = true;
        	
        }
        
    }
    
    public String toString() {
    	update();
    	return "" + is_clicked();
    }
}