package fr.meril.com;

import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/** Point d'entrée CLI sans gestion de politique de ramassage. */
public final class ApplicationCarteAuxTresors {

    @TempDir
    Path tmp;

    private static Path samples(){
        return Paths.get(System.getProperty("fichierstests.dir","fichierstests"));
    }



    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java -jar carte-aux-tresors.jar <entree.txt> <sortie.txt>");
            System.exit(1);
        }

        Path entree = samples().resolve(args[0]);
        Path sortie = Paths.get("target/"+args[0]);

        StatutJeu etat = new ParseurFichierEntree().analyserLeFichierTxt(entree);
        new MoteurJeu().executerJeuTourParTour(etat);
        new EcritureDuResumer().ecrireJeu(etat, sortie);
        System.out.println("Simulation terminée -> " + sortie.toAbsolutePath());
    }
}
