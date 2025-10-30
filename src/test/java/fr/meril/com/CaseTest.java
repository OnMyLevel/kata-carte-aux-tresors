package fr.meril.com;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CaseTest {

    @Test
    void constructeurParDefaut() {
        Case c = new Case();
        assertEquals(TypeZone.PLAINE, c.getType());
        assertEquals(0, c.getTresors());
        assertFalse(c.aDesTresors());
    }

    @Test
    void constructeurViaParametres() {
        Case c = new Case(TypeZone.MONTAGNE, 3);
        assertEquals(TypeZone.MONTAGNE, c.getType());
        assertEquals(3, c.getTresors());
        assertTrue(c.aDesTresors());
    }

    @Test
    void gettersEtSetters() {
        Case c = new Case();

        c.setType(TypeZone.MONTAGNE);
        c.setTresors(5);

        assertEquals(TypeZone.MONTAGNE, c.getType());
        assertEquals(5, c.getTresors());
        assertTrue(c.aDesTresors());

        // Remettre à zéro
        c.setTresors(0);
        assertEquals(0, c.getTresors());
        assertFalse(c.aDesTresors());
    }

    @Test
    void tresorsNegatifs() {
        // Le design actuel n'interdit pas les valeurs négatives
        Case c = new Case(TypeZone.PLAINE, -1);
        assertEquals(-1, c.getTresors());
        assertFalse(c.aDesTresors()); // -1 n'est pas considéré comme "a des trésors"
    }
}
