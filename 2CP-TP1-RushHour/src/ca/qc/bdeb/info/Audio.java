package ca.qc.bdeb.info;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;


/**
 * Effets sonores.
 *
 * @author  Eric Drouin
 * @version 0.1
 * @since   2021-03-29
 *
 * À propos des effets sonores...
 *   Trame sonore de fond:
 *     Fichier: 412009__ghrockotron3000__thinking-music.wav
 *     Auteur:  Michael Page (2017)
 *     Licence: Creative Commons
 *   Collision avec une bordure:
 *     Fichier: 342756__rhodesmas__failure-01.wav
 *     Auteur:  Andy Rhode (2016)
 *     Licence: Creative Commons
 *   Collision avec un véhicule:
 *     Fichier: 26186__fogma__my-car-horn-beep.wav
 *     Auteur:  "Fogma" (2006)
 *     Licence: Creative Commons
 *   Réussite d'un défi:
 *     Fichier: 320657__rhodesmas__level-up-03.wav
 *     Auteur:  Andy Rhode (2015)
 *     Licence: Creative Commons
 */
public class Audio {

    public static final int BACKGROUND = 0;
    public static final int SOLVED = 1;
    public static final int BORDER = 2;
    public static final int VEHICLE = 3;

    public static final int NB_CLIPS = 4;

    private Clip[] clips;

    public Audio() {

        clips = new Clip[NB_CLIPS];

        if (!load())
            System.err.println("ERROR loading sounds");

        clips[BACKGROUND].loop(Clip.LOOP_CONTINUOUSLY);  // la trame de fond boucle sans arrêt
    }

    /**
     * Charge les fichiers sonores à partir du disque.
     *
     * @return true si le chargement s'est bien déroulé, false sinon
     */
    private boolean load() {

        boolean success = true;

        File file;
        AudioInputStream ais;
        String filename;

        try {

            for (int i = 0; i < NB_CLIPS; i++) {

                switch (i) {
                    case BACKGROUND :  filename = "412009__ghrockotron3000__thinking-music.wav";
                    break;
                    case SOLVED :  filename = "320657__rhodesmas__level-up-03.wav";
                    break;
                    case BORDER :  filename = "342756__rhodesmas__failure-01.wav";
                    break;
                    case VEHICLE :  filename = "26186__fogma__my-car-horn-beep.wav";
                    break;
                    default :  filename = "";
                };

                file = new File(filename);
                ais = AudioSystem.getAudioInputStream(file);
                clips[i] = AudioSystem.getClip();
                clips[i].open(ais);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    /**
     * Joue le son spécifié.
     *
     * @param clip l'indicateur du son (clip) à jouer (ex.: BACKGROUND)
     */
    public void play(int clip) {

        assert clip >= 0 && clip < NB_CLIPS;

        clips[clip].setFramePosition(0);
        clips[clip].start();
    }

    /**
     * Arrête le son spécifié.
     *
     * @param clip l'indicateur du son (clip) à arrêter (ex.: BACKGROUND)
     */
    public void stop(int clip) {

        assert clip >= 0 && clip < NB_CLIPS;

        clips[clip].stop();
    }
}