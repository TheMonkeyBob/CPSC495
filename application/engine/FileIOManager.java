package application.engine;

import application.file.GridIO;
import application.file.ProjectIO;
import application.file.SampleIO;
import ij.ImagePlus;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Lukas Pihl
 */
public class FileIOManager
{
    private Engine engine;

    public FileIOManager(Engine engine)
    {
        this.engine = engine;
    }

    public void newProject(String path)
    {
        ProjectIO.newProject(path);
    }

    public ArrayList<String> getSamplePaths()
    {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < engine.getSampleCount(); i++)
        {
            list.add(engine.getSample_FilePath(i));
        }
        return list;
    }

    public ArrayList<String> getSampleNames()
    {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < engine.getSampleCount(); i++)
        {
            list.add(engine.getSample_Name(i));
        }
        return list;
    }

    public void writeAllSamples()
    {
        for (int i = 0; i < engine.getSampleCount(); i++)
        {
            SampleIO.writeSample(engine.getSample_FilePath(i), engine.getSample_Name(i), i, this);
        }
    }

    public void readAllSamples(ArrayList<String> paths, ArrayList<String> names)
    {
        for (int i = 0; i < paths.size(); i++)
        {
            SampleIO.readSample(paths.get(i), names.get(i), this);
        }
    }

    public void writeGrids(String path, int sample)
    {
        GridIO.writeGrids(this, path, sample);
    }

    public void addSample(String path, String name, ImagePlus green, ImagePlus red)
    {
        engine.addSample(this, path, name, green, red);
    }

    public void readGrids(String path, int sample)
    {
        GridIO.readGrids(this, path, sample);
    }

    public int getSampleCount()
    {
        return engine.getSampleCount();
    }

    public void addSample_Grid(int sample, int leftX, int rightX, int topY, int bottomY, double angle, int rows,
                               int columns)
    {
        engine.addSample_Grid(sample, leftX, rightX, topY, bottomY, angle, rows, columns);
    }

    public int getSample_GridCount(int sample)
    {
        return engine.getSample_GridCount(sample);
    }

    public int[] getSample_Grid_MasterPoints(int sample, int grid)
    {
        Polygon p = engine.getSample_Grid_Polygon_Master(sample, grid);
        int[] points = new int[]{p.xpoints[0], p.xpoints[1], p.ypoints[0], p.ypoints[3]};
        return points;
    }

    public double getSample_Grid_Angle(int sample, int grid)
    {
        return engine.getSample_Grid_Angle(sample, grid);
    }

    public int[] getSample_Grid_RowsAndColumns(int sample, int grid)
    {
        return engine.getSample_Grid_RowsAndColumns(sample, grid);
    }
}
