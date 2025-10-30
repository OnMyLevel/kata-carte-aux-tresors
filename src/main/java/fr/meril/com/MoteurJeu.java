package fr.meril.com;

import java.util.*;

/** Moteur tour-par-tour sans politique : rotations, intentions d'avance, collisions, ramassage à l'entrée uniquement. */
public final class MoteurJeu {

    private record Intention(Aventurier a, Position cible) {}

    public void executer(StatutJeu etat) {
        while (auMoinsUnPeutEncoreBouger(etat)) {
            appliquerRotations(etat);
            List<Intention> intentions = collecterIntentionsAvance(etat);
            resoudreIntentions(etat, intentions);
        }
    }

    /* =====================  Étapes du tour  ===================== */

    private boolean auMoinsUnPeutEncoreBouger(StatutJeu etat) {
        return etat.aventuriers.stream().anyMatch(Aventurier::aEncoreUnMouvement);
    }

    /** Étape 1 : appliquer toutes les rotations G/D (et consommer l'action) */
    private void appliquerRotations(StatutJeu etat) {
        for (Aventurier a : etat.aventuriers) {
            if (!a.aEncoreUnMouvement()) continue;
            Mouvement next = a.voirLeProchainMouvement();
            if (next == Mouvement.G || next == Mouvement.D) {
                a.consommer();
                var dir = a.getDirection();
                a.setDirection(next == Mouvement.G ? dir.tournerAGauche() : dir.tournerADroite());
            }
        }
    }

    /** Étape 2 : collecter les intentions d'avancer (A) sans les exécuter */
    private List<Intention> collecterIntentionsAvance(StatutJeu etat) {
        List<Intention> intentions = new ArrayList<>();
        for (Aventurier a : etat.aventuriers) {
            if (!a.aEncoreUnMouvement()) continue;
            if (a.voirLeProchainMouvement() == Mouvement.A) {
                var d = a.getDirection();
                Position cible = a.getPosition().translation(d.dX(), d.dY());
                intentions.add(new Intention(a, cible));
            }
        }
        return intentions;
    }

    /** Étape 3 : résoudre les intentions par ordre d'apparition, gérer collisions & ramassage */
    private void resoudreIntentions(StatutJeu etat, List<Intention> intentions) {
        intentions.sort(Comparator.comparingInt(i -> i.a.getOrdreApparition()));

        Set<Position> ciblesReservees = new HashSet<>();
        for (Intention in : intentions) {
            Aventurier a = in.a();
            a.consommer(); // consomme toujours l'action A
            Position cible = in.cible();

            if (!deplacementValide(etat, cible, ciblesReservees)) continue;

            deplacer(etat, a, cible);
            ciblesReservees.add(cible);

            ramasserSiPossible(etat, a);
        }
    }

    /* =====================  Utilitaires  ===================== */

    private boolean deplacementValide(StatutJeu etat, Position cible, Set<Position> ciblesReservees) {
        if (!etat.carte.dansLesBornes(cible)) return false;
        if (etat.carte.estUneZoneMontagne(cible)) return false;
        if (etat.occupation.containsKey(cible)) return false;
        if (ciblesReservees.contains(cible)) return false;
        return true;
    }

    private void deplacer(StatutJeu etat, Aventurier a, Position cible) {
        etat.occupation.remove(a.getPosition());
        a.setPosition(cible);
        etat.occupation.put(a.getPosition(), a);
    }

    private void ramasserSiPossible(StatutJeu etat, Aventurier a) {
        if (etat.carte.recupererUnTresorSiPossible(a.getPosition())) {
            a.setNbTresorsRamasser(a.getNbTresorsRamasser() + 1);
        }
    }
}
