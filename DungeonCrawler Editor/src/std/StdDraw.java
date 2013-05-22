package std;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *  <i>Standard draw</i>. This class provides a basic capability for
 *  creating drawings with your programs. It uses a simple graphics model that
 *  allows you to create drawings consisting of points, lines, and curves
 *  in a window on your computer and to save the drawings to a file.
 *  <p>
 *  For additional documentation, see <a href="http://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 */
public final class StdDraw{
	
    // pre-defined colors
    public static final Color BLACK      = Color.BLACK;
    public static final Color BLUE       = Color.BLUE;
    public static final Color CYAN       = Color.CYAN;
    public static final Color DARK_GRAY  = Color.DARK_GRAY;
    public static final Color GRAY       = Color.GRAY;
    public static final Color GREEN      = Color.GREEN;
    public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
    public static final Color MAGENTA    = Color.MAGENTA;
    public static final Color ORANGE     = Color.ORANGE;
    public static final Color PINK       = Color.PINK;
    public static final Color RED        = Color.RED;
    public static final Color WHITE      = Color.WHITE;
    public static final Color YELLOW     = Color.YELLOW;
    public static final Color BOOK_BLUE      	= new Color(  9,  90, 166);
    public static final Color BOOK_LIGHT_BLUE 	= new Color(103, 198, 243);
    public static final Color BOOK_RED 			= new Color(150, 35, 31);

    // default colors
    private static final Color DEFAULT_PEN_COLOR   = BLACK;
    private static final Color DEFAULT_CLEAR_COLOR = WHITE;

    // current pen color
    private static Color m_penColor;

    private static int m_width;
    private static int m_height;

    // default pen radius
    private static final double DEFAULT_PEN_RADIUS = 1.0;

    // current pen radius
    private static double m_penRadius = DEFAULT_PEN_RADIUS;

    // show we draw immediately or wait until next show?
    private static boolean m_defer = false;

    // boundary of drawing canvas
    private static final double DEFAULT_XMIN = 0.0;
    private static final double DEFAULT_XMAX = StdWin.DEFAULT_WINDOW_WIDTH;
    private static final double DEFAULT_YMIN = 0.0;
    private static final double DEFAULT_YMAX = StdWin.DEFAULT_WINDOW_HEIGHT;
    private static double xmin, ymin, xmax, ymax;


    // default font
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);

    // current font
    private static Font font;

    // double buffered graphics
    private static BufferedImage offscreenImage, onscreenImage;
    private static Graphics2D offscreen, onscreen;

    private static HashMap<String, Image> imageCache = new HashMap<String, Image>();

    // singleton pattern: client can't instantiate
    private StdDraw() { }


    public static void close(){
    	WindowEvent wev = new WindowEvent(StdWin.getFrame(), WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    // init
    static void init(JLabel draw, JFrame frame){
    	m_width = StdWin.getWidth();
    	m_height = StdWin.getHeight();
    
        offscreenImage = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_ARGB);
        onscreenImage  = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_ARGB);
        offscreen = offscreenImage.createGraphics();
        onscreen  = onscreenImage.createGraphics();
        setXscale();
        setYscale();
        offscreen.setColor(DEFAULT_CLEAR_COLOR);
        offscreen.fillRect(0, 0, m_width, m_height);
        setPenColor();
        setPenRadius();
        setFont();
        clear();

        // add antialiasing
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                                  RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);

        // frame stuff
        ImageIcon icon = new ImageIcon(onscreenImage);
        draw.setIcon(icon);
    }
    
    static void init() {
    	System.out.println("ERR");
    	return;
    }

   /*************************************************************************
    *  User and screen coordinate systems
    *************************************************************************/

    /**
     * Set the x-scale to be the default (between 0.0 and 1.0).
     */
    public static void setXscale() { setXscale(DEFAULT_XMIN, DEFAULT_XMAX); }

    /**
     * Set the y-scale to be the default (between 0.0 and 1.0).
     */
    public static void setYscale() { setYscale(DEFAULT_YMIN, DEFAULT_YMAX); }

    /**
     * @param min the minimum value of the x-scale
     * @param max the maximum value of the x-scale
     */
    public static void setXscale(double min, double max) {
        //synchronized (mouseLock) {
            xmin = min;
            xmax = max;
        //}
    }

    /**
     * @param min the minimum value of the y-scale
     * @param max the maximum value of the y-scale
     */
    public static void setYscale(double min, double max) {
        //synchronized (mouseLock) {
            ymin = min;
            ymax = max;
        //}
    }

    /**
     * @param min the minimum value of the x- and y-scales
     * @param max the maximum value of the x- and y-scales
     */
    public static void setScale(double min, double max) {
        //synchronized (mouseLock) {
            xmin = min;
            xmax = max;
            ymin = min;
            ymax = max;
        //}
    }

    // helper functions that scale from user coordinates to screen coordinates and back
    private static double  scaleX(double x) { return m_width  * (x /*- xmin*/) / (xmax - xmin); }
    private static double  scaleY(double y) { return m_height * (/*ymax -*/ y) / (ymax - ymin); }
    private static double factorX(double w) { return w * m_width  / Math.abs(xmax - xmin);  }
    private static double factorY(double h) { return h * m_height / Math.abs(ymax - ymin);  }
    /*private*/ static double   userX(double x) { return /*xmin +*/ x * (xmax - xmin) / m_width;    }	//Only used for mouse evts
    /*private*/ static double   userY(double y) { return /*ymax -*/ y * (ymax - ymin) / m_height;   }	//


    /**
     * Clear the screen to the default color (white).
     */
    public static void clear() { clear(DEFAULT_CLEAR_COLOR); }
    /**
     * Clear the screen to the given color.
     * @param color the Color to make the background
     */
    public static void clear(Color color) {
        offscreen.setColor(color);
        offscreen.fillRect(0, 0, m_width, m_height);
        offscreen.setColor(m_penColor);
        draw();
    }

    /**
     * Get the current pen radius.
     */
    public static double getPenRadius() { return m_penRadius; }

    /**
     * Set the pen size to the default (.002).
     */
    public static void setPenRadius() { setPenRadius(DEFAULT_PEN_RADIUS); }
    /**
     * Set the radius of the pen to the given size.
     * @param r the radius of the pen
     * @throws RuntimeException if r is negative
     */
    public static void setPenRadius(double r) {
        if (r < 0) throw new RuntimeException("pen radius must be positive");
        m_penRadius = r;
        float scaledPenRadius = (float) r;
        BasicStroke stroke = new BasicStroke(scaledPenRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // BasicStroke stroke = new BasicStroke(scaledPenRadius);
        offscreen.setStroke(stroke);
    }

    /**
     * Get the current pen color.
     */
    public static Color getPenColor() { return m_penColor; }

    /**
     * Set the pen color to the default color (black).
     */
    public static void setPenColor() { setPenColor(DEFAULT_PEN_COLOR); }
    /**
     * Set the pen color to the given color. The available pen colors are
     * BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA,
     * ORANGE, PINK, RED, WHITE, and YELLOW.
     * @param color the Color to make the pen
     */
    public static void setPenColor(Color color) {
        m_penColor = color;
        offscreen.setColor(m_penColor);
    }

    /**
     * Get the current font.
     */
    public static Font getFont() { return font; }

    /**
     * Set the font to the default font (sans serif, 16 point).
     */
    public static void setFont() { setFont(DEFAULT_FONT); }

    /**
     * Set the font to the given value.
     * @param f the font to make text
     */
    public static void setFont(Font f) { font = f; }


   /*************************************************************************
    *  Drawing geometric shapes.
    *************************************************************************/

    /**
     * Draw a line from (x0, y0) to (x1, y1).
     * @param x0 the x-coordinate of the starting point
     * @param y0 the y-coordinate of the starting point
     * @param x1 the x-coordinate of the destination point
     * @param y1 the y-coordinate of the destination point
     */
    public static void line(double x0, double y0, double x1, double y1) {
        offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
        draw();
    }

    /**
     * Draw one pixel at (x, y).
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     */
    private static void pixel(double x, double y) {
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }

    /**
     * Draw a point at (x, y).
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     */
    public static void point(double x, double y) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        float scaledPenRadius = (float) m_penRadius;

        // double ws = factorX(2*r);
        // double hs = factorY(2*r);
        // if (ws <= 1 && hs <= 1) pixel(x, y);
        if (scaledPenRadius <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - scaledPenRadius/2, ys - scaledPenRadius/2,
                                                 scaledPenRadius, scaledPenRadius));
        draw();
    }

    /**
     * Draw a circle of radius r, centered on (x, y).
     * @param x the x-coordinate of the center of the circle
     * @param y the y-coordinate of the center of the circle
     * @param r the radius of the circle
     * @throws RuntimeException if the radius of the circle is negative
     */
     
    public static void circleCentered(double x, double y, double radius){
        circle(x - radius, y - radius, radius*2);
    }
     
    public static void circle(double x, double y, double diameter) {
        if (diameter < 0) throw new RuntimeException("circle diameter can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(diameter);
        double hs = factorY(diameter);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs, ys, ws, hs));
        draw();
    }

    /**
     * Draw filled circle of radius r, centered on (x, y).
     * @param x the x-coordinate of the center of the circle
     * @param y the y-coordinate of the center of the circle
     * @param r the radius of the circle
     * @throws RuntimeException if the radius of the circle is negative
     */
     
    public static void filledCircleCentered(double x, double y, double radius){
    	filledCircle(x - radius, y - radius, radius*2);
    }
     
    public static void filledCircle(double x, double y, double diameter) {
        if (diameter < 0) throw new RuntimeException("circle diameter can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(diameter);
        double hs = factorY(diameter);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs, ys, ws, hs));
        draw();
    }


    /**
     * Draw an ellipse with given semimajor and semiminor axes, centered on (x, y).
     * @param x the x-coordinate of the center of the ellipse
     * @param y the y-coordinate of the center of the ellipse
     * @param semiMajorAxis is the semimajor axis of the ellipse
     * @param semiMinorAxis is the semiminor axis of the ellipse
     * @throws RuntimeException if either of the axes are negative
     */
    public static void ellipseCentered(double x, double y, double semiMajorAxis, double semiMinorAxis){
    	ellipse(x-semiMajorAxis, y-semiMinorAxis, semiMajorAxis*2, semiMinorAxis*2);
    } 
     
    public static void ellipse(double x, double y, double bottomX, double bottomY) {
        if (bottomX < 0) throw new RuntimeException("ellipse semimajor axis can't be negative");
        if (bottomY < 0) throw new RuntimeException("ellipse semiminor axis can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(bottomX);
        double hs = factorY(bottomY);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs, ys, ws, hs));
        draw();
    }

    /**
     * Draw an ellipse with given semimajor and semiminor axes, centered on (x, y).
     * @param x the x-coordinate of the center of the ellipse
     * @param y the y-coordinate of the center of the ellipse
     * @param semiMajorAxis is the semimajor axis of the ellipse
     * @param semiMinorAxis is the semiminor axis of the ellipse
     * @throws RuntimeException if either of the axes are negative
     */
    public static void filledEllipseCentered(double x, double y, double semiMajorAxis, double semiMinorAxis){
    	filledEllipse(x-semiMajorAxis, y-semiMinorAxis, semiMajorAxis*2, semiMinorAxis*2);
    } 
     
    public static void filledEllipse(double x, double y, double bottomX, double bottomY) {
        if (bottomX < 0) throw new RuntimeException("ellipse semimajor axis can't be negative");
        if (bottomY < 0) throw new RuntimeException("ellipse semiminor axis can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(bottomX);
        double hs = factorY(bottomY);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs, ys, ws, hs));
        draw();
    }


    /**
     * Draw an arc of radius r, centered on (x, y), from angle1 to angle2 (in degrees).
     * @param x the x-coordinate of the center of the circle
     * @param y the y-coordinate of the center of the circle
     * @param r the radius of the circle
     * @param angle1 the starting angle. 0 would mean an arc beginning at 3 o'clock.
     * @param angle2 the angle at the end of the arc. For example, if
     *        you want a 90 degree arc, then angle2 should be angle1 + 90.
     * @throws RuntimeException if the radius of the circle is negative
     */
    public static void arcCentered(double x, double y, double r, double angle1, double angle2) {
        if (r < 0) throw new RuntimeException("arc radius can't be negative");
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
        draw();
    }

    /**
     * Draw a square of side length 2r, centered on (x, y).
     * @param x the x-coordinate of the center of the square
     * @param y the y-coordinate of the center of the square
     * @param r radius is half the length of any side of the square
     * @throws RuntimeException if r is negative
     */
    public static void squareCentered(double x, double y, double radius){
    	square(x-radius, y-radius, radius*2);
    }
     
    public static void square(double x, double y, double length) {
        if (length < 0) throw new RuntimeException("square side length can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(length);
        double hs = factorY(length);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Rectangle2D.Double(xs, ys, ws, hs));
        draw();
    }

    /**
     * Draw a filled square of side length 2r, centered on (x, y).
     * @param x the x-coordinate of the center of the square
     * @param y the y-coordinate of the center of the square
     * @param r radius is half the length of any side of the square
     * @throws RuntimeException if r is negative
     */
     
    public static void filledSquareCentered(double x, double y, double radius){
    	filledSquare(x-radius, y-radius, radius*2);
    }
     
    public static void filledSquare(double x, double y, double length) {
        if (length < 0) throw new RuntimeException("square side length can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(length);
        double hs = factorY(length);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Rectangle2D.Double(xs, ys, ws, hs));
        draw();
    }


    /**
     * Draw a rectangle of given half width and half height, centered on (x, y).
     * @param x the x-coordinate of the center of the rectangle
     * @param y the y-coordinate of the center of the rectangle
     * @param halfWidth is half the width of the rectangle
     * @param halfHeight is half the height of the rectangle
     * @throws RuntimeException if halfWidth or halfHeight is negative
     */
    public static void rectangleCentered(double x, double y, double halfWidth, double halfHeight){
    	rectangle(x-halfWidth, y-halfHeight, halfWidth*2, halfHeight*2);
    }
     
    public static void rectangle(double x, double y, double width, double height) {
        if (width  < 0) throw new RuntimeException("half width can't be negative");
        if (height < 0) throw new RuntimeException("half height can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(width);
        double hs = factorY(height);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Rectangle2D.Double(xs, ys, ws, hs));
        draw();
    }

    /**
     * Draw a filled rectangle of given half width and half height, centered on (x, y).
     * @param x the x-coordinate of the center of the rectangle
     * @param y the y-coordinate of the center of the rectangle
     * @param halfWidth is half the width of the rectangle
     * @param halfHeight is half the height of the rectangle
     * @throws RuntimeException if halfWidth or halfHeight is negative
     */
    public static void filledRectangleCentered(double x, double y, double halfWidth, double halfHeight){
    	filledRectangle(x-halfWidth, y-halfHeight, halfWidth*2, halfHeight*2);
    }
     
    public static void filledRectangle(double x, double y, double width, double height) {
        if (width  < 0) throw new RuntimeException("half width can't be negative");
        if (height < 0) throw new RuntimeException("half height can't be negative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(width);
        double hs = factorY(height);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Rectangle2D.Double(xs, ys, ws, hs));
        draw();
    }


    /**
     * Draw a polygon with the given (x[i], y[i]) coordinates.
     * @param x an array of all the x-coordindates of the polygon
     * @param y an array of all the y-coordindates of the polygon
     */
    public static void polygon(double[] x, double[] y) {
        int N = x.length;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < N; i++)
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
        offscreen.draw(path);
        draw();
    }

    /**
     * Draw a filled polygon with the given (x[i], y[i]) coordinates.
     * @param x an array of all the x-coordindates of the polygon
     * @param y an array of all the y-coordindates of the polygon
     */
    public static void filledPolygon(double[] x, double[] y) {
        int N = x.length;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < N; i++)
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
        offscreen.fill(path);
        draw();
    }



   /*************************************************************************
    *  Drawing images.
    *************************************************************************/

    // get an image from the given filename
    private static Image getImage(String filename) {

    	if( imageCache.containsKey(filename) ){
    		return imageCache.get(filename);
    	}

        // to read from file
        ImageIcon icon = new ImageIcon(filename);

        // try to read from URL
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            try {
                URL url = new URL(filename);
                icon = new ImageIcon(url);
            } catch (Exception e) { /* not a url */ }
        }

        // in case file is inside a .jar
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            URL url = StdDraw.class.getResource(filename);
            if (url == null) throw new RuntimeException("image " + filename + " not found");
            icon = new ImageIcon(url);
        }

        Image finished = icon.getImage();
        imageCache.put(filename, finished);

        return finished;
    }

    /**
     * Draw picture (gif, jpg, or png) centered on (x, y).
     * @param x the center x-coordinate of the image
     * @param y the center y-coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @throws RuntimeException if the image is corrupt
     */
    public static void pictureCentered(double x, double y, String s){
    	Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");

        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0), null);
        draw();
    }
     
    public static void picture(double x, double y, String s) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");

        offscreen.drawImage(image, (int) Math.round(xs), (int) Math.round(ys), null);
        draw();
    }

    /**
     * Draw picture (gif, jpg, or png) centered on (x, y),
     * rotated given number of degrees
     * @param x the center x-coordinate of the image
     * @param y the center y-coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public static void pictureCentered(double x, double y, String s, double degrees) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0), null);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);

        draw();
    }
     
    public static void picture(double x, double y, String s, double degrees) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs), (int) Math.round(ys), null);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);

        draw();
    }

    /**
     * Draw picture (gif, jpg, or png) centered on (x, y), rescaled to w-by-h.
     * @param x the center x coordinate of the image
     * @param y the center y coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param w the width of the image
     * @param h the height of the image
     * @throws RuntimeException if the width height are negative
     * @throws RuntimeException if the image is corrupt
     */
    public static void pictureCentered(double x, double y, String s, double w, double h) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        if (w < 0) throw new RuntimeException("width is negative: " + w);
        if (h < 0) throw new RuntimeException("height is negative: " + h);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else {
            offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                       (int) Math.round(ys - hs/2.0),
                                       (int) Math.round(ws),
                                       (int) Math.round(hs), null);
        }
        draw();
    }
     
    public static void picture(double x, double y, String s, double w, double h) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        if (w < 0) throw new RuntimeException("width is negative: " + w);
        if (h < 0) throw new RuntimeException("height is negative: " + h);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else {
            offscreen.drawImage(image, (int) Math.round(xs),
                                       (int) Math.round(ys),
                                       (int) Math.round(ws),
                                       (int) Math.round(hs), null);
        }
        draw();
    }


    /**
     * Draw picture (gif, jpg, or png) centered on (x, y), rotated
     * given number of degrees, rescaled to w-by-h.
     * @param x the center x-coordinate of the image
     * @param y the center y-coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param w the width of the image
     * @param h the height of the image
     * @param degrees is the number of degrees to rotate counterclockwise
     * @throws RuntimeException if the image is corrupt
     */
    public static void picture(double x, double y, String s, double w, double h, double degrees) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs),
                                   (int) Math.round(ys),
                                   (int) Math.round(ws),
                                   (int) Math.round(hs), null);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);

        draw();
    }
    
    public static void pictureCentered(double x, double y, String s, double w, double h, double degrees) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new RuntimeException("image " + s + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                   (int) Math.round(ys - hs/2.0),
                                   (int) Math.round(ws),
                                   (int) Math.round(hs), null);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);

        draw();
    }


   /*************************************************************************
    *  Drawing text.
    *************************************************************************/

    	//TODO: Convert all text methods

    /**
     * Write the given text string in the current font, centered on (x, y).
     * @param x the center x-coordinate of the text
     * @param y the center y-coordinate of the text
     * @param s the text
     */
    public static void text(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs - ws/2.0), (float) (ys + hs));
        draw();
    }

    /**
     * Write the given text string in the current font, centered on (x, y) and
     * rotated by the specified number of degrees  
     * @param x the center x-coordinate of the text
     * @param y the center y-coordinate of the text
     * @param s the text
     * @param degrees is the number of degrees to rotate counterclockwise
     */
    public static void text(double x, double y, String s, double degrees) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        text(x, y, s);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);
    }


    /**
     * Write the given text string in the current font, left-aligned at (x, y).
     * @param x the x-coordinate of the text
     * @param y the y-coordinate of the text
     * @param s the text
     */
    public static void textLeft(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs), (float) (ys + hs));
        draw();
    }

    /**
     * Write the given text string in the current font, right-aligned at (x, y).
     * @param x the x-coordinate of the text
     * @param y the y-coordinate of the text
     * @param s the text
     */
    public static void textRight(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs - ws), (float) (ys + hs));
        draw();
    }



    /**
     * Display on screen, pause for t milliseconds, and turn on
     * <em>animation mode</em>: subsequent calls to
     * drawing methods such as <tt>line()</tt>, <tt>circle()</tt>, and <tt>square()</tt>
     * will not be displayed on screen until the next call to <tt>show()</tt>.
     * This is useful for producing animations (clear the screen, draw a bunch of shapes,
     * display on screen for a fixed amount of time, and repeat). It also speeds up
     * drawing a huge number of shapes (call <tt>show(0)</tt> to defer drawing
     * on screen, draw the shapes, and call <tt>show(0)</tt> to display them all
     * on screen at once).
     * @param t number of milliseconds
     */
    public static void show(int t) {
        m_defer = false;
        draw();
        try { Thread.sleep(t); }
        catch (InterruptedException e) { System.out.println("Error sleeping"); }
        m_defer = true;
    }

    /**
     * Display on-screen and turn off animation mode:
     * subsequent calls to
     * drawing methods such as <tt>line()</tt>, <tt>circle()</tt>, and <tt>square()</tt>
     * will be displayed on screen when called. This is the default.
     */
    public static void show() {
        m_defer = false;
        draw();
    }

    // draw onscreen if defer is false
    private static void draw() {
        if (m_defer) return;
        onscreen.drawImage(offscreenImage, 0, 0, null);
        StdWin.getFrame().repaint();
    }


   /*************************************************************************
    *  Save drawing to a file.
    *************************************************************************/

    /**
     * Save onscreen image to file - suffix must be png, jpg, or gif.
     * @param filename the name of the file with one of the required suffixes
     */
    public static void screenshot(String filename) {
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);

        // png files
        if (suffix.toLowerCase().equals("png")) { //can also manage jpg files just fine...
            try { 
            
            //For image rescaling
            /*
            	Image thumbnail = onscreenImage.getScaledInstance(80, 60, Image.SCALE_SMOOTH);
            	BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null),
                        thumbnail.getHeight(null),
                        BufferedImage.TYPE_INT_RGB);
bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);

				ImageIO.write(bufferedThumbnail, suffix, file);
*/

            	ImageIO.write(onscreenImage, suffix, file); 
            }
            catch (IOException e) { e.printStackTrace(); }
        }

        // need to change from ARGB to RGB for jpeg
        // reference: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
        else if (suffix.toLowerCase().equals("jpg")) {
            WritableRaster raster = onscreenImage.getRaster();
            WritableRaster newRaster;
            newRaster = raster.createWritableChild(0, 0, m_width, m_height, 0, 0, new int[] {0, 1, 2});
            DirectColorModel cm = (DirectColorModel) onscreenImage.getColorModel();
            DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(),
                                                          cm.getRedMask(),
                                                          cm.getGreenMask(),
                                                          cm.getBlueMask());
            BufferedImage rgbBuffer = new BufferedImage(newCM, newRaster, false,  null);
            try { ImageIO.write(rgbBuffer, suffix, file); }
            catch (IOException e) { e.printStackTrace(); }
        }

        else {
            System.out.println("Invalid image file type: " + suffix);
        }
    }
 

}
