package std.anim;


public abstract class AnimBase {
	protected AnimAction m_runWhenDone = null;
	protected boolean m_isRunning = false;
	protected boolean m_isPaused = false;
	protected boolean m_isFinished = false;
	
	public interface AnimAction{
		public void run();
	}
	
	public void start(){
		m_isRunning = true;
		m_isPaused = false;
	}
	
	public void stop(){
		m_isRunning = false;
		m_isPaused = false;
	}
	
	public void pause(){
		m_isPaused = true;
	}
	
	public void resume(){
		m_isPaused = false;
	}
	
	public boolean isFinished(){
		return m_isFinished;
	}
	
	public void update(){
	
	}
	
	public void render(){
	
	}
	
	public void runWhenDone(AnimAction aa){
		m_runWhenDone = aa;
	}
	
	protected void runAnimAction(){
		if(m_runWhenDone != null) m_runWhenDone.run();
	}
}
