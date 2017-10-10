package fractionTest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.common.base.Stopwatch;
import static org.assertj.core.api.Assertions.*;
import eu.ggnet.mavenfxfraction.fraction.Fraction;
import eu.ggnet.mavenfxfraction.fractionfx.FractionCalculationTask;
import java.util.Arrays;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.embed.swing.JFXPanel;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JW
 */
public class FractionTest {

    private final static Logger LOG = LoggerFactory.getLogger(FractionTest.class);

    final int TESTSIZE = 10;

    @Test
    public void testConcurrence() throws InterruptedException
    {
        List list = null;

        Stopwatch timer = Stopwatch.createStarted();
        List<Future<Fraction>> futureList;
        List<Callable<Fraction>> callableList = new LinkedList<>();

        Fraction großerBruch = new Fraction(214748346, 14748344);
        Fraction gekürzterGroßerBruch = großerBruch.cancel();

        for (int i = 0; i < TESTSIZE; i++)
        {
            callableList.add(new CancelCallable(großerBruch));

        }
        ExecutorService executor = Executors.newFixedThreadPool(TESTSIZE);

        futureList = executor.invokeAll(callableList);

        futureList.forEach(e ->
        {
            try
            {
                assertTrue(e.get().equals(gekürzterGroßerBruch));
            } catch (InterruptedException ex)
            {
                LOG.info(ex.toString());
            } catch (ExecutionException ex)
            {
                LOG.info(ex.toString());
            }
        });
        LOG.info("Method took: " + timer.stop());
    }

    @Test
    public void testComparable()
    {

        Random random = new Random();

        SortedSet<Fraction> set = new TreeSet<Fraction>();
        for (int i = 0; i < TESTSIZE; i++)
        {
            set.add(new Fraction(random.nextInt() * 317, random.nextInt() * 73));
        }

        Iterator it = set.iterator();
        Fraction smallerFraction;
        Fraction biggerFraction;

        smallerFraction = (Fraction) it.next();
        while (it.hasNext())
        {

            biggerFraction = (Fraction) it.next();

            assertThat(smallerFraction.compareTo(biggerFraction)).as("is smaller than next Object in set").isEqualTo(-1);

            smallerFraction = biggerFraction;

        }

    }

    @Test
    public void testFractionCalculationTask() throws InterruptedException, ExecutionException
    {

        // initializes JavaFX environment because Task
        new JFXPanel();

        Fraction bruchEins = new Fraction(((int) Math.random()) * 317, ((int) Math.random()) * 713);
        Fraction bruchZwei = new Fraction(((int) Math.random()) * 971, ((int) Math.random()) * 929);
        List<Fraction> fractionList = new LinkedList();
        fractionList.addAll(Arrays.asList(bruchEins, bruchZwei));

        ExecutorService pool = Executors.newWorkStealingPool();

        Fraction sum = bruchEins.add(bruchZwei);
        Fraction difference = bruchEins.substract(bruchZwei);;
        Fraction quotient = bruchEins.multiply(bruchZwei);;
        Fraction product = bruchEins.divide(bruchZwei);;

        FractionCalculationTask addTask = new FractionCalculationTask(fractionList, '+');
        FractionCalculationTask subTask = new FractionCalculationTask(fractionList, '-');
        FractionCalculationTask multiplyTask = new FractionCalculationTask(fractionList, '*');
        FractionCalculationTask divideTask = new FractionCalculationTask(fractionList, '/');

        pool.execute(addTask);
        pool.execute(subTask);
        pool.execute(multiplyTask);
        pool.execute(divideTask);

        assertThat(addTask.get()).isEqualTo(sum);
        assertThat(subTask.get()).isEqualTo(difference);
        assertThat(multiplyTask.get()).isEqualTo(product);
        assertThat(divideTask.get()).isEqualTo(quotient);

    }


}

class CancelCallable implements Callable {

    private Fraction bruch;

    public CancelCallable(Fraction bruch)
    {
        this.bruch = bruch;
    }

    @Override
    public Object call() throws Exception
    {
        Random random = new Random();
        
        bruch = bruch.cancel();
        return bruch;
    }

}
