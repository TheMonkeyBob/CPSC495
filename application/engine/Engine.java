package application.engine;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInput;

import application.GeneImageAspect;
import application.engine.FileIOManager;
import application.engine.GuiManager;
import application.engine.InternalManager;
import application.file.ExpressionWriter;
import application.gui.MainWindow;
import application.internal.AllGeneData;
import application.internal.Project;
import ij.ImagePlus;
import ij.gui.GUI;

import javax.swing.*;

/**
 * Created by Lukas Pihl
 */
public class Engine
{
    private Project project;
    private ExpressionWriter exp_writer;
    private GuiManager manager_GUI;
    private FileIOManager manager_File;
    private InternalManager manager_Internal;

    /**
     * Creates new Engine with default settings
     */
    public Engine()
    {
        manager_GUI = new GuiManager(this);
        manager_File = new FileIOManager(this);
        //manager_Internal = new InternalManager(this);
        project = new Project();
        exp_writer = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Internal Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void newProject(String path)
    {
        manager_File.newProject(path);
        project.setPath(path);
        //project.setName(name);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // File Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean File_ExportExpressionData(String filePath, int sample)
    {
        try
        {
            exp_writer = new ExpressionWriter(filePath);
            AllGeneData agd = new AllGeneData(project.getSample(sample));
            agd.calculate();
            String gname, line;
            for (int i = 0; i < agd.getSpotCount(); i++)
            {
                gname = agd.getGeneName(i);
                boolean flagStatus = agd.getFlagStatus(i);
                if(!gname.equalsIgnoreCase("empty") && !gname.equalsIgnoreCase("blank") &&
                        !gname.equalsIgnoreCase("missing") && !gname.equalsIgnoreCase("none") &&
                        !gname.equalsIgnoreCase("No Gene Specified"))
                {
                    line = gname+'\t';
                    if(!flagStatus)
                    {
                        line = line+String.valueOf(agd.getRatio(i));
                    }
                    else line = line + "\t";
                    exp_writer.writeLine(line);
                }
            }
            //close expression writer
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Sample Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addSample(Object caller, String path, String name, ImagePlus green, ImagePlus red)
    {
        if (caller != null && caller instanceof FileIOManager)
        {
            project.addSample(path, name, green, red);
        }
    }

    public void addSample(Object caller, String greenPath, String redPath, String genePath)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            //manager_File.newSample(greenPath, redPath, genePath)
            project.addSample(greenPath, redPath);
        }
    }

    public int getSampleCount()
    {
        return project.getSampleCount();
    }

    public String getSample_Name(int sample)
    {
        return project.getSample_Name(sample);
    }

    public String getSample_FilePath(int sample)
    {
        return project.getSample_FilePath(sample);
    }

    /**
     * Gets the number of grids for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return The number of grids for the sample.
     */
    public int getSample_GridCount(int sample)
    {
        return project.getSample_GridCount(sample);
    }

    public void addSample_Grid(int sample, int leftX, int rightX, int topY, int bottomY, double angle, int rows, int columns)
    {
        project.addSample_Grid(sample, leftX, rightX, topY, bottomY, angle, rows, columns);
    }

    public void addSample_Grid(int sample, int tlX, int tlY, int trX, int trY, int bX, int bY, int rows, int columns)
    {
        project.addSample_Grid(sample, tlX, tlY, trX, trY, bX, bY, rows, columns);
    }

    public void removeSample_Grid(Object caller, int sample, int grid)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.removeSample_Grid(sample, grid);
        }
    }

    public Polygon getSample_Grid_Polygon_Master(int sample, int grid)
    {
        return project.getSample_Grid_Polygon_Master(sample, grid);
    }

    /**
     * Gets the polygon for the specified grid of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return The polygon of the sample's grid.
     */
    public Polygon getSample_Grid_Polygon_Base(int sample, int grid)
    {
        return project.getSample_Grid_Polygon_Base(sample, grid);
    }

    /**
     * Gets the translated polygon for the specified grid of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return The translated polygon of the sample's grid.
     */
    public Polygon getSample_Grid_Polygon_Outline(int sample, int grid)
    {
        return project.getSample_Grid_Polygon_Outline(sample, grid);
    }

    /**
     * Gets an array of polygons containing the vertical lines separating columns based on the given
     * polygon for a specific grid for a specific sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return Array of polygons containing the vertical lines.
     */
    public Polygon[] getSample_Grid_Polygon_VerticalLines(int sample, int grid)
    {
        return project.getSample_Grid_Polygon_VerticalLines(sample, grid);
    }

    /**
     * Gets an array of polygons containing the horizontal lines separating rows based on the given
     * polygon for a specific grid for a specific sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return Array of polygons containing the vertical lines.
     */
    public Polygon[] getSample_Grid_Polygon_HorizontalLines(int sample, int grid)
    {
        return project.getSample_Grid_Polygon_HorizontalLines(sample, grid);
    }

    public void setSample_Grid_MoveBy(int sample, int grid, int x, int y)
    {
        project.setSample_Grid_MoveBy(sample, grid, x, y);
    }

    public void setSample_Grid_MoveTo(Object caller, int sample, int grid, int x, int y)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.setSample_Grid_MoveTo(sample, grid, x, y);
        }
    }

    public void setSample_Grid_RotateBy(int sample, int grid, double degree)
    {
        project.setSample_Grid_RotateBy(sample, grid, degree);
    }

    public void setSample_Grid_RotateTo(Object caller, int sample, int grid, double degree)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.setSample_Grid_RotateTo(sample, grid, degree);
        }
    }

    public void setSample_Grid_ResizeBy(int sample, int grid, int height, int width)
    {
        project.setSample_Grid_ResizeBy(sample, grid, height, width);
    }

    public void setSample_Grid_ResizeTo(Object caller, int sample, int grid, int height, int width)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.setSample_Grid_ResizeTo(sample, grid, height, width);
        }
    }

    public double getSample_Grid_Angle(int sample, int grid)
    {
        return project.getSample_Grid_Angle(sample, grid);
    }

    public int[] getSample_Grid_MasterPoints(int sample, int grid)
    {
        return project.getSample_Grid_MasterPoints(sample, grid);
    }

    public int[] getSample_Grid_RowsAndColumns(int sample, int grid)
    {
        return project.getSample_Grid_RowsAndColumns(sample, grid);
    }

    public void removeSample_Grid_All(int sample)
    {
        project.removeSample_Grid_All(sample);
    }

    /**
     * Set the horizontal flag for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param hori Flag value to set.
     */
    public void setSample_GridHorizontal(int sample, boolean hori)
    {
        project.setSample_GridHorizontal(sample, hori);
    }

    /**
     * Set the vertical flag for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param vert Flag value to set.
     */
    public void setSample_GridVertical(int sample, boolean vert)
    {
        project.setSample_GridVertical(sample, vert);
    }

    /**
     * Set the direction flag for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param dir Flag value to set.
     */
    public void setSample_GridDirection(int sample, boolean dir)
    {
        project.setSample_GridDirection(sample, dir);
    }

    /**
     * Get the number of columns in the specified grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return Number of columns in the grid.
     */
    public int getSample_Grid_Columns(int sample, int grid)
    {
        return project.getSample_Grid_Columns(sample, grid);
    }

    /**
     * Get the number of spots in the specified grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return Number of spots in the grid.
     */
    public int getSample_Grid_NumberOfSpots(int sample, int grid)
    {
        return project.getSample_Grid_NumberOfSpots(sample, grid);
    }

    /**
     * Sets the focus to the specified grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     */
    public void setSample_CurrentGrid(Object caller, int sample, int grid)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.setSample_CurrentGrid(sample, grid);
        }
    }

    /**
     * Sets the focus to the specified spot of the focused grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param spot The spot in the focused grid.
     */
    public void setSample_CurrentSpot(Object caller, int sample, int spot)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.setSample_CurrentSpot(sample, spot);
        }
    }

    /**
     * Gets the actual spot number from a transformed spot number for the specified grid of the specified sample.
     * An actual spot number is based on left to right, top to bottom, and horizontal (spot 2 to spot 1) placement.
     * A transformed spot number is based on user specified spot placement parameters.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @param spot Transformed spot number.
     * @return Actual spot number.
     */
    public int getSample_Grid_ActualSpotNum(int sample, int grid, int spot)
    {
        return project.getSample_Grid_ActualSpotNum(sample, grid, spot);
    }

    /**
     * Gets the index of the currently focused grid.
     * @param sample The index of the sample. Base index 0.
     * @return Index of the grid.
     */
    public int getSample_CurrentGridNum(int sample)
    {
        return project.getSample_CurrentGridNum(sample);
    }

    /**
     * Sets the spots coordinates for the spots based on a translated polygon and the number of rows and columns of
     * spots for the specified grid of the specified sample. A translated polygon is a polygon which encompasses
     * every spots in the grid (since the grid coordinates are based on the center of a spot).
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @param spots Translated polygon to set the spots from.
     */
    public void setSample_Grid_Spots(Object caller, int sample, int grid, Polygon spots)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.setSample_Grid_Spots(sample, grid, spots);
        }
    }

    /**
     * Gets the polygon of the currently focused spot of the specified grid of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return Polygon of the spot.
     */
    public Polygon getSample_Grid_CurrentSpot(int sample, int grid)
    {
        return project.getSample_Grid_CurrentSpot(sample, grid);
    }

    /**
     * Gets the horizontal flag value for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Horizontal flag value for the grids.
     */
    public boolean getSample_GridHorizontal(int sample)
    {
        return project.getSample_GridHorizontal(sample);
    }

    /**
     * Gets the vertical flag value for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Vertical flag value for the grids.
     */
    public boolean getSample_GridVertical(int sample)
    {
        return project.getSample_GridVertical(sample);
    }

    /**
     * Gets the direction flag value for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Direction flag value for the grids.
     */
    public boolean getSample_GridDirection(int sample)
    {
        return project.getSample_GridDirection(sample);
    }

    /**
     * Set the data for the focused gene in the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param redPixels Relevant image data from the red image.
     * @param greenPixels Relevant image data from the green image.
     * @param height Height of the image data.
     * @param width width of the image data.
     * @param type 1 = Adaptive circle. 2 = Seeded Region.
     * @param params List of extra parameters for the type.
     */
    public void setSample_Gene_Data(Object caller, int sample, int[] redPixels, int[] greenPixels, int height,
                                    int width, int type, Object[] params)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.setSample_Gene_Data(sample, redPixels, greenPixels, height, width, type, params);
        }
    }

    /**
     * Gets the center and radius of the adaptive circle for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return An array with [0] = x position of center, [1] = y position of center, [2] = radius.
     */
    public int[] getSample_Gene_CenterAndRadius(int sample)
    {
        return project.getSample_Gene_CenterAndRadius(sample);
    }

    /**
     * Gets the foreground pixels using the SeededRegion growing method for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param isRed Whether the foreground is for the red image.
     * @return An array of center spots, which is an array consisting of [0] = column and [1] = row.
     */
    public int[][] getSample_Gene_CenterSpots(int sample, boolean isRed)
    {
        return project.getSample_Gene_CenterSpots(sample, isRed);
    }

    /**
     * Gets the ratio of the gene data from the specified sample using the specified method.
     * @param sample The index of the sample. Base index 0.
     * @param aspect Method to use to get ratio.
     * @return The ratio for the gene data.
     */
    public double getSample_Gene_Ratio(int sample, int aspect)
    {
        return project.getSample_Gene_Ratio(sample, aspect);
    }

    /**
     * Get the background total of the green image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Background total of the green image.
     */
    public int getSample_Gene_GreenBackgroundTotal(int sample)
    {
        return project.getSample_Gene_GreenBackgroundTotal(sample);
    }

    /**
     * Get the foreground total of the green image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Foreground total of the green image.
     */
    public int getSample_Gene_GreenForegroundTotal(int sample)
    {
        return project.getSample_Gene_GreenForegroundTotal(sample);
    }

    /**
     * Get the background total of the red image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Background total of the red image.
     */
    public int getSample_Gene_RedBackgroundTotal(int sample)
    {
        return project.getSample_Gene_RedBackgroundTotal(sample);
    }

    /**
     * Get the foreground total of the red image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Foreground total of the red image.
     */
    public int getSample_Gene_RedForegroundTotal(int sample)
    {
        return project.getSample_Gene_RedForegroundTotal(sample);
    }

    /**
     * Get the background average of the green image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Background average of the green image.
     */
    public double getSample_Gene_GreenBackgroundAvg(int sample)
    {
        return project.getSample_Gene_GreenBackgroundAvg(sample);
    }

    /**
     * Get the foreground average of the green image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Foreground average of the green image.
     */
    public double getSample_Gene_GreenForegroundAvg(int sample)
    {
        return project.getSample_Gene_GreenForegroundAvg(sample);
    }

    /**
     * Get the background average of the red image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Background average of the red image.
     */
    public double getSample_Gene_RedBackgroundAvg(int sample)
    {
        return project.getSample_Gene_RedBackgroundAvg(sample);
    }

    /**
     * Get the foreground average of the red image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Foreground average of the red image.
     */
    public double getSample_Gene_RedForegroundAvg(int sample)
    {
        return project.getSample_Gene_RedForegroundAvg(sample);
    }

    /**
     * Set the full filepath for the list of genes for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param filePath The full filepath.
     */
    public void setSample_Gene_ListFile(int sample, String filePath)
    {
        project.setSample_Gene_ListFile(sample, filePath);
    }

    /**
     * Get the polygon for the specified spot in the specified grid of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @param spot The spot in the grid.
     * @return Polygon of the spot in the grid.
     */
    public Polygon getSample_Grid_Spot(int sample, int grid, int spot)
    {
        return project.getSample_Grid_Spot(sample, grid, spot);
    }

    public void setSample_RatioMethod(Object caller, int sample, int method)
    {
        if (caller != null && caller instanceof  GuiManager)
        {
            project.setSample_RatioMethod(sample, method);
        }
    }

    public void setSample_SegmentationMethod(Object caller, int sample, int method)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.setSample_SegmentationMethod(sample, method);
        }
    }

    public void setSample_MethodThreshold(Object caller, int sample, int threshold)
    {
        if (caller != null && caller instanceof GuiManager)
        {
            project.setSample_MethodThreshold(sample, threshold);
        }
    }

    public Image getSample_GreenImage(int sample)
    {
        return project.getSample_GreenImage(sample);
    }

    public Image getSample_RedImage(int sample)
    {
        return project.getSample_RedImage(sample);
    }

    public ImagePlus getSample_GreenImagePlus(int sample)
    {
        return project.getSample_GreenImagePlus(sample);
    }

    public ImagePlus getSample_RedImagePlus(int sample)
    {
        return project.getSample_RedImagePlus(sample);
    }
}
