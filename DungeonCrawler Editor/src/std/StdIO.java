package std;


import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.swing.*;

public class StdIO{

	public enum MouseEventType{
		MouseClicked,
		MousePressed,
		MouseReleased,
		MouseDragged,
		MouseMoved;
		
		public final static int length = MouseEventType.values().length;
	}
	
	public enum KeyEventType{
		KeyTyped,
		KeyPressed,
		KeyReleased;
		
		public final static int length = KeyEventType.values().length;
	}
	
	public interface MouseListener{
		void receiveEvent(MouseEvent e);
	}
	public interface KeyListener{
		void receiveEvent(KeyEvent e);
	}
	
	private static ArrayList<ArrayList<MouseListener>> m_mouseListeners = new ArrayList<ArrayList<MouseListener>>(MouseEventType.length);
	private static ArrayList<ArrayList<KeyListener>> m_keyListeners = new ArrayList<ArrayList<KeyListener>>(MouseEventType.length);
	
	private static class MouseEventData{
		public MouseEvent mEvent;
		public MouseEventType mEventType;
		
		public MouseEventData(MouseEvent evt, MouseEventType evtType){
			mEvent = evt;
			mEventType = evtType;
		}
	}
	
	private static class KeyEventData{
		public KeyEvent mEvent;
		public KeyEventType mEventType;
		
		public KeyEventData(KeyEvent evt, KeyEventType evtType){
			mEvent = evt;
			mEventType = evtType;
		}
	}
	
	private static ArrayList<MouseEventData> m_mouseEvents = new ArrayList<MouseEventData>();
	private static ArrayList<KeyEventData> m_keyEvents = new ArrayList<KeyEventData>();
	
	
	public static void addMouseListener(MouseListener ml, MouseEventType type){
		m_mouseListeners.get(type.ordinal()).add(ml);
	}
	public static void addKeyListener(KeyListener kl, KeyEventType type){
		m_keyListeners.get(type.ordinal()).add(kl);
	}
	
	private static void queueUpMouseEvent(MouseEvent evt, MouseEventType type){
		m_mouseEvents.add( new MouseEventData(evt, type) );
	}
	private static void queueUpKeyEvent(KeyEvent evt, KeyEventType type){
		m_keyEvents.add( new KeyEventData(evt, type) );
	}
	
	public static void sendAllEvents(){
		synchronized(mouseLock){
			for( MouseEventData d : m_mouseEvents)
				for( MouseListener ml : m_mouseListeners.get(d.mEventType.ordinal()))
					ml.receiveEvent(d.mEvent);
					
			m_mouseEvents.clear();
		}	
		
		synchronized(mouseLock){
			for( KeyEventData d : m_keyEvents)
				for( KeyListener ml : m_keyListeners.get(d.mEventType.ordinal()))
					ml.receiveEvent(d.mEvent);
					
			m_keyEvents.clear();
		}
	}

	public static class KeyEventInfo{
		public int mKeyCode;
		public boolean mKeyDown;
		
		KeyEventInfo(int keyCode, boolean keyDown){
			mKeyCode = keyCode;
			mKeyDown = keyDown;
		}
	}
	public static class MouseEventInfo{
		public int mButton;
		public boolean mMouseDown;
		
		MouseEventInfo(int mouseCode, boolean mouseDown){
			mButton = mouseCode;
			mMouseDown = mouseDown;
		}
	}

	private static class EventListenerImpl implements java.awt.event.MouseListener, MouseMotionListener, java.awt.event.KeyListener{
	    public void mouseClicked(MouseEvent e) { 
	    	queueUpMouseEvent(e, MouseEventType.MouseClicked);
	    }
	    public void mouseEntered(MouseEvent e) { }
	    public void mouseExited(MouseEvent e) { }

	    public void mousePressed(MouseEvent e) {
	        synchronized (mouseLock) {
	        	queueUpMouseEvent(e, MouseEventType.MousePressed);
	        
	        	int button = e.getButton();
	        
	        	if(button >= NUMBER_OF_MOUSE_BUTTONS)
	        		return;
	        
	            mouseX = StdDraw.userX(e.getX());
	            mouseY = StdDraw.userY(e.getY());
	            
	            mousePressed[button] = true;
	        }
	    }

	    public void mouseReleased(MouseEvent e) {
	        synchronized (mouseLock) {
	        	queueUpMouseEvent(e, MouseEventType.MouseReleased);
	        
	        	int button = e.getButton();
		        
	        	if(button >= NUMBER_OF_MOUSE_BUTTONS)
	        		return;
	        
	            mousePressed[button] = false;
	        }
	    }

	    public void mouseDragged(MouseEvent e)  {
	        synchronized (mouseLock) {
	        	queueUpMouseEvent(e, MouseEventType.MouseDragged);
	            mouseX = StdDraw.userX(e.getX());
	            mouseY = StdDraw.userY(e.getY());
	        }
	    }

	    public void mouseMoved(MouseEvent e) {
	        synchronized (mouseLock) {
	        	queueUpMouseEvent(e, MouseEventType.MouseMoved);
	            mouseX = StdDraw.userX(e.getX());
	            mouseY = StdDraw.userY(e.getY());
	        }
	    }	
	    
	    public void keyTyped(KeyEvent e) {
	        synchronized (keyLock) {
	        	queueUpKeyEvent(e, KeyEventType.KeyTyped);
	        }
	    }

	    public void keyPressed(KeyEvent e) {
	        synchronized (keyLock) {
	        	queueUpKeyEvent(e, KeyEventType.KeyPressed);
	            keysDown.add(e.getKeyCode());
	        }
	    }

	    public void keyReleased(KeyEvent e) {
	        synchronized (keyLock) {
	        	queueUpKeyEvent(e, KeyEventType.KeyReleased);
	            keysDown.remove(e.getKeyCode());
	        }
	    }	
	}

	private static Object mouseLock = new Object();
    private static Object keyLock = new Object();
    
    private static final int NUMBER_OF_MOUSE_BUTTONS = 4;
    private static boolean mousePressed[] = new boolean[NUMBER_OF_MOUSE_BUTTONS];
    private static double mouseX = 0;
    private static double mouseY = 0;
   
    

    // set of key codes currently pressed down
    private static TreeSet<Integer> keysDown = new TreeSet<Integer>();
    
    static void init(JLabel draw, JFrame frame){
    
    	for(int i=0; i < MouseEventType.length; ++i){
			m_mouseListeners.add( new ArrayList<MouseListener>() );
		}
		for(int i=0; i < KeyEventType.length; ++i){
			m_keyListeners.add( new ArrayList<KeyListener>() );
		}
    
    	EventListenerImpl evtl = new EventListenerImpl();
    	
    	draw.addMouseListener(evtl);
        draw.addMouseMotionListener(evtl);

        frame.addKeyListener(evtl);    // JLabel cannot get keyboard focus
    }
    
    
    private StdIO(){
    	
    }

	  /*************************************************************************
	    *  Mouse interactions.
	    *************************************************************************/

	    /**
	     * Is the mouse being pressed?
	     * @return true or false
	     */
	    public static boolean mousePressed(int whichButton) {
	        synchronized (mouseLock) {
	        
	        	if(whichButton > NUMBER_OF_MOUSE_BUTTONS)
	        		return false;
	        	else
	        		return mousePressed[whichButton];
	        		
	        }
	    }
	    
	    public static boolean mousePressed(){
	    	return mousePressed(1);
	    }

	    /**
	     * What is the x-coordinate of the mouse?
	     * @return the value of the x-coordinate of the mouse
	     */
	    public static double mouseX() {
	        synchronized (mouseLock) {
	            return mouseX;
	        }
	    }

	    /**
	     * What is the y-coordinate of the mouse?
	     * @return the value of the y-coordinate of the mouse
	     */
	    public static double mouseY() {
	        synchronized (mouseLock) {
	            return mouseY;
	        }
	    }

	   /*************************************************************************
	    *  Keyboard interactions.
	    *************************************************************************/


	    /**
	     * Is the keycode currently being pressed? This method takes as an argument
	     * the keycode (corresponding to a physical key). It can handle action keys
	     * (such as F1 and arrow keys) and modifier keys (such as shift and control).
	     * See <a href = "http://download.oracle.com/javase/6/docs/api/java/awt/event/KeyEvent.html">KeyEvent.java</a>
	     * for a description of key codes.
	     * @return true if keycode is currently being pressed, false otherwise
	     */
	    public static boolean isKeyPressed(int keycode) {
	        synchronized (keyLock) {
	            return keysDown.contains(keycode);
	        }
	    }

}
