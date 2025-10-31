package fr.meril.com;

import java.util.*;

public final class MoteurJeu {

    private record Intention(Aventurier a, Position cible) {}

    public void executerJeuTourParTour(StatutJeu etat) {
        int tour = 0;
        while (auMoinsUnJoueurPeutEncoreBouger(etat)) {
            tour++;
            appliquerRotations(etat);
            List<Intention> intentions = collecterIntentionsAvance(etat);
            resoudreIntentions(etat, intentions);
        }
    }

    private boolean auMoinsUnJoueurPeutEncoreBouger(StatutJeu etat) {
        return etat.getAventuriers().stream().anyMatch(Aventurier::aEncoreUnMouvement);
    }

    private void appliquerRotations(StatutJeu etat) {
        for (Aventurier a : etat.getAventuriers()) {
            if (!a.aEncoreUnMouvement()) continue;
            Mouvement next = a.voirLeProchainMouvement();
            if (next == Mouvement.G || next == Mouvement.D) {
                a.consommer();
                var dir = a.getDirection();
                a.setDirection(next ==
                        Mouvement.G ? dir.tournerAGauche() : dir.tournerADroite());
            }
        }
    }

    private List<Intention> collecterIntentionsAvance(StatutJeu etat) {
        List<Intention> intentions = new ArrayList<>();
        for (Aventurier a : etat.getAventuriers()) {
            if (!a.aEncoreUnMouvement()) continue;
            if (a.voirLeProchainMouvement() == Mouvement.A) {
                var d = a.getDirection();
                Position cible = a.getPosition().translation(d.dX(), d.dY());
                intentions.add(new Intention(a, cible));
            }
        }
        return intentions;
    }

    private void resoudreIntentions(StatutJeu etat, List<Intention> intentions) {
        intentions.sort(Comparator.comparingInt(i -> i.a.getOrdreApparition()));

        Set<Position> ciblesReservees = new HashSet<>();
        for (Intention in : intentions) {
            Aventurier a = in.a();
            a.consommer(); // consomme toujours l'action A
            Position cible = in.cible();

            if (!deplacementValide(etat, cible, ciblesReservees))
                continue;

            deplacer(etat, a, cible);
            ciblesReservees.add(cible);
            ramasserSiPossible(etat, a);
        }
    }

    private boolean deplacementValide(StatutJeu etat, Position cible, Set<Position> ciblesReservees) {
        if (!etat.getCarte().dansLesBornes(cible)) return false;
        if (etat.getCarte().estUneZoneMontagne(cible)) return false;
        if (etat.getOccupation().containsKey(cible)) return false;
        return !ciblesReservees.contains(cible);
    }

    private void deplacer(StatutJeu etat, Aventurier a, Position cible) {
        etat.getOccupation().remove(a.getPosition());
        a.setPosition(cible);
        etat.getOccupation().put(a.getPosition(), a);
    }

    private void ramasserSiPossible(StatutJeu etat, Aventurier a) {
        if (etat.getCarte().recupererUnTresorSiPossible(a.getPosition())) {
            a.setNbTresorsRamasser(a.getNbTresorsRamasser() + 1);
        }
    }

}
