package fr.meril.com;

import java.util.ArrayDeque;
import java.util.Deque;

public class Aventurier {

    private final String nom;
    private Position position;
    private Direction direction;
    private Deque<Mouvement> listeDeMouvement;
    private int nbTresorsRamasser;
    private final int ordreApparition;

    public Aventurier(String nom, Position position, Direction direction, Deque<Mouvement> a, int ordreApparition) {
        this.nom = nom;
        this.position = position;
        this.direction = direction;
        this.ordreApparition = ordreApparition;
        this.listeDeMouvement = (a != null) ? new ArrayDeque<>(a) : new ArrayDeque<>();
    }

    public String getNom() { return nom; }

    public Position getPosition() { return position; }
    public void setPosition(Position p) { this.position = p; }

    public Direction getDirection() { return this.direction; }
    public Direction setDirection(Direction direction) {
        this.direction = direction;
        return this.direction;
    }

    public Deque<Mouvement> getListeDeMouvement() { return listeDeMouvement; }
    public void setListeDeMouvement(Deque<Mouvement> listeDeMouvement) { this.listeDeMouvement = listeDeMouvement; }

    public int getNbTresorsRamasser() { return nbTresorsRamasser; }
    public void setNbTresorsRamasser(int nb) { this.nbTresorsRamasser = nb; }

    public int getOrdreApparition() { return ordreApparition; }

    public boolean aEncoreUnMouvement() { return !this.listeDeMouvement.isEmpty(); }
    public Mouvement voirLeProchainMouvement() { return this.listeDeMouvement.peekFirst(); }
    public Mouvement consommer() { return this.listeDeMouvement.pollFirst(); }
}
