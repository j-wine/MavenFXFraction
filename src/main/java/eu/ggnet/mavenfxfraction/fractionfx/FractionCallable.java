/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fractionfx;

import eu.ggnet.mavenfxfraction.fraction.Fraction;
import java.util.List;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jacob.weinhold
 */
public class FractionCallable implements Callable<Fraction>{
 private final static Logger Log = LoggerFactory.getLogger(FractionCalculationTask.class);

    List<Fraction> list;
    char operator;

    public FractionCallable(List<Fraction> list, char operator)
    {
        this.list = list;
        this.operator = operator;
    }
    @Override
    public Fraction call() throws Exception
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
