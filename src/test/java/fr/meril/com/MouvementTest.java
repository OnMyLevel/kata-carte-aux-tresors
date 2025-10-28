package fr.meril.com;

import org.junit.jupiter.api.Test;

import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MouvementTest {

    @Test void parserProgrammeValide(){
        Deque<Mouvement> q = Mouvement.parserChaineEnDirection("AGDA");
        assertEquals(4, q.size());
        assertEquals(Mouvement.A, q.pollFirst());
        assertEquals(Mouvement.G, q.pollFirst());
        assertEquals(Mouvement.D, q.pollFirst());
        assertEquals(Mouvement.A, q.pollFirst());
    }

    @Test void parserProgrammeInvalide(){
        assertThrows(IllegalArgumentException.class,
                () -> Mouvement.parserChaineEnDirection("AZ"));
    }


}
