package data.classes;

import java.io.*;
/*
 * Classe pour enregistrer les différentes informations d'une partie
 * @author Thomas Nicolle
 */

public class Part implements Serializable {
    // Attributs
    private long octet;
    private String name;
    private String white;
    private String black;
    private String result;
    private String date;

    private String op;

    // Constructeur
    public Part() {
        this.name = "";
        this.white = "";
        this.black = "";
        this.result = "";
        this.date = "";
        this.op = "";
    }

    // Guetters & Setters
    public long getOctet() {
        return this.octet;
    }

    public void setOctet(long octet) {
        this.octet = octet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    // Méthodes

    public String[] readPart(long octet) throws IOException {
        File fichier = new File("Projet/lichess_db_standard_rated_2014-04.pgn");
        FileInputStream in = new FileInputStream(fichier);
        in.skipNBytes(octet);
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        String[] s = new String[25];
        String ligne = r.readLine();
        String souschaine = "";
        int i = 0;
        while (!souschaine.equals("[Ev")) {
            s[i] = ligne;
            ligne = r.readLine();
            if (!ligne.equals("")) souschaine = ligne.substring(0, 3);
            else souschaine = "";
            i++;
        }
        return s;
    }

    @Override
    public String toString() {
        String out = "\n Partie " + getName() + " effectuée le " + getDate() + ", bytes: " + getOctet();
        out += ", W:" + getWhite() + ", B:" + getBlack() + ", Res: " + getResult() + "\n OP:" + getOp() + "\n \n";
        return out;
    }
}