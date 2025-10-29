package fr.meril.com;

import org.junit.jupiter.api.Test;

import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MouvementTest {

    @Test void parseDeChaineDinstructionDeDirectionValide(){
        Deque<Mouvement> q = Mouvement.parseDeChaineDinstructionDeDirection("AGDA");
        assertEquals(4, q.size());
        assertEquals(Mouvement.A, q.pollFirst());
        assertEquals(Mouvement.G, q.pollFirst());
        assertEquals(Mouvement.D, q.pollFirst());
        assertEquals(Mouvement.A, q.pollFirst());
    }

    @Test void parseDeChaineDinstructionDeDirectionInvalide(){
        assertThrows(IllegalArgumentException.class,
                () -> Mouvement.parseDeChaineDinstructionDeDirection("AZ"));
    }


}
