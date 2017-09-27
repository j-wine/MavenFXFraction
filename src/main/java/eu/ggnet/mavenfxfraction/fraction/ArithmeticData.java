/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.mavenfxfraction.fraction;

import lombok.Value;

/**
 *
 * @author jacob.weinhold
 */
@Value
public class ArithmeticData {

 private   Fraction fractionOne;
 private   Fraction fractionTwo;
 private   Fraction result;
 private   char operator;

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
    
    
      public String toString()
    {
        return "Fraction one : " + fractionOne + "Fraction Two: " + fractionTwo + "operator: " + operator + "= result: " + result;
    }
}
