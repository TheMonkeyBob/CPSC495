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

import ij.ImagePlus;
import ij.gui.ImageCanvas;

import application.internal.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * ImageDisplayPanel is a JPanel which displays a microarray image.
 */
public class ImageDisplayPanel extends JPanel {

    private Engine engine;
    private int myNumber;

    private double zoomed=1.0; //magnification
    private BorderLayout borderLayout1 = new BorderLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    private Image im; //original image
    /**microarray image displayed in the panel*/
    protected ImagePlus ip;
    /**canvas displaying microarray image*/
    protected ImageCanvas ic;
    /**Currently selected grid**/
    private int current_grid_number;

    public ImageDisplayPanel(Engine engine, Image im, int number)
    {
        this.engine = engine;
        this.myNumber = number;

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
    public void setMagnification(double magnification) {
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
        for(int i = 0; i<engine.getSample_GridCount(myNumber); i++){
            //Grid grid = manager.getGrid(i);
            Polygon p = new Polygon();

        }
    }

    public void setCurrentGridNumber(int number)
    {
        current_grid_number = number;
    }

    private int gridCount = 0;
    private ArrayList<Double> angle_list = new ArrayList<>();
    private ArrayList<Polygon> masterpoly_list = new ArrayList<>();
    private ArrayList<Polygon> basepoly_list = new ArrayList<>();
    private ArrayList<Polygon> outerpoly_list = new ArrayList<>();
    private ArrayList<Polygon[]> vertlines_list = new ArrayList<>();
    private ArrayList<Polygon[]> horilines_list = new ArrayList<>();
    public void addGrid(double angle, Polygon master, Polygon base, Polygon outer, Polygon[] vert, Polygon[] hori)
    {
        angle_list.add(angle);
        masterpoly_list.add(master);
        basepoly_list.add(base);
        outerpoly_list.add(outer);
        vertlines_list.add(vert);
        horilines_list.add(hori);
        gridCount = basepoly_list.size();
    }

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
}