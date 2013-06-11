package std.anim;


import java.awt.Image;

import javax.swing.ImageIcon;

import std.StdDraw;

public class GIFAnim extends AnimBase {
	Image m_image;
	double m_x, m_y;
	int m_frames;

	public void update(){
		m_frames--;
		
		if(m_frames <= 0){
			m_isFinished = true;
			stop();
		}
	}
	
	public void render(){
		StdDraw.image(m_x,m_y, m_image);
	}
	
	public GIFAnim(double x, double y, String path, int frames){
		m_x = x;
		m_y = y;
		m_frames = frames;
		m_image = new ImageIcon(path).getImage();
	}
	
}
