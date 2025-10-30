package fr.meril.com;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EcritureDuResumerTest {

    @TempDir
    Path tmp;

    @Test
    void genereFichierAvecCarteMontagneTresorEtAventurier() throws IOException {

        Carte carte = new Carte(3, 4);
        carte.definirUneMontagne(new Position(1, 0));
        carte.definirUneMontagne(new Position(2, 1));
        carte.ajoutDeTresors(new Position(0, 3), 2);
        carte.ajoutDeTresors(new Position(1, 3), 3);

        Aventurier a = new Aventurier(
                "Lara",
                new Position(1, 1),
                Direction.S,
                new java.util.ArrayDeque<>(),
                0
        );

        a.setNbTresorsRamasser(5);
        StatutJeu etat = new StatutJeu(carte, List.of(a));

        Path fichierSortie = tmp.resolve("resume.txt");
        new EcritureDuResumer().ecrireJeu(etat, fichierSortie);

        assertTrue(Files.exists(fichierSortie), "Le fichier résumé doit être créé");
        List<String> lignes = Files.readAllLines(fichierSortie);

        // attendu :
        // C - 3 - 4
        // M - 1 - 0
        // M - 2 - 1
        // T - 0 - 3 - 2
        // T - 1 - 3 - 3
        // A - Lara - 1 - 1 - S - 5

        assertEquals("C - 3 - 4", lignes.get(0));
        assertTrue(lignes.contains("M - 1 - 0"));
        assertTrue(lignes.contains("M - 2 - 1"));
        assertTrue(lignes.contains("T - 0 - 3 - 2"));
        assertTrue(lignes.contains("T - 1 - 3 - 3"));
        assertTrue(lignes.contains("A - Lara - 1 - 1 - S - 5"));
    }

    @Test
    void creeLeRepertoireManquant() throws IOException {
        Carte carte = new Carte(2, 2);
        StatutJeu etat = new StatutJeu(carte, List.of());

        Path dossier = tmp.resolve("sorties/sous/repertoire");
        Path fichierSortie = dossier.resolve("etat.txt");
        new EcritureDuResumer().ecrireJeu(etat, fichierSortie);

        assertTrue(Files.exists(fichierSortie), "Le fichier doit être créé même si les dossiers n’existent pas");
    }

    @Test
    void fichierContientUniquementLignesNonVides() throws IOException {

        Carte carte = new Carte(1, 1);
        StatutJeu etat = new StatutJeu(carte, List.of());
        Path fichierSortie = tmp.resolve("vide.txt");

        new EcritureDuResumer().ecrireJeu(etat, fichierSortie);
        List<String> lignes = Files.readAllLines(fichierSortie);

        assertFalse(lignes.isEmpty());
        assertTrue(lignes.get(0).startsWith("C -"), "La première ligne doit être la carte");
    }

    @Test void formatMinimal() throws Exception {
        Carte c = new Carte(1,1);
        StatutJeu  e = new StatutJeu(c, java.util.List.of());
        Path out = Paths.get("target/test-sortie-min.txt");
        new EcritureDuResumer().ecrireJeu(e, out);
        List<String> lignes = Files.readAllLines(out);
        assertTrue(lignes.get(0).startsWith("C - 1 - 1"));
    }

    @Test
    void casExempleDeLenoncer() throws IOException {

        String input = """
                # commentaire (doit être ignoré)
                C - 3 - 4
                M - 1 - 0
                M - 2 - 1
                T - 0 - 3 - 2
                T - 1 - 3 - 3
                A - Lara - 1 - 1 - S - AADADAGGA
                """;

        Path entree = tmp.resolve("entree.txt");
        Files.writeString(entree, input, StandardCharsets.UTF_8);

        ParseurFichierEntree parseur = new ParseurFichierEntree();
        StatutJeu etat = parseur.analyserLeFichierTxt(entree);
        new MoteurJeu().executerJeuTourParTour(etat);

        Path sortie = tmp.resolve("out/resume.txt");
        new EcritureDuResumer().ecrireJeu(etat, sortie);

        assertTrue(Files.exists(sortie),
                "Le fichier de sortie doit être créé");
        List<String> lignes = Files.readAllLines(sortie, StandardCharsets.UTF_8);
        assertFalse(lignes.isEmpty(),
                "Le fichier de sortie ne doit pas être vide");
        assertEquals("C - 3 - 4", lignes.get(0), "La ligne carte doit être en tête et correcte");
        assertTrue(lignes.contains("M - 1 - 0"),
                "La montagne (1,0) doit être présente");
        assertTrue(lignes.contains("M - 2 - 1"),
                "La montagne (2,1) doit être présente");
        assertTrue(lignes.contains("T - 1 - 3 - 2"),
                "Il doit rester 2 trésors en (1,3)");
        assertTrue(lignes.stream().noneMatch(s -> s.startsWith("T - 0 - 3 -")),
                "Il ne doit plus rester de trésor en (0,3)");
        assertTrue(lignes.contains("A - Lara - 0 - 3 - S - 3"),
                "Lara doit finir en (0,3), orientation S, avec 3 trésors ramassés");
        assertTrue(lignes.stream().noneMatch(String::isBlank),
                "Aucune ligne de sortie ne doit être vide");
    }
}
