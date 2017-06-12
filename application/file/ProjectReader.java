package application.file;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Lukas Pihl.
 */
public class ProjectReader
{
    public static ArrayList<Object> readProject(String filePath) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        ArrayList<Object> mainList = new ArrayList<>();

        ArrayList<Object> projectList = new ArrayList<>();
        projectList.add(reader.readLine());

        mainList.add(projectList);

        return mainList;
    }
}
