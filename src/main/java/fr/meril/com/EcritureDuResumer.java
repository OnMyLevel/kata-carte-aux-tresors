package fr.meril.com;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EcritureDuResumer {

    public void ecrireJeu(StatutJeu  etat, Path sortie) throws IOException {

        List<String> lignes = new ArrayList<>();
        lignes.add(String.format("C - %d - %d", etat.getCarte().getLargeur(),
                etat.getCarte().getHauteur()));

        for (Position m : etat.getCarte().getMontagnes())
            lignes.add(String.format("M - %d - %d", m.getX(), m.getY()));
        lignes.addAll(etat.getCarte().affichageDeTresorsPourSortie());

        for (Aventurier a : etat.getAventuriers())
            lignes.add(String.format("A - %s - %d - %d - %s - %d",
                    a.getNom(), a.getPosition().getX(),
                    a.getPosition().getY(), a.getDirection(), a.getNbTresorsRamasser()));

        Files.createDirectories(sortie.getParent());
        Files.write(sortie, lignes, StandardCharsets.UTF_8);

        System.out.println("=== Contenu du fichier de sortie ===");
        for (String ligne : lignes) {
            System.out.println(ligne);
        }
    }
}
