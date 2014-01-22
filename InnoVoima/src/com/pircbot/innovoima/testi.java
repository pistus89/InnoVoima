/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pircbot.innovoima;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.Period;

import com.csvreader.CsvReader;

/**
 *
 * @author krister
 */
public class testi {
    public static void main(String[] args) {
    	try {
			CsvReader schedule = new CsvReader("example.csv");
			schedule.readHeaders();
			
			while(schedule.readRecord()) {
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 	
} 


