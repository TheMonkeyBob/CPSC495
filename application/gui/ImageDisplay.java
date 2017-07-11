package application.gui;

import application.engine.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

/**
 * Created by Lukas Pihl
 */
public class ImageDisplay extends JScrollPane
{
    private int myNumber;
    private GuiManager manager;
    private ImageDisplayPanel imageDisplayPanel;
    private ImageDisplayTruePanel parent_panel;
    private boolean clicked = false;

    private int mode = SharedData.GRIDMODE_NORMAL;
    private int x_origin = 0;
    private int y_origin = 0;
    private int x_last = 0;
    private int y_last = 0;
    private int deadzone = 10;

    public ImageDisplay(int num, GuiManager manager, ImageDisplayTruePanel parent)
    {
        myNumber = num;
        this.manager = manager;
        parent_panel = parent;
        setup();
    }

    private void setup()
    {
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

        image = buildImage();
        imageDisplayPanel = new ImageDisplayPanel(manager, image, myNumber);
        imageDisplayPanel.getCanvas().addMouseListener(ma);
        imageDisplayPanel.getCanvas().addMouseMotionListener(ma);
        imageDisplayPanel.getCanvas().addMouseWheelListener(ma);

        setLayout(new ScrollPaneLayout.UIResource());
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        setViewport(createViewport());
        setVerticalScrollBar(createVerticalScrollBar());
        setHorizontalScrollBar(createHorizontalScrollBar());
        if (imageDisplayPanel != null) {
            setViewportView(imageDisplayPanel);
        }
        updateUI();

        if (!this.getComponentOrientation().isLeftToRight()) {
            viewport.setViewPosition(new Point(Integer.MAX_VALUE, 0));
        }
    }
    private Image image;

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
        return ((imageDisplayPanel.getCanvas().getSrcRect().x
                + Math.round((float) ((ex) / imageDisplayPanel.getZoom()))));
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
        return ((imageDisplayPanel.getCanvas().getSrcRect().y
                + Math.round((float) ((ey) / imageDisplayPanel.getZoom()))));
    }

    private void this_mousePressed(MouseEvent e)
    {
        switch (mode)
        {
            case SharedData.GRIDMODE_ROTATE:
                working_angle = current_angle;
                working_master = new Polygon();
                for (int i = 0; i < current_master.xpoints.length; i++)
                {
                    working_master.addPoint(current_master.xpoints[i], current_master.ypoints[i]);
                }
                working_outer = new Polygon();
                for (int i = 0; i < current_outer.xpoints.length; i++)
                {
                    working_outer.addPoint(current_outer.xpoints[i], current_outer.ypoints[i]);
                }
                working_vert = new Polygon[current_vert.length];
                for (int i = 0; i < current_vert.length; i++)
                {
                    working_vert[i] = new Polygon();
                    for (int j = 0; j < current_vert[i].xpoints.length; j++)
                    {
                        working_vert[i].addPoint(current_vert[i].xpoints[j], current_vert[i].ypoints[j]);
                    }
                }
                working_hori = new Polygon[current_hori.length];
                for (int i = 0; i < current_hori.length; i++)
                {
                    working_hori[i] = new Polygon();
                    for (int j = 0; j < current_hori[i].xpoints.length; j++)
                    {
                        working_hori[i].addPoint(current_hori[i].xpoints[j], current_hori[i].ypoints[j]);
                    }
                }
                break;
            case SharedData.GRIDMODE_RESIZE:
                working_master = new Polygon();
                for (int i = 0; i < current_master.xpoints.length; i++)
                {
                    working_master.addPoint(current_master.xpoints[i], current_master.ypoints[i]);
                }
                working_outer = new Polygon();
                for (int i = 0; i < current_outer.xpoints.length; i++)
                {
                    working_outer.addPoint(current_outer.xpoints[i], current_outer.ypoints[i]);
                }
                working_vert = new Polygon[current_vert.length];
                for (int i = 0; i < current_vert.length; i++)
                {
                    working_vert[i] = new Polygon();
                    for (int j = 0; j < current_vert[i].xpoints.length; j++)
                    {
                        working_vert[i].addPoint(current_vert[i].xpoints[j], current_vert[i].ypoints[j]);
                    }
                }
                working_hori = new Polygon[current_hori.length];
                for (int i = 0; i < current_hori.length; i++)
                {
                    working_hori[i] = new Polygon();
                    for (int j = 0; j < current_hori[i].xpoints.length; j++)
                    {
                        working_hori[i].addPoint(current_hori[i].xpoints[j], current_hori[i].ypoints[j]);
                    }
                }
                long dx = Math.abs((long)(working_outer.xpoints[3] - working_outer.xpoints[0]));
                long dy = Math.abs((long)(working_outer.ypoints[3] - working_outer.ypoints[0]));
                double d2 = dx * dx + dy * dy;
                working_outer_height = (int)Math.round(Math.sqrt(d2));
                current_outer_height = working_outer_height;

                dx = Math.abs((long)(working_outer.xpoints[1] - working_outer.xpoints[0]));
                dy = Math.abs((long)(working_outer.ypoints[1] - working_outer.ypoints[0]));
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
                manager.setSample_Grid_MoveTo(myNumber, current_gird_num, current_master.xpoints[0],
                        current_master.ypoints[0]);
                refreshCurrentGrids();
                break;
            case SharedData.GRIDMODE_ROTATE:
                manager.setSample_Grid_RotateTo(myNumber, current_gird_num, current_angle);
                refreshCurrentGrids();
                break;
            case SharedData.GRIDMODE_RESIZE:
                manager.setSample_Grid_ResizeTo(myNumber, current_gird_num, current_master.ypoints[3] -
                        current_master.ypoints[0], current_master.xpoints[1] - current_master.xpoints[0]);
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
                parent_panel.coordinateFound(xCoordinate(e.getX()), yCoordinate(e.getY()));
                break;
            case SharedData.GRIDMODE_MOVETO:
                int x_diff = xCoordinate(e.getX()) - current_master.xpoints[0];
                int y_diff = yCoordinate(e.getY()) - current_master.ypoints[0];
                current_master.xpoints[0] += x_diff;
                current_master.ypoints[0] += y_diff;
                manager.setSample_Grid_MoveTo(myNumber, current_gird_num, current_master.xpoints[0],
                        current_master.ypoints[0]);
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
                int deltaX = x_last - e.getX();
                int deltaY = y_last - e.getY();
                Point point = this.getViewport().getViewPosition();
                int beforeX = point.x;
                int beforeY = point.y;
                if (!refreshing)
                {
                    this.getVerticalScrollBar().setValue(this.getVerticalScrollBar().getValue() + deltaY);
                    this.getHorizontalScrollBar().setValue(this.getHorizontalScrollBar().getValue() + deltaX);
                }
                int afterX = point.x;
                int afterY = point.y;
                x_last += beforeX - afterX;
                y_last += beforeY - afterY;
                break;
            case SharedData.GRIDMODE_MOVE:
                int x_diff = xCoordinate(e.getX()) - x_last;
                int y_diff = yCoordinate(e.getY()) - y_last;
                for (int i = 0; i < current_master.xpoints.length; i++)
                {
                    current_master.xpoints[i] += x_diff;
                    current_master.ypoints[i] += y_diff;
                }
                for (int i = 0; i < current_outer.xpoints.length; i++)
                {
                    current_outer.xpoints[i] += x_diff;
                    current_outer.ypoints[i] += y_diff;
                }
                for (int i = 0; i < current_vert.length; i++)
                {
                    for (int j = 0; j < current_vert[i].xpoints.length; j++)
                    {
                        current_vert[i].xpoints[j] += x_diff;
                        current_vert[i].ypoints[j] += y_diff;
                    }
                }
                for (int i = 0; i < current_hori.length; i++)
                {
                    for (int j = 0; j < current_hori[i].xpoints.length; j++)
                    {
                        current_hori[i].xpoints[j] += x_diff;
                        current_hori[i].ypoints[j] += y_diff;
                    }
                }
                imageDisplayPanel.setNewGridDimensions(current_gird_num, current_angle, current_master, current_base,
                        current_outer, current_vert, current_hori);
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
                        current_angle = working_angle + (yCoordinate(e.getY()) - y_origin - deadzone) * 0.001;
                        aTransform.setToRotation((yCoordinate(e.getY()) - y_origin - deadzone) * 0.001,
                                working_master.xpoints[0], working_master.ypoints[0]);
                    }
                    else
                    {
                        current_angle = working_angle + (yCoordinate(e.getY()) - y_origin + deadzone) * 0.001;
                        aTransform.setToRotation((yCoordinate(e.getY()) - y_origin + deadzone) * 0.001,
                                working_master.xpoints[0], working_master.ypoints[0]);
                    }

                    start = new Point2D.Double(working_outer.xpoints[0], working_outer.ypoints[0]);
                    aTransform.transform(start, done);
                    current_outer.xpoints[0] = (int)Math.round(done.getX());
                    current_outer.ypoints[0] = (int)Math.round(done.getY());

                    start = new Point2D.Double(working_outer.xpoints[1], working_outer.ypoints[1]);
                    aTransform.transform(start, done);
                    current_outer.xpoints[1] = (int)Math.round(done.getX());
                    current_outer.ypoints[1] = (int)Math.round(done.getY());

                    start = new Point2D.Double(working_outer.xpoints[2], working_outer.ypoints[2]);
                    aTransform.transform(start, done);
                    current_outer.xpoints[2] = (int)Math.round(done.getX());
                    current_outer.ypoints[2] = (int)Math.round(done.getY());

                    start = new Point2D.Double(working_outer.xpoints[3], working_outer.ypoints[3]);
                    aTransform.transform(start, done);
                    current_outer.xpoints[3] = (int)Math.round(done.getX());
                    current_outer.ypoints[3] = (int)Math.round(done.getY());

                    double dy1 = (double)(current_outer.ypoints[1] - (current_outer.ypoints[0])) /
                            (double)current_vert.length;
                    double dx1 = (double)(current_outer.xpoints[1] - (current_outer.xpoints[0])) /
                            (double)current_vert.length;
                    for(int j = 0; j < current_vert.length; j++)
                    {
                        current_vert[j].xpoints[0] = current_outer.xpoints[0] + (int)((j + 1) * dx1);
                        current_vert[j].ypoints[0] = current_outer.ypoints[0] + (int)((j + 1) * dy1);
                        current_vert[j].xpoints[1] = current_outer.xpoints[3] + (int)((j + 1) * dx1);
                        current_vert[j].ypoints[1] = current_outer.ypoints[3] + (int)((j + 1) * dy1);
                        current_vert[j].xpoints[2] = current_outer.xpoints[3] + (int)((j) * dx1);
                        current_vert[j].ypoints[2] = current_outer.ypoints[3] + (int)((j) * dy1);
                        current_vert[j].xpoints[3] = current_outer.xpoints[0] + (int)((j) * dx1);
                        current_vert[j].ypoints[3] = current_outer.ypoints[0] + (int)((j) * dy1);
                    }

                    double dy2 = (double)(current_outer.ypoints[3] - (current_outer.ypoints[0])) /
                            (double)current_hori.length;
                    double dx2 = (double)(current_outer.xpoints[3] - (current_outer.xpoints[0])) /
                            (double)current_hori.length;
                    for(int k = 0; k < current_hori.length; k++)
                    {
                        current_hori[k].xpoints[0] = current_outer.xpoints[0] + (int)((k + 1) * dx2);
                        current_hori[k].ypoints[0] = current_outer.ypoints[0] + (int)((k + 1) * dy2);
                        current_hori[k].xpoints[1] = current_outer.xpoints[1] + (int)((k + 1) * dx2);
                        current_hori[k].ypoints[1] = current_outer.ypoints[1] + (int)((k + 1) * dy2);
                        current_hori[k].xpoints[2] = current_outer.xpoints[1] + (int)((k) * dx2);
                        current_hori[k].ypoints[2] = current_outer.ypoints[1] + (int)((k) * dy2);
                        current_hori[k].xpoints[3] = current_outer.xpoints[0] + (int)((k) * dx2);
                        current_hori[k].ypoints[3] = current_outer.ypoints[0] + (int)((k) * dy2);
                    }
                }
                else
                {
                    current_angle = working_angle;
                    current_outer = new Polygon();
                    for (int i = 0; i < working_outer.xpoints.length; i++)
                    {
                        current_outer.addPoint(working_outer.xpoints[i], working_outer.ypoints[i]);
                    }
                    current_vert = new Polygon[working_vert.length];
                    for (int i = 0; i < working_vert.length; i++)
                    {
                        current_vert[i] = new Polygon();
                        for (int j = 0; j < working_vert[i].xpoints.length; j++)
                        {
                            current_vert[i].addPoint(working_vert[i].xpoints[j], working_vert[i].ypoints[j]);
                        }
                    }
                    current_hori = new Polygon[working_hori.length];
                    for (int i = 0; i < working_hori.length; i++)
                    {
                        current_hori[i] = new Polygon();
                        for (int j = 0; j < working_hori[i].xpoints.length; j++)
                        {
                            current_hori[i].addPoint(working_hori[i].xpoints[j], working_hori[i].ypoints[j]);
                        }
                    }
                }
                imageDisplayPanel.setNewGridDimensions(current_gird_num, current_angle, current_master, current_base,
                        current_outer, current_vert, current_hori);
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
                    current_master.ypoints[2] = working_master.ypoints[2] + diff;
                    current_master.ypoints[3] = working_master.ypoints[3] + diff;
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
                    current_master.xpoints[1] = working_master.xpoints[1] + diff;
                    current_master.xpoints[2] = working_master.xpoints[2] + diff;
                    current_outer_width = working_outer_width + diff;
                }
                if (yCoordinate(e.getY()) > y_origin + deadzone || yCoordinate(e.getY()) < y_origin - deadzone ||
                        xCoordinate(e.getX()) > x_origin + deadzone || xCoordinate(e.getX()) < x_origin - deadzone)
                {
                    Point2D.Double done = new Point2D.Double();
                    Point2D.Double start;
                    AffineTransform aTransform = new AffineTransform();
                    aTransform.setToRotation(current_angle, working_outer.xpoints[0], working_outer.ypoints[0]);

                    start = new Point2D.Double(current_outer_width + working_outer.xpoints[0],
                            working_outer.ypoints[0]);
                    aTransform.transform(start, done);
                    current_outer.xpoints[1] = (int)Math.round(done.getX());
                    current_outer.ypoints[1] = (int)Math.round(done.getY());

                    start = new Point2D.Double(working_outer.xpoints[0], current_outer_height +
                            working_outer.ypoints[0]);
                    aTransform.transform(start, done);
                    current_outer.xpoints[3] = (int)Math.round(done.getX());
                    current_outer.ypoints[3] = (int)Math.round(done.getY());

                    start = new Point2D.Double(current_outer_width + working_outer.xpoints[0], current_outer_height +
                            working_outer.ypoints[0]);
                    aTransform.transform(start, done);
                    current_outer.xpoints[2] = (int)Math.round(done.getX());
                    current_outer.ypoints[2] = (int)Math.round(done.getY());

                    double dy1 = (double)(current_outer.ypoints[1] - (current_outer.ypoints[0])) /
                            (double)current_vert.length;
                    double dx1 = (double)(current_outer.xpoints[1] - (current_outer.xpoints[0])) /
                            (double)current_vert.length;
                    for(int j = 0; j < current_vert.length; j++)
                    {
                        current_vert[j].xpoints[0] = current_outer.xpoints[0] + (int)((j + 1) * dx1);
                        current_vert[j].ypoints[0] = current_outer.ypoints[0] + (int)((j + 1) * dy1);
                        current_vert[j].xpoints[1] = current_outer.xpoints[3] + (int)((j + 1) * dx1);
                        current_vert[j].ypoints[1] = current_outer.ypoints[3] + (int)((j + 1) * dy1);
                        current_vert[j].xpoints[2] = current_outer.xpoints[3] + (int)((j) * dx1);
                        current_vert[j].ypoints[2] = current_outer.ypoints[3] + (int)((j) * dy1);
                        current_vert[j].xpoints[3] = current_outer.xpoints[0] + (int)((j) * dx1);
                        current_vert[j].ypoints[3] = current_outer.ypoints[0] + (int)((j) * dy1);
                    }

                    double dy2 = (double)(current_outer.ypoints[3] - (current_outer.ypoints[0])) /
                            (double)current_hori.length;
                    double dx2 = (double)(current_outer.xpoints[3] - (current_outer.xpoints[0])) /
                            (double)current_hori.length;
                    for(int k = 0; k < current_hori.length; k++)
                    {
                        current_hori[k].xpoints[0] = current_outer.xpoints[0] + (int)((k + 1) * dx2);
                        current_hori[k].ypoints[0] = current_outer.ypoints[0] + (int)((k + 1) * dy2);
                        current_hori[k].xpoints[1] = current_outer.xpoints[1] + (int)((k + 1) * dx2);
                        current_hori[k].ypoints[1] = current_outer.ypoints[1] + (int)((k + 1) * dy2);
                        current_hori[k].xpoints[2] = current_outer.xpoints[1] + (int)((k) * dx2);
                        current_hori[k].ypoints[2] = current_outer.ypoints[1] + (int)((k) * dy2);
                        current_hori[k].xpoints[3] = current_outer.xpoints[0] + (int)((k) * dx2);
                        current_hori[k].ypoints[3] = current_outer.ypoints[0] + (int)((k) * dy2);
                    }
                }
                else
                {
                    current_master = new Polygon();
                    for (int i = 0; i < working_master.xpoints.length; i++)
                    {
                        current_master.addPoint(working_master.xpoints[i], working_master.ypoints[i]);
                    }
                    current_outer = new Polygon();
                    for (int i = 0; i < working_outer.xpoints.length; i++)
                    {
                        current_outer.addPoint(working_outer.xpoints[i], working_outer.ypoints[i]);
                    }
                    current_vert = new Polygon[working_vert.length];
                    for (int i = 0; i < working_vert.length; i++)
                    {
                        current_vert[i] = new Polygon();
                        for (int j = 0; j < working_vert[i].xpoints.length; j++)
                        {
                            current_vert[i].addPoint(working_vert[i].xpoints[j], working_vert[i].ypoints[j]);
                        }
                    }
                    current_hori = new Polygon[working_hori.length];
                    for (int i = 0; i < working_hori.length; i++)
                    {
                        current_hori[i] = new Polygon();
                        for (int j = 0; j < working_hori[i].xpoints.length; j++)
                        {
                            current_hori[i].addPoint(working_hori[i].xpoints[j], working_hori[i].ypoints[j]);
                        }
                    }
                    current_outer_width = working_outer_width;
                    current_outer_height = working_outer_height;
                }
                imageDisplayPanel.setNewGridDimensions(current_gird_num, current_angle, current_master, current_base,
                        current_outer, current_vert, current_hori);
                x_last = xCoordinate(e.getX());
                y_last = yCoordinate(e.getY());
                break;
            default:
                break;
        }
    }

    private void this_mouseWheelMoved(MouseWheelEvent e)
    {

    }

    private int gridCount = 0;
    private int current_gird_num;
    private double current_angle;
    private Polygon current_master;
    private Polygon current_base;
    private Polygon current_outer;
    private Polygon[] current_vert;
    private Polygon[] current_hori;
    private int current_outer_width;
    private int current_outer_height;
    private double working_angle;
    private Polygon working_master;
    private Polygon working_base;
    private Polygon working_outer;
    private Polygon[] working_vert;
    private Polygon[] working_hori;
    private int working_outer_width;
    private int working_outer_height;

    public void addGrid()
    {
        if (manager.getSample_GridCount(myNumber) > gridCount)
        {
            current_angle = manager.getSample_Grid_Angle(myNumber, gridCount);
            current_master = manager.getSample_Grid_Polygon_Master(myNumber, gridCount);
            current_base = manager.getSample_Grid_Polygon_Base(myNumber, gridCount);
            current_outer = manager.getSample_Grid_Polygon_Outline(myNumber, gridCount);
            current_vert = manager.getSample_Grid_Polygon_VerticalLines(myNumber, gridCount);
            current_hori = manager.getSample_Grid_Polygon_HorizontalLines(myNumber, gridCount);
            imageDisplayPanel.addGrid(current_angle, current_master, current_base, current_outer, current_vert,
                    current_hori);
            current_gird_num = gridCount;
            gridCount++;
        }
    }

    private void refreshCurrentGrids()
    {
        current_angle = manager.getSample_Grid_Angle(myNumber, current_gird_num);
        current_master = manager.getSample_Grid_Polygon_Master(myNumber, current_gird_num);
        current_base = manager.getSample_Grid_Polygon_Base(myNumber, current_gird_num);
        current_outer = manager.getSample_Grid_Polygon_Outline(myNumber, current_gird_num);
        current_vert = manager.getSample_Grid_Polygon_VerticalLines(myNumber, current_gird_num);
        current_hori = manager.getSample_Grid_Polygon_HorizontalLines(myNumber, current_gird_num);
        imageDisplayPanel.setNewGridDimensions(current_gird_num, current_angle, current_master, current_base,
                current_outer, current_vert, current_hori);
    }

    public void setGridMode(int i)
    {
        mode = i;
    }

    public void zoomToCurrentGrid()
    {
        int x = 1000000000;
        int y = 1000000000;
        for (int i: current_outer.xpoints)
        {
            if (i < x)
            {
                x = i;
            }
        }
        for (int i: current_outer.ypoints)
        {
            if (i < y)
            {
                y = i;
            }
        }
        this.getVerticalScrollBar().setValue(y - 10);
        this.getHorizontalScrollBar().setValue(x - 10);
    }

    public void setCurrentGrid(int grid)
    {
        current_gird_num = grid;
        imageDisplayPanel.setCurrentGridNumber(grid);
        repaint();
    }

    public void removeCurrentGrid()
    {
        imageDisplayPanel.removeCurrentGrid();
        gridCount--;
    }

    public void setMagnification(double zoom)
    {
        imageDisplayPanel.setMagnification(zoom);
    }

    public void changeContrast(int val)
    {
        Thread thread = new Thread(){
            public void run(){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                double contrast = (double)val;
                contrast/=100;
                int w = image.getWidth(null);
                int h = image.getHeight(null);
                int[] pixels = new int[w*h];


                PixelGrabber pg = new PixelGrabber(image, 0,0,w,h,pixels,0,w);

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
                imageDisplayPanel.ip.setImage(createImage(new MemoryImageSource(w,h,pixels,0,w)));
                imageDisplayPanel.repaint();
                setCursor(Cursor.getDefaultCursor());
            }
        };
        thread.start();
    }
}
