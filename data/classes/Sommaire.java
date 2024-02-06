package data.classes;

import java.io.*;
import java.util.Hashtable;

/**
 * Classe contenant un type paramétré permettant de définir le type que la clé utilisée doit avoir
 * uniquement au moment de l'instanciation.
 * Cela permet d'avoir une classe commune, quelque soit le type utilisé pour la clé, et ne pas avoir
 * à réimplémenter la classe Annuaire à chaque fois qu'un nouveau type est nécessaire.
 *
 * @author Thomas Nicolle
 */
public class Sommaire<T> implements Serializable {
    private Hashtable<String, Hashtable<T, Part>> sommaire;
    // Constructeur standard
    public Sommaire() {
        this.sommaire = new Hashtable<String, Hashtable<T, Part>>();
    }

    // Constructeur depuis fichier contenant un sommaire sérialisé
    public Sommaire(String file) {
        try {
            FileInputStream src = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(src);
            this.sommaire = (Hashtable<String, Hashtable<T, Part>>) in.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Méthode permettant d'ajouter une partie dans le sommaire en fct de son ouverture/date et du numero de la partie/heure.
    public void addPart(String opening, Part game, T numPart) {
        if (this.sommaire.containsKey(opening)) {
            Hashtable<T, Part> openingSummary = this.sommaire.get(opening);
            openingSummary.put(numPart, game);
            this.sommaire.put(opening, openingSummary);
        } else {
            Hashtable<T, Part> dateSummary = new Hashtable<T, Part>();
            dateSummary.put(numPart, game);
            this.sommaire.put(opening, dateSummary);
        }
    }

    /**
     * Méthode permettant de retrouver une partie dans le sommaire en fonction de son ouverture et du numero partie.
     *
     * @return La partie si elle existe, null autrement
     */

    public Part getPart(String date, T numPart) {
        if (this.sommaire.containsKey(date)) {
            if (this.sommaire.get(date).containsKey(numPart)) {
                return this.sommaire.get(date).get(numPart);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Hashtable<T, data.classes.Part> getPart2(String date) {
        return this.sommaire.get(date);
    }

    // Methode permettant de sérialiser le sommaire dans le fichier donné en paramètre.

    public void write(String file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.sommaire);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getSize() { // Methode pour recuperer taille sommaire
        return this.sommaire.size();
    }

    public int getSizeSElement(String SElement) {// Methode pour recuperer taille sous-sommaire
        return this.sommaire.get(SElement).size();
    }

    public String toString() {
        return this.sommaire.toString();
    }
}