package newapp.file;

import magictool.image.GridManager;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by Lukas Pihl
 */
public class ExpressionWriter
{
    private int sample_number;
    private String file_path;
    private BufferedWriter writer;

    public ExpressionWriter(int sampleNum, String filePath)
    {
        this.sample_number = sampleNum;
        this.file_path = filePath;
        writer = null;
    }

    public void writeLine(String geneName, double ratio)
    {
        try
        {
            writer = new BufferedWriter(new FileWriter(file_path));
            writer.write(geneName + "," + ratio + "\n");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getSampleNumber()
    {
        return sample_number;
    }
}
