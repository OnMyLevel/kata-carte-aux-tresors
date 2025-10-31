package fr.meril.com;

import java.util.ArrayList;
import java.util.List;

public class Carte {
    private final int largeur;
    private final int hauteur;
    private final Case[][] grille;
    private final List<Position> montagnes = new ArrayList<>();

    public Carte(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.grille = new Case[hauteur][largeur];
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                grille[y][x] = new Case();
            }
        }
    }

    public boolean dansLesBornes(Position p) {
        return 0 <= p.getX() && p.getX() < this.largeur
                && 0 <= p.getY() && p.getY() < this.hauteur;
    }

    public boolean estUneZoneMontagne(Position p) {
        return this.grille[p.getY()][p.getX()].getType() == TypeZone.MONTAGNE;
    }

    public int getTresors(Position p) {
        return this.grille[p.getY()][p.getX()].getTresors();
    }

    public boolean recupererUnTresorSiPossible(Position p) {
        Case c = this.grille[p.getY()][p.getX()];
        if (c.getTresors() > 0) {
            c.setTresors(c.getTresors() - 1);
            return true;
        }
        return false;
    }

    public void ajoutDeTresors(Position p, int n) {
        if (!dansLesBornes(p)) {
            throw new IllegalArgumentException("Trésor hors carte en " + p);
        }
        this.grille[p.getY()][p.getX()]
                .setTresors(this.grille[p.getY()][p.getX()].getTresors() + n);
    }

    public List<String> affichageDeTresorsPourSortie() {
        List<String> outPrint = new ArrayList<>();
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int t = grille[y][x].getTresors();
                if (t > 0) {
                    outPrint.add(String.format("T - %d - %d - %d", x, y, t));
                }
            }
        }
        return outPrint;
    }

    public void definirUneMontagne(Position p) {
        this.grille[p.getY()][p.getX()].setType(TypeZone.MONTAGNE);
        this.montagnes.add(p);
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public Case[][] getGrille() {
        return grille;
    }

    public List<Position> getMontagnes() {
        return montagnes;
    }
}
