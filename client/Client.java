package client;

import server.ConsoleColors;

import java.io.*;
import java.net.*;

public class Client {
    static boolean arreter = false;
    static int port = 8080;
    static String ip = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }// Si un adresse ip et un port est specifié alors on les prends comme valeur

        Socket socket = new Socket(ip, port);
        System.out.println("Connexion au serveur..." + ConsoleColors.GREEN_BRIGHT + "réussite!" + ConsoleColors.RESET);

        // Illustration des capacites bidirectionnelles du flux:
        // Ouverture d'un flux de reception
        BufferedReader sisr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Ouverture d'un flux d'émission
        PrintWriter sisw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        GererSaisie saisie = new GererSaisie(sisw);
        saisie.start();

        String str;
        while (!arreter) { // Afficher les données reçu du serveur
            str = sisr.readLine();
            if (str != null) System.out.println(str); // Evite boucle infinie si serveur se ferme pdt que client ouvert
            else arreter = true;
        }
        System.out.println("Déconnexion réussite!");
        sisr.close();
        sisw.close();
        socket.close();
    }
}

class GererSaisie extends Thread {
    private BufferedReader entreeClavier;
    private PrintWriter pw;

    public GererSaisie(PrintWriter pw) {
        entreeClavier = new BufferedReader(new InputStreamReader(System.in));
        this.pw = pw;
    }

    public void run() {
        String str;
        try {
            do {
                str = entreeClavier.readLine();
                pw.println(str);
            } while (!str.equals("/exit"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Client.arreter = true;
    }
}