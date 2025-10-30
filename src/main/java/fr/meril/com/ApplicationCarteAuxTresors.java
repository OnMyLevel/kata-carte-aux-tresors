package fr.meril.com;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Point d'entrée CLI sans gestion de politique de ramassage. */
public final class ApplicationCarteAuxTresors {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java -jar carte-aux-tresors.jar <entree.txt> <sortie.txt>");
            System.exit(1);
        }

        Path entree = Paths.get(args[0]);
        Path sortie = Paths.get(args[1]);

        StatutJeu etat = new ParseurFichierEntree().analyserLeFichierTxt(entree);
        new MoteurJeu().executerJeuTourParTour(etat);
        new EcritureDuResumer().ecrireJeu(etat, sortie);
        System.out.println("Simulation terminée -> " + sortie.toAbsolutePath());
    }
}
