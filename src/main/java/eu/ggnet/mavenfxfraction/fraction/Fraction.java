/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fraction;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jacob.weinhold
 * @Fraction.class implements abstract fraction datatype with basic arithmetic
 * functionality
 */
public class Fraction implements Comparable<Fraction> {

    private final static Logger LOG = LoggerFactory.getLogger(Fraction.class);

    @Getter
    private int zaehler;

    @Getter
    private int nenner;

    public Fraction(int numerator, int denominator)
    {
        //catch "division" by zero
        if (denominator < 0)
           denominator = 1;
        zaehler = numerator;
        nenner = denominator;

    }

    @Override
    public String toString()
    {
        return zaehler + "/" + nenner;
    }

    @Override
    public int compareTo(Fraction fraction)
    {
        if ((this.zaehler / (double) this.nenner) < (fraction.zaehler / (double) fraction.nenner))
        {
            return -1;
        } else
        {
            if ((this.zaehler / (double) this.nenner) > (fraction.zaehler / (double) fraction.nenner))
            {
                return 1;
            } else
            {
                return 0;
            }
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!obj.getClass().equals(getClass()))
        {
            return false;
        }

        Fraction that = (Fraction) obj;

        return this.nenner == that.nenner && this.zaehler == that.zaehler;

    }

    //  eigene Implementierung des euklidschen Algorithmus
    public Fraction cancel() throws ArithmeticException
    {

        int tempZaehler = zaehler;
        int tempNenner
                = nenner;
        try
        {
            while (tempZaehler > 0 && tempNenner > 0)
            {
                if (tempZaehler < tempNenner)
                {
                    tempNenner %= tempZaehler;
                } else if (tempZaehler > tempNenner)

                {
                    tempZaehler %= tempNenner;
                }
            }

            if (tempZaehler == 0)
            {
                zaehler /= tempNenner;
                nenner /= tempNenner;
            } else
            {
                zaehler /= tempZaehler;

                nenner /= tempZaehler;
            }

            return new Fraction(zaehler, nenner);
        } catch (ArithmeticException e)
        {
            LOG.info(e.toString());
            return null;

        }

    }

    public Fraction add(Fraction a)
    {
        int x = zaehler * a.nenner
                + nenner * a.zaehler;
        int y = nenner * a.nenner;

        Fraction b = new Fraction(x, y);

        return b;

    }

    public Fraction substract(Fraction a)
    {
        int x = zaehler * a.nenner
                - nenner * a.zaehler;
        int y = nenner * a.nenner;

        Fraction b = new Fraction(x, y);
        return b;
    }

    public Fraction multiply(Fraction other)
    {

        int zaehler = this.zaehler * other.zaehler;
        int nenner = this.nenner * other.nenner;
        return new Fraction(zaehler, nenner);
    }

    public Fraction divide(Fraction bruch)
    {
        int resultZaehler = this.zaehler * bruch.nenner;
        int resultNenner = this.nenner * bruch.zaehler;
        return new Fraction(resultZaehler, resultNenner);

    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 29 * hash + this.zaehler;
        hash = 29 * hash + this.nenner;
        return hash;
    }
    


}
