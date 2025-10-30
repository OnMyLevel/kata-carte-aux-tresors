package fr.meril.com;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoteurJeuTest {

    private Deque<Mouvement> seq(Mouvement... m) {
        ArrayDeque<Mouvement> q = new ArrayDeque<>();
        q.addAll(Arrays.asList(m));
        return q;
    }

    @Test
    void rotationsEtAvance_enchainementSimple() {
        Carte carte = new Carte(3, 3);
        Aventurier a = new Aventurier(
                "Lara",
                new Position(1, 1),
                Direction.N,
                seq(Mouvement.G, Mouvement.A, Mouvement.D, Mouvement.A),
                1
        );
        StatutJeu etat = new StatutJeu(carte, List.of(a));

        new MoteurJeu().executerJeuTourParTour(etat);
        assertEquals(new Position(0, 0), a.getPosition());
    }

    @Test
    void avanceBloqueeParBordDeCarte() {
        Carte carte = new Carte(2, 2);
        Aventurier a = new Aventurier(
                "Indy",
                new Position(0, 0),
                Direction.O,
                seq(Mouvement.A),
                1
        );
        StatutJeu etat = new StatutJeu(carte, List.of(a));

        new MoteurJeu().executerJeuTourParTour(etat);
        assertEquals(new Position(0, 0), a.getPosition());
    }

    @Test
    void avanceBloqueeParMontagne() {
        Carte carte = new Carte(3, 1);
        carte.definirUneMontagne(new Position(1, 0));

        Aventurier a = new Aventurier(
                "Indy",
                new Position(0, 0),
                Direction.E,
                seq(Mouvement.A),
                1
        );

        StatutJeu etat = new StatutJeu(carte, List.of(a));
    }

    @Test
    void sequenceDeMouvementsComplexe() {
        // Grille 5x3, indices x:0..4, y:0..2
        Carte carte = new Carte(5, 3);
        // Montagne devant le départ pour tester un A bloqué
        carte.definirUneMontagne(new Position(2, 0));

        Aventurier a = new Aventurier(
                "Croft",
                new Position(2, 1),   // centre vertical
                Direction.N,
                // A (bloqué par montagne), D, A, A, G, A, A (dernier A bloqué par bord haut)
                seq(Mouvement.A, Mouvement.D, Mouvement.A, Mouvement.A, Mouvement.G, Mouvement.A, Mouvement.A),
                1
        );

        StatutJeu etat = new StatutJeu(carte, List.of(a));

        new MoteurJeu().executerJeuTourParTour(etat);

        assertEquals(new Position(4, 0), a.getPosition(), "La position finale doit être (4,0)");
        assertEquals(Direction.N, a.getDirection(), "Direction finale attendue : Nord");
        assertFalse(a.aEncoreUnMouvement(), "Tous les mouvements doivent être consommés");
    }


    @Test
    void collisionMemeCiblePrioriteParOrdreApparition() {
        // Deux aventuriers veulent entrer en (1,0) depuis (0,0) et (2,0)
        Carte carte = new Carte(3, 1);
        Aventurier a1 = new Aventurier(
                "A1",
                new Position(0, 0),
                Direction.E,
                seq(Mouvement.A),
                1 // apparaît en premier
        );
        Aventurier a2 = new Aventurier(
                "A2",
                new Position(2, 0),
                Direction.O,
                seq(Mouvement.A),
                2 // apparaît après
        );
        StatutJeu etat = new StatutJeu(carte, List.of(a1, a2));

        new MoteurJeu().executerJeuTourParTour(etat);

        assertEquals(new Position(1, 0), a1.getPosition());
        assertEquals(new Position(2, 0), a2.getPosition());
    }

    @Test
    void deplacementBloqueSiCibleDejaOccuper() {
        Carte carte = new Carte(3, 1);
        Aventurier statique = new Aventurier(
                "Guard",
                new Position(1, 0),
                Direction.N,
                seq(),    // ne bouge pas
                1
        );
        Aventurier mobile = new Aventurier(
                "Runner",
                new Position(0, 0),
                Direction.E,
                seq(Mouvement.A), // veut aller en (1,0) mais c'est occupé
                2
        );
        StatutJeu etat = new StatutJeu(carte, List.of(statique, mobile));

        new MoteurJeu().executerJeuTourParTour(etat);

        assertEquals(new Position(1, 0), statique.getPosition(), "Le garde ne bouge pas");
        assertEquals(new Position(0, 0), mobile.getPosition(), "Le déplacement vers une case occupée est refusé");
    }

    @Test
    void rotationSansDeplacement() {
        Carte carte = new Carte(3, 3);
        Aventurier a = new Aventurier(
                "Spin",
                new Position(1, 1),
                Direction.N,
                seq(Mouvement.G, Mouvement.G, Mouvement.G, Mouvement.G), // tour complet
                1
        );
        StatutJeu etat = new StatutJeu(carte, List.of(a));

        new MoteurJeu().executerJeuTourParTour(etat);

        assertEquals(new Position(1, 1), a.getPosition());
        assertEquals(Direction.N, a.getDirection());
    }

    @Test
    void intentionsSurPlusieursTours() {
        Carte carte = new Carte(5, 1);
        Aventurier a1 = new Aventurier(
                "A1",
                new Position(0, 0),
                Direction.E,
                seq(Mouvement.A, Mouvement.A),
                1
        );
        StatutJeu etat = new StatutJeu(carte, List.of(a1));

        new MoteurJeu().executerJeuTourParTour(etat);

        assertEquals(new Position(2, 0), a1.getPosition());
        assertFalse(a1.aEncoreUnMouvement(), "La séquence doit être entièrement consommée");
    }

}