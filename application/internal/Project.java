package application.internal;

import ij.ImagePlus;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Lukas Pihl
 */
public class Project
{
    private ArrayList<Sample> sample_list;
    private ArrayList<GridProfile> grid_profiles;

    private String path;
    private String name;

    /**
     * Creates new project with default settings.
     */
    public Project()
    {
        sample_list = new ArrayList<Sample>();
        grid_profiles = new ArrayList<GridProfile>();
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public String getName()
    {
        return name;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Grid Profile Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Adds a new grid profile to the list of grid profiles
     * @param name The name of the grid profile
     * @param num The number of grids in the grid profile
     * @param hor The horizontal flag for the grid profile
     * @param ver The vertical flag for the grid profile
     * @param dir The direction flag for the grid profile
     */
    public void addGridProfile(String name, int num, boolean hor, boolean ver, boolean dir)
    {
        grid_profiles.add(new GridProfile(name, num, hor, ver, dir));
    }

    /**
     * Returns the number of grid profiles
     * @return The current number of grid profiles
     */
    public int getGridProfile_Count()
    {
        return grid_profiles.size();
    }

    /**
     * Removes the grid profile from the specific index.
     * @param index The index of the grid profile to remove. Base index 0.
     */
    public void removeGridProfile(int index)
    {
        grid_profiles.remove(index);
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
        grid_profiles.get(index).setAll(name, num, hor, ver, dir);
    }

    /**
     * Return the name for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The name of the grid profile.
     */
    public String getGridProfile_Name(int index)
    {
        return grid_profiles.get(index).getName();
    }

    /**
     * Return the number of grids for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The number of grids.
     */
    public int getGridProfile_Number(int index)
    {
        return grid_profiles.get(index).getNumber();
    }

    /**
     * Return the horizontal flag for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The horizontal flag.
     */
    public boolean getGridProfile_Horizontal(int index)
    {
        return grid_profiles.get(index).getHorizontal();
    }

    /**
     * Return the vertical flag for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The vertical flag.
     */
    public boolean getGridProfile_Vertical(int index)
    {
        return grid_profiles.get(index).getVertical();
    }

    /**
     * Return the direction flag for the specified grid profile.
     * @param index The index of the grid profile. Base index 0.
     * @return The direction flag.
     */
    public boolean getGridProfile_Direction(int index)
    {
        return grid_profiles.get(index).getDirection();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Sample Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add new profile to the project.
     */
    public void addSample()
    {
        sample_list.add(new Sample());
    }

    public void addSample(String path, String name, ImagePlus green, ImagePlus red)
    {
        sample_list.add(new Sample(path, name, green, red));
    }

    public void addSample(String greenPath, String redPath) { sample_list.add(new Sample(greenPath, redPath)); }

    public int getSampleCount()
    {
        return sample_list.size();
    }

    public String getSample_Name(int sample)
    {
        return sample_list.get(sample).getName();
    }

    public String getSample_FilePath(int sample)
    {
        return sample_list.get(sample).getFilePath();
    }

    public Sample getSample(int sample)
    {
        return sample_list.get(sample);
    }

    /**
     * Gets the number of grids for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return The number of grids for the sample.
     */
    public int getSample_GridCount(int sample)
    {
        if (sample_list.size() > 0)
        {
            return sample_list.get(sample).getGridCount();
        }
        return 0;
    }

    public void addSample_Grid(int sample, int leftX, int rightX, int topY, int bottomY, double angle, int rows, int columns)
    {
        sample_list.get(sample).addGrid(leftX, rightX, topY, bottomY, angle, rows, columns);
    }

    public void addSample_Grid(int sample, int tlX, int tlY, int trX, int trY, int bX, int bY, int rows, int columns)
    {
        sample_list.get(sample).addGrid(tlX, tlY, trX, trY, bX, bY, rows, columns);
    }

    public void removeSample_Grid(int sample, int grid)
    {
        sample_list.get(sample).removeGrid(grid);
    }

    public Polygon getSample_Grid_Polygon_Master(int sample, int grid)
    {
        return sample_list.get(sample).getGrid_Polygon_Master(grid);
    }

    /**
     * Gets the polygon for the specified grid of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return The polygon of the sample's grid.
     */
    public Polygon getSample_Grid_Polygon_Base(int sample, int grid)
    {
        return sample_list.get(sample).getGrid_Polygon_Base(grid);
    }

    /**
     * Gets the translated polygon for the specified grid of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return The translated polygon of the sample's grid.
     */
    public Polygon getSample_Grid_Polygon_Outline(int sample, int grid)
    {
        return sample_list.get(sample).getGrid_Polygon_Outline(grid);
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
        return sample_list.get(sample).getGrid_Polygon_VerticalLines(grid);
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
        return sample_list.get(sample).getGrid_Polygon_HorizontalLines(grid);
    }

    public void setSample_Grid_MoveBy(int sample, int grid, int x, int y)
    {
        sample_list.get(sample).setGrid_MoveBy(grid, x, y);
    }

    public void setSample_Grid_MoveTo(int sample, int grid, int x, int y)
    {
        sample_list.get(sample).setGrid_MoveTo(grid, x, y);
    }

    public void setSample_Grid_RotateBy(int sample, int grid, double degree)
    {
        sample_list.get(sample).setGrid_RotateBy(grid, degree);
    }

    public void setSample_Grid_RotateTo(int sample, int grid, double degree)
    {
        sample_list.get(sample).setGrid_RotateTo(grid, degree);
    }

    public void setSample_Grid_ResizeBy(int sample, int grid, int height, int width)
    {
        sample_list.get(sample).setGrid_ResizeBy(grid, height, width);
    }

    public void setSample_Grid_ResizeTo(int sample, int grid, int height, int width)
    {
        sample_list.get(sample).setGrid_ResizeTo(grid, height, width);
    }

    public double getSample_Grid_Angle(int sample, int grid)
    {
        return sample_list.get(sample).getGrid_Angle(grid);
    }

    public int[] getSample_Grid_MasterPoints(int sample, int grid)
    {
        return sample_list.get(sample).getGrid_MasterPoints(grid);
    }

    public int[] getSample_Grid_RowsAndColumns(int sample, int grid)
    {
        return sample_list.get(sample).getGrid_RowsAndColumns(grid);
    }

    public void removeSample_Grid_All(int sample)
    {
        sample_list.get(sample).removeGrid_All();
    }

    /**
     * Set the horizontal flag for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param hori Flag value to set.
     */
    public void setSample_GridHorizontal(int sample, boolean hori)
    {
        sample_list.get(sample).setGridHorizontal(hori);
    }

    /**
     * Set the vertical flag for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param vert Flag value to set.
     */
    public void setSample_GridVertical(int sample, boolean vert)
    {
        sample_list.get(sample).setGridVertical(vert);
    }

    /**
     * Set the direction flag for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param dir Flag value to set.
     */
    public void setSample_GridDirection(int sample, boolean dir)
    {
        sample_list.get(sample).setGridDirection(dir);
    }

    /**
     * Get the number of columns in the specified grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return Number of columns in the grid.
     */
    public int getSample_Grid_Columns(int sample, int grid)
    {
        return sample_list.get(sample).getGrid_Columns(grid);
    }

    /**
     * Get the number of spots in the specified grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return Number of spots in the grid.
     */
    public int getSample_Grid_NumberOfSpots(int sample, int grid)
    {
        return sample_list.get(sample).getGrid_NumberOfSpots(grid);
    }

    /**
     * Sets the focus to the specified grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     */
    public void setSample_CurrentGrid(int sample, int grid)
    {
        sample_list.get(sample).setCurrentGrid(grid);
    }

    /**
     * Sets the focus to the specified spot of the focused grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param spot The spot in the focused grid.
     */
    public void setSample_CurrentSpot(int sample, int spot)
    {
        sample_list.get(sample).setCurrentSpot(spot);
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
        return sample_list.get(sample).getGrid_ActualSpotNum(grid, spot);
    }

    /**
     * Gets the index of the currently focused grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Index of the grid.
     */
    public int getSample_CurrentGridNum(int sample)
    {
        return sample_list.get(sample).getCurrentGridNumber();
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
        sample_list.get(sample).setGrid_Spots(grid, spots);
    }

    /**
     * Gets the polygon of the currently focused spot of the specified grid of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @return Polygon of the spot.
     */
    public Polygon getSample_Grid_CurrentSpot(int sample, int grid)
    {
        return sample_list.get(sample).getGrid_CurrentSpot(grid);
    }

    /**
     * Gets the horizontal flag value for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Horizontal flag value for the grids.
     */
    public boolean getSample_GridHorizontal(int sample)
    {
        if (sample > sample_list.size()-1)
        {
            return true;
        }
        return sample_list.get(sample).getGridHorizontal();
    }

    /**
     * Gets the vertical flag value for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Vertical flag value for the grids.
     */
    public boolean getSample_GridVertical(int sample)
    {
        if (sample > sample_list.size()-1)
        {
            return true;
        }
        return sample_list.get(sample).getGridVertical();
    }

    /**
     * Gets the direction flag value for the grids of the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Direction flag value for the grids.
     */
    public boolean getSample_GridDirection(int sample)
    {
        if (sample > sample_list.size()-1)
        {
            return true;
        }
        return sample_list.get(sample).getGridDirection();
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
        sample_list.get(sample).setGene_Data(redPixels, greenPixels, height, width, type, params);
    }

    /**
     * Gets the center and radius of the adaptive circle for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return An array with [0] = x position of center, [1] = y position of center, [2] = radius.
     */
    public int[] getSample_Gene_CenterAndRadius(int sample)
    {
        return sample_list.get(sample).getGene_CenterAndRadius();
    }

    /**
     * Gets the foreground pixels using the SeededRegion growing method for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param isRed Whether the foreground is for the red image.
     * @return An array of center spots, which is an array consisting of [0] = column and [1] = row.
     */
    public int[][] getSample_Gene_CenterSpots(int sample, boolean isRed)
    {
        return sample_list.get(sample).getGene_CenterSpots(isRed);
    }

    /**
     * Gets the ratio of the gene data from the specified sample using the specified method.
     * @param sample The index of the sample. Base index 0.
     * @param aspect Method to use to get ratio.
     * @return The ratio for the gene data.
     */
    public double getSample_Gene_Ratio(int sample, int aspect)
    {
        return sample_list.get(sample).getGene_Ratio(aspect);
    }

    /**
     * Get the background total of the green image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Background total of the green image.
     */
    public int getSample_Gene_GreenBackgroundTotal(int sample)
    {
        return sample_list.get(sample).getGene_GreenBackgroundTotal();
    }

    /**
     * Get the foreground total of the green image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Foreground total of the green image.
     */
    public int getSample_Gene_GreenForegroundTotal(int sample)
    {
        return sample_list.get(sample).getGene_GreenForegroundTotal();
    }

    /**
     * Get the background total of the red image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Background total of the red image.
     */
    public int getSample_Gene_RedBackgroundTotal(int sample)
    {
        return sample_list.get(sample).getGene_RedBackgroundTotal();
    }

    /**
     * Get the foreground total of the red image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Foreground total of the red image.
     */
    public int getSample_Gene_RedForegroundTotal(int sample)
    {
        return sample_list.get(sample).getGene_RedForegroundTotal();
    }

    /**
     * Get the background average of the green image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Background average of the green image.
     */
    public double getSample_Gene_GreenBackgroundAvg(int sample)
    {
        return sample_list.get(sample).getGene_GreenBackgroundAvg();
    }

    /**
     * Get the foreground average of the green image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Foreground average of the green image.
     */
    public double getSample_Gene_GreenForegroundAvg(int sample)
    {
        return sample_list.get(sample).getGene_GreenForegroundAvg();
    }

    /**
     * Get the background average of the red image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Background average of the red image.
     */
    public double getSample_Gene_RedBackgroundAvg(int sample)
    {
        return sample_list.get(sample).getGene_RedBackgroundAvg();
    }

    /**
     * Get the foreground average of the red image of the gene data for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @return Foreground average of the red image.
     */
    public double getSample_Gene_RedForegroundAvg(int sample)
    {
        return sample_list.get(sample).getGene_RedForegroundAvg();
    }

    /**
     * Set the full filepath for the list of genes for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param filePath The full filepath.
     */
    public void setSample_Gene_ListFile(int sample, String filePath)
    {
        sample_list.get(sample).setGene_ListFile(filePath);
    }

    /**
     * Get the name of the gene at the specified spot of the specified grid for the specified sample.
     * @param sample The index of the sample. Base index 0.
     * @param grid The index of the grid. Base index 0.
     * @param spot The spot in the grid.
     * @return Name of the gene.
     */
    public String getSample_Gene_Name(int sample, int grid, int spot)
    {
        return sample_list.get(sample).getGene_Name(grid, spot);
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
        return sample_list.get(sample).getGrid_Spot(grid, spot);
    }

    /**
     * TODO
     * @param sample
     * @return
     */
    public int getSampleCurrentSpotNumber(int sample)
    {
        return sample_list.get(sample).getCurrentSpotNumber();
    }

    public void setSample_RatioMethod(int sample, int method)
    {
        sample_list.get(sample).setRatioMethod(method);
    }

    public void setSample_SegmentationMethod(int sample, int method)
    {
        sample_list.get(sample).setSegmentationMethod(method);
    }

    public void setSample_MethodThreshold(int sample, int threshold)
    {
        sample_list.get(sample).setMethodThreshold(threshold);
    }

    public Image getSample_GreenImage(int sample)
    {
        return sample_list.get(sample).getGreenImage();
    }

    public Image getSample_RedImage(int sample)
    {
        return sample_list.get(sample).getRedImage();
    }

    public ImagePlus getSample_GreenImagePlus(int sample)
    {
        return sample_list.get(sample).getGreenImagePlus();
    }

    public ImagePlus getSample_RedImagePlus(int sample)
    {
        return sample_list.get(sample).getRedImagePlus();
    }

    public void setSample_Grid_RowsAndColumns(int sample, int grid, int[] rc)
    {
        sample_list.get(sample).setGrid_RowsAndColumns(grid, rc);
    }
}
