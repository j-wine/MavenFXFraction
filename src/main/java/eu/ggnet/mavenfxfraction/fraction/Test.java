package eu.ggnet.mavenfxfraction.fraction;

import eu.ggnet.mavenfxfraction.fractionfx.FractionFXMLController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jacob.weinhold
 */
public class Test {

    private final static Logger LOG = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args)
    {
        List<Log> list = new LinkedList<>();
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            File file = new File("C:\\Users\\jacob.weinhold\\Desktop\\1.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String strLine = "";
            StringTokenizer st = null;

            br.readLine();

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
                    } if (st.countTokens() == 9)
                    {
                        list.add(new Log(
                                new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                                new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                                new Fraction(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())),
                                st.nextToken().charAt(0), simpleDateFormat.parse(st.nextToken()), st.nextToken()));
                    }
                }
                
            }
            list.forEach(e -> System.out.println(e.toString()));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

//        List<Log> list = new LinkedList<>();
//        String csvFile = "C:\\Users\\jacob.weinhold\\Desktop\\123.csv";
//        BufferedReader br = null;
//        String line = "";
//        String cvsSplitBy = ",";
//
//        try
//        {
//
//            br = new BufferedReader(new FileReader(csvFile));
//            System.out.println(br.readLine());
//            while ((line = br.readLine()) != null)
//            {
//                
//               
//                String[] country = line.split(cvsSplitBy);
//                
//
//                
//
//            }
//
//        } catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        } finally
//        {
//            if (br != null)
//            {
//                try
//                {
//                    br.close();
//                } catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}
