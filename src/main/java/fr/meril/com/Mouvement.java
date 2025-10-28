package fr.meril.com;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Actions de mouvement : Avancer, Gauche, Droite.
 * */
public enum Mouvement {
    A,G,D;

    /**
     * Parse une chaîne "AGDA" en file d'actions.
     * */
    public static Deque<Mouvement> parserChaineEnDirection(String prog){
        Deque<Mouvement> q = new ArrayDeque<>();
        for (char c : prog.toCharArray()) {
            switch (c) {
                case 'A' -> q.add(Mouvement.A);
                case 'G' -> q.add(Mouvement.G);
                case 'D' -> q.add(Mouvement.D);
                default -> throw new IllegalArgumentException("Action non valide: " + c);
            }
        }
        return q;
    }
}
