package fr.meril.com;

/** Direction avec les points cardinaux et
 * avec la rotations et projection (dx, dy).
 * */
public enum Direction {
    N, S, E, O;

    public Direction tournerAGauche() {
        return switch (this) {
            case N -> O;
            case O -> S;
            case S -> E;
            case E -> N;
        };
    }
    public Direction tournerADroite() {
        return switch (this) {
            case N -> E;
            case E -> S;
            case S -> O;
            case O -> N; };
    }

    public int dX() {
        return switch (this) {
            case E -> 1;
            case O -> -1;
            default -> 0;
        };
    }
    public int dY() {
        return switch (this) {
            case S -> 1;
            case N -> -1;
            default -> 0;
        };
    }
}
