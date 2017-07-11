package application.file;

import application.engine.Engine;
import application.engine.FileIOManager;
import ij.ImagePlus;
import ij.io.Opener;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by Lukas Pihl
 */
public class SampleIO
{
    public static boolean newSample(String path, String name, String greenPath, String redPath, String genePath, FileIOManager manager)
    {
        try
        {
            File dir = new File(path);
            if (!dir.exists())
            {
                dir.mkdir();
            }
            String fullPath = path + "\\" + name + ".smpl";
            File file = new File(fullPath);
            file.createNewFile();

            String greenName = parseFileName(greenPath);
            String redName = parseFileName(redPath);
            String geneName = parseFileName(genePath);

            copyFile(greenPath, path + "\\" + greenName);
            copyFile(redPath, path + "\\" + redName);
            copyFile(genePath, path + "\\" + geneName);

            RandomAccessFile writer = new RandomAccessFile(path, "rw");
            writer.writeChars("SMPL");
            writer.writeByte(1); //Version
            writer.writeByte(0);
            writer.writeChars(name);
            writer.writeByte(0);
            writer.writeChars(greenName); //write green image file name
            writer.writeByte(0);
            writer.writeChars(redName); //write red image file name
            writer.writeByte(0);
            writer.writeChars(geneName); //write gene data file name
            writer.writeByte(0);
            writer.writeChars("END");
            writer.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean writeSample(String path, String name, int sample, FileIOManager manager)
    {
        //nothing until program states are implemented
        manager.writeGrids(path, sample);
        return true;
    }

    public static boolean readSample(String path, String name, FileIOManager manager)
    {
        try
        {
            RandomAccessFile file = new RandomAccessFile(path + "\\" + name + ".smpl", "r");
            String type = "";
            type += file.readChar();
            type += file.readChar();
            type += file.readChar();
            type += file.readChar();
            //do something if type != "SMPL"
            byte version = file.readByte();
            String nam = readString(file);
            String greenName = readString(file);
            String redName = readString(file);
            String geneName = readString(file);

            Opener greenImage = new Opener();
            Opener redImage = new Opener();
            ImagePlus green_IP = greenImage.openImage(path + "\\" + greenName);
            ImagePlus red_IP = redImage.openImage(path + "\\" + redName);
            //load gene data

            manager.addSample(path, name, green_IP, red_IP);

            //int endPoint = 'E' * 256 * 256 + 'N' * 256 + 'D';

            manager.readGrids(path + "\\" + "Grids.grid", manager.getSampleCount() - 1);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static String readString(RandomAccessFile file) throws IOException
    {
        char c = 1;
        String s = "";
        while (c != 0)
        {
            c = file.readChar();
            if (c != 0)
            {
                s += c;
            }
        }
        return s;
    }

    private static String parseFileName(String str)
    {
        char[] chrs = str.toCharArray();
        char c;
        int i = chrs.length-1;
        String s = "";
        c = chrs[i];
        while (c != '\\')
        {
            i--;
            s = c + s;
            c = chrs[i];
        }
        return s;
    }

    private static void copyFile(String sourcePath, String destPath) throws IOException
    {
        FileChannel source = new FileInputStream(sourcePath).getChannel();
        FileChannel destination = new FileOutputStream(destPath).getChannel();

        long count = 0;
        long size = source.size();

        while (count < size)
        {
            count += destination.transferFrom(source, count, size - count);
        }

        source.close();
        destination.close();
    }
}
