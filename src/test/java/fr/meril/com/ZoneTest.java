package fr.meril.com;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests unitaires pour la classe Zone.
 * Vérifie l’état initial, la modification et les types de zone.
 */
public class ZoneTest {

    @Test
    void zoneParDefaut(){
        Zone c = new Zone(); assertEquals(TypeZone.PLAINE, c.type);
        assertEquals(0,
                c.nbTresors);
    }

    @Test
    void etatInitial() {
        Zone zone = new Zone();
        assertEquals(TypeZone.PLAINE, zone.type, "Le type par defaut -> PLAINE");
        assertEquals(0, zone.nbTresors, "Le nombre de tresors -> 0");
    }

    @Test
    void modifierTypeZone() {
        Zone zone = new Zone();
        zone.type = TypeZone.MONTAGNE;
        assertEquals(TypeZone.MONTAGNE, zone.type, "Type de zone -> MONTAGNE");
    }

    @Test
    void modifierLeNbTresors() {
        Zone zone = new Zone();
        zone.nbTresors = 3;
        assertEquals(3, zone.nbTresors, "Le nombre de tresors mis a jour -> 3 ");
        zone.nbTresors--;
        assertEquals(2, zone.nbTresors, "Le decrement -> 2 ");
    }

    @Test
    void independanceDesInstances() {
        Zone z1 = new Zone();
        Zone z2 = new Zone();
        z1.nbTresors = 5;
        z2.nbTresors = 0;
        assertNotEquals(z1.nbTresors, z2.nbTresors, "Chaque zone doit avoir son propre compteur de tresors");
    }
}
