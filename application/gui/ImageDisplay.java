/*
 *   MAGIC Tool, A microarray image and data analysis program
 *   Copyright (C) 2003  Laurie Heyer
 *
 *   This program is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU General Public License
 *   as published by the Free Software Foundation; either version 2
 *   of the License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *   Contact Information:
 *   Laurie Heyer
 *   Dept. of Mathematics
 *   PO Box 6959
 */
/*
 * Modified by Lukas Pihl
 */

package application.gui;

import application.engine.GuiManager;
import ij.ImagePlus;
import ij.gui.ImageCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;

/**
 * ImageDisplayPanel is a JPanel which displays a microarray image.
 */
public class ImageDisplay extends JPanel {

    private GuiManager manager;
    private int myNumber;

    private double zoomed=1.0; //magnification
    private BorderLayout borderLayout1 = new BorderLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    private int mode = SharedData.GRIDMODE_NORMAL;
    private int x_origin = 0;
    private int y_origin = 0;
    private int x_last = 0;
    private int y_last = 0;
    private int deadzone = 10;
    private boolean clicked = false;
    private JPanel panel_image = new JPanel();

    private Image im; //original image
    /**microarray image displayed in the panel*/
    protected ImagePlus ip;
    /**canvas displaying microarray image*/
    protected ImageCanvas ic;
    /**Currently selected grid**/
    private int current_grid_number;

    public ImageDisplay(GuiManager manager, int number)
    {

        this.manager = manager;
        this.myNumber = number;
        this.im = buildImage();

        ip = new ImagePlus("Overlayed",im);
        ic = new ImageCanvas(ip){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.setColor(Color.white);
                drawGrids(g);
            }
        };

        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        MouseAdapter ma = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                this_mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                this_mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                this_mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e)
            {
                this_mouseDragged(e);
            }
        };

        this.getCanvas().addMouseListener(ma);
        this.getCanvas().addMouseMotionListener(ma);
        this.getCanvas().addMouseWheelListener(ma);
    }

    /**
     * Paints the panel on the specified graphics
     * @param g graphics to paint the panel on
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0,0,this.getHeight(),this.getWidth());
        g.setColor(Color.white);
        super.paintComponent(g);

    }

    /**
     * magnifies the zoom by the given factor
     * @param zoomFactor factor to zoom by
     */
    public void zoom(double zoomFactor) {
        zoomed = zoomed*zoomFactor;
        ic.setMagnification(zoomed);
        ic.setImageUpdated();
        this.setPreferredSize(new Dimension(Math.round((float)(ip.getWidth()*zoomed)),Math.round((float)(ip.getHeight()*zoomed))));
        ic.repaint();
        this.repaint();
    }

    /**
     * sets the magnification level
     * @param magnification magnification level
     */
    public void setMagnification(double magnification)
    {
        ic.setMagnification(magnification);
        zoomed = magnification;
        ic.setImageUpdated();
        this.setPreferredSize(new Dimension(Math.round((float)(ip.getWidth()*zoomed)),Math.round((float)(ip.getHeight()*zoomed))));
        ic.repaint();
        this.repaint();
    }

    /**
     * gets the magnification
     * @return magnification level
     */
    public double getZoom(){
        return zoomed;
    }

    /**
     * gets the canvas the microarray image is painted on
     * @return canvas the microarray image is painted on
     */
    public ImageCanvas getCanvas() {
        return ic;
    }

    /**
     * gets the screen x-coordinate from a canvas x-coordinate
     * @param canvasX canvas x-coordinate
     * @return screen x-coordinate
     */
    public int screenX(int canvasX) {
        return (Math.round((float)this.getZoom()*(canvasX-this.ic.getSrcRect().x)));
    }

    /**
     * gets the screen y-coordinate from a canvas y-coordinate
     * @param canvasY canvas y-coordinate
     * @return screen y-coordinate
     */
    public int screenY(int canvasY) {
        return (Math.round((float)this.getZoom()*(canvasY-this.ic.getSrcRect().y)));
    }

    private void jbInit() throws Exception {
        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                this_mouseEntered(e);
            }
        });
        this.setBackground(Color.white);
        this.setLayout(borderLayout2);
        ic.setSrcRectPos(0,0);
        this.add(ic,BorderLayout.CENTER);
    }

    //sets the cursor when mouse enter the panel
    private void this_mouseEntered(MouseEvent e) {
        ic.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Draws a blue 2-px line on the panel from (x1,y1) to (x2,y2) - coordinates are on the <b>image</b>
     * @param x1 first x-coordinate on the image
     * @param y1 first y-coordinate on the image
     * @param x2 second x-coordinate on the image
     * @param y2 second y-coordinate on the image
     */
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        Graphics g = ic.getGraphics();
        //super.paintComponent(g);
        //paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(Color.blue);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(screenX(x1), screenY(y1), screenX(x2), screenY(y2));
    }

    //draws the grids on the panel
    private void drawGrids(Graphics g) {
        for(int i = 0; i<gridCount; i++){
            Polygon p = basepoly_list.get(i);
            if(p!=null ){
                Polygon newP = outerpoly_list.get(i);
                for(int j=0; j<p.xpoints.length; j++){
                    p.xpoints[j]=screenX(p.xpoints[j]);
                    p.ypoints[j]=screenY(p.ypoints[j]);
                    newP.xpoints[j]=screenX(newP.xpoints[j]);
                    newP.ypoints[j]=screenY(newP.ypoints[j]);
                }
                Polygon[] vertLines = vertlines_list.get(i);
                Polygon[] horiLines = horilines_list.get(i);

                if(i == current_grid_number) {
                    //Current Grid. Draw bounding rectangles.
                    g.setColor(Color.yellow);
                    g.drawPolygon(newP);
                    for(int v = 0;v<vertLines.length;v++) {
                        g.drawPolygon(vertLines[v]);
                    }
                    for(int h = 0;h<horiLines.length;h++) {
                        g.drawPolygon(horiLines[h]);
                    }
                    g.setColor(Color.LIGHT_GRAY);
                    //int h = GridMoverAdapter.height/4;

                    //GridMoverAdapter.updateRectangles();
                    //for(int z=0;z<4;z++) g.fillOval(screenX(GridMoverAdapter.vertices[z].x)-h/2, screenY(GridMoverAdapter.vertices[z].y)-h/2, h, h);
                }else{
                    g.setColor(Color.white);
                    //Draw other grids.
                    g.drawPolygon(newP);
                    for(int v = 0;v<vertLines.length;v++) {
                        g.drawPolygon(vertLines[v]);
                    }
                    for(int h = 0;h<horiLines.length;h++) {
                        g.drawPolygon(horiLines[h]);
                    }
                }
            }
        }
        //Draw the current grids little boxes so that we know where to click.
    }

    //This method will connec the first two lines.
    private void drawLines(Graphics g, int x1, int y1, int x2, int y2){
        for(int i = 0; i< manager.getSample_GridCount(myNumber); i++){
            //Grid grid = manager.getGrid(i);
            Polygon p = new Polygon();

        }
    }

    public void setCurrentGrid(int number)
    {
        current_grid_number = number;
        repaint();
    }

    private ArrayList<Double> angle_list = new ArrayList<>();
    private ArrayList<Polygon> masterpoly_list = new ArrayList<>();
    private ArrayList<Polygon> basepoly_list = new ArrayList<>();
    private ArrayList<Polygon> outerpoly_list = new ArrayList<>();
    private ArrayList<Polygon[]> vertlines_list = new ArrayList<>();
    private ArrayList<Polygon[]> horilines_list = new ArrayList<>();

    public Object[] getGrid(int i)
    {
        Object[] list = new Object[]{angle_list.get(i), masterpoly_list.get(i), basepoly_list.get(i),
                outerpoly_list.get(i), vertlines_list.get(i), horilines_list.get(i)};
        return list;
    }

    public void setNewGridDimensions(int grid, double angle, Polygon master, Polygon base, Polygon outer, Polygon[] vert, Polygon[] hori)
    {
        angle_list.set(grid, angle);
        masterpoly_list.set(grid, master);
        basepoly_list.set(grid, base);
        outerpoly_list.set(grid, outer);
        vertlines_list.set(grid, vert);
        horilines_list.set(grid, hori);
        repaint();
    }

    public void removeCurrentGrid()
    {
        angle_list.remove(current_grid_number);
        masterpoly_list.remove(current_grid_number);
        basepoly_list.remove(current_grid_number);
        outerpoly_list.remove(current_grid_number);
        vertlines_list.remove(current_grid_number);
        horilines_list.remove(current_grid_number);
        if (current_grid_number > basepoly_list.size() - 1)
        {
            current_grid_number = basepoly_list.size() - 1;
        }
        gridCount--;
    }

    private Image buildImage()
    {
        Image green = manager.getSample_GreenImage(myNumber);
        Image red = manager.getSample_RedImage(myNumber);

        Dimension redDim = new Dimension(red.getWidth(null), red.getHeight(null));
        Dimension greenDim = new Dimension(green.getWidth(null), green.getHeight(null));
        int w = greenDim.width;
        int h = greenDim.height;

        // Use green as base.
        int[] pixels = new int[w * h];
        int[] redpixels = new int[w * h];

        PixelGrabber pg = new PixelGrabber(green, 0, 0, w, h, pixels, 0, w);
        PixelGrabber redpg = new PixelGrabber(red, 0, 0, w, h, redpixels, 0, w);
        try
        {
            pg.grabPixels();
            redpg.grabPixels();
        } catch (Exception e)
        {
            System.out.print("(Error Grabbing Pixels) " + e);
        }

        for (int i = 0; i < pixels.length; i++)
        {
            int p = pixels[i];
            int redp = redpixels[i];
            int a = (p >> 24) & 0xFF;
            int r = (redp >> 16) & 0xFF;
            int b = 0;
            int g = (p >> 8) & 0xFF;

            pixels[i] = (a << 24 | r << 16 | g << 8 | b);
        }
        return createImage(new MemoryImageSource(w, h, pixels, 0, w));
    }

    /**
     * gets the actual x-coordinate on the image based on the screen
     * x-coordinate
     *
     * @param ex screen x-coordinate
     * @return actual x-coordinate on the image based on the screen x-coordinate
     */
    public int xCoordinate(int ex)
    {
        return ((this.getCanvas().getSrcRect().x + Math.round((float) ((ex) / this.getZoom()))));
    }

    /**
     * gets the actual y-coordinate on the image based on the screen
     * y-coordinate
     *
     * @param ey    screen y-coordinate
     * @return actual y-coordinate on the image based on the screen y-coordinate
     */
    public int yCoordinate(int ey)
    {
        return ((this.getCanvas().getSrcRect().y + Math.round((float) ((ey) / this.getZoom()))));
    }

    private void this_mousePressed(MouseEvent e)
    {
        switch (mode)
        {
            case SharedData.GRIDMODE_ROTATE:
                working_angle.set(current_grid_number, angle_list.get(current_grid_number));
                working_master.set(current_grid_number, new Polygon());
                for (int i = 0; i < masterpoly_list.get(current_grid_number).xpoints.length; i++)
                {
                    working_master.get(current_grid_number).addPoint(masterpoly_list.get(current_grid_number).xpoints[i], masterpoly_list.get(current_grid_number).ypoints[i]);
                }
                working_outer.set(current_grid_number, new Polygon());
                for (int i = 0; i < outerpoly_list.get(current_grid_number).xpoints.length; i++)
                {
                    working_outer.get(current_grid_number).addPoint(outerpoly_list.get(current_grid_number).xpoints[i], outerpoly_list.get(current_grid_number).ypoints[i]);
                }
                working_vert.set(current_grid_number, new Polygon[vertlines_list.get(current_grid_number).length]);
                for (int i = 0; i < vertlines_list.get(current_grid_number).length; i++)
                {
                    working_vert.get(current_grid_number)[i] = new Polygon();
                    for (int j = 0; j < vertlines_list.get(current_grid_number)[i].xpoints.length; j++)
                    {
                        working_vert.get(current_grid_number)[i].addPoint(vertlines_list.get(current_grid_number)[i].xpoints[j], vertlines_list.get(current_grid_number)[i].ypoints[j]);
                    }
                }
                working_hori.set(current_grid_number, new Polygon[horilines_list.get(current_grid_number).length]);
                for (int i = 0; i < horilines_list.get(current_grid_number).length; i++)
                {
                    working_hori.get(current_grid_number)[i] = new Polygon();
                    for (int j = 0; j < horilines_list.get(current_grid_number)[i].xpoints.length; j++)
                    {
                        working_hori.get(current_grid_number)[i].addPoint(horilines_list.get(current_grid_number)[i].xpoints[j], horilines_list.get(current_grid_number)[i].ypoints[j]);
                    }
                }
                break;
            case SharedData.GRIDMODE_RESIZE:
                working_master.set(current_grid_number, new Polygon());
                for (int i = 0; i < masterpoly_list.get(current_grid_number).xpoints.length; i++)
                {
                    working_master.get(current_grid_number).addPoint(masterpoly_list.get(current_grid_number).xpoints[i], masterpoly_list.get(current_grid_number).ypoints[i]);
                }
                working_outer.set(current_grid_number, new Polygon());
                for (int i = 0; i < outerpoly_list.get(current_grid_number).xpoints.length; i++)
                {
                    working_outer.get(current_grid_number).addPoint(outerpoly_list.get(current_grid_number).xpoints[i], outerpoly_list.get(current_grid_number).ypoints[i]);
                }
                working_vert.set(current_grid_number, new Polygon[vertlines_list.get(current_grid_number).length]);
                for (int i = 0; i < vertlines_list.get(current_grid_number).length; i++)
                {
                    working_vert.get(current_grid_number)[i] = new Polygon();
                    for (int j = 0; j < vertlines_list.get(current_grid_number)[i].xpoints.length; j++)
                    {
                        working_vert.get(current_grid_number)[i].addPoint(vertlines_list.get(current_grid_number)[i].xpoints[j], vertlines_list.get(current_grid_number)[i].ypoints[j]);
                    }
                }
                working_hori.set(current_grid_number, new Polygon[horilines_list.get(current_grid_number).length]);
                for (int i = 0; i < horilines_list.get(current_grid_number).length; i++)
                {
                    working_hori.get(current_grid_number)[i] = new Polygon();
                    for (int j = 0; j < horilines_list.get(current_grid_number)[i].xpoints.length; j++)
                    {
                        working_hori.get(current_grid_number)[i].addPoint(horilines_list.get(current_grid_number)[i].xpoints[j], horilines_list.get(current_grid_number)[i].ypoints[j]);
                    }
                }
                long dx = Math.abs((long)(working_outer.get(current_grid_number).xpoints[3] - working_outer.get(current_grid_number).xpoints[0]));
                long dy = Math.abs((long)(working_outer.get(current_grid_number).ypoints[3] - working_outer.get(current_grid_number).ypoints[0]));
                double d2 = dx * dx + dy * dy;
                working_outer_height = (int)Math.round(Math.sqrt(d2));
                current_outer_height = working_outer_height;

                dx = Math.abs((long)(working_outer.get(current_grid_number).xpoints[1] - working_outer.get(current_grid_number).xpoints[0]));
                dy = Math.abs((long)(working_outer.get(current_grid_number).ypoints[1] - working_outer.get(current_grid_number).ypoints[0]));
                d2 = dx * dx + dy * dy;
                working_outer_width = (int)Math.round(Math.sqrt(d2));
                current_outer_width = working_outer_width;
                break;
            default:
                break;
        }
        x_origin = xCoordinate(e.getX());
        y_origin = yCoordinate(e.getY());
        x_last = x_origin;
        y_last = y_origin;
        clicked = true;
    }

    private void this_mouseReleased(MouseEvent e)
    {
        switch (mode)
        {
            case SharedData.GRIDMODE_MOVE:
                manager.setSample_Grid_MoveTo(myNumber, current_grid_number, masterpoly_list.get(current_grid_number).xpoints[0],
                        masterpoly_list.get(current_grid_number).ypoints[0]);
                refreshCurrentGrids();
                break;
            case SharedData.GRIDMODE_ROTATE:
                manager.setSample_Grid_RotateTo(myNumber, current_grid_number, angle_list.get(current_grid_number));
                refreshCurrentGrids();
                break;
            case SharedData.GRIDMODE_RESIZE:
                manager.setSample_Grid_ResizeTo(myNumber, current_grid_number, masterpoly_list.get(current_grid_number).ypoints[3] -
                        masterpoly_list.get(current_grid_number).ypoints[0], masterpoly_list.get(current_grid_number).xpoints[1] - masterpoly_list.get(current_grid_number).xpoints[0]);
                refreshCurrentGrids();
                break;
            default:
                break;
        }
        clicked = false;
    }

    private void this_mouseClicked(MouseEvent e)
    {
        switch (mode)
        {
            case SharedData.GRIDMODE_SETUP:
                manager.coordinateFound(xCoordinate(e.getX()), yCoordinate(e.getY()));
                break;
            case SharedData.GRIDMODE_MOVETO:
                int x_diff = xCoordinate(e.getX()) - masterpoly_list.get(current_grid_number).xpoints[0];
                int y_diff = yCoordinate(e.getY()) - masterpoly_list.get(current_grid_number).ypoints[0];
                masterpoly_list.get(current_grid_number).xpoints[0] += x_diff;
                masterpoly_list.get(current_grid_number).ypoints[0] += y_diff;
                manager.setSample_Grid_MoveTo(myNumber, current_grid_number, masterpoly_list.get(current_grid_number).xpoints[0],
                        masterpoly_list.get(current_grid_number).ypoints[0]);
                refreshCurrentGrids();
                break;
            default:
                break;
        }
    }

    private boolean refreshing = false;
    private void this_mouseDragged(MouseEvent e)
    {
        switch(mode)
        {
            case SharedData.GRIDMODE_NORMAL:
                System.out.println("SPIT");
                int deltaX = x_last - e.getX();
                int deltaY = y_last - e.getY();
                if (!refreshing)
                {
                    scrollBar_vert.setValue(scrollBar_vert.getValue() + deltaY);
                    scrollBar_hori.setValue(scrollBar_hori.getValue() + deltaX);
                }
                x_last = e.getX();
                y_last = e.getY();
                refreshing = !refreshing;
                break;
            case SharedData.GRIDMODE_MOVE:
                int x_diff = xCoordinate(e.getX()) - x_last;
                int y_diff = yCoordinate(e.getY()) - y_last;
                for (int i = 0; i < masterpoly_list.get(current_grid_number).xpoints.length; i++)
                {
                    masterpoly_list.get(current_grid_number).xpoints[i] += x_diff;
                    masterpoly_list.get(current_grid_number).ypoints[i] += y_diff;
                }
                for (int i = 0; i < outerpoly_list.get(current_grid_number).xpoints.length; i++)
                {
                    outerpoly_list.get(current_grid_number).xpoints[i] += x_diff;
                    outerpoly_list.get(current_grid_number).ypoints[i] += y_diff;
                }
                for (int i = 0; i < vertlines_list.get(current_grid_number).length; i++)
                {
                    for (int j = 0; j < vertlines_list.get(current_grid_number)[i].xpoints.length; j++)
                    {
                        vertlines_list.get(current_grid_number)[i].xpoints[j] += x_diff;
                        vertlines_list.get(current_grid_number)[i].ypoints[j] += y_diff;
                    }
                }
                for (int i = 0; i < horilines_list.get(current_grid_number).length; i++)
                {
                    for (int j = 0; j < horilines_list.get(current_grid_number)[i].xpoints.length; j++)
                    {
                        horilines_list.get(current_grid_number)[i].xpoints[j] += x_diff;
                        horilines_list.get(current_grid_number)[i].ypoints[j] += y_diff;
                    }
                }
                x_last = xCoordinate(e.getX());
                y_last = yCoordinate(e.getY());
                break;
            case SharedData.GRIDMODE_ROTATE:
                if (yCoordinate(e.getY()) > y_origin + deadzone || yCoordinate(e.getY()) < y_origin - deadzone)
                {
                    Point2D.Double done = new Point2D.Double();
                    Point2D.Double start;
                    AffineTransform aTransform = new AffineTransform();
                    if (yCoordinate(e.getY()) > y_origin)
                    {
                        angle_list.set(current_grid_number, working_angle.get(current_grid_number) +
                                (yCoordinate(e.getY()) - y_origin - deadzone) * 0.001);
                        aTransform.setToRotation((yCoordinate(e.getY()) - y_origin - deadzone) * 0.001,
                                working_master.get(current_grid_number).xpoints[0],
                                working_master.get(current_grid_number).ypoints[0]);
                    }
                    else
                    {
                        angle_list.set(current_grid_number, working_angle.get(current_grid_number) +
                                (yCoordinate(e.getY()) - y_origin + deadzone) * 0.001);
                        aTransform.setToRotation((yCoordinate(e.getY()) - y_origin + deadzone) * 0.001,
                                working_master.get(current_grid_number).xpoints[0],
                                working_master.get(current_grid_number).ypoints[0]);
                    }

                    start = new Point2D.Double(working_outer.get(current_grid_number).xpoints[0],
                            working_outer.get(current_grid_number).ypoints[0]);
                    aTransform.transform(start, done);
                    outerpoly_list.get(current_grid_number).xpoints[0] = (int)Math.round(done.getX());
                    outerpoly_list.get(current_grid_number).ypoints[0] = (int)Math.round(done.getY());

                    start = new Point2D.Double(working_outer.get(current_grid_number).xpoints[1],
                            working_outer.get(current_grid_number).ypoints[1]);
                    aTransform.transform(start, done);
                    outerpoly_list.get(current_grid_number).xpoints[1] = (int)Math.round(done.getX());
                    outerpoly_list.get(current_grid_number).ypoints[1] = (int)Math.round(done.getY());

                    start = new Point2D.Double(working_outer.get(current_grid_number).xpoints[2],
                            working_outer.get(current_grid_number).ypoints[2]);
                    aTransform.transform(start, done);
                    outerpoly_list.get(current_grid_number).xpoints[2] = (int)Math.round(done.getX());
                    outerpoly_list.get(current_grid_number).ypoints[2] = (int)Math.round(done.getY());

                    start = new Point2D.Double(working_outer.get(current_grid_number).xpoints[3],
                            working_outer.get(current_grid_number).ypoints[3]);
                    aTransform.transform(start, done);
                    outerpoly_list.get(current_grid_number).xpoints[3] = (int)Math.round(done.getX());
                    outerpoly_list.get(current_grid_number).ypoints[3] = (int)Math.round(done.getY());

                    double dy1 = (double)(outerpoly_list.get(current_grid_number).ypoints[1] -
                            (outerpoly_list.get(current_grid_number).ypoints[0])) /
                            (double)vertlines_list.get(current_grid_number).length;
                    double dx1 = (double)(outerpoly_list.get(current_grid_number).xpoints[1] -
                            (outerpoly_list.get(current_grid_number).xpoints[0])) /
                            (double)vertlines_list.get(current_grid_number).length;
                    for(int j = 0; j < vertlines_list.get(current_grid_number).length; j++)
                    {
                        vertlines_list.get(current_grid_number)[j].xpoints[0] = outerpoly_list.get(current_grid_number).xpoints[0] + (int)((j + 1) * dx1);
                        vertlines_list.get(current_grid_number)[j].ypoints[0] = outerpoly_list.get(current_grid_number).ypoints[0] + (int)((j + 1) * dy1);
                        vertlines_list.get(current_grid_number)[j].xpoints[1] = outerpoly_list.get(current_grid_number).xpoints[3] + (int)((j + 1) * dx1);
                        vertlines_list.get(current_grid_number)[j].ypoints[1] = outerpoly_list.get(current_grid_number).ypoints[3] + (int)((j + 1) * dy1);
                        vertlines_list.get(current_grid_number)[j].xpoints[2] = outerpoly_list.get(current_grid_number).xpoints[3] + (int)((j) * dx1);
                        vertlines_list.get(current_grid_number)[j].ypoints[2] = outerpoly_list.get(current_grid_number).ypoints[3] + (int)((j) * dy1);
                        vertlines_list.get(current_grid_number)[j].xpoints[3] = outerpoly_list.get(current_grid_number).xpoints[0] + (int)((j) * dx1);
                        vertlines_list.get(current_grid_number)[j].ypoints[3] = outerpoly_list.get(current_grid_number).ypoints[0] + (int)((j) * dy1);
                    }

                    double dy2 = (double)(outerpoly_list.get(current_grid_number).ypoints[3] - (outerpoly_list.get(current_grid_number).ypoints[0])) /
                            (double)horilines_list.get(current_grid_number).length;
                    double dx2 = (double)(outerpoly_list.get(current_grid_number).xpoints[3] - (outerpoly_list.get(current_grid_number).xpoints[0])) /
                            (double)horilines_list.get(current_grid_number).length;
                    for(int k = 0; k < horilines_list.get(current_grid_number).length; k++)
                    {
                        horilines_list.get(current_grid_number)[k].xpoints[0] = outerpoly_list.get(current_grid_number).xpoints[0] + (int)((k + 1) * dx2);
                        horilines_list.get(current_grid_number)[k].ypoints[0] = outerpoly_list.get(current_grid_number).ypoints[0] + (int)((k + 1) * dy2);
                        horilines_list.get(current_grid_number)[k].xpoints[1] = outerpoly_list.get(current_grid_number).xpoints[1] + (int)((k + 1) * dx2);
                        horilines_list.get(current_grid_number)[k].ypoints[1] = outerpoly_list.get(current_grid_number).ypoints[1] + (int)((k + 1) * dy2);
                        horilines_list.get(current_grid_number)[k].xpoints[2] = outerpoly_list.get(current_grid_number).xpoints[1] + (int)((k) * dx2);
                        horilines_list.get(current_grid_number)[k].ypoints[2] = outerpoly_list.get(current_grid_number).ypoints[1] + (int)((k) * dy2);
                        horilines_list.get(current_grid_number)[k].xpoints[3] = outerpoly_list.get(current_grid_number).xpoints[0] + (int)((k) * dx2);
                        horilines_list.get(current_grid_number)[k].ypoints[3] = outerpoly_list.get(current_grid_number).ypoints[0] + (int)((k) * dy2);
                    }
                }
                else
                {
                    angle_list.set(current_grid_number, working_angle.get(current_grid_number));
                    outerpoly_list.set(current_grid_number, new Polygon());
                    for (int i = 0; i < working_outer.get(current_grid_number).xpoints.length; i++)
                    {
                        outerpoly_list.get(current_grid_number).addPoint(working_outer.get(current_grid_number).xpoints[i], working_outer.get(current_grid_number).ypoints[i]);
                    }
                    vertlines_list.set(current_grid_number, new Polygon[working_vert.get(current_grid_number).length]);
                    for (int i = 0; i < working_vert.get(current_grid_number).length; i++)
                    {
                        vertlines_list.get(current_grid_number)[i] = new Polygon();
                        for (int j = 0; j < working_vert.get(current_grid_number)[i].xpoints.length; j++)
                        {
                            vertlines_list.get(current_grid_number)[i].addPoint(working_vert.get(current_grid_number)[i].xpoints[j], working_vert.get(current_grid_number)[i].ypoints[j]);
                        }
                    }
                    horilines_list.set(current_grid_number, new Polygon[working_hori.get(current_grid_number).length]);
                    for (int i = 0; i < working_hori.get(current_grid_number).length; i++)
                    {
                        horilines_list.get(current_grid_number)[i] = new Polygon();
                        for (int j = 0; j < working_hori.get(current_grid_number)[i].xpoints.length; j++)
                        {
                            horilines_list.get(current_grid_number)[i].addPoint(working_hori.get(current_grid_number)[i].xpoints[j], working_hori.get(current_grid_number)[i].ypoints[j]);
                        }
                    }
                }
                x_last = xCoordinate(e.getX());
                y_last = yCoordinate(e.getY());
                break;
            case SharedData.GRIDMODE_RESIZE:
                if (yCoordinate(e.getY()) > y_origin + deadzone || yCoordinate(e.getY()) < y_origin - deadzone)
                {
                    int diff;
                    if (yCoordinate(e.getY()) > y_origin)
                    {
                        diff = yCoordinate(e.getY()) - y_origin - deadzone;
                    }
                    else
                    {
                        diff = yCoordinate(e.getY()) - y_origin + deadzone;
                    }
                    masterpoly_list.get(current_grid_number).ypoints[2] = working_master.get(current_grid_number).ypoints[2] + diff;
                    masterpoly_list.get(current_grid_number).ypoints[3] = working_master.get(current_grid_number).ypoints[3] + diff;
                    current_outer_height = working_outer_height + diff;
                }
                if (xCoordinate(e.getX()) > x_origin + deadzone || xCoordinate(e.getX()) < x_origin - deadzone)
                {
                    int diff;
                    if (xCoordinate(e.getX()) > x_origin)
                    {
                        diff = xCoordinate(e.getX()) - x_origin - deadzone;
                    }
                    else
                    {
                        diff = xCoordinate(e.getX()) - x_origin + deadzone;
                    }
                    masterpoly_list.get(current_grid_number).xpoints[1] = working_master.get(current_grid_number).xpoints[1] + diff;
                    masterpoly_list.get(current_grid_number).xpoints[2] = working_master.get(current_grid_number).xpoints[2] + diff;
                    current_outer_width = working_outer_width + diff;
                }
                if (yCoordinate(e.getY()) > y_origin + deadzone || yCoordinate(e.getY()) < y_origin - deadzone ||
                        xCoordinate(e.getX()) > x_origin + deadzone || xCoordinate(e.getX()) < x_origin - deadzone)
                {
                    Point2D.Double done = new Point2D.Double();
                    Point2D.Double start;
                    AffineTransform aTransform = new AffineTransform();
                    aTransform.setToRotation(angle_list.get(current_grid_number), working_outer.get(current_grid_number).xpoints[0], working_outer.get(current_grid_number).ypoints[0]);

                    start = new Point2D.Double(current_outer_width + working_outer.get(current_grid_number).xpoints[0],
                            working_outer.get(current_grid_number).ypoints[0]);
                    aTransform.transform(start, done);
                    outerpoly_list.get(current_grid_number).xpoints[1] = (int)Math.round(done.getX());
                    outerpoly_list.get(current_grid_number).ypoints[1] = (int)Math.round(done.getY());

                    start = new Point2D.Double(working_outer.get(current_grid_number).xpoints[0], current_outer_height +
                            working_outer.get(current_grid_number).ypoints[0]);
                    aTransform.transform(start, done);
                    outerpoly_list.get(current_grid_number).xpoints[3] = (int)Math.round(done.getX());
                    outerpoly_list.get(current_grid_number).ypoints[3] = (int)Math.round(done.getY());

                    start = new Point2D.Double(current_outer_width + working_outer.get(current_grid_number).xpoints[0], current_outer_height +
                            working_outer.get(current_grid_number).ypoints[0]);
                    aTransform.transform(start, done);
                    outerpoly_list.get(current_grid_number).xpoints[2] = (int)Math.round(done.getX());
                    outerpoly_list.get(current_grid_number).ypoints[2] = (int)Math.round(done.getY());

                    double dy1 = (double)(outerpoly_list.get(current_grid_number).ypoints[1] - (outerpoly_list.get(current_grid_number).ypoints[0])) /
                            (double)vertlines_list.get(current_grid_number).length;
                    double dx1 = (double)(outerpoly_list.get(current_grid_number).xpoints[1] - (outerpoly_list.get(current_grid_number).xpoints[0])) /
                            (double)vertlines_list.get(current_grid_number).length;
                    for(int j = 0; j < vertlines_list.get(current_grid_number).length; j++)
                    {
                        vertlines_list.get(current_grid_number)[j].xpoints[0] = outerpoly_list.get(current_grid_number).xpoints[0] + (int)((j + 1) * dx1);
                        vertlines_list.get(current_grid_number)[j].ypoints[0] = outerpoly_list.get(current_grid_number).ypoints[0] + (int)((j + 1) * dy1);
                        vertlines_list.get(current_grid_number)[j].xpoints[1] = outerpoly_list.get(current_grid_number).xpoints[3] + (int)((j + 1) * dx1);
                        vertlines_list.get(current_grid_number)[j].ypoints[1] = outerpoly_list.get(current_grid_number).ypoints[3] + (int)((j + 1) * dy1);
                        vertlines_list.get(current_grid_number)[j].xpoints[2] = outerpoly_list.get(current_grid_number).xpoints[3] + (int)((j) * dx1);
                        vertlines_list.get(current_grid_number)[j].ypoints[2] = outerpoly_list.get(current_grid_number).ypoints[3] + (int)((j) * dy1);
                        vertlines_list.get(current_grid_number)[j].xpoints[3] = outerpoly_list.get(current_grid_number).xpoints[0] + (int)((j) * dx1);
                        vertlines_list.get(current_grid_number)[j].ypoints[3] = outerpoly_list.get(current_grid_number).ypoints[0] + (int)((j) * dy1);
                    }

                    double dy2 = (double)(outerpoly_list.get(current_grid_number).ypoints[3] - (outerpoly_list.get(current_grid_number).ypoints[0])) /
                            (double)horilines_list.get(current_grid_number).length;
                    double dx2 = (double)(outerpoly_list.get(current_grid_number).xpoints[3] - (outerpoly_list.get(current_grid_number).xpoints[0])) /
                            (double)horilines_list.get(current_grid_number).length;
                    for(int k = 0; k < horilines_list.get(current_grid_number).length; k++)
                    {
                        horilines_list.get(current_grid_number)[k].xpoints[0] = outerpoly_list.get(current_grid_number).xpoints[0] + (int)((k + 1) * dx2);
                        horilines_list.get(current_grid_number)[k].ypoints[0] = outerpoly_list.get(current_grid_number).ypoints[0] + (int)((k + 1) * dy2);
                        horilines_list.get(current_grid_number)[k].xpoints[1] = outerpoly_list.get(current_grid_number).xpoints[1] + (int)((k + 1) * dx2);
                        horilines_list.get(current_grid_number)[k].ypoints[1] = outerpoly_list.get(current_grid_number).ypoints[1] + (int)((k + 1) * dy2);
                        horilines_list.get(current_grid_number)[k].xpoints[2] = outerpoly_list.get(current_grid_number).xpoints[1] + (int)((k) * dx2);
                        horilines_list.get(current_grid_number)[k].ypoints[2] = outerpoly_list.get(current_grid_number).ypoints[1] + (int)((k) * dy2);
                        horilines_list.get(current_grid_number)[k].xpoints[3] = outerpoly_list.get(current_grid_number).xpoints[0] + (int)((k) * dx2);
                        horilines_list.get(current_grid_number)[k].ypoints[3] = outerpoly_list.get(current_grid_number).ypoints[0] + (int)((k) * dy2);
                    }
                }
                else
                {
                    masterpoly_list.set(current_grid_number, new Polygon());
                    for (int i = 0; i < working_master.get(current_grid_number).xpoints.length; i++)
                    {
                        masterpoly_list.get(current_grid_number).addPoint(working_master.get(current_grid_number).xpoints[i], working_master.get(current_grid_number).ypoints[i]);
                    }
                    outerpoly_list.set(current_grid_number, new Polygon());
                    for (int i = 0; i < working_outer.get(current_grid_number).xpoints.length; i++)
                    {
                        outerpoly_list.get(current_grid_number).addPoint(working_outer.get(current_grid_number).xpoints[i], working_outer.get(current_grid_number).ypoints[i]);
                    }
                    vertlines_list.set(current_grid_number, new Polygon[working_vert.get(current_grid_number).length]);
                    for (int i = 0; i < working_vert.get(current_grid_number).length; i++)
                    {
                        vertlines_list.get(current_grid_number)[i] = new Polygon();
                        for (int j = 0; j < working_vert.get(current_grid_number)[i].xpoints.length; j++)
                        {
                            vertlines_list.get(current_grid_number)[i].addPoint(working_vert.get(current_grid_number)[i].xpoints[j], working_vert.get(current_grid_number)[i].ypoints[j]);
                        }
                    }
                    horilines_list.set(current_grid_number,  new Polygon[working_hori.get(current_grid_number).length]);
                    for (int i = 0; i < working_hori.get(current_grid_number).length; i++)
                    {
                        horilines_list.get(current_grid_number)[i] = new Polygon();
                        for (int j = 0; j < working_hori.get(current_grid_number)[i].xpoints.length; j++)
                        {
                            horilines_list.get(current_grid_number)[i].addPoint(working_hori.get(current_grid_number)[i].xpoints[j], working_hori.get(current_grid_number)[i].ypoints[j]);
                        }
                    }
                    current_outer_width = working_outer_width;
                    current_outer_height = working_outer_height;
                }
                x_last = xCoordinate(e.getX());
                y_last = yCoordinate(e.getY());
                break;
            default:
                break;
        }
        repaint();
    }

    private void this_mouseWheelMoved(MouseWheelEvent e)
    {

    }

    private int gridCount = 0;
    private int current_outer_width;
    private int current_outer_height;
    private ArrayList<Double> working_angle = new ArrayList<>();
    private ArrayList<Polygon> working_master = new ArrayList<>();
    private ArrayList<Polygon> working_base = new ArrayList<>();
    private ArrayList<Polygon> working_outer = new ArrayList<>();
    private ArrayList<Polygon[]> working_vert = new ArrayList<>();
    private ArrayList<Polygon[]> working_hori = new ArrayList<>();
    private int working_outer_width;
    private int working_outer_height;

    public void addGrid()
    {
        if (manager.getSample_GridCount(myNumber) > gridCount)
        {
            angle_list.add(manager.getSample_Grid_Angle(myNumber, gridCount));
            masterpoly_list.add(manager.getSample_Grid_Polygon_Master(myNumber, gridCount));
            basepoly_list.add(manager.getSample_Grid_Polygon_Base(myNumber, gridCount));
            outerpoly_list.add(manager.getSample_Grid_Polygon_Outline(myNumber, gridCount));
            vertlines_list.add(manager.getSample_Grid_Polygon_VerticalLines(myNumber, gridCount));
            horilines_list.add(manager.getSample_Grid_Polygon_HorizontalLines(myNumber, gridCount));
            working_angle.add(angle_list.get(angle_list.size() - 1));
            working_master.add(masterpoly_list.get(masterpoly_list.size() - 1));
            working_base.add(basepoly_list.get(basepoly_list.size() - 1));
            working_outer.add(outerpoly_list.get(outerpoly_list.size() - 1));
            working_vert.add(vertlines_list.get(vertlines_list.size() - 1));
            working_hori.add(horilines_list.get(horilines_list.size() - 1));
            current_grid_number = gridCount;
            gridCount++;
        }
    }

    private void refreshCurrentGrids()
    {
        angle_list.set(current_grid_number, manager.getSample_Grid_Angle(myNumber, current_grid_number));
        masterpoly_list.set(current_grid_number, manager.getSample_Grid_Polygon_Master(myNumber, current_grid_number));
        basepoly_list.set(current_grid_number, manager.getSample_Grid_Polygon_Base(myNumber, current_grid_number));
        outerpoly_list.set(current_grid_number, manager.getSample_Grid_Polygon_Outline(myNumber, current_grid_number));
        vertlines_list.set(current_grid_number, manager.getSample_Grid_Polygon_VerticalLines(myNumber, current_grid_number));
        horilines_list.set(current_grid_number, manager.getSample_Grid_Polygon_HorizontalLines(myNumber, current_grid_number));
        repaint();
    }

    public void reloadGrid(int grid)
    {
        this.setNewGridDimensions(grid, manager.getSample_Grid_Angle(myNumber, current_grid_number),
                manager.getSample_Grid_Polygon_Master(myNumber, current_grid_number),
                manager.getSample_Grid_Polygon_Base(myNumber, current_grid_number),
                manager.getSample_Grid_Polygon_Outline(myNumber, current_grid_number),
                manager.getSample_Grid_Polygon_VerticalLines(myNumber, current_grid_number),
                manager.getSample_Grid_Polygon_HorizontalLines(myNumber, current_grid_number));
    }

    public void setGridMode(int i)
    {
        mode = i;
    }

    public void changeContrast(int val)
    {
        Thread thread = new Thread(){
            public void run(){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                double contrast = (double)val;
                contrast/=100;
                int w = im.getWidth(null);
                int h = im.getHeight(null);
                int[] pixels = new int[w*h];


                PixelGrabber pg = new PixelGrabber(im, 0,0,w,h,pixels,0,w);

                try{
                    pg.grabPixels();

                }catch(Exception e3){System.out.print("Error");}

                for(int i=0; i<pixels.length; i++){
                    int p = pixels[i];

                    int a = (p >> 24) & 0xFF;
                    int r = (p >> 16) & 0xFF;
                    int b = 0;
                    int g = (p >>  8) & 0xFF;
                    r=Math.round((float)(r*contrast));
                    //b=Math.round((float)(b*contrast));
                    g=Math.round((float)(g*contrast));
                    if(r>255) r=255;
                    //if(b>255) b=255;
                    if(g>255) g=255;

                    pixels[i] = (a << 24 | r << 16 | g << 8 | b);
                }
                ip.setImage(createImage(new MemoryImageSource(w,h,pixels,0,w)));
                ip.repaintWindow();
                setCursor(Cursor.getDefaultCursor());
            }
        };
        thread.start();
        repaint();
    }

    public void removeGrid(int grid)
    {
        if (current_grid_number == grid && current_grid_number == angle_list.size() - 1)
        {
            current_grid_number--;
        }
        else if (current_grid_number > grid)
        {
            current_grid_number--;
        }
        angle_list.remove(grid);
        masterpoly_list.remove(grid);
        basepoly_list.remove(grid);
        outerpoly_list.remove(grid);
        vertlines_list.remove(grid);
        horilines_list.remove(grid);
    }

    private JScrollBar scrollBar_hori;
    private JScrollBar scrollBar_vert;
    public void addScrollBars(JScrollBar hori, JScrollBar vert)
    {
        scrollBar_hori = hori;
        scrollBar_vert = vert;
    }
}