package application.tools;

/**
 * Created by TheMonkeyBob on 2017-07-17.
 */
public class StringTool
{
    private StringTool()
    {
        //NO!
    }

    public static String parse_FilePath_LeadingPath(String s)
    {
        if (s == null)
        {
            return null;
        }
        if (s.equals(""))
        {
            return "";
        }

        char[] c = s.toCharArray();
        int i = c.length - 1;
        if (c[i] == '/' || c[i] == '\\')
        {
            i--;
        }
        while (i >= 0 && c[i] != '/' && c[i] != '\\')
        {
            i--;
        }
        i--;
        String str = "";
        while (i >= 0)
        {
            str = c[i] + str;
            i--;
        }

        return str;
    }

    public static String parse_FilePath_File(String s)
    {
        if (s == null)
        {
            return null;
        }
        if (s.equals(""))
        {
            return "";
        }

        char[] c = s.toCharArray();
        int i = c.length - 1;
        String str = "";
        while (i >= 0 && c[i] != '/' && c[i] != '\\')
        {
            str = c[i] + str;
            i--;
        }

        return str;
    }

    public static String parse_FilePath_File_NameOnly(String s)
    {
        String str = parse_FilePath_File(s);
        if (str == null)
        {
            return null;
        }
        if (str.equals(""))
        {
            return "";
        }

        char[] c = str.toCharArray();
        str = "";
        int i = 0;

        while (i < c.length && c[i] != '.')
        {
            str = str + c[i];
            i++;
        }

        return str;
    }
}
