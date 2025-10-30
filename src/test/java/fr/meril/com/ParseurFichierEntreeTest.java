package fr.meril.com;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ParseurFichierEntreeTest {

    @TempDir
    Path tmp;

    private static Path samples(){ return Paths.get(System.getProperty("fichierstests.dir","fichierstests")); }


    @Test void montagne() throws Exception {
        Path entree = samples().resolve("entree_montagne.txt"); Path attendu = samples().resolve("sortie-attendue_montagne.txt"); Path sortie = Paths.get("target/int-montagne.txt");
        StatutJeu etat = new ParseurFichierEntree().analyserLeFichierTxt(entree); new MoteurJeu().executerJeuTourParTour(etat); new EcritureDuResumer().ecrireJeu(etat, sortie);
        assertEquals(
                String.join("\n", Files.readAllLines(attendu)).trim(),
                String.join("\n", Files.readAllLines(sortie)).trim());
    }

    @Test void exempleSimple() throws Exception {
        Path entree = samples().resolve("entree.txt"); Path attendu = samples().resolve("sortie-attendue.txt"); Path sortie = Paths.get("target/int-exemple.txt");
        StatutJeu etat = new ParseurFichierEntree().analyserLeFichierTxt(entree); new MoteurJeu().executerJeuTourParTour(etat); new EcritureDuResumer().ecrireJeu(etat, sortie);
        assertEquals(
                String.join("\n", Files.readAllLines(attendu)).trim(),
                String.join("\n", Files.readAllLines(sortie)).trim());
    }

    private Path write(Path dir, String name, String content) throws IOException {
        Path p = dir.resolve(name);
        Files.writeString(p, content, StandardCharsets.UTF_8);
        return p;
    }

    @Test
    void parse_exempleMinimal_ok() throws IOException {
        String in = """
                # commentaire ignoré
                C - 3 - 4
                M - 1 - 0
                T - 0 - 3 - 2
                A - Lara - 1 - 1 - S - AADG
                """;
        Path f = write(tmp, "in-ok.txt", in);

        StatutJeu etat = new ParseurFichierEntree().analyserLeFichierTxt(f);

        assertEquals(3, etat.getCarte().getLargeur());
        assertEquals(4, etat.getCarte().getHauteur());
        assertTrue(etat.getCarte().estUneZoneMontagne(new Position(1, 0)));
        assertEquals(2, etat.getCarte().getTresors(new Position(0, 3)));

        assertEquals(1, etat.getAventuriers().size());
        Aventurier a = etat.getAventuriers().get(0);
        assertEquals("Lara", a.getNom());
        assertEquals(new Position(1, 1), a.getPosition());
        assertEquals(Direction.S, a.getDirection());
        assertEquals(4, a.getListeDeMouvement().size()); // A A D G
    }

    @Test
    void parse_ignoreEspacesAutourDesTirets() throws IOException {
        String in = """
                C-3-4
                M - 1 - 1
                T-2-3-1
                A- Indy - 0 - 0 - N - AGD
                """;
        Path f = write(tmp, "espaces.txt", in);

        StatutJeu etat = new ParseurFichierEntree().analyserLeFichierTxt(f);

        assertEquals(3, etat.getCarte().getLargeur());
        assertEquals(4, etat.getCarte().getHauteur());
        assertTrue(etat.getCarte().estUneZoneMontagne(new Position(1, 1)));
        assertEquals(1, etat.getCarte().getTresors(new Position(2, 3)));
        assertEquals(1, etat.getAventuriers().size());
        assertEquals("Indy", etat.getAventuriers().get(0).getNom());
    }

    @Test
    void erreur_carteManquante() throws IOException {
        String in = """
                M - 0 - 0
                """;
        Path f = write(tmp, "no-carte.txt", in);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> new ParseurFichierEntree().analyserLeFichierTxt(f));
        assertTrue(ex.getMessage().contains("Définissez la carte"), "Message explicite attendu");
    }

    @Test
    void erreur_formatInvalide_surC() throws IOException {
        String in = """
                C - 3
                """;
        Path f = write(tmp, "bad-C.txt", in);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ParseurFichierEntree().analyserLeFichierTxt(f));
        assertTrue(ex.getMessage().contains("Format invalide"), "Doit expliquer le format attendu");
    }

    @Test
    void erreur_montagneHorsBornes() throws IOException {
        String in = """
                C - 2 - 2
                M - 2 - 0
                """;
        Path f = write(tmp, "mount-out.txt", in);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ParseurFichierEntree().analyserLeFichierTxt(f));
        assertTrue(ex.getMessage().contains("Montagne hors carte"));
    }

    @Test
    void erreur_tresorHorsBornes() throws IOException {
        String in = """
                C - 2 - 2
                T - 0 - 2 - 1
                """;
        Path f = write(tmp, "treasure-out.txt", in);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ParseurFichierEntree().analyserLeFichierTxt(f));
        assertTrue(ex.getMessage().contains("Trésor hors carte"));
    }

    @Test
    void erreur_aventurierAvantCarte() throws IOException {
        String in = """
                A - Indy - 0 - 0 - N - A
                C - 2 - 2
                """;
        Path f = write(tmp, "aventurier-first.txt", in);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> new ParseurFichierEntree().analyserLeFichierTxt(f));
        assertTrue(ex.getMessage().contains("Définissez la carte"));
    }

    @Test
    void erreur_aventurierHorsBornes() throws IOException {
        String in = """
                C - 2 - 2
                A - Indy - 2 - 0 - N - A
                """;
        Path f = write(tmp, "aventurier-out.txt", in);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ParseurFichierEntree().analyserLeFichierTxt(f));
        assertTrue(ex.getMessage().contains("Aventurier hors carte"));
    }

    @Test
    void erreur_aventurierSurMontagneInitiale() throws IOException {
        String in = """
                C - 3 - 3
                M - 1 - 1
                A - Lara - 1 - 1 - E - A
                """;
        Path f = write(tmp, "aventurier-on-mountain.txt", in);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ParseurFichierEntree().analyserLeFichierTxt(f));
        assertTrue(ex.getMessage().contains("Position initiale sur une montagne"));
    }

    @Test
    void erreur_tagInconnu() throws IOException {
        String in = """
                C - 2 - 2
                X - ?? - ??
                """;
        Path f = write(tmp, "unknown-tag.txt", in);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ParseurFichierEntree().analyserLeFichierTxt(f));
        assertTrue(ex.getMessage().contains("Ligne inconnue"));
    }

    @Test
    void erreur_directionInvalide() throws IOException {
        String in = """
                C - 2 - 2
                A - Test - 0 - 0 - Q - A
                """;
        Path f = write(tmp, "bad-dir.txt", in);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new ParseurFichierEntree().analyserLeFichierTxt(f));
        assertTrue(ex.getMessage().contains("Direction invalide"));
    }
}
