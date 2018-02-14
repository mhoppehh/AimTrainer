import javax.swing.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.awt.*;
public class Display implements ActionListener, MouseListener
{
    double FRAME_SCALING_FACTOR = 0.5;

    JFrame j;
    JPanel btn_pnl;
    JLabel img_lbl;
    public Display(){
        init_frame();

        init_ui();
    }

    private void init_frame(){
        j = new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screenSize.getWidth() * 0.5);
        int height = (int)(screenSize.getHeight() * 0.5);

        j.setSize(width, height);
        j.setDefaultCloseOperation(j.EXIT_ON_CLOSE);
    }

    private void init_ui(){
        JButton button = new JButton("Start");
        button.addActionListener(this);
        btn_pnl = new JPanel();
        btn_pnl.setLayout(new GridLayout(3,3));

        btn_pnl.add(new JPanel());
        btn_pnl.add(new JPanel());
        btn_pnl.add(new JPanel());
        btn_pnl.add(new JPanel());
        btn_pnl.add(button);
        btn_pnl.add(new JPanel());
        btn_pnl.add(new JPanel());
        btn_pnl.add(new JPanel());
        btn_pnl.add(new JPanel());

        j.getContentPane().add(btn_pnl);
        //j.setResizable(true);
        j.setVisible(true);
        j.setDefaultCloseOperation(j.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        start_game();
    }
    
    Game g;
    
    private void start_game() {
    	j.setResizable(false);
    	int width = btn_pnl.getWidth();
    	int height = btn_pnl.getHeight();
    	
    	img_lbl = new JLabel();
    	img_lbl.addMouseListener(this);
    	j.remove(btn_pnl);
        j.getContentPane().add(img_lbl);

    	g = new Game(width, height, 1, 0.02, 45, 5000);
    	
    	(new Thread(new RefreshThread(g, this))).start();
    }
    
    public void display_image(BufferedImage img) {
    	ImageIcon icn = new ImageIcon(img);
    	icn.setImage(img);
		img_lbl.setIcon(icn);
		j.repaint();
    }
    
    @Override	public void mouseEntered(MouseEvent e) 	{}
    @Override	public void mousePressed(MouseEvent e)	{
    	if(g.is_running())
    		g.click(e.getX(), e.getY());
    	else {
    		j.remove(img_lbl);
    		g = null;
    		init_ui();
    		j.repaint();
    	}
    		
    }
    @Override	public void mouseReleased(MouseEvent e) {}
    @Override	public void mouseExited(MouseEvent e) 	{}
    @Override   public void mouseClicked(MouseEvent e) 	{}


    public static void main(String [] args){
        try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        
        Display d = new Display();} catch(Exception e){System.out.println(e);}
    }
}
