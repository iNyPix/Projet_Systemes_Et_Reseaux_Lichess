package server;

import data.classes.*;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

/*
    Un programme serveur
 */
public class Server {
    public static final int maxClients = 50;
    public static Sommaire<Integer> playerSummary;
    public static Sommaire<String> dateSummary;
    public static Sommaire<Integer> openingSummary;
    public static int port = 8080;
    public static PrintWriter[] pw;
    public static int numClient = 0;

    public static void main(String[] args) throws Exception {
        // Deserialisation des sommaires
        System.out.print("Deserialization in progress");
        playerSummary = new Sommaire<>("Player_Summary.ser");
        System.out.print(".");
        dateSummary = new Sommaire<>("Date_Summary.ser");
        System.out.print(".");
        openingSummary = new Sommaire<>("Opening_Summary.ser");
        System.out.print(".");
        System.out.println(ConsoleColors.GREEN_BRIGHT + "DONE!" + ConsoleColors.RESET);
        // Démarrage socket serveur avec paramètres personnalisés
        if (args.length != 0) {port = Integer.parseInt(args[0]);};
        pw = new PrintWriter[maxClients];
        ServerSocket s = new ServerSocket(port);

        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "##############################\n" +
                "#    " + ConsoleColors.YELLOW_BOLD + "Starting Server v1.0" + ConsoleColors.YELLOW_BOLD_BRIGHT + "    #\n" +
                "#    " + ConsoleColors.BLACK_BRIGHT + s.getLocalSocketAddress() + ConsoleColors.YELLOW_BOLD_BRIGHT + "    #\n" +
                "##############################" + ConsoleColors.RESET);

        while (numClient < maxClients) {
            Socket soc = s.accept();
            ConnexionClient cc = new ConnexionClient(numClient, soc);
            System.out.println(ConsoleColors.GREEN + "CLIENT CONNECTION" + ConsoleColors.RESET + "(" + numClient + ") => " + soc);
            numClient++;
            cc.start();
        }
    }
}

class ConnexionClient extends Thread {
    private static boolean end;
    private final int id;
    private final Socket s;
    private BufferedReader sisr;
    private PrintWriter sisw;

    private StringTokenizer st; // Permet de decouper les commandes

    public Socket getSocket() {
        return this.s;
    }

    public ConnexionClient(int id, Socket s) {
        this.id = id;
        this.s = s;
        ConnexionClient.end = false;
        try {
            // Ouverture d'un flux de reception
            sisr = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // Ouverture d'un flux d'émission
            sisw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Server.pw[id] = sisw;
    }

    /*
     * Thread pour la gestion des requetes du client
     */
    public void run() {
        sisw.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "##############################\n" +
                "#    " + ConsoleColors.YELLOW_BOLD + "   LiChess  Client      " + ConsoleColors.YELLOW_BOLD_BRIGHT + "#\n" +
                "#      " + ConsoleColors.BLACK_BRIGHT + s.getLocalSocketAddress() + ConsoleColors.YELLOW_BOLD_BRIGHT + "       #\n" +
                "##############################" + ConsoleColors.RESET);
        try {
            while (!end) {
                sisw.println(ConsoleColors.PURPLE_BRIGHT + "Voici les commandes disponibles :\n" + ConsoleColors.RESET + "1. /userparts username -> Voir les parties d'un joueur \n" + "2. /dateparts date -> Voir les parties du dd/mm/aaaa\n" + "3. /mostop -> Ouvertures les plus jouées\n" + "4. /exit -> Se déconnecter");
                // lecture du message
                String str = sisr.readLine();
                if (str.equals("")) {
                    str = "null";
                } // Evite erreur si aucune commande envoyé
                System.out.println(ConsoleColors.PURPLE + "CLIENT(" + id + ") COMMANDE" + ConsoleColors.RESET + " => " + ConsoleColors.WHITE_BRIGHT + str + ConsoleColors.RESET);
                st = new StringTokenizer(str);
                switch (st.nextToken()) {
                    case "/userparts" -> {
                        sisw.println(Server.playerSummary.getPart2(st.nextToken()));
                    }
                    case "/dateparts" -> {
                        sisw.println(Server.dateSummary.getPart2(st.nextToken()));
                    }
                    case "/mostop" -> {
                        sisw.print(Server.openingSummary.toString());
                    }
                    case "/exit" -> {
                        System.out.println(ConsoleColors.RED + "CLIENT DISCONNECTION" + ConsoleColors.RESET + "(" + id + ") =>" + getSocket());
                        ConnexionClient.end = true;
                    }
                    default -> {
                        sisw.println(ConsoleColors.RED + "ERREUR : COMMANDE INCONNUE" + ConsoleColors.RESET);
                    }
                }
            }
            sisr.close();
            sisw.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
