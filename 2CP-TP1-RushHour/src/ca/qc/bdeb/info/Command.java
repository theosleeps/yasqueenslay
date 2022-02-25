package ca.qc.bdeb.info;

/**
 * Une commande valide effectuée par l'utilisateur.
 *
 * @author  Théo Moukhallelati
 * @version 0.1
 * @since   2022-02-23
 */
public class Command {

    private char vehiculeCible;
    private Direction direction;
    boolean quitter;

    public Command(char vehiculeCible, Direction direction) {

        this.direction = direction;
        this.vehiculeCible = vehiculeCible;
        if (vehiculeCible == 'Q') {
            this.quitter = true;
        }

    }

    /*
    ** Getters
    */

    public char getVehiculeCible() {
        return vehiculeCible;
    }

    public Direction getDirection() {
        return direction;
    }

    /*
    ** Autre(s) méthode(s)
    */

    public boolean isQuit() {
        return quitter;
    }


}
