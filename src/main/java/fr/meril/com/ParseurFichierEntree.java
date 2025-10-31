package fr.meril.com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParseurFichierEntree {

    public StatutJeu analyserLeFichierTxt(Path fichier) throws IOException {
        List<String> lignes = Files.readAllLines(fichier).stream()
                .map(l -> l.replaceFirst("\\s*#.*", "")) // coupe les commentaires inline (avec ou sans espaces avant #)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        Carte carte = null;
        List<Aventurier> aventuriers = new ArrayList<>();
        int ordre = 0;

        for (String ligne : lignes) {
            String[] parts = ligne.split("\\s*-\\s*");
            if (parts.length == 0) continue;

            String tag = parts[0].trim().toUpperCase(Locale.ROOT); // tolère 'c', 'm', etc.
            switch (tag) {
                case "C" -> {
                    exigerColonnes(parts, 3, "C - largeur - hauteur", ligne);
                    int w = parseIntStrict(parts[1], "largeur", ligne);
                    int h = parseIntStrict(parts[2], "hauteur", ligne);
                    carte = new Carte(w, h);
                }
                case "M" -> {
                    exigerCarte(carte);
                    exigerColonnes(parts, 3, "M - x - y", ligne);
                    Position p = new Position(
                            parseIntStrict(parts[1], "x", ligne),
                            parseIntStrict(parts[2], "y", ligne)
                    );
                    exigerDansBornes(carte, p, "Montagne hors carte");
                    carte.definirUneMontagne(p);
                }
                case "T" -> {
                    exigerCarte(carte);
                    exigerColonnes(parts, 4, "T - x - y - n", ligne);
                    Position p = new Position(
                            parseIntStrict(parts[1], "x", ligne),
                            parseIntStrict(parts[2], "y", ligne)
                    );
                    exigerDansBornes(carte, p, "Trésor hors carte");
                    int n = parseIntStrict(parts[3], "n (quantité de trésors)", ligne);
                    carte.ajoutDeTresors(p, n);
                }
                case "A" -> {
                    exigerCarte(carte);
                    exigerColonnes(parts, 6, "A - nom - x - y - O - prog", ligne);
                    String nom = parts[1];
                    Position p = new Position(
                            parseIntStrict(parts[2], "x", ligne),
                            parseIntStrict(parts[3], "y", ligne)
                    );
                    Direction o = parseDirectionStrict(parts[4], ligne);
                    String prog = parts[5];
                    // Nettoyage : garder uniquement A/G/D (tolère espaces, virgules, deux-points, etc.)
                    prog = prog.replaceAll("[^AGD]", "");

                    exigerDansBornes(carte, p, "Aventurier hors carte");
                    if (carte.estUneZoneMontagne(p)) {
                        throw new IllegalArgumentException("Position initiale sur une montagne pour " + nom);
                    }
                    Aventurier a = new Aventurier(
                            nom, p, o,
                            Mouvement.parseDeChaineDinstructionDeDirection(prog),
                            ordre++
                    );
                    aventuriers.add(a);
                }
                default -> throw new IllegalArgumentException("Ligne inconnue: " + ligne);
            }
        }

        return new StatutJeu(carte, aventuriers);
    }

    private static void exigerColonnes(String[] parts, int attendu, String formatAttendu, String ligne) {
        if (parts.length != attendu) {
            throw new IllegalArgumentException(
                    "Format invalide. Attendu: " + formatAttendu + " ; Reçu: \"" + ligne + "\""
            );
        }
    }

    private static int parseIntStrict(String s, String champ, String ligne) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Nombre invalide pour " + champ + " dans la ligne: \"" + ligne + "\""
            );
        }
    }

    private static Direction parseDirectionStrict(String s, String ligne) {
        try {
            return Direction.valueOf(s.toUpperCase(Locale.ROOT)); // tolère 'n', 'e', 's', 'w'
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Direction invalide \"" + s + "\" (attendu: N/E/S/W, etc.) dans la ligne: \"" + ligne + "\""
            );
        }
    }

    private static void exigerCarte(Carte carte){
        if (carte == null)
            throw new IllegalStateException("Définissez la carte avant M/T/A");
    }

    private static void exigerDansBornes(Carte carte, Position p, String msg){
        if (!carte.dansLesBornes(p))
            throw new IllegalArgumentException(msg + " en " + p);
    }
}
