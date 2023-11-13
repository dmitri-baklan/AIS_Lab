package org.example;


import org.example.domain.ITFBarcodePrinter;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ITFBarcodePrinter barcodePrinter = new ITFBarcodePrinter();
        barcodePrinter.printBarcode(Arrays.asList(9,8,7,6,5,4,3,2,1,0));
    }
}
