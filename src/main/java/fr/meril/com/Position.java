package fr.meril.com;

import java.util.Objects;

/** Classe pour indexer la localisation sur la carte. */
public final class Position {

    public final int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position translation(int dx, int dy) {
        return new Position(x+dx, y+dy);
    }

    @Override
    public boolean equals(Object o){
        return (o instanceof Position p) && p.x==x && p.y==y;
    }
    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }
    @Override
    public String toString(){
        return x + "," + y;
    }
}