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
 *
 * @author jacob.weinhold
 */
public class CSVExportTask extends Task {
    private final static Logger Log = LoggerFactory.getLogger(CSVExportTask.class);

    List<Log> logList;
    Writer writer;

    public CSVExportTask(List<Log> logList, Writer writer)
    {
        this.logList = logList;
        this.writer = writer;
    }

    @Override
    protected Object call() throws Exception
    {

        Thread.sleep(1000);
        try
        {

            writer.write(logList.get(0).toCSVHEader());
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

