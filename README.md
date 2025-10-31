## KATA-CARTE-AUX-TRESORS 
Ce projet s’inspire du jeu 2048 que j’avais développé en C++ lors de mon DUT, ce qui m’a permis de réutiliser certaines logiques et concepts appris à l’époque.
### But

Lire un fichier d’entrée, simuler les déplacements d’aventuriers (tour par tour), ramasser les trésors (à **l’entrée** d’une case), puis écrire l’état final.

### Règles clés

* Carte rectangulaire, indexée (x: O→E, y: N→S).
* Montagnes = obstacles.
* Trésors : ramassage **1 par entrée** ; pour tout prendre, il faut repasser.
* Mouvements : `A` (avance), `G`/`D` (pivot 90°).
* Conflit d’arrivée même case → priorité à l’**ordre d’apparition**.

### Architecture (classes)

* `ParseurFichierEntree` : lit & valide (C/M/T/A) → `StatutJeu`.
* `MoteurJeu` : boucle des tours (rotations, intentions, résolution, pickup).
* `EcritureDuResumer` : écrit sortie (C, M, T restants, A).
* Domaine : `Carte`, `Position`, `Direction`, `Mouvement`, `Aventurier`, `StatutJeu`.

### Choix techniques

* Split robuste `\\s*-\\s*`, contrôles de colonnes, messages clairs.
* Résolution déterministe : tri par ordre d’apparition + set de cibles réservées.
* Stockage carte cohérent (ex. matrices `[y][x]`) + re-vérifs de bornes.

### Exécuter

```bash
mvn clean test package
java -jar target/carte-aux-tresors.jar <entree.txt> <sortie.txt>
```

### Tests

* Unitaires : parse, moteur (bord/montagne/collisions), écriture.
* End-to-end : exemple officiel (entrée → sortie attendue).

---

## Séquence d’un tour

```plantuml
@startuml
actor "CLI" as CLI
participant "Application" as App
participant "Parseur" as P
participant "MoteurJeu" as M
participant "StatutJeu" as S
participant "Carte" as C
participant "Aventurier*" as A

CLI -> App : main(entree, sortie)
App -> P : analyser(entree)
P --> App : StatutJeu (S)

loop Tant qu'au moins un A a des mouvements
  App -> M : executerTour(S)
  M -> A : voirLeProchainMouvement()
  alt Rotation (G/D)
    M -> A : consommer(); setDirection(...)
  else Avance (A)
    M -> A : intention(position + direction)
  end
  M -> M : trier(intentions par ordre d'apparition)
  loop intentions
    M -> C : bornesOk(cible)? / estMontagne(cible)?
    M -> S : case libre et non réservée ?
    alt OK
      M -> A : consommer(); setPosition(cible)
      M -> C : recupererUnTresorSiPossible(cible)?
      alt Trésor pris
        M -> A : ++nbTresorsRamasser
      end
    end
  end
end

App -> EcritureDuResumer : ecrire(S, sortie)
@enduml
```
merilb78@gmail.com