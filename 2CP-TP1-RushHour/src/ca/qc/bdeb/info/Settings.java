package ca.qc.bdeb.info;

import java.util.HashMap;
import java.util.Map;


/**
 * Les paramètres de l'application. Singleton.
 *
 * @author  Eric Drouin
 * @version 0.1
 * @since   2022-01-05
 */
class Settings {

    public static final String INDENTATION = "      ";
    public static final Character PROMPT_SYMBOL = '?';
    public static final Character BORDER_SYMBOL = '▓';
    public static final Character EMPTY_SYMBOL = ' ';
    public static final Character RED_SYMBOL = 'R';

    public static final int PARKING_SIZE = 8;  // bordure + 6 espaces + bordure
    public static final int NB_CHALLENGES = 3;

    private static Settings instance = null;

    // "dictionnaire" associatif des symboles et couleurs (codes d'échappement)
    private final Map<Character, Colour> colourMap = new HashMap<>();


    /**
     * Constructeur privé du singleton.
     */
    private Settings() {



        for (Colour colour : Colour.values()) {

            Character symbol = null;

                    switch(colour) {
                case PROMPT :
                    symbol = PROMPT_SYMBOL;
                    break;
                case BORDER :
                    symbol = BORDER_SYMBOL;
                    break;
                case EMPTY :
                    symbol = EMPTY_SYMBOL;
                    break;
                default :
                    colour.name().charAt(0);  // le symbole est le premier caractère du nom de la couleur
                    break;
            };

            colourMap.put(symbol, colour);
        }
    }

    /**
     * Retourne l'instance unique des paramètres de jeu.
     *
     * @return instance des paramètres de jeu
     */
    public static Settings get() {

        if (null == instance)
            instance = new Settings();

        return instance;
    }

    /**
     * Retourne la couleur associée à un symbole.
     *
     * @param symbol le symbole (un caractère, ex.: 'A')
     * @return la couleur associée au symbole
     */
    public Colour getColour(Character symbol) {

        Colour colour = colourMap.get(symbol);
        assert(null != colour);

        return colour;
    }

    /**
     * Vérifie si un symbole est valide (se retrouve dans le "dictionnaire" de couleurs).
     * @param symbol le symbole (un caractère, ex.: 'A')
     * @return true si le symbole est valide, false sinon
     */
    public boolean isValidSymbol(Character symbol) {

        return colourMap.containsKey(symbol);
    }
}