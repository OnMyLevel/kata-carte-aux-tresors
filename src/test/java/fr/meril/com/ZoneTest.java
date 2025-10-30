package fr.meril.com;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Zone.
 * Vérifie l’état initial, la modification et les types de zone.
 */
public class ZoneTest {

    @Test
    void constructeurParDefaut() {
        Zone zone = new Zone();
        assertEquals(TypeZone.PLAINE, zone.getType());
        assertEquals(0, zone.getNbTresors());
        assertFalse(zone.contientDesTresors());
    }

    @Test
    void constructeurParametresAvecInitialisation() {
        Zone zone = new Zone(TypeZone.MONTAGNE, 3);
        assertEquals(TypeZone.MONTAGNE, zone.getType());
        assertEquals(3, zone.getNbTresors());
        assertTrue(zone.contientDesTresors());
    }

    @Test
    void constructeurParParametres() {
        Zone zone = new Zone(TypeZone.PLAINE, -2);
        assertEquals(0, zone.getNbTresors());
    }

    @Test
    void testSetters() {
        Zone zone = new Zone();
        zone.setType(TypeZone.MONTAGNE);
        zone.setNbTresors(5);

        assertEquals(TypeZone.MONTAGNE, zone.getType());
        assertEquals(5, zone.getNbTresors());
        assertTrue(zone.contientDesTresors());

        zone.setNbTresors(-3);
        assertEquals(0, zone.getNbTresors());
    }

    @Test
    void ajouterTresors_ajouteSeulementSiPositif() {
        Zone zone = new Zone(TypeZone.PLAINE, 1);
        zone.ajouterTresors(3);
        assertEquals(4, zone.getNbTresors());

        zone.ajouterTresors(0);
        assertEquals(4, zone.getNbTresors());

        zone.ajouterTresors(-5); // ne devrait rien changer
        assertEquals(4, zone.getNbTresors());
    }

    @Test
    void recupererUnTresor() {
        Zone zone = new Zone(TypeZone.PLAINE, 2);

        assertTrue(zone.recupererUnTresor());
        assertEquals(1, zone.getNbTresors());

        assertTrue(zone.recupererUnTresor());
        assertEquals(0, zone.getNbTresors());

        // Plus de trésors => ne doit rien retirer
        assertFalse(zone.recupererUnTresor());
        assertEquals(0, zone.getNbTresors());
    }


    @Test
    void zoneParDefaut(){
        Zone c = new Zone(); assertEquals(TypeZone.PLAINE, c.getType());
        assertEquals(0,
                c.getNbTresors());
    }

    @Test
    void etatInitial() {
        Zone zone = new Zone();
        assertEquals(TypeZone.PLAINE, zone.getType(), "Le type par defaut -> PLAINE");
        assertEquals(0, zone.getNbTresors(), "Le nombre de tresors -> 0");
    }

    @Test
    void modifierTypeZone() {
        Zone zone = new Zone();
        zone.setType(TypeZone.MONTAGNE);
        assertEquals(TypeZone.MONTAGNE, zone.getType(), "Type de zone -> MONTAGNE");
    }

    @Test
    void modifierLeNbTresors() {
        Zone zone = new Zone();
        zone.setNbTresors(3);
        assertEquals(3, zone.getNbTresors(), "Le nombre de tresors mis a jour -> 3 ");
        zone.setNbTresors(zone.getNbTresors()-1);
        assertEquals(2, zone.getNbTresors(), "Le decrement -> 2 ");
    }

    @Test
    void independanceDesInstances() {
        Zone z1 = new Zone();
        Zone z2 = new Zone();
        z1.setNbTresors(5);
        z2.setNbTresors(0);
        assertNotEquals(z1.getNbTresors(), z2.getNbTresors(), "Chaque zone doit avoir son propre compteur de tresors");
    }
}
