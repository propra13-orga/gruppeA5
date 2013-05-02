package Std;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.TreeSet;
import javax.swing.*;

public class StdIO{

	private static class EventListenerImpl implements MouseListener, MouseMotionListener, KeyListener{
	    public void mouseClicked(MouseEvent e) { }
	    public void mouseEntered(MouseEvent e) { }
	    public void mouseExited(MouseEvent e) { }

	    public void mousePressed(MouseEvent e) {
	        synchronized (mouseLock) {
	        
	        	if(e.getButton() > NUMBER_OF_MOUSE_BUTTONS)
	        		return;
	        
	            mouseX = StdDraw.userX(e.getX());
	            mouseY = StdDraw.userY(e.getY());
	            mousePressed[e.getButton()] = true;
	            
	        }
	    }

	    public void mouseReleased(MouseEvent e) {
	        synchronized (mouseLock) {
	        
	        	if(e.getButton() > NUMBER_OF_MOUSE_BUTTONS)
	        		return;
	        		
	        	mousePressed[e.getButton()] = false;
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
	            keysTyped.addFirst(e.getKeyChar());
	        }
	    }

	    public void keyPressed(KeyEvent e) {
	        synchronized (keyLock) {
	            keysDown.add(e.getKeyCode());
	        }
	    }

	    public void keyReleased(KeyEvent e) {
	        synchronized (keyLock) {
	            keysDown.remove(e.getKeyCode());
	        }
	    }	
	}

	private static Object mouseLock = new Object();
    private static Object keyLock = new Object();
    
    private static final int NUMBER_OF_MOUSE_BUTTONS = 3;
    private static boolean mousePressed[] = new boolean[NUMBER_OF_MOUSE_BUTTONS];
    private static double mouseX = 0;
    private static double mouseY = 0;
   
    
 // queue of typed key characters
    private static LinkedList<Character> keysTyped = new LinkedList<Character>();

    // set of key codes currently pressed down
    private static TreeSet<Integer> keysDown = new TreeSet<Integer>();
    
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
	     * Has the user typed a key?
	     * @return true if the user has typed a key, false otherwise
	     */
	    public static boolean hasNextKeyTyped() {
	        synchronized (keyLock) {
	            return !keysTyped.isEmpty();
	        }
	    }

	    /**
	     * What is the next key that was typed by the user? This method returns
	     * a Unicode character corresponding to the key typed (such as 'a' or 'A').
	     * It cannot identify action keys (such as F1
	     * and arrow keys) or modifier keys (such as control).
	     * @return the next Unicode key typed
	     */
	    public static char nextKeyTyped() {
	        synchronized (keyLock) {
	            return keysTyped.removeLast();
	        }
	    }

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
