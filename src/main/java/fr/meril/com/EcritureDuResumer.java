package fr.meril.com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EcritureDuResumer {

    public void ecrire(StatutJeu  etat, Path sortie) throws IOException {

        List<String> lignes = new ArrayList<>();
        lignes.add(String.format("C - %d - %d", etat.carte.getLargeur(),
                etat.carte.getHauteur()));

        for (Position m : etat.carte.getMontagnes())
            lignes.add(String.format("M - %d - %d", m.getX(), m.getY()));
        lignes.addAll(etat.carte.affichageDeTresorsPourSortie());

        for (Aventurier a : etat.aventuriers)
            lignes.add(String.format("A - %s - %d - %d - %s - %d",
                    a.getNom(), a.getPosition().getX(),
                    a.getPosition().getY(), a.getDirection(), a.getNbTresorsRamasser()));

        Files.createDirectories(sortie.getParent());
        Files.write(sortie, lignes, java.nio.charset.StandardCharsets.UTF_8);
    }
}
