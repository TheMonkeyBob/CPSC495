package ij.plugin;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.io.OpenDialog;
import ij.io.RoiDecoder;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;

/** Opens ImageJ, NIH Image and Scion Image for windows ROI outlines. */
public class RoiReader implements PlugIn {
	final int polygon=0, rect=1, oval=2, line=3,freeLine=4, segLine=5, noRoi=6,freehand=7, traced=8;
	byte[] data;

	public void run(String arg) {
		OpenDialog od = new OpenDialog("Open ROI...", arg);
		String dir = od.getDirectory();
		String name = od.getFileName();
		if (name==null)
			return;
		try {
			openRoi(dir, name);
		} catch (IOException e) {
			String msg = e.getMessage();
			if (msg==null || msg.equals(""))
				msg = ""+e;
			IJ.showMessage("ROI Reader", msg);
		}
	}

	public void openRoi(String dir, String name) throws IOException {
		String path = dir+name;
		RoiDecoder rd = new RoiDecoder(path);
		Roi roi = rd.getRoi();
		Rectangle r = roi.getBoundingRect();
		ImagePlus img = WindowManager.getCurrentImage();
		if (img==null || img.getWidth()<(r.x+r.width) || img.getHeight()<(r.y+r.height)) {
			ImageProcessor ip =  new ByteProcessor(r.x+r.width+10, r.y+r.height+10);
			ip.setColor(Color.white);
			ip.fill();
			img = new ImagePlus(name, ip);
			img.show();
		}
		img.setRoi(roi);
	}

}