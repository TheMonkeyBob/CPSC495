package application.engine;

import application.gui.MainWindow;
import application.gui.TabPanel;
import ij.ImagePlus;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Lukas Pihl
 */
public class GuiManager
{
    private Engine engine;
    private MainWindow window;
    private JTabbedPane tabbedPane;

    private int tab_panel_number = 0;

    public GuiManager(Engine engine)
    {
        this.engine = engine;
        window = new MainWindow(this);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        window.setTabbedPane(tabbedPane);

        window.pack();
        window.setVisible(true);

        // Causes frame to open in the center of the screen.
        window.setLocationRelativeTo(null);

        // Sets the frame to maximum size on start
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void addSample(String name)
    {
        TabPanel panel = new TabPanel(this, tab_panel_number);
        tab_panel_number++;
        tabbedPane.addTab(name, null, panel, null);
        //tabbedPane.setLayout(null);
        panel.setPreferredSize(tabbedPane.getSize());
        panel.setSize(new Dimension((int)tabbedPane.getSize().getWidth(), (int)tabbedPane.getSize().getHeight()-20));
        panel.reSize();
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    public void fileIO_ExportExpressionData(String path, int sample)
    {

    }

    public void addSample_Grid(int sample, int tlX, int tlY, int trX, int trY, int bX, int bY, int row, int col)
    {
        engine.addSample_Grid(sample, tlX, tlY, trX, trY, bX, bY, row, col);
    }

    public int getSample_GridCount(int sample)
    {
        return engine.getSample_GridCount(sample);
    }

    public int getSample_Grid_Columns(int sample, int grid)
    {
        return engine.getSample_Grid_Columns(sample, grid);
    }

    public int getSample_Grid_NumberOfSpots(int sample, int grid)
    {
        return engine.getSample_Grid_NumberOfSpots(sample, grid);
    }

    public void setSample_CurrentGrid(int sample, int grid)
    {
        engine.setSample_CurrentGrid(this, sample, grid);
        ((TabPanel)tabbedPane.getComponentAt(sample)).setCurrentGrid(grid);
    }

    public void setSample_CurrentSpot(int sample, int spot)
    {
        engine.setSample_CurrentSpot(this, sample, spot);
    }

    public int getSample_Grid_ActualSpotNum(int sample, int grid, int spot)
    {
        return engine.getSample_Grid_ActualSpotNum(sample, grid, spot);
    }

    public double getSample_Grid_Angle(int sample, int grid)
    {
        return engine.getSample_Grid_Angle(sample, grid);
    }

    public Polygon getSample_Grid_Polygon_Master(int sample, int grid)
    {
        return engine.getSample_Grid_Polygon_Master(sample, grid);
    }

    public Polygon getSample_Grid_Polygon_Base(int sample, int grid)
    {
        return engine.getSample_Grid_Polygon_Base(sample, grid);
    }

    public Polygon getSample_Grid_Polygon_Outline(int sample, int grid)
    {
        return engine.getSample_Grid_Polygon_Outline(sample, grid);
    }

    public Polygon[] getSample_Grid_Polygon_VerticalLines(int sample, int grid)
    {
        return engine.getSample_Grid_Polygon_VerticalLines(sample, grid);
    }

    public Polygon[] getSample_Grid_Polygon_HorizontalLines(int sample, int grid)
    {
        return engine.getSample_Grid_Polygon_HorizontalLines(sample, grid);
    }

    public Polygon getSample_Grid_Spot(int sample, int grid, int spot)
    {
        return engine.getSample_Grid_Spot(sample, grid, spot);
    }

    public int getSample_CurrentGridNum(int sample)
    {
        return engine.getSample_CurrentGridNum(sample);
    }

    public void setSample_Grid_Spots(int sample, int grid, Polygon spots)
    {
        engine.setSample_Grid_Spots(this, sample, grid, spots);
    }

    public Polygon getSample_Grid_CurrentSpot(int sample, int grid)
    {
        return engine.getSample_Grid_CurrentSpot(sample, grid);
    }

    public void removeSample_Grid(int sample, int grid)
    {
        engine.removeSample_Grid(this, sample, grid);
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).removeGrid(grid);
    }

    public Image getSample_GreenImage(int sample)
    {
        return engine.getSample_GreenImage(sample);
    }

    public Image getSample_RedImage(int sample)
    {
        return engine.getSample_RedImage(sample);
    }

    public ImagePlus getSample_GreenImagePlus(int sample)
    {
        return engine.getSample_GreenImagePlus(sample);
    }

    public ImagePlus getSample_RedImagePlus(int sample)
    {
        return engine.getSample_RedImagePlus(sample);
    }

    public void setSample_Grid_MoveTo(int sample, int grid, int x, int y)
    {
        engine.setSample_Grid_MoveTo(this, sample, grid, x, y);
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).reloadGrid(grid);
    }

    public void setSample_Grid_RotateTo(int sample, int grid, double angle)
    {
        engine.setSample_Grid_RotateTo(this, sample, grid, angle);
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).reloadGrid(grid);
    }

    public void setSample_Grid_ResizeTo(int sample, int grid, int height, int width)
    {
        engine.setSample_Grid_ResizeTo(this, sample, grid, height, width);
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).reloadGrid(grid);
    }

    public void setSample_Grid_RowsAndColumns(int sample, int grid, int[] rc)
    {
        engine.setSample_Grid_RowsAndColumns(this, sample, grid, rc);
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).reloadGrid(grid);
    }

    public void setSample_SegmentationMethod(int sample, int method)
    {
        engine.setSample_SegmentationMethod(this, sample, method);
    }

    public void setSample_MethodThreshold(int sample, int thresh)
    {
        engine.setSample_MethodThreshold(this, sample, thresh);
    }

    public void setSample_RatioMethod(int sample, int method)
    {
        engine.setSample_RatioMethod(this, sample, method);
    }

    public void setSample_Gene_Data(int sample, int[] redPixels, int[] greenPixels, int height, int width, int type,
                                    Object[] params)
    {
        engine.setSample_Gene_Data(this, sample, redPixels, greenPixels, height, width, type, params);
    }

    public int[] getSample_Gene_CenterAndRadius(int sample)
    {
        return engine.getSample_Gene_CenterAndRadius(sample);
    }

    public int[][] getSample_Gene_CenterSpots(int sample, boolean isRed)
    {
        return engine.getSample_Gene_CenterSpots(sample, isRed);
    }

    public float getSample_Gene_GreenBackgroundTotal(int sample)
    {
        return engine.getSample_Gene_GreenBackgroundTotal(sample);
    }

    public float getSample_Gene_GreenForegroundTotal(int sample)
    {
        return engine.getSample_Gene_GreenBackgroundTotal(sample);
    }

    public float getSample_Gene_RedBackgroundTotal(int sample)
    {
        return engine.getSample_Gene_GreenBackgroundTotal(sample);
    }

    public float getSample_Gene_RedForegroundTotal(int sample)
    {
        return engine.getSample_Gene_GreenBackgroundTotal(sample);
    }

    public float getSample_Gene_GreenBackgroundAvg(int sample)
    {
        return engine.getSample_Gene_GreenBackgroundTotal(sample);
    }

    public float getSample_Gene_GreenForegroundAvg(int sample)
    {
        return engine.getSample_Gene_GreenBackgroundTotal(sample);
    }

    public float getSample_Gene_RedBackgroundAvg(int sample)
    {
        return engine.getSample_Gene_GreenBackgroundTotal(sample);
    }

    public float getSample_Gene_RedForegroundAvg(int sample)
    {
        return engine.getSample_Gene_GreenBackgroundTotal(sample);
    }

    public double getSample_Gene_Ratio(int sample, int method)
    {
        return engine.getSample_Gene_Ratio(sample, method);
    }

    public MainWindow getWindow()
    {
        return window;
    }

    public void newProject(String path)
    {
        engine.newProject(path);
    }

    public void saveProject()
    {
        engine.saveProject();
    }

    public void openProject(String path)
    {
        engine.openProject(path);
    }

    public void setGridMode(int mode)
    {
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).setGridMode(mode);
    }

    public void newSample(String name, String greenPath, String redPath, String genePath)
    {
        String s = "Sample " + tabbedPane.getTabCount() + 1;
        engine.newSample(this, s, greenPath, redPath, genePath);
    }

    public void zoomToCurrentGrid()
    {
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).zoomToGrid();
    }

    public void addGrid(int tlX, int tlY, int trX, int trY, int bX, int bY, int row, int col)
    {
        engine.addSample_Grid(tabbedPane.getSelectedIndex(), tlX, tlY, trX, trY, bX, bY, row, col);
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).addGrid(row, col);
    }

    public void coordinateFound(int x, int y)
    {
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).coordinateFound(x, y);
    }

    public int[] getSample_Grid_RowsAndColumns(int sample, int grid)
    {
        return engine.getSample_Grid_RowsAndColumns(sample, grid);
    }

    public boolean getSample_GridHorizontal(int sample)
    {
        return engine.getSample_GridHorizontal(sample);
    }

    public boolean getSample_GridVertical(int sample)
    {
        return engine.getSample_GridVertical(sample);
    }

    public boolean getSample_GridDirection(int sample)
    {
        return engine.getSample_GridDirection(sample);
    }

    public void setSample_GridHorizontal(int sample, boolean hori)
    {
        engine.setSample_GridHorizontal(sample, hori);
    }

    public void setSample_GridVertical(int sample, boolean vert)
    {
        engine.setSample_GridVertical(sample, vert);
    }

    public void setSample_GridDirection(int sample, boolean dir)
    {
        engine.setSample_GridDirection(sample, dir);
    }

    public void setSegmentationValues(Object[] vals)
    {
        ((TabPanel)tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())).setSegmentationValues(vals);
    }

    public void updateSegmentData(int sample, int grid)
    {
        engine.updateSegmentData(sample, grid);
    }
}
