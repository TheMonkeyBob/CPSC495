package application.file;

import application.engine.Engine;
import application.engine.FileIOManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by Lukas Pihl
 */
public class GridIO
{
    public static boolean writeGrids(FileIOManager manager, String path, int sample)
    {
        try
        {
            RandomAccessFile file = new RandomAccessFile(path, "rw");
            file.writeChars("GRID");
            file.writeByte(1); //Version
            int size = manager.getSample_GridCount(sample);
            int[] points;
            double angle;
            int[] dim;
            for (int i = 0; i < size; i++)
            {
                points = manager.getSample_Grid_MasterPoints(sample, i);
                file.writeInt(points[0]);
                file.writeInt(points[1]);
                file.writeInt(points[2]);
                file.writeInt(points[3]);
                angle = manager.getSample_Grid_Angle(sample, i);
                file.writeDouble(angle);
                dim = manager.getSample_Grid_RowsAndColumns(sample, i);
                file.writeInt(dim[0]);
                file.writeInt(dim[1]);
            }
            file.writeByte(0);
            file.writeChars("END");
            file.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!");
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    public static boolean readGrids(FileIOManager manager, String path, int sample)
    {
        try
        {
            RandomAccessFile file = new RandomAccessFile(path, "r");
            String type = "";
            type += file.readChar();
            type += file.readChar();
            type += file.readChar();
            type += file.readChar();
            //do something if type != "GRID"
            byte version = file.readByte();
            ArrayList<int[]> points = new ArrayList<>();
            ArrayList<Double> angles = new ArrayList<>();
            ArrayList<int[]> dims = new ArrayList<>();
            int size = 0;
            int endPoint = 'E' * 256 * 256 + 'N' * 256 + 'D';
            int s;
            int[] p;
            int[] d;
            while (true)
            {
                s = file.readInt();
                if (s == endPoint)
                {
                    if (file.getFilePointer() == file.length())
                    {
                        break;
                    }
                    p = new int[4];
                    d = new int[2];
                    p[0] = file.readInt();
                    p[1] = file.readInt();
                    p[2] = file.readInt();
                    p[3] = file.readInt();
                    points.add(p);
                    angles.add(file.readDouble());
                    d[0] = file.readInt();
                    d[1] = file.readInt();
                    dims.add(d);
                    size++;
                }
            }
            for (int i = 0; i < size; i++)
            {
                p = points.get(i);
                d = dims.get(i);
                manager.addSample_Grid(sample, p[0], p[1], p[2], p[3], angles.get(i), d[0], d[1]);
            }
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }
}
