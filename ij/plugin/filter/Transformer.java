package ij.plugin.filter;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import ij.process.StackProcessor;

/** Implements the flip and rotate commands in the Image/Transformations submenu. */
public class Transformer implements PlugInFilter {
	
	ImagePlus imp;
	String arg;

	public int setup(String arg, ImagePlus imp) {
		this.arg = arg;
		this.imp = imp;
		if (arg.equals("fliph") || arg.equals("flipv"))
			return IJ.setupDialog(imp, DOES_ALL+NO_UNDO);
		else
			return DOES_ALL+NO_UNDO+NO_CHANGES;
	}

	public void run(ImageProcessor ip) {

		if (arg.equals("fliph")) {
			ip.flipHorizontal();
			return;
		}
		
		if (arg.equals("flipv")) {
			ip.flipVertical();
			return;
		}
		
		if (arg.equals("right") || arg.equals("left")) {
	    	StackProcessor sp = new StackProcessor(imp.getStack(), ip);
	    	ImageStack s2 = null;
			if (arg.equals("right"))
	    		s2 = sp.rotateRight();
	    	else
	    		s2 = sp.rotateLeft();
	    	imp.changes = false;
	    	imp.getWindow().close();
	    	new ImagePlus(imp.getTitle(), s2).show();
			return;
		}
	}

}
