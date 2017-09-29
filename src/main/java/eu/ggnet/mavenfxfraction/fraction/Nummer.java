/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author jacob.weinhold
 */
public class Nummer implements Comparable<Nummer> {

    private final static Logger L = LoggerFactory.getLogger(Nummer.class);
    /**
     * implementing functionality of following methods for Nummmer.class
     * instances Equals(), hashcode(), tostring(), comparable()
     */
    private int ganzzahl;
    
    private String name;

    public void setGanzzahl(int ganzzahl)
    {
        L.info("setGanzzahl!");
        this.ganzzahl = ganzzahl;
    }

    public int getGanzzahl()
    {
        L.info("getGanzzahl!");
        return ganzzahl;
    }

    public Nummer()
    {
    }

    public Nummer(int zahl)
    {
        ganzzahl = zahl;

    }

    /**
     *
     * @param obj compare equality of Nummer objects by comparing int values of
     * Ganzzahl variable from calling Nummer with @param obj
     * @return true if Ganzzahl is same
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (this == obj)
        {
            return true;
        }

        if (!obj.getClass().equals(getClass()))
        {
            return false;
        }

        Nummer that = (Nummer) obj;

        return this.ganzzahl == that.ganzzahl;

    }

    /**
     * calculate hashCode for Nummer objecty with @Ganzzahl objectvariable
     * differentiate hash value with additional operations with constants of
     * positive ints
     *
     * @return hash value
     */
    @Override
    public int hashCode()
    {
        
        int hash = 5;
        hash = 43 * hash + this.ganzzahl + ((name == null) ? 0 : name.hashCode());
   
        return hash;
    }

    /**
     * @return String containing only the @Ganzzahl objectvariable value
     */
    @Override
    public String toString()
    {
      
        return ganzzahl + "";

    }

    /**
     * compareTo implementation comparing Nummer objects by @Ganzzahl object
     * variable
     *
     * @param zahl non null
     * @return difference of objectvariables by substracting @param zahl
     * @Ganzzahl from caller @Ganzzahl
     */
    @Override
    public int compareTo(Nummer zahl)
    {
        L.info("compareTo!");
        if (zahl.getGanzzahl() < this.getGanzzahl())
        {
            return 1;
        } else if (zahl.getGanzzahl() > this.getGanzzahl())
        {
            return -1;
        }

        return 0;
    }

}