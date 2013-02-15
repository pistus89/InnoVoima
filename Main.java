
import java.net.URISyntaxException;
import java.util.Scanner;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jibble.pircbot.IrcException;



public class Main {

    public static class bots {
        private MyBot[] botti;
        private int last;
        
        public bots() {
            this.botti = new MyBot[100];
            this.last = 0;
        }
        
        public void addList(MyBot bot) {
            this.botti[this.last] = bot;
            this.last++;
        }
        
        public void removeList(int i) {
            this.botti[i] = null;
        }
        
        public int getLast() {
            return this.last;
        }
        
        public MyBot getBot(int i) {
            return this.botti[i];
        }
    }
    
    public static class rajapinta {
    
        public static bots list = new bots();
        
        public static void create(String server, String channel) {
            try {
                Scanner inputFile1 = new Scanner(new File("")); //add here the exact location of loki.txt
                Scanner inputFile2 = new Scanner(new File("")); //add here the exact location of shouts.txt
                
                MyBot bot = new MyBot();
                
                while (inputFile2.hasNextLine()) {
                    bot.addJMegaHal(inputFile2.nextLine());
                }
                
                while (inputFile1.hasNextLine()) {
                    bot.addJMegaHal(inputFile1.nextLine());
                }
                
                bot.makeKielet();
                bot.makeInsultList();
                bot.setMaster(""); //set here the irc-nick who will own the most priviledges of ircbot (
means your irc-nick :) )
                try {
                    bot.connect(server);
                    bot.joinChannel(channel);
                    bot.setEncoding("UTF-8");
                    bot.saveServer();
                    list.addList(bot);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IrcException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                
                }
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
   
    
    public static void main(String[] args) throws Exception {
        MyBot bot = new MyBot();
       
        //fullfill the locations of each file, for example /home/user/sitaatit.txt
        Scanner inputFile1 = new Scanner(new File("sitaatit.txt")); 
        Scanner inputFile2 = new Scanner(new File("loki.txt"));
        Scanner inputFile3 = new Scanner(new File("shout.txt"));
        
        bot.setVerbose(true);
        
        
        bot.makeKielet();
         
        
        while (inputFile2.hasNextLine()) {
            bot.addJMegaHal(inputFile2.nextLine());
        }
        while (inputFile1.hasNextLine()) {
            bot.addJMegaHal(inputFile1.nextLine());
        }
        while (inputFile3.hasNextLine()) {
            String line = inputFile3.nextLine();
            int border = line.indexOf('|');
            String key = line.substring(0, border);
            String value = line.substring(border, line.length());
            bot.addShouts(key, value);
        }
        System.out.println("done");
        
        bot.makeInsultList();
        
        bot.setMaster(""); //add here your irc-nick
        
        bot.connect(""); //add here the server, where you want the irc bot to connect
        bot.saveServer();
        bot.joinChannel(""); //add here the channel, where you want the irc bot to join in
        bot.saveChannels();
        bot.setWriter();
        
        
        
        
        
        bot.setEncoding("UTF-8");
        
       
        
        
        
    }
    
      
}


