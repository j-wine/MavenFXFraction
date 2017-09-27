/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fractionfx;

import eu.ggnet.mavenfxfraction.fraction.Fraction;
import eu.ggnet.mavenfxfraction.fraction.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javafx.concurrent.Task;

/**
 *
 * @author jacob.weinhold
 */
public class CSVImportTask extends Task {

    File file;

    public CSVImportTask(File file)
    {
        this.file = file;
    }

    @Override
    protected Object call() throws Exception
    {
        List<Log> list = new LinkedList<>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String strLine = "";
            StringTokenizer st = null;
            File cfile = new File("csv.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(cfile));
            int tokenNumber = 0;

            br.readLine();

            while ((strLine = br.readLine()) != null)
            {
                st = new StringTokenizer(strLine, ",");
                while (st.hasMoreTokens())
                {

                    tokenNumber++;
                    list.add(new Log(
                            new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                            new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                            new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                            st.nextToken().charAt(0), st.nextToken()));

                    writer.write(tokenNumber + "  " + st.nextToken());
                    writer.newLine();
                }

                tokenNumber = 0;
                writer.flush();
            }
        } catch (Exception e)
        {
            e.getMessage();
        }

        return null;

    }

}
