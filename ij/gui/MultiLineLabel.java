package ij.gui;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.StringTokenizer;

/**Custom component for displaying multiple lines. Based on 
   MultiLineLabel class from "Java in a Nutshell" by David Flanagan.*/
public class MultiLineLabel extends Canvas {
	String[] lines;
	int num_lines;
	int margin_width = 6;
	int margin_height = 6;
	int line_height;
	int line_ascent;
	int[] line_widths;
	int max_width;
    
    // Breaks the specified label up into an array of lines.
    public MultiLineLabel(String label) {
        StringTokenizer t = new StringTokenizer(label, "\n");
        num_lines = t.countTokens();
        lines = new String[num_lines];
        line_widths = new int[num_lines];
        for(int i = 0; i < num_lines; i++) lines[i] = t.nextToken();
    }
    

    // Figures out how wide each line of the label
    // is, and how wide the widest line is.
    protected void measure() {
        FontMetrics fm = this.getFontMetrics(this.getFont());
        // If we don't have font metrics yet, just return.
        if (fm == null) return;
        
        line_height = fm.getHeight();
        line_ascent = fm.getAscent();
        max_width = 0;
        for(int i = 0; i < num_lines; i++) {
            line_widths[i] = fm.stringWidth(lines[i]);
            if (line_widths[i] > max_width) max_width = line_widths[i];
        }
    }
    

    public void setFont(Font f) {
        super.setFont(f);
        measure();
        repaint();
    }


	// This method is invoked after our Canvas is first created
	// but before it can actually be displayed.  After we've
	// invoked our superclass's addNotify() method, we have font
	// metrics and can successfully call measure() to figure out
	// how big the label is.
	public void addNotify() {
		super.addNotify();
		measure();
	}
    

    // Called by a layout manager when it wants to
    // know how big we'd like to be.  
    public Dimension getPreferredSize() {
        return new Dimension(max_width + 2*margin_width, 
                     num_lines * line_height + 2*margin_height);
    }
    

    // Called when the layout manager wants to know
    // the bare minimum amount of space we need to get by.
    public Dimension getMinimumSize() {
        return new Dimension(max_width, num_lines * line_height);
    }
    
    // Draws the label
    public void paint(Graphics g) {
        int x, y;
        Dimension d = this.getSize();
        y = line_ascent + (d.height - num_lines * line_height)/2;
        for(int i = 0; i < num_lines; i++, y += line_height) {
            x = margin_width;
            g.drawString(lines[i], x, y);
        }
    }

}