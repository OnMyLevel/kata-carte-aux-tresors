package fr.meril.com;

public class Case {
    private TypeZone type;
    private int tresors = 0;

    public Case() {
        this.type = TypeZone.PLAINE;
        this.tresors = 0;
    }

    public Case(TypeZone type, int tresors) {
        this.type = type;
        this.tresors = tresors;
    }

    public TypeZone getType() {
        return type;
    }

    public void setType(TypeZone type) {
        this.type = type;
    }

    public int getTresors() {
        return tresors;
    }

    public void setTresors(int tresors) {
        this.tresors = tresors;
    }

    public boolean aDesTresors() {
        return tresors > 0;
    }
}
