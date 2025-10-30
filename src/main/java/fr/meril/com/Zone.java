package fr.meril.com;

/** Zone de la carte ( case ):
 * type de zone  et le  nombre de trésors.
 * */
public class Zone {
    private TypeZone type = TypeZone.PLAINE;
    private int nbTresors = 0;

    public Zone() {
        this(TypeZone.PLAINE, 0);
    }

    public Zone(TypeZone type, int nbTresors) {
        this.type = type;
        this.nbTresors = Math.max(nbTresors, 0); // Empêche les valeurs négatives
    }

    public TypeZone getType() {
        return type;
    }

    public void setType(TypeZone type) {
        this.type = type;
    }

    public int getNbTresors() {
        return nbTresors;
    }

    public void setNbTresors(int nbTresors) {
        this.nbTresors = Math.max(nbTresors, 0);
    }

    public boolean contientDesTresors() {
        return nbTresors > 0;
    }

    public boolean recupererUnTresor() {
        if (nbTresors > 0) {
            nbTresors--;
            return true;
        }
        return false;
    }

    public void ajouterTresors(int n) {
        if (n > 0) {
            nbTresors += n;
        }
    }
}
