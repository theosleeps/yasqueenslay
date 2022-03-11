package ca.qc.bdeb.info;

import java.util.concurrent.TimeUnit;


/**
 * RushHour
 * Version texte du jeu RushHour de ThinkFun.
 *
 * @author  Gilles-Philippe Grégoire et Eric Drouin
 * @version 0.1
 * @since   2022-01-05
 */
public class RushHour {

    private static int nextChallenge = 1; //compteur pour le challenge

    /**
     * Programme principal.
     *
     * @param args inutilisé
     */
    public static void main(String[] args) {

        Challenge challenge = loadNextChallenge();

        Audio sounds = new Audio();
        sounds.play(Audio.BACKGROUND);

        Prompt prompt = new Prompt();

        // boucle de jeu
        while (true) {
            challenge.print();
            Command command = prompt.readUserCommand(challenge);

            if (command.isQuit()) {
                break;
            }


            MoveResult result = challenge.moveVehicle(command);
            playSFX(sounds, result);

            if (challenge.isSolved()) {
                if (nextChallenge > Settings.NB_CHALLENGES) {
                    // le joueur vient de réussir le dernier défi
                    challenge.print();
                    challenge.printLastOneCompleted();

                    // on laisse le temps à l'effet sonore de terminer
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    }
                    catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }

                    break;
                }

                challenge = loadNextChallenge();
            }
        }

        sounds.stop(Audio.BACKGROUND);
    }

    /**
     * Charge le prochain défi.
     *
     * @return le défi chargé, null s'il y a une erreur au chargement
     */
    private static Challenge loadNextChallenge() {
        Challenge defiactuel = Challenge.loadChallenge(nextChallenge);
        nextChallenge++;
        return defiactuel;
    }

    /**
     * Joue un effet sonore en fonction du résultat d'une commande.
     *
     * @param sounds le module audio
     * @param result le résultat de la commande
     */
    private static void playSFX(Audio sounds, MoveResult result) {

    }
}