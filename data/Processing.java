package data;

import data.classes.*;
import server.ConsoleColors;

import java.io.*;

/**
 * - un qui lit le fichier et qui produit des objets parties avec les attributs
 * et dans le même un ensemble de thread qui prennent des objets parties depuis un file
 * et construisent des Hashtables ou BTree pour faciliter les recherche (par nom, date, etc.)
 * ensuite ces structure sont enregistrées sur le disque par sérialisation
 **/


public class Processing {

    public static Sommaire<Integer> playerSummary = new Sommaire<>(); //hashtable avec comme clé le pseudo du joueur et en 2ème clé un numéro attribué à la partie pour les ordonner
    public static Sommaire<String> dateSummary = new Sommaire<>(); // avec comme 1ère clé la date, puis en 2ème l'heure
    public static Sommaire<Integer> openingSummary = new Sommaire<>(); //en 1ère clé le nom de l'ouverture, en 2ème numéro attribuée à la partie
    public static void main(String[] arg) throws IOException {
        File src = new File("src/data/lichess.pgn");
        System.out.println("###### Loading " + src.getName() + " ######");
        FileInputStream in = new FileInputStream(src);
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StreamTokenizer st = new StreamTokenizer(r);
        String chaine;
        String date = "";
        Part partie = new Part();
        int numPart = 1;
        long cptByte = 0;
        System.out.print("- Indexing in progress...");
        while (st.nextToken() != StreamTokenizer.TT_EOF) {
            int nligne = 0;
            if (StreamTokenizer.TT_WORD == st.ttype) {
                chaine = r.readLine();
                switch (st.sval) {
                    case "Event" -> {
                        partie.setOctet(cptByte);
                    }
                    case "Site" -> {
                        chaine = chaine.substring(chaine.indexOf("lichess.org/") + 12, chaine.indexOf("\"]")); // récupère le nom de la partie entre guillemets
                        partie.setName(chaine);
                    }
                    case "White" -> {
                        chaine = chaine.substring(chaine.indexOf("\"") + 1, chaine.indexOf("\"]"));
                        partie.setWhite(chaine);
                        playerSummary.addPart(chaine, partie, numPart);
                    }
                    case "Black" -> {
                        chaine = chaine.substring(chaine.indexOf("\"") + 1, chaine.indexOf("\"]"));
                        partie.setBlack(chaine);
                        playerSummary.addPart(chaine, partie, numPart);
                    }
                    case "Result" -> {
                        chaine = chaine.substring(chaine.indexOf("\"") + 1, chaine.indexOf("\"]"));
                        partie.setResult(chaine);
                    }
                    case "UTCDate" -> {
                        date = chaine.substring(chaine.indexOf("\"") + 1, chaine.indexOf("\"]"));
                        partie.setDate(date);
                    }
                    case "UTCTime" -> {
                        String time = chaine.substring(chaine.indexOf("\"") + 1, chaine.indexOf("\"]"));
                        dateSummary.addPart(date, partie, time);
                    }
                    case "Opening" -> {
                        chaine = chaine.substring(chaine.indexOf("\"") + 1, chaine.indexOf("\"]"));
                        partie.setOp(chaine);
                        openingSummary.addPart(chaine, partie, numPart);
                        numPart++;
                    }
                    default -> {
                    }
                }
                cptByte += chaine.getBytes().length + 1; // Marquer le retour à la ligne
            }
        }
        System.out.println(ConsoleColors.GREEN_BRIGHT + "DONE!" + ConsoleColors.RESET);
        System.out.print("- Serializing in progress");
        dateSummary.write("Date_Summary.ser");
        System.out.print(".");
        playerSummary.write("Player_Summary.ser");
        System.out.print(".");
        openingSummary.write("Opening_Summary.ser");
        System.out.print(".");
        System.out.println(ConsoleColors.GREEN_BRIGHT + "DONE!" + ConsoleColors.RESET);
        System.out.println("- " + openingSummary.getSize() + " games serialized!");
        r.close();
        in.close();
    }
}

