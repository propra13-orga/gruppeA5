package std.anim;


import java.awt.Image;

import std.StdDraw;

public class BGFadeAnim extends AnimBase {
	private float m_alphaMin;
	private float m_alphaCurrent;
	private float m_alphaStep;
	private Image m_image;
	
	public BGFadeAnim(){
		m_alphaMin = 0.f;
		m_alphaCurrent = 1.0f;
		m_alphaStep = 0.02f;
		
		m_image = StdDraw.getSnapshot();
	}
	
	public void render(){
		if(m_isPaused)
			return;
	
		StdDraw.setAlpha(m_alphaCurrent);
		StdDraw.image(0,0,m_image);
		StdDraw.setAlpha(1.0f);
	}
	
	public void update(){
		if(m_isFinished || m_isPaused || m_isRunning == false)
			return;
	
		if(m_alphaCurrent <= m_alphaMin){
			stop();
			m_isFinished = true;
			runAnimAction();
		}
	
		m_alphaCurrent -= m_alphaStep;
	}
}
