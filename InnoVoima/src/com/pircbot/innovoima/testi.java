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

import org.joda.time.DateTime;
import org.joda.time.Period;

import com.pircbot.*;
import com.csvreader.CsvReader;

/**
 *
 * @author krister
 */
public class testi {
	
	public enum weekDays {
		MONDAY("MON", 2),TUESDAY("TUE", 3),WEDNESDAY("WED", 4),THURSDAY("THU",5),FRIDAY("FRI",6),
		SATURDAY("SAT",7),SUNDAY("SUN",1); //numbering is made compatible with Calendar class
	
		private String day;
		private int order;
		
		private weekDays(String day, int order) {
			this.day = day;
			this.order = order;
		}
		
		private String getDay() {
			return this.day;
		}
		
		private int getOrder() {
			return this.order;
		}
		
		private static weekDays getDayByString(String name) {
			weekDays current = null;
			for (weekDays day : weekDays.values()) {
				if (day.getDay().equals(name)) {
					current = day;
				}
			}
			return current;
		}
		
		private static weekDays getDayByInt(int number) {
			weekDays current = null;
			for (weekDays day : weekDays.values()) {
				if (day.getOrder() == number) {
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
    	boolean courseAdd = addCourse("Pistus", "SWE4TN003-6");
    	
    	if (courseAdd) {
    		System.out.println("Course added successfully");
    	}
    	
    	Lession next = getNextLession("Pistus");
    	int startM = next.getStarts().getMinuteOfHour();
    	int endM = next.getEnds().getMinuteOfHour(); //for sfome unknown reason
    	//it converts the hours in minutes as well. this if-sentence will takes
    	//care that minutes will be displayed in correct format
    	if (startM > 60 && endM > 60) {
    		startM = startM - (next.getStarts().getHourOfDay() * 60);
    		endM = endM - (next.getEnds().getHourOfDay() * 60);
    	}
    	System.out.println("next class is " + next.getName() + ", room: " 
    			+ next.getRooms() + ", length: " + next.getStarts().getHourOfDay()
    			+ ":" + startM + " - " + next.getEnds().getHourOfDay()
    			+ ":" + endM);
    	
    	System.out.println(findCourse("ENG4TN003-7"));
    	System.out.println(findCourse("eng4tn003-7"));
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
	public static String findCourse(String course) {
		Lession lession = lessions.get(course);
		if (lession == null) {
			return "couldn't find the course " + course;
		} else {
			return lession.toString();
		}
	}
	public static boolean addCourse(String user, String course) {
		ArrayList<String> courses;
		if (users.containsKey(user)) {
			courses = users.get(user);
		} else {
			courses = new ArrayList<String>();
		}
    	if(lessions.containsKey(course)) {
    		courses.add(course);
    		users.put(user, courses);
    		return true;
    	}
    	return false;
	}
	
    public static DateTime saveClock(int hours, int minutes) {
    	DateTime start = new DateTime();
    	start = start.withHourOfDay(hours);
    	start = start.withMinuteOfHour(minutes);
    	return start;
    }
    
    public static DateTime saveDate(int day, int month, int year) {
    	DateTime start = new DateTime();
    	start = start.withDate(year, month, day);
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
	public static boolean compareDates(DateTime time, String day) {
    	int firstHours = time.getHourOfDay();
    	int firstMinutes = time.getMinuteOfHour();
    	int secondHours = getCurrentDate().getTime().getHours();
    	int secondMinutes = getCurrentDate().getTime().getHours();
    	weekDays day1 = weekDays.getDayByString(day);
    	weekDays day2 = weekDays.getDayByInt(getCurrentDate().get(Calendar.DAY_OF_WEEK));
    	
    	if (day1 == day2) { //first check if theres lessions coming today
    		if (firstHours > secondHours ||
    				(firstHours == secondHours &&
    				firstMinutes > secondMinutes)) { //if the time of calendar is ahead, then return true
        		return true;
        	}
    	}
    	
    	if (day1.getOrder() > day2.getOrder()) { //next days first lession will be next
    		return true;
    	}
    	return false;
    	
    }
    
    public static Calendar getCurrentDate() {
    	GregorianCalendar calendar = new GregorianCalendar();
    	calendar.set(2014, 2, 2, 16, 30);
    	return calendar;
    }
    
    
} 


