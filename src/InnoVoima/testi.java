/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InnoVoima;

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
        Scanner inputFile3 = null;
        try {
            inputFile3 = new Scanner(new FileInputStream("shout.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(testi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while (inputFile3.hasNextLine()) {
            String line = inputFile3.nextLine();
            int border = line.indexOf(' ');
            String key = line.substring(0, border).trim();
            String value = line.substring(border + 1, line.length());
            System.out.println(key + " " + value);
        }
        
    }
public double calculatePermill(String nick, Double grams) {
        
        
        
        double weight = 75;
        double permill;
        
        
            permill = grams/ (weight*0.75); // a male gains 75% percent water of his weight
        
        
        return permill;
        
    }
    
    public double addAlcohol(String nick, int grams, GregorianCalendar last, Double alcohol, int paino) { //first removes alcoholgrams that has been sobered up, then adds new grams onto list
        
        GregorianCalendar calendar = new GregorianCalendar();
        long timeNow = calendar.getTimeInMillis();
        long timeLast = last.getTimeInMillis();
        int hours = (int) ((((timeNow - timeLast)/1000)/60)/60);
        double currentGrams;
        
        
            currentGrams = alcohol;
        
        
        double completeGrams = (currentGrams - (hours* (paino/10)));
        
        if (completeGrams < 0) { //set the former gram sum to 0 if sobered up, so it won't affect the result
            completeGrams = 0.0;
        }
        return completeGrams + grams;
        
        
    }
   
    public static void calculateBurning(String nick, GregorianCalendar last, HashMap<String, Double> alcoholGrams) {
        GregorianCalendar calendar = new GregorianCalendar();
        long timeNow = calendar.getTimeInMillis();
        long timeLast = last.getTimeInMillis();
        Period p = new Period(timeNow, timeLast);
        long hours = p.getHours();
        double currentGrams;
        
        if (!alcoholGrams.containsKey(nick)) {
            currentGrams = 0;
        } else {
            currentGrams = alcoholGrams.get(nick);
        }
        
        double completeGrams = (currentGrams - (hours* (75/10)));
        
        if (completeGrams < 0) { //set the former gram sum to 0 if sobered up, so it won't affect the result
            completeGrams = 0.0;
        }
        
        alcoholGrams.put(nick, completeGrams);
        
    }
} 


