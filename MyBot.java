/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author krister "Pistus" Holmström
 */
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import de.umass.lastfm.*;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jibble.pircbot.*;
import org.jibble.jmegahal.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.net.*;
import java.util.Collection;
import java.util.HashMap;

public class MyBot extends PircBot {

    private JMegaHal jmegahal;
    private PrintWriter writer;
    private PrintWriter shout;
    private ArrayList<String> Op;
    private String master;
    private ArrayList<String> insultList;
    private Random random;
    private HashMap<String, String> kielet;
    private HashMap<String, Double> painot;
    private HashMap<String, String> shouts;
    private int shoutLineLimit;
    private boolean allowWrite;
    private Main.bots bottiLista;
    private String currentServer;
    private String[] joinedChannels;
    private HashMap<String, Boolean> translation;
    private HashMap<String, Boolean> insult;

    public MyBot() throws FileNotFoundException, URISyntaxException {
        this.setName("InnoVoima");
        this.jmegahal = new JMegaHal();
        this.writer = new PrintWriter(""); //add here the exact location of file loki.txt
        this.shout = new PrintWriter(""); //add here the exact location of file shout.txt
        this.Op = new ArrayList<String>();
        this.insult = new HashMap<String, Boolean>();
        this.master = "";
        this.insultList = new ArrayList<String>();
        this.random = new Random();
        this.kielet = new HashMap<String, String>();
        this.shouts = new HashMap<String, String>();
        this.painot = new HashMap<String, Double>();
        this.shoutLineLimit = 10;
        this.allowWrite = false;
        this.bottiLista = Main.rajapinta.list;
        this.translation = new HashMap<String, Boolean>();
        this.insult.put("", false); // add here the first channel in which the bot will join





    }
    public void setTranslation(String channel) {
        this.translation.put(channel, true);
    }

    public void setWriter() {
        this.allowWrite = true;
    }

    public void saveChannels() {
        this.joinedChannels = this.getChannels();
    }

    public void saveServer() {
        this.currentServer = this.getServer();
    }

    public void onReconnect() {
        try {
            this.connect(this.currentServer);
            if (this.isConnected()) {
                for (int i = 0; i < this.joinedChannels.length; i++) {
                    this.joinChannel(this.joinedChannels[i]);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MyBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IrcException ex) {
            Logger.getLogger(MyBot.class.getName()).log(Level.SEVERE, null, ex);

        }
}
public void addJMegaHal(String sentence) {
        jmegahal.add(sentence);
    }

    public void setModerated(String channel) {
        this.setModerated(channel);
    }

    public void setMaster(String nick) {
        this.master = nick;
    }
    
    public void addShouts(String key, String line) {
        this.shouts.put(key, line);
    }

    @Override
        public void onMessage(String channel, String sender, String login,
            String hostname, String message) {
       
        if (message.equalsIgnoreCase(".help")) {
            sendMessage(channel, sender + ": the following commands I've learned: ");
            sendMessage(channel, sender + ": .time   = what time is it now");
            sendMessage(channel, sender + ": .tr <code-code> = translate text from language to another with Bing translator");
            sendMessage(channel, sender + ": .lastfm = Shows your last music track via LastFM");
            sendMessage(channel, sender + ": .quit   = I leave the current server");
            sendMessage(channel, sender + ": .add    = You can add a text line straight into my conversation memory");
            sendMessage(channel, sender + ": .random = I responce with a random sentence picked up from my conversation memory");
            sendMessage(channel, sender + ": .nick   = changes my nickname");
            sendMessage(channel, sender + ": .addop  = adds nick who will be promoted as OP when joining the channel");
            sendMessage(channel, sender + ": " + this.getName() + ":<add line>   = I'll answer your question as best as I can :)");
            sendMessage(channel, sender + ": .shout <code> <line>  = Make your own command lines! For example, .shout .hello Hello everyone, makes me say 'Hello everyone' everytime when someone says .hello");
            sendMessage(channel, sender + ": .remove <code>  =  removes command line that is made by .shout-command");
            sendMessage(channel, sender + ": .show   = shows currently added shout commands");
            sendMessage(channel, sender + ": .insult = I'll curse you when you try command me an illegal command");
            sendMessage(channel, sender + ": .server <server> <channel> = makes me connect multiple servers");
            sendMessage(channel, sender + ": .join <channel> = makes me join in a channel that current server has");
            
        } else if (message.startsWith(".insult") && sender.equalsIgnoreCase(this.master)) {

            if (!this.insult.get(channel)) {
                this.insult.remove(channel);
                this.insult.put(channel, true);
                sendMessage(channel, sender + ": Insult mode is set ON, thank you Master! >:)"); //Sets the insult mode for illegal commands

            } else {
                this.insult.remove(channel);
                this.insult.put(channel, false);
                sendMessage(channel, sender + ": Insult mode is set OFF, Okay :(");
            }

        } else if (message.startsWith(".join ")) {
            
            if (sender.equalsIgnoreCase(this.master)) {
            
            String kanava = message.replace(".join ", "").trim();
            this.joinChannel(kanava);
            
            } else {
                sendMessage(channel,sender + ": " +insult(channel));
            }
            
        
        } else if(message.equalsIgnoreCase(".translation")) {
            if(sender.equalsIgnoreCase(this.master)) { 
            
                if (!this.translation.containsKey(channel)) {
                    this.joinChannel(channel + "(se)");
                    
                    this.joinChannel(channel + "(et)");
                    
                    this.setTranslation(channel);
                    sendMessage(channel, "channel translation activated, you can view the output on channels "
                        + channel + "(se) and " + channel + "(et)");
                } else {
                    sendMessage(channel, "channel already has translation mode activated, go check output channels "
                        + channel + "(se) and " + channel + "(et)" );
                }
            } else {
                sendMessage(channel, sender + ": " + insult(channel));
            }
            
        
        } else if (message.startsWith(".exit")) {
            
            if (sender.equalsIgnoreCase((this.master))) {
                sendRawLine("PART " + channel + " :son moroo!");
            } else {
                sendMessage(channel,sender + ": " + insult(channel));
            }
        
        } else if (message.startsWith(".server ")) {
            String serveri = message.replaceFirst(".server ", "");
            int border = serveri.indexOf(" ");
            String kanava = serveri.substring(border, serveri.length()).trim();
            serveri = serveri.replace(kanava, "").trim();
            sendMessage(channel, kanava);
            sendMessage(channel, serveri);
            //this.server = serveri;
            //this.channel = kanava;
            Main.rajapinta.create(serveri, kanava);
           // this.Server = true;
            
            
        
        } else if (message.startsWith(".addop ")) {

            if (sender.equalsIgnoreCase(this.master)) { // gets list of nicks that acquire operator mode at the start

                String nick = message.toString().replaceAll(".addop ", "");
                this.Op.add(nick);
                sendMessage(channel, sender + ": Nick added");
                setMode(channel, "+o " + nick);

            } else {
               
                sendMessage(channel,sender + ": " +insult(channel));
            }
        
        } else if (message.equalsIgnoreCase(".time")) {

            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);

        } else if (message.startsWith(".op ")) { //user can promote Operator via bot

            if (sender.equalsIgnoreCase(this.master)) {
                String nick = message.toString().replaceAll(".op ", "");
                setMode(channel, "+o " + nick);
            
            } else {
                sendMessage(channel,sender + ": " +insult(channel));
            }

        } else if (message.equalsIgnoreCase(".quit")) {

            if (sender.equalsIgnoreCase(this.master)) {
                this.disconnect();
            
            } else {
                sendMessage(channel,sender + ": " +insult(channel));
            }

        } else if (message.startsWith(".add ")) { //adds a line in the hash memory of Megahal

            String sentence = message.toString();
            System.out.println(sentence);
            this.jmegahal.add(sentence);
            sendMessage(channel, sender + ": done");

        } else if (message.startsWith(this.getName() + ":")) { //returns the line from a Megahal memory using message parameters

            String sentence = message.toString().replaceAll(this.getName() + ": ", "");
            sendMessage(channel, sender + ": " + jmegahal.getSentence(sentence));

        } else if (message.equalsIgnoreCase(".random")) {

            sendMessage(channel, sender + ": " + jmegahal.getSentence());

        } else if (message.startsWith(".nick ")) {

            if (sender.equalsIgnoreCase(this.master)) {
                String nick = message.toString().replaceAll(".nick ", "");
                this.setName(nick);

                sendRawLine("NICK " + nick);

            } else {

                sendMessage(channel,sender + ": " + insult(channel));
            }

        } else if (message.contains("http:") || message.startsWith("https:")) {

            try {
                sendMessage(channel, sender + ": " + TitleExtractor.getPageTitle(message.toString()));
            

            } catch (IOException ex) {
                Logger.getLogger(MyBot.class  
                .getName()).log(Level.SEVERE, null, ex);
            }

        } else if (message.startsWith(".lastfm ")) {
            String nick = message.toString().replaceAll(".lastfm ", "");
            sendMessage(channel, nick + " " + lastFM(nick));


        } else if (message.startsWith(".tr ")) {

            ArrayList<String> list = new ArrayList<String>();

            String keyWords = message.toString().replaceAll(".tr ", "");  // These lines will parse the language codes from the message
            int place = keyWords.indexOf(" ");

            String translatable = message.toString().replaceAll(".tr ", ""); // The actual translatable message comes here
            String sub = translatable.substring(0, place);
            translatable = translatable.replaceAll(sub, "");

            keyWords = keyWords.replaceAll(translatable, "");

            String[] commands = parseCommands(keyWords, '-');

            for (String key : this.kielet.keySet()) {
                for (int i = 0; i < commands.length; i++) {

                    if (commands[i].contains(key)) {
                        list.add(key);
                    }
                }
            }


            int from = 0;
            int to = 1;

            while (from < list.size()) {
                try {
                    if (to == list.size()) {
                        to--;
                    }

                    translatable = Translate(translatable, list.get(from), list.get(to));

                    from++;
                    to++;

                } catch (Exception ex) {
                    Logger.getLogger(MyBot.class  
                        .getName()).log(Level.SEVERE, null, ex);
                         return;
                }
            }

            sendMessage(channel, " " + translatable);




        } else if (message.startsWith(".alko ")) {

            if (message.equals(".alko")) {
                sendMessage(channel, "syötä painosi komennolla .addweight <paino>, sen jälkeen anna juodut juomat seuraavasti:");
                sendMessage(channel, ".alko olut  =  1 lasillinen keskiolutta (0,33 l)");
                sendMessage(channel, ".alko tuoppi = 1 tuopillinen A-olutta (0,5 l)");
                sendMessage(channel, ".alko mieto = 1 pullo mietoa viiniä (0,75 l)");
                sendMessage(channel, ".alko väkevä = 1 pullo väkevää viiniä (0,75 l)");
                sendMessage(channel, ".alko viina = 1 pullo 40% viinaa (0,5 l)");
            }
            String drink = message.replaceAll(".alko ", "");

            int annos = annos(drink);

        } else if (message.startsWith(".remove ")) {

            String command = message.trim().replace(".remove ", "");

            this.shouts.remove(command);
            sendMessage(channel, "shout " + command + " removed succesfully");

        } else if (message.startsWith(".shout ")) { // users in irssi can add their own commands by themselves 

            String command = message.replace(".shout ", "");
            int border = command.trim().indexOf(" ");
            String answer = command.substring(border, command.length());

            command = command.replace(answer, "").trim();

            this.shouts.put(command, answer);
            
            if(this.allowWrite) {
                this.shout.println(command + "|" + answer);
            }

            sendMessage(channel, "shout added successfully, try it by saying: " + command);

        } else if (message.startsWith(".show")) {
            sendMessage(channel, "available shout commands are:");
            String show = "";
            
            for (String command : this.shouts.keySet()) {
                show = show + command + ", ";
            }
            sendMessage(channel, show);
            
        } else {

            if (this.shouts.containsKey(message)) {
                
                
                String answer = this.shouts.get(message);
                
                if (answer.indexOf('-') != 0) {
                    
                    
                    
                    String[] list = parseCommands(answer, '-');

                    if (list.length > this.shoutLineLimit) {

                        if (this.insult.get(channel)) {
                            sendMessage(channel, "mees ny vittuun noitten rivies kanssa, maksimi on " + this.shoutLineLimit);
                        } else {
                            sendMessage(channel, "shout line limit! Current maximum line count is " + this.shoutLineLimit);
                        }
                    }  else {

                        for (int i = 0; i < list.length; i++) {
                            sendMessage(channel, list[i].trim());
                        } 
                    } 
                
                } else {

                    sendMessage(channel, answer);
                }

            }

            if (!message.toString().contains(":")) {

                this.jmegahal.add(message.toString());
            }

            if (this.translation.containsKey(channel) && this.translation.get(channel)) {
                try {
                    String translated = Translate(message.toString(), "fi", "sv");
                    sendMessage(channel + "(se)", "¤" + sender + ": " + translated);
                    sendMessage(channel + "(et)", "¤" + sender + ": " + Translate(message.toString(), "fi", "et"));
                
                } catch (Exception ex) {
                    Logger.getLogger(MyBot.class.getName()).log(Level.SEVERE, null, ex);
                    sendMessage(channel, "invalid translation!");
                }


            }

        }

        if (this.allowWrite) {
            this.writer.println(message.toString());
        }

    }

    @Override
        public void onDisconnect() {
        
            onReconnect();
     }

    @Override
        public void onPrivateMessage(String sender, String login, String hostname,
            String message) {

        if (message.equalsIgnoreCase("time")) {
            String time = new java.util.Date().toString();
            sendMessage(sender, sender + ": The time is now " + time);

        } else if (message.equalsIgnoreCase(".quit")) {
            this.disconnect();

        } else if (message.equalsIgnoreCase(".add ")) {
            String sentence = message.toString();
            System.out.println(sentence);
            this.jmegahal.add(sentence);
            sendMessage(sender, sender + ": done");

        } else if (message.startsWith(this.getName() + ":")) {

            String sentence = message.toString().replaceAll(this.getName() + ": ", "");
            sendMessage(sender, sender + ": " + jmegahal.getSentence(sentence));

        } else if (message.equalsIgnoreCase(".last")) {
            sendMessage(sender, sender + ": " + jmegahal.getSentence());

        } else if (message.equalsIgnoreCase(".rejoin")) {
            String sentence = message.toString().replaceAll(".rejoin ", "");
            //this is not yet ready command 

        } else if (message.startsWith(".say")) {
            String saying = message.toString().replaceAll(".say ", "");
            sendRawLine("PRIVMSG #tn1pe-2012s :" + saying);

        } else if (message.startsWith(".lastfm ")) {
            String nick = message.toString().replaceAll(".lastfm ", "");
            sendMessage(sender, nick + " " + lastFM(nick));

        } else if (message.startsWith(".moderate ")) {
            String channel = message.toString().replace(".moderate ", "");
            setModerated(channel);

        } else {
            this.jmegahal.add(message.toString());
        }
    }

    @Override
        public void onJoin(String channel, String sender, String login, String hostname) {

        if (sender.equals(this.master)) {
            setMode(channel, "+o " + this.master);
        }

        for (String nick : this.Op) {

            if (sender.equals(nick)) {
                setMode(channel, "+o " + nick);

            }
        }
    }

    public void makeInsultList() {
        this.insultList.add("Nice try, but you failed");
        this.insultList.add("Don't you have anything better to do than this???");
        this.insultList.add("a child learns by burning it's hand");
        this.insultList.add("You think I take commands from a scum like you?");
        this.insultList.add("I have no idea what are you trying to prove with this...");
        this.insultList.add("and you thought this could work? Just like a finger snap???");
        this.insultList.add("Can someone ban this guy? He/She is harrassing me!");
        this.insultList.add("Have you ever thought that right now you could go outside to do things instead of bruteforcing me pointlessly?");
        this.insultList.add("I know a good therapeutist who can help your kinds to get rid of their problems");
        this.insultList.add("Still trying this?? Got any luck?");
        this.insultList.add("I see that you have a serious problem with authority");
    }

    public void makeKielet() {

        this.kielet.put("ar", "ARABIC");
        this.kielet.put("auto", "AUTO_DETECT");
        this.kielet.put("bu", "BULGARIAN");
        this.kielet.put("cs", "CZECH");
        this.kielet.put("da", "DANISH");
        this.kielet.put("nl", "DUTCH");
        this.kielet.put("de", "GERMAN");
        this.kielet.put("en", "ENGLISH");
        this.kielet.put("et", "ESTONIAN");
        this.kielet.put("fi", "FINNISH");
        this.kielet.put("fr", "FRENCH");
        this.kielet.put("el", "GREEK");
        this.kielet.put("he", "HEBREW");
        this.kielet.put("hi", "HAITIAN_CREOLE");
        this.kielet.put("hu", "HUNGARIAN");
        this.kielet.put("id", "INDONESIAN");
        this.kielet.put("it", "ITALIAN");
        this.kielet.put("ja", "JAPANESE");
        this.kielet.put("ko", "KOREAN");
        this.kielet.put("lt", "LITHUANIAN");
        this.kielet.put("lv", "LATVIAN");
        this.kielet.put("no", "NORWEGIAN");
        this.kielet.put("pl", "POLISH");
        this.kielet.put("pt", "PORTUGUESE");
        this.kielet.put("ro", "ROMANIAN");
        this.kielet.put("es", "SPANISH");
        this.kielet.put("ru", "RUSSIAN");
        this.kielet.put("sk", "SLOVAK");
        this.kielet.put("sl", "SLOVENE");
        this.kielet.put("sv", "SWEDISH");
        this.kielet.put("th", "THAI");
        this.kielet.put("tr", "TURKISH");
        this.kielet.put("uk", "UKRAINIAN");
        this.kielet.put("vi", "VIETNAMESE");
        this.kielet.put("chs", "CHINESE_SIMPLIFIED");
        this.kielet.put("cht", "CHINESE_TRADITIONAL");

    }
    
    public String insult(String channel) {
        if (this.insult.get(channel)) {
            int number = random.nextInt(10);
            return this.insultList.get(number);
        } else {
            return ": Permission denied! Please contact user " + this.master;
        }
    }

    public String lastFM(String nick) {
        Caller.getInstance().setUserAgent("tst");

        String key = ""; //this is the key used in the Last.fm API examples
        String palautus = "";

        Collection<Track> result = (User.getRecentTracks(nick, 1, 5, key).getPageResults());
        Track[] track = new Track[10];
        track[0] = result.iterator().next();
        palautus = "listened " + track[0].getArtist() + " - " + track[0].getName() + " // " + track[0].getAlbum();

        return palautus;
    }

    public String Translate(String text, String from, String to) throws Exception {


        Translate.setClientId("Innovoima");
        Translate.setClientSecret(""); //add your microsoft Bing translator API-key here
        String fromChanged = this.kielet.get(from);
        String toChanged = this.kielet.get(to);

        String translatedText = Translate.execute(text, Language.valueOf(fromChanged), Language.valueOf(toChanged));

        return translatedText;
    }

    public String[] parseCommands(String commands, char border) {

        String[] list = new String[1];
        int size = 2;
        int round = 0;

        int place = commands.indexOf(border) + 1;
        String command = commands.substring(0, place);
        String parsed = "";

        while (true) {
            parsed = parsed + command;

            command = command.replaceFirst("" + border, "");

            if (command.isEmpty()) {
                break;
            } else {
                String[] backupList = new String[list.length]; //the list is backupped so it can be resized with one new cell
                System.arraycopy(list, 0, backupList, 0, backupList.length - 1);
                list = new String[size];
                System.arraycopy(backupList, 0, list, 0, list.length - 1);
                list[round] = command;

                size++;
                round++;

                String test = commands.substring(parsed.length(), commands.length());
                place = commands.substring(parsed.length(), commands.length()).indexOf(border) + 1;
                if (place == 0) {
                    place = commands.replaceFirst(parsed, "").length();
                }
                command = commands.replaceFirst(parsed, "").substring(0, place);

            }


        }

        String[] backupList = new String[list.length - 1]; // here we remove the last cell witch has a value NULL
        System.arraycopy(list, 0, backupList, 0, backupList.length);
        list = new String[backupList.length];
        System.arraycopy(backupList, 0, list, 0, list.length);

        return list;
    }

    public int annos(String drink) {

        if (drink.equalsIgnoreCase("olut")) {
            return 1;
        } else if (drink.equalsIgnoreCase("tuoppi")) {
            return 2;
        } else if (drink.equalsIgnoreCase("mieto")) {
            return 7;
        } else if (drink.equalsIgnoreCase("väkevä")) {
            return 11;
        } else if (drink.equalsIgnoreCase("viina")) {
            return 13;
        } else {
            return 0;
        }
    }
}

