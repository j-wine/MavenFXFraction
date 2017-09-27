/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fraction;

import java.util.regex.Pattern;

/**
 *
 * @author jacob.weinhold
 */
public class TestClass {

    static String normalize(String partNo)
    {
        if (partNo != null && Pattern.matches("[0-9]{6}", partNo))
        { // Rebuild
            return partNo.substring(0, 3) + "." + partNo.substring(3);
        }

        if (partNo != null && Pattern.matches("[0-9]{8}", partNo))
        { // Rebuild
            return partNo.substring(0, 2) + "." + partNo.substring(3, 6) + "." + partNo.substring(6);
        }
        return partNo;
    }
    

    public static void main(String[] args)
    {
        System.out.println(normalize("12213123"));
        System.out.println(normalize("123123"));

    }
}
