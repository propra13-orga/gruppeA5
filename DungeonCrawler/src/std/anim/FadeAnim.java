package std.anim;


import std.StdDraw;

public class FadeAnim extends AnimBase {
	private float m_alphaMin;
	private float m_alphaCurrent;
	private float m_alphaStep;
	private String m_toDraw;
	private double m_x;
	private double m_y;
	
	public FadeAnim(double x, double y, String toDraw){
		m_alphaMin = 0;
		m_alphaCurrent = 1.0f;
		m_alphaStep = 0.01f;
		
		m_toDraw = toDraw;
		m_x = x;
		m_y = y;
	}
	
	public FadeAnim(double x, double y, String toDraw, int timeInFrames){
		m_alphaMin = 0;
		m_alphaCurrent = 1.0f;
		m_alphaStep = 1.0f / timeInFrames;
		
		m_toDraw = toDraw;
		m_x = x;
		m_y = y;
	}
	
	public void render(){
		if(m_isPaused)
			return;
	
		StdDraw.setAlpha(m_alphaCurrent);
		StdDraw.picture(m_x, m_y, m_toDraw);
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
