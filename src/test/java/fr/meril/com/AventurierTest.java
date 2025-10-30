package fr.meril.com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;

public class AventurierTest {
    private Aventurier aventurier;
    private Position position;

    @BeforeEach
    void setUp() {
        position = new Position(1, 2);
        aventurier = new Aventurier("Lara", position, Direction.N, Mouvement.parseDeChaineDinstructionDeDirection("A"), 3);
    }

    @Test
    void constructeurEtGetter() {
        assertEquals("Lara", aventurier.getNom());
        assertSame(position, aventurier.getPosition());
        assertEquals(Direction.N, aventurier.getDirection());
        assertEquals(3, aventurier.getOrdreApparition());
        assertEquals(0, aventurier.getNbTresorsRamasser(), "Par défaut, 0 trésor ramassé.");
    }

    @Test
    void setListeDeMouvement() {
        Deque<Mouvement> file = new ArrayDeque<>();
        file.add(Mouvement.A);
        file.add(Mouvement.D);
        file.add(Mouvement.G);

        aventurier.setListeDeMouvement(file);

        assertTrue(aventurier.aEncoreUnMouvement());
        assertEquals(Mouvement.A, aventurier.voirLeProchainMouvement(), "Peek ne consomme pas.");
        assertEquals(Mouvement.A, aventurier.consommer(), "Poll consomme l’élément en tête.");
        assertEquals(Mouvement.D, aventurier.voirLeProchainMouvement());
        assertEquals(Mouvement.D, aventurier.consommer());
        assertEquals(Mouvement.G, aventurier.consommer());

        // Une fois vide
        assertFalse(aventurier.aEncoreUnMouvement());
        assertNull(aventurier.voirLeProchainMouvement(), "peekFirst() retourne null quand vide.");
        assertNull(aventurier.consommer(), "pollFirst() retourne null quand vide.");
    }

    @Test
    void gestionNbTresorsRamasser() {
        assertEquals(0, aventurier.getNbTresorsRamasser());
        aventurier.setNbTresorsRamasser(2);
        assertEquals(2, aventurier.getNbTresorsRamasser());
        aventurier.setNbTresorsRamasser(0);
        assertEquals(0, aventurier.getNbTresorsRamasser());
    }

}
