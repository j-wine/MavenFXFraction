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
import eu.ggnet.mavenfxfraction.fractionfx.FractionCallable;

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
import java.util.logging.Level;
import javafx.concurrent.Task;
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

    final int TESTSIZE = 1000;

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
        Thread.sleep(random.nextInt(500));
        bruch = bruch.cancel();
        return bruch;
    }

}




