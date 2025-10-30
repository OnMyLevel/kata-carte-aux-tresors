package fr.meril.com;

import java.util.List;
import java.util.Map;

public class StatutJeu {
    private final Carte carte;
    private final List<Aventurier> aventuriers;
    private final Map<Position, Aventurier> occupation = new java.util.HashMap<>();

    public StatutJeu(Carte carte, List<Aventurier> aventuriers){
        this.carte = carte;
        this.aventuriers = aventuriers;
        for (Aventurier a : aventuriers) {
            if (occupation.put(a.getPosition(), a) != null) throw new IllegalArgumentException("Deux aventuriers sur la même case initiale: " + a.getPosition());
        }
    }

    public Carte getCarte() {
        return carte;
    }

    public List<Aventurier> getAventuriers() {
        return aventuriers;
    }

    public Map<Position, Aventurier> getOccupation() {
        return occupation;
    }
}
