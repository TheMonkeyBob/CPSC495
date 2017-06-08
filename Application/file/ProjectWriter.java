package newapp.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lukas Pihl.
 */
public class ProjectWriter
{
    /**
     * Writes project state to a file.
     * @param filePath Full path of the file to write to.
     * @param state The state of the project.
     *              0: Project specific data
     *                  0: Project name
     *             1+: Sample data
     *                  0:
     * @throws IOException
     */
    public static void writeProject(String filePath, ArrayList<Object> state) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        state.get(0);
    }
}
