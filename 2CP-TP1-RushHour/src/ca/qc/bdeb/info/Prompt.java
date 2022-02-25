package ca.qc.bdeb.info;

import java.util.Scanner;


/**
 * Saisie des commandes utilisateurs.
 *
 * @author  Gilles-Philippe Grégoire et Eric Drouin
 * @version 0.1
 * @since   2022-01-05
 */
public class Prompt {

    final private Scanner scanner = new Scanner(System.in);

    /**
     * Affiche le prompt.
     */
    private void print() {

        System.out.print(Settings.INDENTATION + Colour.PROMPT + "Commande (#N, #S, #E, #W ou Q): ");
    }

    /**
     * Saisit une commande *obligatoirement valide* de l'utilisateur.
     * La méthode répète la saisie si la commande est invalide.
     *
     * @param challenge le défi présentement en cours (celui où récupérer les véhicules)
     * @return la commande saisie
     */
    public Command readUserCommand(Challenge challenge) {

        boolean validation;
        String entree;
        Direction direct;

        do {
            print();
            entree = scanner.next();

            validation = cmdValide(entree, challenge);

        } while (!validation);

        return new Command(entree.charAt(0), detectionDirection(entree.charAt(1))); //nouvelle instance qui sera créée à
                                                                                    //partir d'informations extraites de
                                                                                    //la saisie clavier de l'utilisateur

    }

    private boolean cmdValide(String cmd, Challenge challenge) {

        boolean vehicExists = false;
        char vehic;
        Direction direc = detectionDirection(cmd.charAt(1));
        vehic = cmd.charAt(0);

        for (Vehicle vehicTest: //le véhicule existe-t-il dans ce niveau ?
             challenge.getVehicles()) {

            if (cmd.charAt(0) == vehicTest.getSymbol()) {
                vehicExists = true;
            }

        }

        if (vehicExists && direc != Direction.Invalid) { //validation
            return true;
        }

        else {
            return false;
        }

    }

    /**
     * Méthode qui, selon la lettre, assigne la bonne dicrection.
     * @param lettre
     * @return
     */
    private Direction detectionDirection(char lettre) {

        Direction direc = Direction.Invalid;

        switch (lettre) {
            case 'N':
                direc = Direction.North;
                break;
            case 'S':
                direc = Direction.South;
                break;
            case 'E':
                direc = Direction.East;
                break;
            case 'W':
                direc = Direction.West;
            case 'Q' :
                System.exit(0);
                break;
            default:
                //direc = Direction.Invalid; (déjà par défaut dans l'initialisation)
                System.out.println("Entrée invalide.");
        }

        return direc;

    }
}