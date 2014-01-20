/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pircbot.innovoima;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.Period;

/**
 *
 * @author krister
 */
public class testi {
    public static void main(String[] args) {
    	Scanner lukija = new Scanner(System.in);
    	String url = lukija.nextLine();
    	
    	if (url.contains("www")) {
    		System.out.println("true");
    	} else {
    		System.out.println("false");
    	}
    } 	
} 


