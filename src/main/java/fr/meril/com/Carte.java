package fr.meril.com;

import java.util.ArrayList;
import java.util.List;

public  class Carte {
    private final int largeur;
    private final int hauteur;
    private final Case[][] grille;
    private final List<Position> montagnes  = new ArrayList<>();

    public Carte(int largeur, int hauteur){
        this.largeur = largeur; this.hauteur = hauteur;
        this.grille = new Case[hauteur][largeur];
        for (int y=0; y<hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                grille[y][x] = new Case();
            }
        }
    }

    public boolean bornesOk(Position p){
        return 0<=p.getX() && p.getX()<this.largeur && 0<=p.getY() && p.getY()<this.hauteur;
    }

    public boolean estUneZoneMontagne(Position p){
        return this.grille[p.getY()][p.getX()].getType() == TypeZone.MONTAGNE;
    }

    public int getTresors(Position p){
        return this.grille[p.getY()][p.getX()].getTresors();
    }

    public boolean recupererUnTresorSiPossible(Position p){
        Case c = this.grille[p.getY()][p.getX()];
        if (c.getTresors()>0){
            c.setTresors(c.getTresors()-1);
            return true;
        }
        return false;
    }

    public void definirUneMontagne(Position p){
        this.grille[p.getY()][p.getX()].setType(TypeZone.MONTAGNE);
        this.getMontagnes().add(p);
    }
    public void ajoutDeTresors(Position p, int n){
        this.grille[p.getY()][p.getX()].setTresors( this.grille[p.getY()][p.getY()].getTresors() + n);
    }
    public java.util.List<String> affichageDeTresorsPourSortie(){
        java.util.List<String> out = new java.util.ArrayList<>();
        for (int y=0; y<hauteur; y++) for (int x=0; x<largeur; x++){
            int t = this.grille[y][x].getTresors();
            if (t>0)
                out.add(String.format("T - %d - %d - %d", x, y, t));
        }
        return out;
    }

    public int getLargeur() {
        return this.largeur;
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
