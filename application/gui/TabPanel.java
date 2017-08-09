package application.gui;

import application.engine.GuiManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;

public class TabPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private int myNumber;
    private GuiManager manager;

    private SegmentPanel segmentPanel;
    private GridingPanel gridingPanel;
    private ImageDisplayPanel imageDisplayPanel;

    private Border border_BlackLine = BorderFactory.createLineBorder(Color.black);

    public TabPanel(GuiManager manager, int number)
    {
        this.manager = manager;
        this.myNumber = number;
        setup();
    }

    private void setup()
    {
        this.setLayout(null);

        imageDisplayPanel = new ImageDisplayPanel(manager, myNumber);
        imageDisplayPanel.setBounds(10, 10, (int)(this.getWidth()*(3.0/5.0) - 10), this.getHeight()-20);
        this.add(imageDisplayPanel);

        // Gridding panel starts here
        gridingPanel = new GridingPanel(manager, myNumber);
        gridingPanel.setBounds(imageDisplayPanel.getX() + imageDisplayPanel.getWidth(), imageDisplayPanel.getY(),
                (int)(this.getWidth() - this.getWidth()*(3.0/5.0) - 20), (int)(this.getHeight()*(3.0/5.0) - 10));
        this.add(gridingPanel);

        // Segmentation panel starts here
        segmentPanel = new SegmentPanel(manager, myNumber);
        TitledBorder seg_title = BorderFactory.createTitledBorder(border_BlackLine, "Segmentation");
        seg_title.setTitleJustification(TitledBorder.LEFT);
        segmentPanel.setBorder(seg_title);
        segmentPanel.setLayout(null);
        segmentPanel.setBounds(gridingPanel.getX(), gridingPanel.getY() + gridingPanel.getHeight(), gridingPanel.getWidth(),
                (int)(this.getHeight() - this.getHeight()*(3.0/5.0) - 20));
        this.add(segmentPanel);
    }

    public void addGrid(int row, int col)
    {
        gridingPanel.addGrid(row, col);
        imageDisplayPanel.addGrid();
        refreshSegmentation();
    }

    public void coordinateFound(int x, int y)
    {
        gridingPanel.addCoordinates(x, y);
    }

    protected void refreshSegmentation()
    {
        segmentPanel.updateSegmentation();
    }

    public void setGridMode(int mode)
    {
        System.out.println("GridMode: " + mode);
        imageDisplayPanel.setGridMode(mode);
        gridingPanel.setGridMode(mode);
    }

    public void zoomToGrid()
    {
        imageDisplayPanel.zoomToCurrentGrid();
    }

    public void setCurrentGrid(int grid)
    {
        imageDisplayPanel.setCurrentGrid(grid);
        gridingPanel.setCurrentGrid(grid);
    }

    public void removeGrid(int grid)
    {
        imageDisplayPanel.removeGrid(grid);
        gridingPanel.removeGrid(grid);
    }

    public void reSize()
    {
        imageDisplayPanel.setBounds(10, 10, (int)(this.getWidth() * (1.0 / 2.0) - 10), this.getHeight() - 20);
        gridingPanel.setBounds(imageDisplayPanel.getX() + imageDisplayPanel.getWidth(), imageDisplayPanel.getY(),
                (int)(this.getWidth() - this.getWidth() * (1.0 / 2.0) - 20),
                (int)(this.getHeight() * (1.0 / 2.0) - 10));
        segmentPanel.setBounds(gridingPanel.getX(), gridingPanel.getY() + gridingPanel.getHeight(), gridingPanel.getWidth(),
                (int)(this.getHeight() - this.getHeight() * (1.0 / 2.0) - 20));
        imageDisplayPanel.reSize((int)(this.getWidth() * (1.0 / 2.0) - 10), this.getHeight() - 20);
    }

    public void reloadGrid(int grid)
    {
        imageDisplayPanel.reloadGrid(grid);
        gridingPanel.reloadGrid(grid);
    }

    public void setSegmentationValues(Object[] vals)
    {
        segmentPanel.setSegmentationValues(vals);
    }
}