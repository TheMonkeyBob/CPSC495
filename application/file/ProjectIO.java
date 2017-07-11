package application.file;

import application.engine.FileIOManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by Lukas Pihl
 */
public class ProjectIO
{
    public static boolean newProject(String path)
    {
        try
        {
            File file = new File(path);
            if (!file.exists())
            {
                file.mkdir();
                String fullPath = path + "\\" + parseFileName(path) + ".proj";
                file = new File(fullPath);
                file.createNewFile();
                RandomAccessFile writer = new RandomAccessFile(fullPath, "rw");
                writer.writeChars("PROJ");
                writer.writeByte(1); //Version
                writer.writeByte(0);
                writer.writeChars("END");
                writer.close();
                file = new File(path + "\\" + "Grids");
                file.mkdir();
            }
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    public static boolean writeProject(String path, FileIOManager manager)
    {
        try
        {
            String tempPath = path + "temp";//+ "\\" + parseFileName(path) + "_temp";
            File tempFile = new File(tempPath);
            RandomAccessFile writer = new RandomAccessFile(tempPath, "rw");
            writer.writeChars("PROJ");
            writer.writeByte(1); //Version
            ArrayList<String> pathList = manager.getSamplePaths();
            ArrayList<String> nameList = manager.getSampleNames();

            for (int i = 0; i < pathList.size(); i++)
            {
                writer.writeByte(0);
                writer.writeChars(pathList.get(i));
                writer.writeByte(0);
                writer.writeChars(nameList.get(i));
            }

            writer.writeByte(0);
            writer.writeChars("END");
            writer.close();

            File file = new File(path);
            file.delete();
            tempFile.renameTo(file);

            manager.writeAllSamples();
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

    public static boolean readProject(String path, FileIOManager manager)
    {
        try
        {
            RandomAccessFile file = new RandomAccessFile(path, "r");
            String type = "";
            type += file.readChar();
            type += file.readChar();
            type += file.readChar();
            type += file.readChar();
            //do something if type != "PROJ"
            byte version = file.readByte();
            ArrayList<String> paths = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            file.readChar(); //pass buffer
            char c;
            String p = "";
            String n = "";
            boolean isPath = true;
            while (true)
            {
                c = file.readChar();
                if (c == 0)
                {
                    if (isPath)
                    {
                        paths.add(p);
                        p = "";
                    }
                    else
                    {
                        names.add(n);
                        n = "";
                    }
                }
                else
                {
                    if (isPath)
                    {
                        p += c;
                        if (p.equals("END") && file.getFilePointer() == file.length()-1)
                        {
                            break;
                        }
                    }
                    else
                    {
                        n += c;
                    }
                }
            }
            manager.readAllSamples(paths, names);
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
}
