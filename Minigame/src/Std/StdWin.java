package Std;


import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class StdWin {

    private static class ActionListenerImpl implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO: Use getter for StdDraw.frame
	        FileDialog chooser = new FileDialog(m_frame, "Use a .png or .jpg extension", FileDialog.SAVE);
	        chooser.setVisible(true);
	        String filename = chooser.getFile();
	        if (filename != null) {
	            StdDraw.screenshot(chooser.getDirectory() + File.separator + chooser.getFile());
	        }
	    }
    }

    static final int DEFAULT_WINDOW_HEIGHT = 600;
    static final int DEFAULT_WINDOW_WIDTH = 800;
    private static int m_width  = DEFAULT_WINDOW_WIDTH;
    private static int m_height = DEFAULT_WINDOW_HEIGHT;

    // the frame for drawing to the screen
    private static JFrame m_frame;

    public static int getWidth(){
    	return m_width;
    }
    
    public static int getHeight(){
    	return m_height;
    }

    static JFrame getFrame() {
		return m_frame;
	}

    /**
     * Set the window size to w-by-h pixels.
     *
     * @param w the width as a number of pixels
     * @param h the height as a number of pixels
     * @throws a RunTimeException if the width or height is 0 or negative
     */
    public static void setCanvasSize(int w, int h) {
        if (w < 1 || h < 1) throw new RuntimeException("width and height must be positive");
        m_width = w;
        m_height = h;
        init();
    }

    public static void shutdown(){
    	WindowEvent wev = new WindowEvent(m_frame, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    // init
    public static void init() {
    
    	if (m_frame != null) 
    		m_frame.setVisible(false);
    
        m_frame = new JFrame();

        JLabel draw = new JLabel();

        StdDraw.init(draw, m_frame);
        StdIO.init(draw, m_frame);

        m_frame.setContentPane(draw);
        m_frame.setResizable(false);
        m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            // closes all windows
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
        m_frame.setTitle("Standard Draw");
        m_frame.setJMenuBar(createMenuBar());
        m_frame.pack();
        m_frame.requestFocusInWindow();
        m_frame.setVisible(true);
    }

    // create the menu bar (changed to private)
    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItem1 = new JMenuItem(" Save...   ");
        menuItem1.addActionListener(new ActionListenerImpl());
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem1);
        return menuBar;
    }
	
}
