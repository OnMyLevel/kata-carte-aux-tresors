package fr.meril.com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarteTest {
    private Carte carte;

    @BeforeEach
    void setUp() {
        // Carte 3x4
        carte = new Carte(3, 4);
    }

    @Test
    void testInitialisationGrille() {
        assertEquals(3, carte.getLargeur());
        assertEquals(4, carte.getHauteur());
        for (int y = 0; y < carte.getHauteur(); y++) {
            for (int x = 0; x < carte.getLargeur(); x++) {
                assertNotNull(carte.getGrille()[y][x], "La case (" + x + "," + y + ") devrait être instanciée.");
            }
        }
    }

    @Test
    void testBornesOk() {
        assertTrue(carte.bornesOk(new Position(0, 0)));
        assertTrue(carte.bornesOk(new Position(2, 3))); // coin opposé (largeur-1, hauteur-1)

        assertFalse(carte.bornesOk(new Position(-1, 0)));
        assertFalse(carte.bornesOk(new Position(0, -1)));
        assertFalse(carte.bornesOk(new Position(3, 0))); // x == largeur
        assertFalse(carte.bornesOk(new Position(0, 4))); // y == hauteur
    }

    @Test
    void testDefinirUneMontagneEtVerification() {
        Position p = new Position(1, 2);
        assertFalse(carte.estUneZoneMontagne(p), "Par défaut, ce n’est pas une montagne.");
        int tailleAvant = carte.getMontagnes().size();

        carte.definirUneMontagne(p);

        assertTrue(carte.estUneZoneMontagne(p), "Après définition, la case doit être une montagne.");
        assertEquals(tailleAvant + 1, carte.getMontagnes().size(), "La liste des montagnes doit être mise à jour.");
        assertTrue(carte.getMontagnes().contains(p), "La position ajoutée doit être présente dans la liste montagnes.");
    }

    @Test
    void testEstUneZoneMontagneFalseParDefaut() {
        assertFalse(carte.estUneZoneMontagne(new Position(0, 0)));
    }

    @Test
    void testAjoutEtLectureTresors() {
        Position p = new Position(0, 0);
        assertEquals(0, carte.getTresors(p));

        carte.ajoutDeTresors(p, 3);
        assertEquals(3, carte.getTresors(p));
    }

    @Test
    void testRecupererUnTresorSiPossible() {

        Position p = new Position(1, 1);
        carte.ajoutDeTresors(p, 2);
        assertEquals(2, carte.getTresors(p));

        assertTrue(carte.recupererUnTresorSiPossible(p));
        assertEquals(1, carte.getTresors(p));
        assertTrue(carte.recupererUnTresorSiPossible(p));
        assertEquals(0, carte.getTresors(p));

        assertFalse(carte.recupererUnTresorSiPossible(p));
        assertEquals(0, carte.getTresors(p));
    }

    @Test
    void testAffichageDeTresorsPourSortie() {

        Position p1 = new Position(1, 1); // 2 trésors
        Position p2 = new Position(0, 2); // 1 trésor
        carte.ajoutDeTresors(p1, 2);
        carte.ajoutDeTresors(p2, 1);

        List<String> sortie = carte.affichageDeTresorsPourSortie();

        assertEquals(2, sortie.size());
        assertEquals("T - 1 - 1 - 2", sortie.get(0));
        assertEquals("T - 0 - 2 - 1", sortie.get(1));
    }
}
