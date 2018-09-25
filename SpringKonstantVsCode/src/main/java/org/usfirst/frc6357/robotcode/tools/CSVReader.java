package org.usfirst.frc6357.robotcode.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class exists to read the data stored in a CSV file and then parse it
 * This data will be used to make auto-commands and allow easy alteration in
 * excel or sheets The methods will be primarily static and will parse files
 * using scanner
 */
public class CSVReader
{
    /**
     * This method parses through the selected CSV file and reads in the data
     * 
     * @param fileNamen
     *            a string representing the file path for the selected csv
     * @return a 2D array of Strings containing all of the data in the CSV file with
     *         the following format: line[0] is irrelevant (user reference),
     *         line[foo][0] is the command type, and line[foo][1 : line[foo].length
     *         - 1] are params
     * @exception IOException
     *                required for input/output
     */
    public static String[][] parse(String fileName) throws IOException
    {
        Scanner sc = null;
        try
        {
            sc = new Scanner(new File(fileName));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Auto-plan file not found: " + e);
        }
        ArrayList<String[]> l = new ArrayList<>();
        while (sc.hasNextLine())
            l.add(sc.nextLine().split(","));

        String[][] output = new String[l.size()][];
        for (int i = 0; i < l.size(); i++)
            output[i] = l.get(i);

        sc.close();
        System.out.println(Arrays.deepToString(output));
        return output;
    }
}
