package ca.qc.bdeb.info;


import java.util.ArrayList;

/**
 * Un véhicule.
 *
 * @author  Gilles-Philippe Grégoire et Eric Drouin
 * @version 0.1
 * @since   2022-01-05
 */
public class Vehicle {

    private Character symbol;  // symbole représentant le véhicule de façon unique
    private int length;  // longueur du véhicule (2 ou 3)

    private Coordinate position;  // emplacement du véhicule dans le stationnement
    private Orientation orientation;  // disposition du véhicule (horizontale ou verticale)

    /**
     * Constructeur.
     *
     * @param symbol symbole (caractère associée, ex.: 'A')
     * @param length longueur du véhicule
     * @param position emplacement du véhicule dans le stationnement (ex.: 1,2)
     * @param orientation orientation du véhicule (ex.: horizontale)
     */
    public Vehicle(Character symbol, int length, Coordinate position, Orientation orientation) {

        this.symbol = symbol;
        this.length = length;
        this.position = position;
        this.orientation = orientation;

    }

    /**
     * Retourne toutes les coordonnées occupées par le véhicule en fonction de sa position, de sa longueur et
     * de son orientation.
     *
     * @return une liste de coordonnées
     */
    public ArrayList<Coordinate> getCoordinates() {

        ArrayList<Coordinate> cellulesOccupees = new ArrayList<>();
        cellulesOccupees.add(position);

        for (int i = 0; i < (length - 1); i++) {

            if (orientation == Orientation.HORIZONTAL) {
                Coordinate dummy = new Coordinate(position.posX + 1, position.posY);
                cellulesOccupees.add(dummy);
            }

            else if (orientation == Orientation.VERTICAL) {
                Coordinate dummy = new Coordinate(position.posX, position.posY + 1 + i);
                cellulesOccupees.add(dummy);
            }

        }

        return cellulesOccupees;

    }

    /**
     * Déplace le véhicule (change sa position).
     *
     * @param deltaX déplacement sur l'axe horizontal
     * @param deltaY déplacement sur l'axe vertical
     */
    public void move(int deltaX, int deltaY) {

        // INSÉREZ VOTRE CODE ICI
    }

    // setters et getters

    public Orientation getOrientation() { return orientation; }
    public Coordinate getPosition() { return position; }
    public char getSymbol() { return symbol; }
}