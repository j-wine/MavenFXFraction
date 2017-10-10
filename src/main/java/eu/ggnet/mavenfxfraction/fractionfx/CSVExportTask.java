/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fractionfx;

import eu.ggnet.mavenfxfraction.fraction.Log;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CSVExportTask writes Log Objects of logList in .csv format (see below for format details)
 * @author jacob.weinhold
 */
public class CSVExportTask extends Task {
    private final static Logger Log = LoggerFactory.getLogger(CSVExportTask.class);

    /**
     * Format of .csv file:
     * first line and header is return of static method: Log.toCSVHeader()
     * each line after: int,int,int,int,int,int,char,date,string
     * 
     */
    List<Log> logList;
    Writer writer;
    
    /**
     * 
     * @param logList List containing Log Objects
     * @param writer which was instantiated elsewhere with the file to write in
     */

    public CSVExportTask(List<Log> logList, Writer writer)
    {
        this.logList = logList;
        this.writer = writer;
    }

    @Override
    protected Object call() throws Exception
    {

        
        try
        {

            writer.write(logList.get(0).toCSVHeader());
            for (Log log : logList)
            {
                
                writer.write(log.toCSVString());
                writer.write("\n");
            }

        } catch (IOException ex)
        {
            Log.info("IOException");
            ex.printStackTrace();
        } finally
        {
            
            writer.flush();
            writer.close();
        }
        return null;

    }

}

