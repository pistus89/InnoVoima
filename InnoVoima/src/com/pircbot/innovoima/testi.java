/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pircbot.innovoima;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
	
	public enum weekDays {
		MONDAY("MON"),TUESDAY("TUE"),WEDNESDAY("WED"),THURSDAY("THU"),FRIDAY("FRI"),
		SATURDAY("SAT"),SUNDAY("SUN");
	
		private String day;
		
		private weekDays(String day) {
			this.day = day;
		}
		
		private String getDay() {
			return this.day;
		}
		
		private weekDays getDayByString(String name) {
			weekDays current = null;
			for (weekDays day : weekDays.values()) {
				if (day.getDay().equals(name)) {
					current = day;
				}
			}
			return current;
		}
		
	}
	
    private static HashMap<String, ArrayList<String>> users;
    private static HashMap<String, Lession> lessions;
	
    
	public static void main(String[] args) {
    	
    	Lession java = new Lession("java", "ICT4TN002-4", "ICT Core", "FIN",
    			"Erkki Esimerkki", "Pasila", "Tiko", "TN4PC", "4001", saveClock(8,00), 
    			saveClock(11,45), saveDate(21,1,2014), saveDate(28,2,2014),"MON", 3);
    	Lession ruotsi = new Lession("ruotsi", "SWE4TN003-6", "Language", "FIN", "Elli Esimerkki",
    			"Pasila", "Tiko", "TN4PC", "4001", saveClock(12,00), saveClock(15,45), 
    			saveDate(20,1,2014), saveDate(28,2,2014),"MON",3);
    	Lession englanti = new Lession("englanti", "ENG4TN003-7", "Language", "FIN", "Eija Esimerkki",
    			"Pasila", "Tiko", "TN4PC", "3001", saveClock(9,00), saveClock(13,45),
    			saveDate(20,1,2014), saveDate(28,2,2014),"TUE",3);
    	lessions = new HashMap<String,Lession>();
    	lessions.put(java.getCode(),java);
    	lessions.put(ruotsi.getCode(),ruotsi);
    	lessions.put(englanti.getCode(),englanti);
    	
    	users = new HashMap<String,ArrayList<String>>();
    	ArrayList<String> courses = new ArrayList<String>();
    	courses.add("ICT4TN002-4");
    	courses.add("ENG4TN003-7");
    	users.put("Pistus", courses);
    	System.out.println("next class is " + getNextLession("Pistus").getName());
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
    
    public static Lession getNextLession(String user) {
    	ArrayList<String> classes = users.get(user);
    	Lession next = null;
    	
    	for (String code : classes) {
    		Lession current = lessions.get(code);
    		if (compareDates(current.getStarts(), current.getDay())) {
    			next = current;
    			break;
    		}
    	}
    	
    	return next;
    }
    
    
    
    @SuppressWarnings("deprecation")
	public static boolean compareDates(Calendar calendar, String day) {
    	int firstHours = calendar.getTime().getHours();
    	int firstMinutes = calendar.getTime().getHours();
    	int secondHours = getCurrentDate().getTime().getHours();
    	int secondMinutes = getCurrentDate().getTime().getHours();
    	weekDays day1 = weekDays.valueOf(day);
    	//weekDays day2 = weekDays(getCurrentDate().get(Calendar.DAY_OF_WEEK));
    	
    	if (firstHours > secondHours) { //if the time of calendar is ahead, then return true
    		return true;
    	}
    	return false;
    	
    }
    
    public static Calendar getCurrentDate() {
    	GregorianCalendar calendar = new GregorianCalendar();
    	return calendar;
    }
    
    
} 


