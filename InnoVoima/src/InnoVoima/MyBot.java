package InnoVoima;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Krister "Pistus" Holmström
 */

/*
 * notelist to check before running the bot:
 * - set correct channel on this.defaultChannel
 * - set correct API-keys on lastFM() and translate() -methods
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.joda.time.Period;

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
    private boolean die;
    private String defaultChannel;
    private HashMap<String, Double> alcoholGrams;
    private HashMap<String, GregorianCalendar> lastLog;
    private HashMap<String, Integer> soberTime;
    private HashMap<String, Boolean> male;
    

    public MyBot() throws FileNotFoundException, URISyntaxException {
        this.setName("InnoVoima");
        this.jmegahal = new JMegaHal();
        this.writer = new PrintWriter("loki.txt"); //add here the exact location of file loki.txt
        this.shout = new PrintWriter("shout.txt"); //add here the exact location of file shout.txt
        this.defaultChannel = ""; // add here the first channel in which the bot will join
        this.Op = new ArrayList<>();
        this.insult = new HashMap<>();
        this.master = "";
        this.insultList = new ArrayList<>();
        this.random = new Random();
        this.kielet = new HashMap<>();
        this.shouts = new HashMap<>();
        this.painot = new HashMap<>();
        this.shoutLineLimit = 10;
        this.bottiLista = Main.rajapinta.list;
        this.translation = new HashMap<>();
        this.insult.put(defaultChannel, false);
        this.die = false;
        this.alcoholGrams = new HashMap<>();
        this.lastLog = new HashMap<>();
        this.soberTime = new HashMap<>();
        this.allowWrite = true;
        this.male = new HashMap<>();





    }

    public void setTranslation(String channel) {
        this.translation.put(channel, true);
    }

    public void setWriter() {
        this.allowWrite = true;
    }

    public void saveChannels() {

        if (this.joinedChannels == null) {
            this.joinedChannels = this.getChannels();
        }
        int listSize = this.joinedChannels.length;
        boolean contains = false;
        String[] channels = this.getChannels();
        for (String channel : channels) {
            for (int i = 0; i <= this.joinedChannels.length; i++) {
                if (channel.equals(this.joinedChannels[i])) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                this.joinedChannels[listSize - 1] = channel;
            } else {
                contains = false;
            }

        }
    }

    public void saveServer() {
        this.currentServer = this.getServer();
    }

    @Override
    protected void onUserMode(String targetNick, String sourceNick,
            String sourceLogin, String sourceHostname, String mode) {


        if (this.joinedChannels != null && sourceNick.equals(this.getNick()) && mode.equals("MODE")) {
            System.out.println("hello");
            for (int i = 0; i < this.joinedChannels.length; i++) {
                this.joinChannel(this.joinedChannels[i]);
            }

        } else if (sourceNick.equals(this.getNick()) && mode.equals("MODE")) {
            this.joinChannel((String) this.insult.keySet().toArray()[0]);
        }

    }

    public void addJMegaHal(String[] sentence) {
        int i = 0;
        while (i < sentence.length - 1) {
            jmegahal.add(sentence[i]);
            i++;
        }
    }

    public void setModerated(String channel) {
        this.setModerated(channel);
    }

    public void setMaster(String nick) {
        this.master = nick;
    }

    public void addShouts(String[] list) {
        
        int i = 0;
        while (i < list.length - 1) {
            
            int border = list[i].indexOf(' ');
            String key = list[i].substring(0, border).trim();
            String value = list[i].substring(border, list[i].length());
            this.shouts.put(key, value);
            i++;
        }
    }

    @Override
    public void onMessage(String channel, String sender, String login,
            String hostname, String message) {

        if (message.equalsIgnoreCase(".help")) {
            sendMessage(sender, "the following commands I've learned: ");
            sendMessage(sender, "  .time   = what time is it now");
            sendMessage(sender, "  [work in progress].tr <code-code> = translate text from language to another with Bing translator");
            sendMessage(sender, "  .lastfm = Shows your last music track via LastFM");
            sendMessage(sender, "  .quit   = I leave the current server");
            sendMessage(sender, "  .add    = You can add a text line straight into my conversation memory");
            sendMessage(sender, "  .random = I responce with a random sentence picked up from my conversation memory");
            sendMessage(sender, "  .nick   = changes my nickname");
            sendMessage(sender, "  .addop  = adds nick who will be promoted as OP when joining the channel");
            sendMessage(sender, "  " + this.getName() + ":<add line>   = I'll answer your question as best as I can :)");
            sendMessage(sender, "  .shout <code> <line>  = Make your own command lines! For example, .shout .hello Hello everyone, makes me say 'Hello everyone' everytime when someone says .hello");
            sendMessage(sender, "  .remove <code>  =  removes command line that is made by .shout-command");
            sendMessage(sender, "  .show   = shows currently added shout commands");
            sendMessage(sender, "  .insult = I'll curse you when you try command me an illegal command");
            sendMessage(sender, "  .server <server> <channel> = makes me connect multiple servers");
            sendMessage(sender, "  .join <channel> = makes me join in a channel that current server has");
            sendMessage(sender, "  .choose <option1>|<option2> = give me some options and I choose for you. I.e. .choose me|you");
            sendMessage(sender, "  .alko  = pre mill calculator! You'll get more info by using this command");

        } else if (message.startsWith(".insult") && sender.equals(this.master)) {

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
                saveChannels();

            } else {
                sendMessage(channel, sender + ": " + insult(channel));
            }

        } else if (message.startsWith(".choose ")) {
            String options = message.replace(".choose ", "");
            String[] listOptions = parseCommands(options, '|');

            if (listOptions.length > 0) {
                int number = this.random.nextInt(listOptions.length - 1);
                sendMessage(channel, "I'll choose " + listOptions[number]);
            }

        } else if (message.equalsIgnoreCase(".translation")) {
            if (sender.equalsIgnoreCase(this.master)) {

                if (!this.translation.containsKey(channel)) {
                    this.joinChannel(channel + "(se)");

                    this.joinChannel(channel + "(et)");

                    this.setTranslation(channel);
                    sendMessage(channel, "channel translation activated, you can view the output on channels "
                            + channel + "(se) and " + channel + "(et)");
                } else {
                    sendMessage(channel, "channel already has translation mode activated, go check output channels "
                            + channel + "(se) and " + channel + "(et)");
                }
            } else {
                sendMessage(channel, sender + ": " + insult(channel));
            }


        } else if (message.startsWith(".suomi")) {
            char s = 2588;
            sendMessage(channel, Colors.WHITE + s + s + s + Colors.BLUE + "  " + Colors.WHITE + "     ");
            sendMessage(channel, Colors.BLUE + "          ");
            sendMessage(channel, Colors.WHITE + "   " + Colors.BLUE + "  " + Colors.WHITE + "     ");

        } else if (message.startsWith(".exit")) {

            if (sender.equalsIgnoreCase((this.master))) {
                sendRawLine("PART " + channel + " :son moroo!");
            } else {
                sendMessage(channel, sender + ": " + insult(channel));
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

                sendMessage(channel, sender + ": " + insult(channel));
            }

        } else if (message.equalsIgnoreCase(".time")) {

            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);

        } else if (message.startsWith(".op ")) { //user can promote Operator via bot

            if (sender.equalsIgnoreCase(this.master)) {
                String nick = message.toString().replaceAll(".op ", "");
                setMode(channel, "+o " + nick);

            } else {
                sendMessage(channel, sender + ": " + insult(channel));
            }

        } else if (message.equalsIgnoreCase(".quit")) {

            if (sender.equalsIgnoreCase(this.master)) {
                this.die = true;
                this.disconnect();

            } else {
                sendMessage(channel, sender + ": " + insult(channel));
            }

        } else if (message.startsWith(".add ")) { //adds a line in the hash memory of Megahal

            String sentence = message.toString();
            System.out.println(sentence);
            this.jmegahal.add(sentence);
            sendMessage(channel, sender + ": done");

        } else if (message.startsWith(this.getName() + ":")) { //returns the line from the Megahal memory using message parameters

            String sentence = message.toString().replaceAll(this.getName() + ": ", "");
            sendMessage(channel, jmegahal.getSentence(sentence));

        } else if (message.equalsIgnoreCase(".random")) {

            sendMessage(channel, jmegahal.getSentence());

        } else if (message.startsWith(".nick ")) {

            if (sender.equalsIgnoreCase(this.master)) {
                String nick = message.toString().replaceAll(".nick ", "");
                this.setName(nick);

                sendRawLine("NICK " + nick);

            } else {

                sendMessage(channel, sender + ": " + insult(channel));
            }

        } else if (message.contains("http:") || message.startsWith("https:")) {
            try {
                sendMessage(channel, TitleExtractor.getPageTitle(message.toString()));
            } catch (IOException ex) {
                Logger.getLogger(MyBot.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (message.startsWith(".lastfm ")) {
            String nick = message.toString().replaceAll(".lastfm ", "");
            sendMessage(channel, nick + " " + lastFM(nick));


        } else if (message.startsWith(".tr ")) {

            ArrayList<String> list = new ArrayList<>();

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




        } else if (message.startsWith(".alko")) {

            if (message.equals(".alko")) {
                sendMessage(sender, "Promillemittari! haluatko kuulla, miten paljon juomasi nosti veresi alkoholipitoisuutta ja kauanko kestää selvitä? komennot voi antaa joko kanavalla tai privatena");
                sendMessage(sender, "syötä painosi komennolla .addweight <paino> ja sukupuolesi komennolla .addgender <mies/nainen>, sen jälkeen ilmoita nauttimasi juomat seuraavasti:");
                sendMessage(sender, ".alko pullo  =  1 pullo keskiolutta (0,33 l)");
                sendMessage(sender, ".alko tuoppi = 1 tuopillinen A-olutta (0,5 l)");
                sendMessage(sender, ".alko mieto = 1 pullo mietoa viiniä (0,75 l)");
                sendMessage(sender, ".alko väkevä = 1 pullo väkevää viiniä (0,75 l)");
                sendMessage(sender, ".alko vodka = 1 pullo 40% vodkaa (0,5 l)");
                sendMessage(sender, "");
                sendMessage(sender, "HUOMAUTUS! promillelaskuri antaa vain suuntaa-antavia arvioita, eikä sitä tule käyttää ajokunnon arvioimiseksi");
                sendMessage(sender, "promillemittarin avainkomennot:");
                sendMessage(sender, ".alko  = näyttää tämän viestin");
                sendMessage(sender, ".alko <juoma> = lisää antamasi juoman annosmäärän ja laskee promillet sekä toipumisajan");
                sendMessage(sender, ".sober  = näyttää senhetkisen promillemäärän ja jäljellä olevan toipumisajan");
                sendMessage(sender, ".reset  = nollaa laskurin tiedot, paitsi jättää tiedot painosta ja sukupuolesta");
                sendMessage(sender, ".addweight <paino>  = määritä painosi kilogrammoina laskukaavaa varten (voi määrittää uudestaan niin monta kertaa kuin haluaa)");
                sendMessage(sender, ".addgender <mies/nainen> = määritä sukupuolesi (voi määrittää uudestaan tarvittaessa)");
            } else {
                String drink = message.replaceAll(".alko ", "");
                int grammoina = annos(drink) * 12; //alkoholin tyyppi puhtaaksi alkoholigrammoiksi muutettuna

                if (!this.painot.containsKey(sender)) {
                    sendMessage(channel, "anna ensin painosi komennolla .addweight <paino kiloina>");

                } else if (!this.male.containsKey(sender)) {
                    sendMessage(channel, "anna ensin sukupuolesi komennolla .addgender <mies/nainen>");

                } else if (grammoina == 0) {
                    sendMessage(channel, "alkoholityyppiä ei tunnistettu, yritä uudelleen");

                } else {
                    if (!this.lastLog.containsKey(sender)) { // if no previous data, then current time is the of the first info log
                        this.lastLog.put(sender, new GregorianCalendar());
                    }

                    addAlcohol(sender, grammoina);
                    sendMessage(channel, "Promillet tällä hetkellä: " + calculatePermill(sender) + " promillea");
                    calculateSoberTime(sender);
                    sendMessage(channel, "Alkoholin palamisen arvoitu kesto: " + this.soberTime.get(sender) + " tuntia");
                }

            }

        } else if (message.startsWith(".sober")) {

            if (!this.alcoholGrams.containsKey(sender) || this.alcoholGrams.get(sender) == 0) {
                sendMessage(channel, "harvinaisen selvinpäin :)");
            } else {

                sendMessage(channel, "Promillet tällä hetkellä " + calculatePermill(sender) + " promillea");
                calculateSoberTime(sender);
                sendMessage(channel, "Alkoholin palamisen arvioitu kesto: " + this.soberTime.get(sender) + " tuntia");

            }

        } else if (message.startsWith(".addweight")) {
            Double weight;
            try {
                weight = Double.parseDouble(message.replace(".addweight ", ""));
                this.painot.put(sender, weight);
                sendMessage(channel, "weight added successfully");
            } catch (Exception e) {
                sendMessage(channel, "error, weight must be at number format, please try again");
            }




        } else if (message.startsWith(".reset")) {
            this.lastLog.remove(sender);
            this.alcoholGrams.remove(sender);
            this.soberTime.remove(sender);
            sendMessage(channel, "laskuri nollattu onnistuneesti");

        } else if (message.startsWith(".addgender")) { // user can give his/her gender for alcohol calculation
            String gender = message.replace(".addgender ", "");
            int size = this.male.size();

            switch (gender) {
                case "male":
                    this.male.put(sender, true);
                    break;
                case "female":
                    this.male.put(sender, false);
                    break;
                case "mies":
                    this.male.put(sender, true);
                    break;
                case "nainen":
                    this.male.put(sender, false);
                    break;
                case "jonne":
                    this.male.put(sender, true);
                    break;
                case "jonna":
                    this.male.put(sender, false);
                    break;
                case "poika":
                    this.male.put(sender, true);
                    break;
                case "tyttö":
                    this.male.put(sender, false);
                    break;
                case "boy":
                    this.male.put(sender, true);
                    break;
                case "girl":
                    this.male.put(sender, false);
            }

            if (this.male.size() > size) {
                sendMessage(channel, "gender added succesfully");
            } else {
                sendMessage(channel, "unknown type of gender, please try again.");
            }

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

            this.shout.println(command + answer);


            sendMessage(channel, "shout added successfully, try it by saying: " + command);

            this.shout.flush();

        } else if (message.startsWith(".limit")) { //sets the shout limit
            if (sender.equals(this.master)) {
                String limitNumber = message.replace(".limit ", "");
                int number = Integer.parseInt(limitNumber);
                this.shoutLineLimit = number;
                sendMessage(channel, "Shout line limit changed, new limit is "
                        + number + " lines maximum");
            } else {
                sendMessage(channel, sender + ": " + insult(channel));
            }


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

                if (answer.indexOf('|') != -1) {



                    String[] list = parseCommands(answer, '|');

                    if (list.length > this.shoutLineLimit) {

                        if (this.insult.get(channel)) {
                            sendMessage(channel, "mees ny vittuun noitten rivies kanssa, maksimi on " + this.shoutLineLimit);
                        } else {
                            sendMessage(channel, "shout line limit! Current maximum line count is " + this.shoutLineLimit);
                        }
                    } else {

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
            this.writer.flush();
        }

    }

    @Override
    public void onDisconnect() {

        if (!die) {
            try {
                reconnect();
                this.joinChannel(this.defaultChannel);

            } catch (IOException | IrcException ex) {
                this.shout.close();
                this.writer.close();
                Logger.getLogger(MyBot.class.getName()).log(Level.FINE, null, ex);

            }
        } else {
            this.shout.close();
            this.writer.close();
        }

    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname,
            String message) {

        if (message.equalsIgnoreCase("time")) {
            String time = new java.util.Date().toString();
            sendMessage(sender, sender + ": The time is now " + time);

        } else if (message.equalsIgnoreCase(".quit")) {
            this.die = true;
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
        } else if (message.startsWith(".alko")) {

            if (message.equals(".alko")) {
                sendMessage(sender, "Promillemittari! haluatko kuulla, miten paljon juomasi nosti veresi alkoholipitoisuutta ja kauanko kestää selvitä? komennot voi antaa joko kanavalla tai privatena");
                sendMessage(sender, "syötä painosi komennolla .addweight <paino> ja sukupuolesi komennolla .addgender <mies/nainen>, sen jälkeen ilmoita nauttimasi juomat seuraavasti:");
                sendMessage(sender, ".alko pullo  =  1 pullo keskiolutta (0,33 l)");
                sendMessage(sender, ".alko tuoppi = 1 tuopillinen A-olutta (0,5 l)");
                sendMessage(sender, ".alko mieto = 1 pullo mietoa viiniä (0,75 l)");
                sendMessage(sender, ".alko väkevä = 1 pullo väkevää viiniä (0,75 l)");
                sendMessage(sender, ".alko vodka = 1 pullo 40% vodkaa (0,5 l)");
                sendMessage(sender, "");
                sendMessage(sender, "HUOMAUTUS! promillelaskuri antaa vain suuntaa-antavia arvioita, eikä sitä tule käyttää ajokunnon arvioimiseksi");
                sendMessage(sender, "promillemittarin avainkomennot:");
                sendMessage(sender, ".alko  = näyttää tämän viestin");
                sendMessage(sender, ".alko <juoma> = lisää antamasi juoman annosmäärän ja laskee promillet sekä toipumisajan");
                sendMessage(sender, ".sober  = näyttää senhetkisen promillemäärän ja jäljellä olevan toipumisajan");
                sendMessage(sender, ".reset  = nollaa laskurin tiedot, paitsi jättää tiedot painosta ja sukupuolesta");
                sendMessage(sender, ".addweight <paino>  = määritä painosi kilogrammoina laskukaavaa varten (voi määrittää uudestaan niin monta kertaa kuin haluaa)");
                sendMessage(sender, ".addgender <mies/nainen> = määritä sukupuolesi (voi määrittää uudestaan tarvittaessa)");
            } else {
                String drink = message.replaceAll(".alko ", "");
                int grammoina = annos(drink) * 12; //alkoholin tyyppi puhtaaksi alkoholigrammoiksi muutettuna

                if (!this.painot.containsKey(sender)) {
                    sendMessage(sender, "anna ensin painosi komennolla .addweight <paino kiloina>");

                } else if (!this.male.containsKey(sender)) {
                    sendMessage(sender, "anna ensin sukupuolesi komennolla .addgender <mies/nainen>");

                } else if (grammoina == 0) {
                    sendMessage(sender, "alkoholityyppiä ei tunnistettu, yritä uudelleen");

                } else {
                    if (!this.lastLog.containsKey(sender)) { // if no previous data, then current time is the of the first info log
                        this.lastLog.put(sender, new GregorianCalendar());
                    }

                    addAlcohol(sender, grammoina);
                    sendMessage(sender, "Promillet tällä hetkellä: " + calculatePermill(sender) + " promillea");
                    calculateSoberTime(sender);
                    sendMessage(sender, "Alkoholin palamisen arvoitu kesto: " + this.soberTime.get(sender) + " tuntia");
                }

            }

        } else if (message.startsWith(".sober")) {

            if (!this.alcoholGrams.containsKey(sender) || this.alcoholGrams.get(sender) == 0 ) {
                sendMessage(sender, "harvinaisen selvinpäin :)");
            } else {

                sendMessage(sender, "Promillet tällä hetkellä " + calculatePermill(sender) + " promillea");
                calculateSoberTime(sender);
                sendMessage(sender, "Alkoholin palamisen arvioitu kesto: " + this.soberTime.get(sender) + " tuntia");

            }

        } else if (message.startsWith(".addweight")) {
            Double weight;
            try {
                weight = Double.parseDouble(message.replace(".addweight ", ""));
                this.painot.put(sender, weight);
                sendMessage(sender, "weight added successfully");
            } catch (Exception e) {
                sendMessage(sender, "error, weight must be at number format, please try again");
            }




        } else if (message.startsWith(".reset")) {
            this.lastLog.remove(sender);
            this.alcoholGrams.remove(sender);
            this.soberTime.remove(sender);
            sendMessage(sender, "laskuri nollattu onnistuneesti");

        } else if (message.startsWith(".addgender")) { // user can give his/her gender for alcohol calculation
            String gender = message.replace(".addgender ", "");
            int size = this.male.size();

            switch (gender) {
                case "male":
                    this.male.put(sender, true);
                    break;
                case "female":
                    this.male.put(sender, false);
                    break;
                case "mies":
                    this.male.put(sender, true);
                    break;
                case "nainen":
                    this.male.put(sender, false);
                    break;
                case "jonne":
                    this.male.put(sender, true);
                    break;
                case "jonna":
                    this.male.put(sender, false);
                    break;
                case "poika":
                    this.male.put(sender, true);
                    break;
                case "tyttö":
                    this.male.put(sender, false);
            }

            if (this.male.size() > size) {
                sendMessage(sender, "gender added succesfully");
            } else {
                sendMessage(sender, "unknown type of gender, please try again.");
            }
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
        this.insultList.add("Have you thought that right now you could go outside to do things instead of bruteforcing me pointlessly?");
        this.insultList.add("I know a good therapist who can help your kinds to get rid of their problems");
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
        if (this.insult.containsKey(channel)) {
            if (this.insult.get(channel)) {
                int number = random.nextInt(10);
                return this.insultList.get(number);
            }
        } else {
            return ": Permission denied! Please contact user " + this.master;
        }
        return null;
    }

    public String lastFM(String nick) {
        Caller.getInstance().setUserAgent("tst");

        String key = ""; //this is the key used in the Last.fm API examples
        String palautus;

        Collection<Track> result = (User.getRecentTracks(nick, 1, 5, key).getPageResults());
        Track[] track = new Track[10];
        track[0] = result.iterator().next();
        palautus = "listened " + track[0].getArtist() + " - " + track[0].getName() + " // " + track[0].getAlbum();

        return palautus;
    }

    public String Translate(String text, String from, String to) throws Exception {


        Translate.setClientId(""); //add your API-client ID here
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

            command = command.replace("" + border, "");

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
                    place = commands.replace(parsed, "").length();
                }
                command = commands.replace(parsed, "").substring(0, place);

            }


        }

        String[] backupList = new String[list.length - 1]; // here we remove the last cell witch has a value NULL
        System.arraycopy(list, 0, backupList, 0, backupList.length);
        list = new String[backupList.length];
        System.arraycopy(backupList, 0, list, 0, list.length);

        return list;
    }

    public int annos(String drink) {

        if (drink.equalsIgnoreCase("pullo")) {
            return 1;
        } else if (drink.equalsIgnoreCase("tuoppi")) {
            return 2;
        } else if (drink.equalsIgnoreCase("mieto")) {
            return 7;
        } else if (drink.equalsIgnoreCase("väkevä") || drink.equalsIgnoreCase("vakeva")) {
            return 11;
        } else if (drink.equalsIgnoreCase("vodka")) {
            return 13;
        } else {
            return 0;
        }
    }

    public double calculatePermill(String nick) {

        if (calculateBurning(nick) == 0) {
            this.alcoholGrams.put(nick, 0.0);
            return 0.0;
        }

        double weight = this.painot.get(nick);
        double permill;

        if (this.male.get(nick)) {
            permill = calculateBurning(nick) / (weight * 0.75); // a male gains 75% percent water of his weight
        } else {
            permill = calculateBurning(nick) / (weight * 0.66); // a female gains 66% percent water of her weight
        }

        return permill;

    }

    public void addAlcohol(String nick, int grams) { //first removes alcoholgrams that has been sobered up, then adds new grams onto list


        Double completeGrams = calculateBurning(nick) + grams;
        this.alcoholGrams.put(nick, completeGrams);
        this.lastLog.put(nick, new GregorianCalendar()); //update the current time when added alcohol info


    }

    public void calculateSoberTime(String nick) {

        Double alcohol = calculateBurning(nick);
        Double weight = this.painot.get(nick);

        int soberTimeHours = (int) (alcohol / (weight / 10));

        this.soberTime.put(nick, soberTimeHours);

    }

    public double calculateBurning(String nick) {
        GregorianCalendar calendar = new GregorianCalendar();
        long timeNow = calendar.getTimeInMillis();
        long timeLast = this.lastLog.get(nick).getTimeInMillis();
        Period p = new Period(timeLast, timeNow);
        int hours = p.getHours();
        double currentGrams;

        if (!this.alcoholGrams.containsKey(nick)) {
            currentGrams = 0;
        } else {
            currentGrams = this.alcoholGrams.get(nick);
        }

        double completeGrams = (currentGrams - (hours * (this.painot.get(nick) / 10)));

        if (completeGrams < 0) { //set the former gram sum to 0 if sobered up, so it won't affect the result
            completeGrams = 0.0;
        }
        return completeGrams;


    }
}
