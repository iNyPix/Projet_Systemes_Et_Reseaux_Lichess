package data.classes;

import java.io.*;
/**
 * La classe Player doit implémenter l'interface Serializable afin que l'annuaire
 * puisse être sérialisé.
 *
 * @author Thomas Nicolle
 */
public class Player implements Serializable {
    private String pseudo;

    public Player(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public String toString() {
        return this.getPseudo();
    }
}