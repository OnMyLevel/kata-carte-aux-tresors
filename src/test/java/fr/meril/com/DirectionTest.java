package fr.meril.com;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le fonctionnement de l'énumération Direction.
 */
class DirectionTest {

    @Test
    void tournerAGaucheCycleComplet() {
        assertEquals(Direction.O, Direction.N.tournerAGauche(), "N -> O");
        assertEquals(Direction.S, Direction.O.tournerAGauche(), "O -> S");
        assertEquals(Direction.E, Direction.S.tournerAGauche(), "S -> E");
        assertEquals(Direction.N, Direction.E.tournerAGauche(), "E -> N");
    }

    @Test
    void tournerADroiteCycleComplet() {
        assertEquals(Direction.E, Direction.N.tournerADroite(), "N -> E");
        assertEquals(Direction.S, Direction.E.tournerADroite(), "E -> S");
        assertEquals(Direction.O, Direction.S.tournerADroite(), "S -> O");
        assertEquals(Direction.N, Direction.O.tournerADroite(), "O -> N");
    }

    @Test
    void rotationEtRetourPointDepart() {
        for (Direction d : Direction.values()) {
            Direction g = d.tournerAGauche().tournerAGauche().tournerAGauche().tournerAGauche();
            Direction dr = d.tournerADroite().tournerADroite().tournerADroite().tournerADroite();
            assertEquals(d, g, "4x gauche depuis " + d);
            assertEquals(d, dr, "4x droite depuis " + d);
        }
    }

    @Test
    void coherenceSurLesRotationsInverse() {
        for (Direction d : Direction.values()) {
            assertEquals(d, d.tournerAGauche().tournerADroite(), "G puis D depuis " + d);
            assertEquals(d, d.tournerADroite().tournerAGauche(), "D puis G depuis " + d);
        }
    }

    @Test
    void dXParDirection() {
        assertEquals(0, Direction.N.dX(), "dx(N) doit être 0");
        assertEquals(0, Direction.S.dX(), "dx(S) doit être 0");
        assertEquals(1, Direction.E.dX(), "dx(E) doit être 1");
        assertEquals(-1, Direction.O.dX(), "dx(O) doit être -1");
    }

    @Test
    void dYParDirection() {
        assertEquals(-1, Direction.N.dY(), "dy(N) doit être -1");
        assertEquals(1, Direction.S.dY(), "dy(S) doit être 1");
        assertEquals(0, Direction.E.dY(), "dy(E) doit être 0");
        assertEquals(0, Direction.O.dY(), "dy(O) doit être 0");
    }
}

