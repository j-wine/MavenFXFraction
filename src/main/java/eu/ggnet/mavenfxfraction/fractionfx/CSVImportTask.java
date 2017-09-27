/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fractionfx;

import eu.ggnet.mavenfxfraction.fraction.Fraction;
import eu.ggnet.mavenfxfraction.fraction.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CSVImportTask parses File for Log.class Objects and returns them as a List
 *
 * @author jacob.weinhold
 */
public class CSVImportTask extends Task<List<Log>> {

    private final static Logger LOG = LoggerFactory.getLogger(CSVImportTask.class);
    /**
     * Format of .csv file: first line and header is return of static method:
     * Log.toCSVHeader() each line after:
     * int,int,int,int,int,int,char,date,string
     *
     */
    File file;

    /**
     *
     * @param file .csv file to read from, containing Log.class Objects
     */
    public CSVImportTask(File file)
    {
        this.file = file;
    }

    @Override
    protected List<Log> call() throws IllegalArgumentException, FileNotFoundException, IOException, ParseException
    {
        List<Log> list = new LinkedList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String strLine = "";
        StringTokenizer st = null;

        String testHeader = br.readLine() + "\n";
        if (!(testHeader.equals(Log.toCSVHeader())))
        {
            LOG.info("IllegalArgumentException: File Header don't match");
            throw new IllegalArgumentException("FAIL");
        }

        while ((strLine = br.readLine()) != null)
        {

            st = new StringTokenizer(strLine, ",");
            while (st.hasMoreTokens())
            {
                if (st.countTokens() == 8)
                {
                    list.add(new Log(
                            new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                            new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                            new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                            st.nextToken().charAt(0), simpleDateFormat.parse(st.nextToken()), ""));
                }
                if (st.countTokens() == 9)
                {
                    list.add(new Log(
                            new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                            new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                            new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                            st.nextToken().charAt(0), simpleDateFormat.parse(st.nextToken()), st.nextToken()));
                }
            }

        }

        return list;
    }

    /**
     * Show alert if task fails. Specific alerts for each expected exception.
     */
    @Override
    protected void failed()
    {

        LOG.info("EXCEPTION : " + this.getException());

        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (this.getException() instanceof IllegalArgumentException)
        {
            alert.setTitle("ERROR: IMPORT OF CSV ERROR");
            alert.setContentText("FAILED TO IMPORT CSV");
        } else if (this.getException() instanceof ParseException)
        {
            alert.setTitle("ERROR: COULD NOT PARSE CSV");
            alert.setHeaderText("");
            alert.setContentText("FAILED TO PARSE CSV");
        } else if (this.getException() instanceof IOException)
        {
            alert.setTitle("INPUT/OUTPUT ERROR");
            alert.setHeaderText("");
            alert.setContentText("FAILED TO PROCEED FILE");
        } else if (this.getException() instanceof FileNotFoundException)
        {
            alert.setTitle("FILE ERROR");
            alert.setHeaderText("");
            alert.setContentText("File not found!");
        }

        alert.show();
        super.failed();
    }

}
