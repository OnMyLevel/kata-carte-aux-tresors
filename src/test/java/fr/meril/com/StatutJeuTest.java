package fr.meril.com;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StatutJeuTest {

    private Aventurier av1(String nom, int x, int y, int ordre) {
        return new Aventurier(nom, new Position(x, y), Direction.N, Mouvement.parseDeChaineDinstructionDeDirection("A"), ordre);
    }

    private Aventurier av2(String nom, int x, int y, int ordre) {
        return new Aventurier(nom, new Position(x, y), Direction.N, Mouvement.parseDeChaineDinstructionDeDirection("A"), ordre);
    }

    @Test
    void constructeurEtAlimentation() {
        Carte carte = new Carte(3, 3);
        Aventurier a1 = av1("Lara", 0, 0, 1);
        Aventurier a2 = av2("Indy", 1, 2, 2);

        List<Aventurier> liste = Arrays.asList(a1, a2);
        StatutJeu sj = new StatutJeu(carte, liste);

        // Références
        assertSame(carte, sj.getCarte());
        assertSame(liste, sj.getAventuriers());

        // Occupation
        assertEquals(2, sj.getOccupation().size());
        assertTrue(sj.getOccupation().containsKey(a1.getPosition()));
        assertTrue(sj.getOccupation().containsKey(a2.getPosition()));
        assertEquals(a1, sj.getOccupation().get(a1.getPosition()));
        assertEquals(a2, sj.getOccupation().get(a2.getPosition()));
    }

    @Test
    void constructeurAccepteListeVide() {
        Carte carte = new Carte(2, 2);
        StatutJeu sj = new StatutJeu(carte, Collections.emptyList());

        assertNotNull(sj.getOccupation());
        assertTrue(sj.getOccupation().isEmpty());
        assertSame(carte, sj.getCarte());
        assertTrue(sj.getAventuriers().isEmpty());
    }

    @Test
    void constructeurErreurSiDeuxAventuriersSurLaMemeCase() {
        Carte carte = new Carte(3, 3);
        Aventurier a1 = av1("A", 1, 1, 1);
        Aventurier a2 = av2("B", 1, 1, 2); // même position

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new StatutJeu(carte, Arrays.asList(a1, a2))
        );
        assertTrue(ex.getMessage().contains("Deux aventuriers sur la même case"),
                "Le message d'erreur devrait évoquer le conflit de position.");
    }

    @Test
    void constructeurAvecUnNullPourListe() {
        Carte carte = new Carte(2, 2);
        assertThrows(NullPointerException.class, () -> new StatutJeu(carte, null),
                "Le for-each sur une liste nulle devrait lever un NPE (comportement actuel).");
    }

}
