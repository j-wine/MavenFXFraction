/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fractionfx;

import eu.ggnet.mavenfxfraction.fraction.Log;
import java.io.*;
import java.util.*;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 *
 * @author jacob.weinhold
 */
public final class CSVUtil {
    
    private final static Logger Log = LoggerFactory.getLogger(CSVUtil.class);
    
    CellProcessor[] processors = new CellProcessor[] {
    
    };

    private CSVUtil()
    {
    }

    public static String getCSVHeader()
    {
        return "FractionOne,FractionTwo,result,operator,stamp,id,comment\n";
    }

    public static List<Log> parseCSVtoLog(File file)
    {
        List<Log> list = new LinkedList<>();
        String strLine = "";
        StringTokenizer st = null;
        BufferedReader br = null;
        int tokenNumber = 0;
        try
        {
            br = new BufferedReader(new FileReader(file));

            String testHeader = br.readLine();
            if (!testHeader.equals(getCSVHeader()))
            {
                Log.info("parseCSVtoLog() IllegalArgumentException: CSV Header doesn't match");
                throw new IllegalArgumentException();
            }

            
            while ((strLine = br.readLine()) != null)
            {
                st = new StringTokenizer(strLine, ",");
                while (st.hasMoreTokens())
                {
//                    list.add(new Log());

                    tokenNumber++;
               
                }


            }

        } catch (Exception e)
        {
        }
        return list;
    }

}
