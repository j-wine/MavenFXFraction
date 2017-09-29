/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fractionfx;

import eu.ggnet.mavenfxfraction.fraction.Fraction;

import java.util.List;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jacob.weinhold
 *
 */
public class FractionCalculationTask extends Task<Fraction> {


    

    private final static Logger Log = LoggerFactory.getLogger(FractionCalculationTask.class);

    List<Fraction> list;
    char operator;

    /**
     * @param list holds two Fraction instances
     * @param operator one of: '+', '-', '/', '*'
     * @throws IllegalArgumentException if List is null, size!=2 or operator is
     * no arithmetic operator (except modulo)
     *
     */
    public FractionCalculationTask(List<Fraction> list, char operator) throws IllegalArgumentException
    {
        if (list == null)
        {
            Log.info("FractionTask(() IllegalArgumentException: list == null");
            throw new IllegalArgumentException();
        }

        if (list.size() != 2)
        {
            Log.info("FractionTask() IllegalArgumentException: list.size()!=2");
            throw new IllegalArgumentException();
        }

        if (!(operator == '+' || operator == '-' || operator == '/' || operator == '*'))
        {
            Log.info("FraktionTask() IllegalArgumentExceptionm  operator: " + operator);
            throw new IllegalArgumentException();
        }
        this.list = list;
        this.operator = operator;

    }

    /**
     * invokes methods add(), subtract(), multiply() and divide() from @Fraction.class for desired arithemtic operation
     *
     * @return result or null if failed
     * @throws java.lang.InterruptedException if sleep is interrupted
     *
     */
    @Override
    protected Fraction call() throws InterruptedException
    {

        Log.info("FractionTask.call()");

        Fraction bruch1 = list.get(0);
        Fraction bruch2 = list.get(1);
        Fraction result;
        Thread.sleep(1000);
        switch (operator)
        {

            case '+':

                result = bruch1.add(bruch2);

                return result;

            case '-':
                result = bruch1.substract(bruch2);

                return result;

            case '*':
                result = bruch1.multiply(bruch2);

                return result;

            case '/':
                result = bruch1.divide(bruch2);

                return result;

        }

        return null;
    }

}
