package application.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Lukas Pihl
 */
public class ExpressionWriter
{
    private BufferedWriter writer;

    public ExpressionWriter(String filePath) throws IOException
    {
        writer = new BufferedWriter(new FileWriter(filePath));
    }

    public void writeLine(String line) throws  IOException
    {
        writer.write(line);
    }
}
