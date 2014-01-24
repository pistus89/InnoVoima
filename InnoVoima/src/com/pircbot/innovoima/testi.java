/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pircbot.innovoima;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.Period;

import com.pircbot.*;
import com.csvreader.CsvReader;

/**
 *
 * @author krister
 */
public class testi {
    public static void main(String[] args) {
    	
    	Lession java = new Lession("java", "ICT4TN002-4", "ICT Core", "FIN",
    			"Erkki Esimerkki", "Pasila", "Tiko", "TN4PC", "4001", saveClock(8,00), 
    			saveClock(11,45), saveDate(21,1,2014), saveDate(28,2,2014), 3);
    	Lession ruotsi = new Lession("ruotsi", "SWE4TN003-6", "Language", "FIN", "Elli Esimerkki",
    			"Pasila", "Tiko", "TN4PC", "4001", saveClock(12,00), saveClock(15,45), 
    			saveDate(20,1,2014), saveDate(28,2,2014),3);
    	ArrayList<Lession> lessions = new ArrayList<Lession>();
    	lessions.add(java);
    	lessions.add(ruotsi);
    	
    	HashMap<String,ArrayList<String>> users = new HashMap<String,ArrayList<String>>();
    	
//        	try { 
//			CsvReader schedule = new CsvReader("example.csv");
//			schedule.readHeaders();
//			
//			while(schedule.readRecord()) {
//				
//			}
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    	
    	
    	
    
    
    } 	
    
    public static Calendar saveClock(int hours, int minutes) {
    	Calendar start = new GregorianCalendar();
    	start.set(Calendar.HOUR_OF_DAY, hours);
    	start.set(Calendar.MINUTE, minutes);
    	
    	return start;
    }
    
    public static Calendar saveDate(int day, int month, int year) {
    	Calendar start = new GregorianCalendar();
    	start.set(year, month, day);
    	return start;
    }
} 


