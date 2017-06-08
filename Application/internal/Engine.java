package newapp.internal;

import java.awt.*;

import Application.GeneImageAspect;
import newapp.file.ExpressionWriter;

/**
 * Created by Lukas Pihl
 */
public class Engine
{
    private Project project;
    private ExpressionWriter exp_writer;

    /**
     * Creates new Engine with default settings
     */
    public Engine()
    {
        project = new Project();
        exp_writer = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // File Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Opens the file writer for exporting expression data.
     * Be sure to use close function when the writer is no longer needed.
     * @param sample The number of the sample to export expression data from
     * @param filePath The full file path to export the data to
     */
    public void File_ExportExpression_Open(int sample, String filePath)
    {
        exp_writer = new ExpressionWriter(sample, filePath);
    }

    /**
     * Closes the file writer for exporting expression data.
     */
    public void File_ExportExpression_Close()
    {
        //TODO: Close ExpressionWriter
        exp_writer = null;
    }

    //TODO: Find a better way of handling parameters
    /**
     * Sends data to the expression data writer. The sample number will have been specified when initiating the writer.
     * @param grid The grid to get data from.
     * @param spot The spot in the grid to get data from.
     * @param params Various pieces of data needed to get gene data. Array should be size 6.
     *               0 = int[] of red pixel data
     *               1 = int[] of green pixel data
     *               2 = int of height
     *               3 = int of width
     *               4 = int of type
     *               5 = Object[] of extra parameter
     */
    public void File_ExportExpression_WriteLine(int grid, int spot, Object[] params)
    {
        if (exp_writer != null)
        {
            String name = project.getSample_Gene_Name(exp_writer.getSampleNumber(), grid, spot);
            project.setSample_Gene_Data(exp_writer.getSampleNumber(), (int[])params[0], (int[])params[1], (int)params[2], (int)params[3], (int)params[4], (Object[])params[5]);
            double ratio = project.getSample_Gene_Ratio(1, GeneImageAspect.AVG_SIGNAL);
            exp_writer.writeLine(name, ratio);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Grid Profile Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a grid profile to the project
     * @param name The name of the grid profile
     * @param num The number of grids in the grid profile
     * @param hor The horizontal flag for the grid profile
     * @param ver The vertical flag for the grid profile
     * @param dir The direction flag for the grid profile
     */
    public void addGridProfile(String name, int num, boolean hor, boolean ver, boolean dir)
    {
        project.addGridProfile(name, num, hor, ver, dir);
    }

    /**
     * Returns the number of grid profiles
     * @return The current number of grid profiles
     */
    public int getGridProfile_Count()
    {
        return project.getGridProfile_Count();
    }

    /**
     * Removes the grid profile from the specific index.
     * @param index The index of the grid profile to remove. Base index 0.
     */
    public void removeGridProfile(int index)
    {
        project.removeGridProfile(index);
    }

    /**
     * Modifies the grid profile at the specified index.
     * @param index The index of the grid profile to remove. Base index 0.
     * @param name The new name for the grid profile.
     * @param num The new number of grids for the grid profile.
     * @param hor The new horizontal flag for the grid profile.
     * @param ver The new vertical flag for the grid profile.
     * @param dir The new direction flag for the grid profile.
     */
    public void modifyGridProfile(int index, String name, int num, boolean hor, boolean ver, boolean dir)
    {
        project.modifyGridProfile(index, name, num, hor, ver, dir);
    }

    /**
     * Return the name for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The name of the grid profile.
     */
    public String getGridProfile_Name(int index)
    {
        return project.getGridProfile_Name(index);
    }

    /**
     * Return the number of grids for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The number of grids.
     */
    public int getGridProfile_Number(int index)
    {
        return project.getGridProfile_Number(index);
    }

    /**
     * Return the horizontal flag for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The horizontal flag.
     */
    public boolean getGridProfile_Horizontal(int index)
    {
        return project.getGridProfile_Horizontal(index);
    }

    /**
     * Return the vertical flag for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The vertical flag.
     */
    public boolean getGridProfile_Vertical(int index)
    {
        return project.getGridProfile_Vertical(index);
    }

    /**
     * Return the direction flag for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The direction flag.
     */
    public boolean getGridProfile_Direction(int index)
    {
        return project.getGridProfile_Direction(index);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Sample Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add new profile to the project.
     */
    public void addSample()
    {
        project.addSample();
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

    /**
     * Gets the polygon for the specified grid of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return The polygon of the sample's grid.
     */
    public Polygon getSample_Grid_Polygon(int sample, int grid)
    {
        return project.getSample_Grid_Polygon(sample, grid);
    }

    /**
     * Gets the translated polygon for the specified grid of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return The translated polygon of the sample's grid.
     */
    public Polygon getSample_Grid_TranslatedPolygon(int sample, int grid)
    {
        return project.getSample_Grid_TranslatedPolygon(sample, grid);
    }

    /**
     * Gets an array of polygons containing the vertical lines separating columns based on the given
     * polygon for a specific grid for a specific sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @param poly Polygon to create vertical lines from.
     * @return Array of polygons containing the vertical lines.
     */
    public Polygon[] getSample_Grid_VertLines(int sample, int grid, Polygon poly)
    {
        return project.getSample_Grid_VertLines(sample, grid, poly);
    }

    /**
     * Gets an array of polygons containing the horizontal lines separating rows based on the given
     * polygon for a specific grid for a specific sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @param poly Polygon to create vertical lines from.
     * @return Array of polygons containing the vertical lines.
     */
    public Polygon[] getSample_Grid_HoriLines(int sample, int grid, Polygon poly)
    {
        return project.getSample_Grid_HoriLines(sample, grid, poly);
    }

    /**
     * Set all the features for a specific grid for a specific sample at once.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @param tlX Top left x coordinate.
     * @param tlY Top left y coordinate.
     * @param trX Top right x coordinate.
     * @param trY Top right y coordinate.
     * @param blX Bottom left x coordinate.
     * @param blY Bottom left y coordinate.
     * @param brX Bottom right x coordinate.
     * @param brY Bottom right y coordinate.
     * @param row Number of rows.
     * @param col Number of columns.
     */
    public void setSample_Grid_AllFeatures(int sample, int grid, int tlX, int tlY, int trX, int trY, int blX, int blY,
                                           int brX, int brY, int row, int col)
    {
        project.setSample_Grid_AllFeatures(sample, grid, tlX, tlY, trX, trY, blX, blY, brX, brY, row, col);
    }

    /**
     * Set the number of grids for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param count Number to set for grid count.
     */
    public void setSample_GridCount(int sample, int count)
    {
        project.setSample_GridCount(sample, count);
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
    public void setSample_CurrentGrid(int sample, int grid)
    {
        project.setSample_CurrentGrid(sample, grid);
    }

    /**
     * Sets the focus to the specified spot of the focused grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param spot The spot in the focused grid.
     */
    public void setSample_CurrentSpot(int sample, int spot)
    {
        project.setSample_CurrentSpot(sample, spot);
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
    public void setSample_Grid_Spots(int sample, int grid, Polygon spots)
    {
        project.setSample_Grid_Spots(sample, grid, spots);
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
    public void setSample_Gene_Data(int sample, int[] redPixels, int[] greenPixels, int height, int width, int type, Object[] params)
    {
        project.setSample_Gene_Data(sample, redPixels, greenPixels, height, width, type, params);
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
    public double getSample_Gene_Ratio(int sample, GeneImageAspect aspect)
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
}
