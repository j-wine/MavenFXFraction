/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fraction;

import eu.ggnet.mavenfxfraction.fractionfx.FractionFXMLController;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log.class Objects contain all information about one arithmetic operation on two Fraction.class objects, the time they
 * were created and possibly a comment.
 * Used in listView in @FractionFXMLController.class
 * @author jacob.weinhold
 */
public final class Log {

    private final static Logger LOG = LoggerFactory.getLogger(Log.class);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Log(Fraction fractionOne, Fraction fractionTwo, Fraction result, char operator, String comment)
    {

        this.fractionOne = fractionOne;
        this.fractionTwo = fractionTwo;
        this.result = result;
        this.operator = operator;

        this.comment = comment;
        stamp = new Date();

    }

    public Log(Fraction fractionOne, Fraction fractionTwo, Fraction result, char operator, Date stamp, String comment)
    {
        this.fractionOne = fractionOne;
        this.fractionTwo = fractionTwo;
        this.result = result;
        this.operator = operator;
        this.stamp = stamp;
        this.comment = comment;
    }

    private final Fraction fractionOne;
    private final Fraction fractionTwo;
    private final Fraction result;
    private final char operator;
    private final Date stamp;

    private String comment;

    public static String toCSVHeader()
    {
        return "FractionOneNominator,FractionOneDenominator,FractionTwoNominator, FractionTwoDenominator,resultNominator,resultDenominator,operator,stamp,comment\n";
    }

    public String toCSVString()
    {

        return fractionOne.getZaehler() + "," + fractionOne.getNenner() + "," + fractionTwo.getZaehler() + "," + fractionTwo.getNenner()
                + "," + result.getZaehler() + "," + result.getNenner() + "," + operator + "," + simpleDateFormat.format(stamp) + "," + comment;

    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public static Logger getLOG()
    {
        return LOG;
    }

    public Fraction getFractionOne()
    {
        return fractionOne;
    }

    public Fraction getFractionTwo()
    {
        return fractionTwo;
    }

    public Fraction getResult()
    {
        return result;
    }

    public char getOperator()
    {
        return operator;
    }

    public Date getStamp()
    {
        return stamp;
    }

    @Override
    public String toString()
    {
        return "Log{" + "fractionOne=" + fractionOne + ", fractionTwo=" + fractionTwo + ", result=" + result + ", operator=" + operator + ", stamp=" + stamp + ", comment=" + comment + '}';
    }

    public String getComment()
    {
        return comment;
    }

    public String toFixOutput()
    {
        return "\n" + simpleDateFormat.format(stamp) + "\n" + toArithmeticString();
    }

    public String toCommentString()
    {

        return comment;
    }

    public String toArithmeticString()
    {
        return "" + fractionOne + operator + fractionTwo + "\n= result:\n" + result;
    }

}
