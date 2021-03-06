package application.internal;

import java.awt.*;

import application.tools.GeneImageAspect;
import ij.ImagePlus;
import ij.io.Opener;

/**
 * Created by Lukas Pihl
 */
public class Sample
{
    private GridManager grid_manager;
    private FlagManager flag_manager;
    private int current_grid_number = 0;
    private int current_spot_number = 0;
    private GeneData gene_data;
    private SingleGeneImage current_gene;
    private GeneList gene_list;
    private ImagePlus green_IP = null;
    private ImagePlus red_IP = null;
    private Image green_I = null;
    private Image red_I = null;

    private int segment_method = 101;
    private int ratio_method = 1;
    private int method_threshold = 10;

    private String name;
    private String path;

    /**
     * Creates new sample with default settings.
     */
    public Sample()
    {
        grid_manager = new GridManager();
        flag_manager = new FlagManager();
        gene_list = null;
    }

    /**
     * Creates new sample with image pixels loaded.
     */
    public Sample(String greenPath, String redPath)
    {
        this();
        loadImagePlus(greenPath, redPath);
        name = "";
        path = "";
    }

    public Sample(String path, String name, ImagePlus green, ImagePlus red)
    {
        this();
        this.path = path;
        this.name = name;
        this.green_IP = green;
        this.red_IP = red;
    }

    public String getName()
    {
        return name;
    }

    public String getFilePath()
    {
        return path;
    }

    public void setRatioMethod(int method)
    {
        ratio_method = method;
    }

    public void setSegmentationMethod(int method)
    {
        segment_method = method;
    }

    public int getRatioMethod()
    {
        return ratio_method;
    }

    public int getSegmentationMethod()
    {
        return segment_method;
    }

    public void setMethodThreshold(int threshold)
    {
        method_threshold = threshold;
    }

    public int getMethodThreshold()
    {
        return method_threshold;
    }

    public Object[] getParameters()
    {
        Object[] params = null;
        if (segment_method == GeneImageAspect.FIXED_CIRCLE)
        {
            params = new Object[]{6};
        }
        else if (segment_method == GeneImageAspect.SEEDED_REGION)
        {
            params = new Object[]{getMethodThreshold()};
        }
        else if (segment_method == GeneImageAspect.ADAPTIVE_CIRCLE)
        {
            params = new Object[]{3, 8, getMethodThreshold()};
        }

        return params;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Image Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void loadImagePlus(String greenPath, String redPath) {
        Opener greenImage = new Opener();
        Opener redImage = new Opener();
        green_IP = greenImage.openImage(greenPath);
        red_IP = redImage.openImage(redPath);
    }

    public int[][] getGreenPixels()
    {
        int[][] grnPixels = new int[green_IP.getHeight()][green_IP.getWidth()];

        for(int i = 0; i < green_IP.getHeight(); i++)
        {
            for (int j = 0; j < green_IP.getWidth(); j++)
            {
                grnPixels[i][j] = green_IP.getProcessor().getPixel(j,i);
            }
        }

        return grnPixels;
    }

    public int[][] getRedPixels()
    {
        int[][] redPixels = new int[red_IP.getHeight()][red_IP.getWidth()];

        for(int i = 0; i < red_IP.getHeight(); i++)
        {
            for(int j = 0; j < red_IP.getWidth(); j++)
            {
                redPixels[i][j] = red_IP.getProcessor().getPixel(j,i);
            }
        }

        return redPixels;
    }

    public Image getGreenImage()
    {
        if (green_I == null)
        {
            green_I = green_IP.getImage();
        }
        return green_I;
    }

    public Image getRedImage()
    {
        if (red_I == null)
        {
            red_I = red_IP.getImage();
        }
        return red_I;
    }

    public ImagePlus getGreenImagePlus()
    {
        return green_IP;
    }

    public ImagePlus getRedImagePlus()
    {
        return red_IP;
    }

    public int[] getGreenCellPixels(int grid, int spotNum){
        Polygon p = this.getGrid_Spot(grid, spotNum);
        int w = Math.abs(p.xpoints[1]-p.xpoints[0]);
        int h = Math.abs(p.ypoints[2]-p.ypoints[0]);
        int[] pixels = new int[w*h];

        int[][] RawPixels = this.getGreenPixels();
        try{
            int k = 0;
            for(int y = p.ypoints[0]; y < p.ypoints[2]; y++) {
                for(int x =p.xpoints[0]; x < p.xpoints[1]; x++) {
                    pixels[k++] = RawPixels[y][x];
                }

            }
            return pixels;

        }catch(Exception e3){System.out.print("Error");}

        return null;
    }

    public int[] getRedCellPixels(int grid, int spotNum){
        Polygon p = this.getGrid_Spot(grid, spotNum);
        int w = Math.abs(p.xpoints[1]-p.xpoints[0]);
        int h = Math.abs(p.ypoints[2]-p.ypoints[0]);
        int[] pixels = new int[w*h];

        int[][] RawPixels = this.getGreenPixels();
        try{
            int k = 0;
            for(int y = p.ypoints[0]; y < p.ypoints[2]; y++) {
                for(int x =p.xpoints[0]; x < p.xpoints[1]; x++) {
                    pixels[k++] = RawPixels[y][x];
                }

            }
            return pixels;

        }catch(Exception e3){System.out.print("Error");}

        return null;
    }

    public int getCellHeight(int grid, int spotNum)
    {
        Polygon p = this.getGrid_Spot(grid, spotNum);
        return Math.abs(p.ypoints[2]-p.ypoints[0]);
    }

    public int getCellWidth(int grid, int spotNum)
    {
        Polygon p = this.getGrid_Spot(grid, spotNum);
        return Math.abs(p.xpoints[1]-p.xpoints[0]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Grid Manager Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public GridManager getGridManager()
    {
        return grid_manager;
    }

    /**
     * Gets the number of grids.
     * @return The number of grids for the sample.
     */
    public int getGridCount()
    {
        return grid_manager.getNumGrids();
    }

    public void addGrid(int leftX, int rightX, int topY, int bottomY, double angle, int rows, int columns)
    {
        grid_manager.setGridNum(getGridCount() + 1);
        grid_manager.setGrid(getGridCount() - 1, new Grid(leftX, rightX, topY, bottomY, angle, rows, columns));
    }

    public void addGrid(int tlX, int tlY, int trX, int trY, int bX, int bY, int rows, int columns)
    {
        grid_manager.setGridNum(getGridCount() + 1);
        grid_manager.setGrid(getGridCount() - 1, new Grid(tlX, tlY, trX, trY, bX, bY, rows, columns));
    }

    public void removeGrid(int grid)
    {
        int i = grid;
        while (i < getGridCount() - 1)
        {
            grid_manager.setGrid(i, grid_manager.getGrid(i + 1));
            i++;
        }
        if (grid < getGridCount())
        {
            grid_manager.setGridNum(getGridCount() - 1);
        }
    }

    public void removeGrid_All()
    {
        grid_manager.setGridNum(0);
    }

    public Polygon getGrid_Polygon_Master(int grid)
    {
        return grid_manager.getGrid(grid).getPolygon_Master();
    }

    /**
     * Gets the polygon for the specified grid.
     * @param grid The index of the grid. Base index 0.
     * @return The polygon of the sample's grid.
     */
    public Polygon getGrid_Polygon_Base(int grid)
    {
        return grid_manager.getGrid(grid).getPolygon_Base();
    }

    /**
     * Gets the translated polygon for the specified grid.
     * @param grid The index of the grid. Base index 0.
     * @return The translated polygon of the sample's grid.
     */
    public Polygon getGrid_Polygon_Outline(int grid)
    {
        return grid_manager.getGrid(grid).getPolygon_Outline();
    }

    /**
     * Gets an array of polygons containing the vertical lines separating columns based on the given
     * polygon for a specific grid.
     * @param grid The index of the grid. Base index 0.
     * @return Array of polygons containing the vertical lines.
     */
    public Polygon[] getGrid_Polygon_VerticalLines(int grid)
    {
        return grid_manager.getGrid(grid).getPolygon_VerticalLines(getGrid_Polygon_Outline(grid));
    }

    /**
     * Gets an array of polygons containing the horizontal lines separating rows based on the given
     * polygon for a specific grid.
     * @param grid The index of the grid. Base index 0.
     * @return Array of polygons containing the vertical lines.
     */
    public Polygon[] getGrid_Polygon_HorizontalLines(int grid)
    {
        return grid_manager.getGrid(grid).getPolygon_HorizontalLines(getGrid_Polygon_Outline(grid));
    }

    public void setGrid_MoveBy(int grid, int x, int y)
    {
        grid_manager.getGrid(grid).moveBy(x, y);
    }

    public void setGrid_MoveTo(int grid, int x, int y)
    {
        grid_manager.getGrid(grid).moveTo(x, y);
    }

    public void setGrid_RotateBy(int grid, double degree)
    {
        grid_manager.getGrid(grid).rotateBy(degree);
    }

    public void setGrid_RotateTo(int grid, double degree)
    {
        grid_manager.getGrid(grid).rotateTo(degree);
    }

    public void setGrid_ResizeBy(int grid, int height, int width)
    {
        grid_manager.getGrid(grid).resizeBy(height, width);
    }

    public void setGrid_ResizeTo(int grid, int height, int width)
    {
        grid_manager.getGrid(grid).resizeTo(height, width);
    }

    public double getGrid_Angle(int grid)
    {
        return grid_manager.getGrid(grid).getAngle();
    }

    public int[] getGrid_MasterPoints(int grid)
    {
        Grid g = grid_manager.getGrid(grid);
        return new int[]{g.getLeftX(), g.getRightX(), g.getTopY(), g.getBottomY()};
    }

    public int[] getGrid_RowsAndColumns(int grid)
    {
        Grid g = grid_manager.getGrid(grid);
        return new int[]{g.getRows(), g.getColumns()};
    }

    /**
     * Set the horizontal flag for the grids.
     * @param hori Flag value to set.
     */
    public void setGridHorizontal(boolean hori)
    {
        grid_manager.setLeftRight(hori);
    }

    /**
     * Set the vertical flag for the grids.
     * @param vert Flag value to set.
     */
    public void setGridVertical(boolean vert)
    {
        grid_manager.setTopBottom(vert);
    }

    /**
     * Set the direction flag for the grids.
     * @param dir Flag value to set.
     */
    public void setGridDirection(boolean dir)
    {
        grid_manager.setSpotDirection(dir);
    }

    /**
     * Get the number of columns in the specified grid.
     * @param grid The index of the grid. Base index 0.
     * @return Number of columns in the grid.
     */
    public int getGrid_Columns(int grid)
    {
        Grid g = grid_manager.getGrid(grid);
        if (g == null)
        {
            return 0;
        }
        return g.getColumns();
    }

    /**
     * Get the number of spots in the specified grid.
     * @param grid The index of the grid. Base index 0.
     * @return Number of spots in the grid.
     */
    public int getGrid_NumberOfSpots(int grid)
    {
        Grid g = grid_manager.getGrid(grid);
        if (g == null)
        {
            return 0;
        }
        return g.getNumOfSpots();
    }

    /**
     * Sets the focus to the specified grid.
     * @param grid The index of the grid. Base index 0.
     */
    public void setCurrentGrid(int grid)
    {
        grid_manager.setCurrentGrid(grid);
        current_grid_number = grid;
    }

    /**
     * Sets the focus to the specified spot of the focused grid.
     * @param spot The spot in the focused grid.
     */
    public void setCurrentSpot(int spot)
    {
        grid_manager.getCurrentGrid().setCurrentSpot(spot);
        current_spot_number = spot;
    }

    /**
     * TODO
     * @return
     */
    public int getCurrentSpotNumber()
    {
        return current_spot_number;
    }

    /**
     * Gets the actual spot number from a transformed spot number for the specified grid.
     * An actual spot number is based on left to right, top to bottom, and horizontal (spot 2 to spot 1) placement.
     * A transformed spot number is based on user specified spot placement parameters.
     * @param grid The index of the grid. Base index 0.
     * @param spot Transformed spot number.
     * @return Actual spot number.
     */
    public int getGrid_ActualSpotNum(int grid, int spot)
    {
        return grid_manager.getActualSpotNum(grid, spot);
    }

    /**
     * Gets the index of the currently focused grid.
     * @return Index of the grid.
     */
    public int getCurrentGridNumber()
    {
        return current_grid_number;
    }

    /**
     * Sets the spots coordinates for the spots based on a translated polygon and the number of rows and columns of
     * spots for the specified grid. A translated polygon is a polygon which encompasses
     * every spots in the grid (since the grid coordinates are based on the center of a spot).
     * @param grid The index of the grid. Base index 0.
     * @param spots Translated polygon to set the spots from.
     */
    public void setGrid_Spots(int grid, Polygon spots)
    {
        grid_manager.getGrid(grid).setSpots(spots);
    }

    /**
     * Gets the polygon of the currently focused spot of the specified grid.
     * @param grid The index of the grid. Base index 0.
     * @return Polygon of the spot.
     */
    public Polygon getGrid_CurrentSpot(int grid)
    {
        return grid_manager.getGrid(grid).getCurrentSpot();
    }

    /**
     * Gets the horizontal flag value for the grids.
     * @return Horizontal flag value for the grids.
     */
    public boolean getGridHorizontal()
    {
        return grid_manager.getLeftRight();
    }

    /**
     * Gets the vertical flag value for the grids.
     * @return Vertical flag value for the grids.
     */
    public boolean getGridVertical()
    {
        return grid_manager.getTopBottom();
    }

    /**
     * Gets the direction flag value for the grids.
     * @return Direction flag value for the grids.
     */
    public boolean getGridDirection()
    {
        return grid_manager.getSpotDirection();
    }

    /**
     * Set the full filepath for the list of genes.
     * @param filePath The full filepath.
     */
    public void setGene_ListFile(String filePath)
    {
        gene_list = new GeneList(filePath);
        grid_manager.setGeneList(gene_list);
    }

    /**
     * Get the name of the gene at the specified spot of the specified grid.
     * @param grid The index of the grid. Base index 0.
     * @param spot The spot in the grid.
     * @return Name of the gene.
     */
    public String getGene_Name(int grid, int spot)
    {
        return grid_manager.getGeneName(grid, spot);
    }

    /**
     * Get the polygon for the specified spot in the specified grid.
     * @param grid The index of the grid. Base index 0.
     * @param spot The spot in the grid.
     * @return Polygon of the spot in the grid.
     */
    public Polygon getGrid_Spot(int grid, int spot)
    {
        return grid_manager.getGrid(grid).getSpot(spot);
    }

    public void setGrid_RowsAndColumns(int grid, int[] rc)
    {
        Grid g = grid_manager.getGrid(grid);
        g.setRows(rc[0]);
        g.setColumns(rc[1]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Gene Data Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Set the data for the focused gene.
     * @param redPixels Relevant image data from the red image.
     * @param greenPixels Relevant image data from the green image.
     * @param height Height of the image data.
     * @param width width of the image data.
     * @param type 1 = Adaptive circle. 2 = Seeded Region.
     * @param params List of extra parameters for the type.
     */
    public void setGene_Data(int[] redPixels, int[] greenPixels, int height, int width, int type, Object[] params)
    {
        current_gene = new SingleGeneImage(redPixels, greenPixels, height, width);
        if (type == 2)
        {
            gene_data = current_gene.getData(SingleGeneImage.ADAPTIVE_CIRCLE, params);
        }
        else if (type == 1)
        {
            gene_data = current_gene.getData(SingleGeneImage.SEEDED_REGION, params);
        }
    }

    /**
     * Gets the center and radius of the adaptive circle.
     * @return An array with [0] = x position of center, [1] = y position of center, [2] = radius.
     */
    public int[] getGene_CenterAndRadius()
    {
        return current_gene.getCenterAndRadius();
    }

    /**
     * Gets the foreground pixels using the SeededRegion growing method.
     * @param isRed Whether the foreground is for the red image.
     * @return An array of center spots, which is an array consisting of [0] = column and [1] = row.
     */
    public int[][] getGene_CenterSpots(boolean isRed)
    {
        return current_gene.getCenterSpots(isRed);
    }

    /**
     * Gets the ratio of the gene data using the specified method.
     * @param aspect Method to use to get ratio.
     * @return The ratio for the gene data.
     */
    public double getGene_Ratio(int aspect)
    {
        return gene_data.getRatio(aspect);
    }

    /**
     * Get the background total of the green image of the gene data.
     * @return Background total of the green image.
     */
    public int getGene_GreenBackgroundTotal()
    {
        return gene_data.getGreenBackgroundTotal();
    }

    /**
     * Get the foreground total of the green image of the gene data.
     * @return Foreground total of the green image.
     */
    public int getGene_GreenForegroundTotal()
    {
        return gene_data.getGreenForegroundTotal();
    }

    /**
     * Get the background total of the red image of the gene data.
     * @return Background total of the red image.
     */
    public int getGene_RedBackgroundTotal()
    {
        return gene_data.getRedBackgroundTotal();
    }

    /**
     * Get the foreground total of the red image of the gene data.
     * @return Foreground total of the red image.
     */
    public int getGene_RedForegroundTotal()
    {
        return gene_data.getRedForegroundTotal();
    }

    /**
     * Get the background average of the green image of the gene data.
     * @return Background average of the green image.
     */
    public double getGene_GreenBackgroundAvg()
    {
        return gene_data.getGreenBackgroundAvg();
    }

    /**
     * Get the foreground average of the green image of the gene data.
     * @return Foreground average of the green image.
     */
    public double getGene_GreenForegroundAvg()
    {
        return gene_data.getGreenForegroundAvg();
    }

    /**
     * Get the background average of the red image of the gene data.
     * @return Background average of the red image.
     */
    public double getGene_RedBackgroundAvg()
    {
        return gene_data.getRedBackgroundAvg();
    }

    /**
     * Get the foreground average of the red image of the gene data.
     * @return Foreground average of the red image.
     */
    public double getGene_RedForegroundAvg()
    {
        return gene_data.getRedForegroundAvg();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Flag Manager Functions
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public FlagManager getFlagManager()
    {
        return flag_manager;
    }
}
