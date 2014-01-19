package InnoVoima;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jibble.pircbot.IrcException;

//notelist to check before running the bot:
//- exact locations of loki.txt, shout.txt and sitaatit.txt in Scanner objects
//- master nick is set correctly on BOTH classes
//- first server is set correctly in main-class
//- first channel is set correctly in main-class

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
                Scanner inputFile1 = new Scanner(new File("loki.txt")); //add here the exact location of loki.txt
                String[] loki = new String[1];
                while (inputFile1.hasNextLine()) {
                    String line = inputFile1.nextLine();
                    loki = Main.addLine(line, loki);
                }
                inputFile1.close();
                
                Scanner inputFile2 = new Scanner(new File("shout.txt")); //add here the exact location of shouts.txt
                String[] shouts = new String[1];
                while (inputFile2.hasNextLine()) {
                    String line = inputFile2.nextLine();
                    shouts = Main.addLine(line, shouts);
                }
                inputFile2.close();
                
                MyBot bot = new MyBot();
                
                
                bot.addJMegaHal(loki);
                loki = null;
                
                bot.addJMegaHal(shouts);
                shouts = null;
                
                bot.makeKielet();
                bot.makeInsultList();
                bot.setMaster("Pistus"); //set here the irc-nick who will own the most priviledges of ircbot (means your irc-nick :) )
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
        
        //fullfill the locations of each file, for example /home/user/sitaatit.txt
        String[] sitaatit = new String[1];
        Scanner inputFile1 = new Scanner(new FileInputStream("sitaatit.txt"));
        
        while (inputFile1.hasNextLine()) {
            String line = inputFile1.nextLine();
            sitaatit = addLine(line,sitaatit);
        }
        inputFile1.close();
        
        
        String[] shouts = new String[1];
        Scanner inputFile3 = new Scanner(new FileInputStream("shout.txt"));
        
        while (inputFile3.hasNextLine()) {
            String line = inputFile3.nextLine();
            shouts = addLine(line,shouts);    
        }
        inputFile3.close();
        
        String[] loki = new String[1];
        Scanner inputFile2 = new Scanner(new FileInputStream("loki.txt"));
        
        while (inputFile2.hasNextLine()) {
            String line = inputFile2.nextLine();
            loki = addLine(line,loki);
        }
        inputFile2.close();
        
        
        MyBot bot = new MyBot(); //shout.txt and loki.txt must be read before 
        //creating a new bot object, otherwise it'd wipe out both of the text files
        // it's because bot object opens those files for writing, which irritates
        //data if they are left open for reading in scanners
       
        
         
        bot.setVerbose(true);
        
        
        bot.makeKielet();
         
        
        bot.addShouts(shouts); // adds shouts that were collected from files
        shouts = null; //marks the lists for carbage collector so it releases memory
        bot.addJMegaHal(loki);
        loki = null;
        bot.addJMegaHal(sitaatit);
        sitaatit = null;
        
        
        System.out.println("done");
        
        bot.makeInsultList();
        
        bot.setMaster("Pistus"); //add here your irc-nick
        
        bot.connect("irc.freenode.org"); //add here the server, where you want the irc bot to connect
        bot.saveServer();
        bot.joinChannel("#tn1pe-2012s2"); //add here the channel, where you want the irc bot to join in
        bot.saveChannels();
        bot.setWriter();
        
        
        
        
        
        bot.setEncoding("UTF-8");
        
       
        
        
        
    }
    
    public static String[] addLine(String line, String[] list) {
        String[] newList = new String[list.length + 1];
        System.arraycopy(list, 0, newList, 0, list.length);
        newList[newList.length - 2] = line;
        return newList;
    }
    
      
}


