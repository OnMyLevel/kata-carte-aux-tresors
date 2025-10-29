package fr.meril.com;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Position (valeur immuable).
 */
class PositionTest {

    @Test

    void constructionEtAccess() {
        Position p = new Position(3, 5);
        assertEquals(3, p.x);
        assertEquals(5, p.y);
    }

    @Test
    void translationImmutabilite() {
        Position p = new Position(1, 2);
        Position q = p.translation(3, -1);

        assertEquals(1, p.x);
        assertEquals(2, p.y);

        assertEquals(new Position(4, 1), q);
        assertNotSame(p, q, "translate doit creer une nouvelle instance");
    }

    @Test
    void equalsContratAvecABCD() {
        Position a = new Position(2, 7);
        Position b = new Position(2, 7);
        Position c = new Position(2, 7);
        Position d = new Position(2, 8);

        assertEquals(a, a);
        assertEquals(b, a);
        assertEquals(b, c);
        assertEquals(a, c);
        assertNotEquals(a, d);
        assertNotEquals(null, a);
        assertNotEquals("2,7", a);
    }

    @Test
    void hashCodeCohérentAvecEquals() {
        Position a = new Position(0, 0);
        Position b = new Position(0, 0);
        Position c = new Position(0, 1);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode(), "hashCode doit être identique pour des objets égaux");
        assertNotEquals(a, c);
    }


    @Test
    void toStringFormat() {
        assertEquals("3,5",
                new Position(3, 5).toString());
        assertEquals("-1,0",
                new Position(-1, 0).toString());
    }

    @Test
    void casLimites() {
        Position pNeg = new Position(-10, -20);
        assertEquals(-10, pNeg.x);
        assertEquals(-20, pNeg.y);

        Position pLarge = new Position(Integer.MAX_VALUE, Integer.MIN_VALUE);
        assertEquals(Integer.MAX_VALUE, pLarge.x);
        assertEquals(Integer.MIN_VALUE, pLarge.y);

        Position p = new Position(7, 8);
        Position q = p.translation(0, 0);
        assertEquals(p, q);
    }
}
