package std;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.TreeSet;
import javax.swing.*;

public class StdIO{

	public static class KeyEventInfo{
		//See the class KeyEvent for a listing of all key codes.
		public int mKeyCode;
		
		//'true' if the key was pressed down, 'false' if otherwise.
		public boolean mKeyDown;
		
		KeyEventInfo(int keyCode, boolean keyDown){
			mKeyCode = keyCode;
			mKeyDown = keyDown;
		}
	}
	
	public static class MouseEventInfo{
		//See the class MouseEvent for a listing of all button codes.
		public int mButton;
		
		//'true' if the button was pressed down, 'false' if otherwise
		public boolean mMouseDown;
		
		MouseEventInfo(int mouseCode, boolean mouseDown){
			mButton = mouseCode;
			mMouseDown = mouseDown;
		}
	}

	//Private listener class to collect input data from mouse and keyboard
	private static class EventListenerImpl implements MouseListener, MouseMotionListener, KeyListener{
	    public void mouseClicked(MouseEvent e) { }
	    public void mouseEntered(MouseEvent e) { }
	    public void mouseExited(MouseEvent e) { }

	    public void mousePressed(MouseEvent e) {
	        synchronized (mouseLock) {
	        	int button = e.getButton();
	        
	        	if(button >= NUMBER_OF_MOUSE_BUTTONS)
	        		return;
	        
	            mouseX = StdDraw.userX(e.getX());
	            mouseY = StdDraw.userY(e.getY());
	            mousePressed[button] = true;
	            
	            mouseEvents.add( new MouseEventInfo(button, true) );
	        }
	    }

	    public void mouseReleased(MouseEvent e) {
	        synchronized (mouseLock) {
	        	int button = e.getButton();
		        
	        	if(button >= NUMBER_OF_MOUSE_BUTTONS)
	        		return;
	        
	            mousePressed[button] = false;
	            
	            mouseEvents.add( new MouseEventInfo(button, false) );
	        }
	    }

	    public void mouseDragged(MouseEvent e)  {
	        synchronized (mouseLock) {
	            mouseX = StdDraw.userX(e.getX());
	            mouseY = StdDraw.userY(e.getY());
	        }
	    }

	    public void mouseMoved(MouseEvent e) {
	        synchronized (mouseLock) {
	            mouseX = StdDraw.userX(e.getX());
	            mouseY = StdDraw.userY(e.getY());
	        }
	    }	
	    
	    public void keyTyped(KeyEvent e) {
	        synchronized (keyLock) {
	            //keysTyped.addFirst(e.getKeyChar());
	        }
	    }

	    public void keyPressed(KeyEvent e) {
	        synchronized (keyLock) {
	        	
	            keysDown.add(e.getKeyCode());
	            keyEvents.add( new KeyEventInfo(e.getKeyCode(), true) );
	            
	        }
	    }

	    public void keyReleased(KeyEvent e) {
	        synchronized (keyLock) {
	        
	            keysDown.remove(e.getKeyCode());
	            keyEvents.add( new KeyEventInfo(e.getKeyCode(), false) );
	            
	        }
	    }	
	}

	//Objects to synchronize mouse or keyboard functions
	private static Object mouseLock = new Object();
    private static Object keyLock = new Object();
    
    //Mouse data
    private static final int NUMBER_OF_MOUSE_BUTTONS = 4;
    private static boolean mousePressed[] = new boolean[NUMBER_OF_MOUSE_BUTTONS];
    private static double mouseX = 0;
    private static double mouseY = 0;
   
    
    // queue of typed key characters
    //private static LinkedList<Character> keysTyped = new LinkedList<Character>();
    
    //Queue of mouse and keyboard events.
    private static LinkedList<KeyEventInfo> keyEvents = new LinkedList<KeyEventInfo>();
    private static LinkedList<MouseEventInfo> mouseEvents = new LinkedList<MouseEventInfo>();

    // set of key codes currently pressed down
    private static TreeSet<Integer> keysDown = new TreeSet<Integer>();
    
    //Will be called by StdWin, must be called once before StdIO methods can be used.
    static void init(JLabel draw, JFrame frame){
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

	    /**
	     * Returns true if the mouse event queue is not empty
	     */
	    public static boolean hasMouseEvents(){
	    	return !mouseEvents.isEmpty();
	    }

	    /**
	     * Returns the next mouse event from the event queue.
	     * Throws an exception if no mouse event is queued up.
	     */
	    public static MouseEventInfo pollMouseEvent(){
	    	return mouseEvents.removeFirst();
	    }
	    

	   /*************************************************************************
	    *  Keyboard interactions.
	    *************************************************************************/

	    /**
	     * Has the user typed a key?
	     * @return true if the user has typed a key, false otherwise
	     */
	     /*
	    public static boolean hasNextKeyTyped() {
	        synchronized (keyLock) {
	            return !keysTyped.isEmpty();
	        }
	    }
*/
	    /**
	     * What is the next key that was typed by the user? This method returns
	     * a Unicode character corresponding to the key typed (such as 'a' or 'A').
	     * It cannot identify action keys (such as F1
	     * and arrow keys) or modifier keys (such as control).
	     * @return the next Unicode key typed
	     */
/*	    public static char nextKeyTyped() {
	        synchronized (keyLock) {
	            return keysTyped.removeLast();
	        }
	    }*/

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

	    /**
	     * Returns true if the keyboard event queue is not empty
	     */
	    public static boolean hasKeyEvents(){
	    	return !keyEvents.isEmpty();
	    }

	    /**
	     * Returns the next keyboard event from the event queue.
	     * Throws an exception if no keyboard event is queued up.
	     */
	    public static KeyEventInfo pollKeyEvent(){
	    	return keyEvents.removeFirst();
	    }

}
