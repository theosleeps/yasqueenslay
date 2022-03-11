package ca.qc.bdeb.info;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Défi à résoudre (niveau de jeu)
 *
 * @author  Gilles-Philippe Grégoire et Eric Drouin
 * @version 0.1
 * @since   2022-01-05
 */
public class Challenge {

    private ArrayList<Vehicle> vehicles;  // liste des véhicules dans le stationnement

    private Cell[][] parking;  // grille de stationnement

    private int number;  // numéro du défi


    /**
     * Construit un objet représentant un défi.
     * Ce constructeur est privé. Il est appelé par la méthode statique publique loadChallenge().
     *
     * @param number numéro du défi à construire
     */
    private Challenge(int number) {

        this.number = number;
    }

    /**
     * Getter
     */
    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Construit la grille de stationnement.
     */
    private void buildParking() {

        parking = new Cell[Settings.PARKING_SIZE][Settings.PARKING_SIZE];

        /*
        ** Parking vide
        */

        for (int i = 0; i < parking.length; i++) {
            for (int j = 0; j < parking[i].length; j++) {

                if (i == 0 || j == 0 || i == 7|| j == 7  && i != 3) {             //bordure
                    parking[i][j] = new Cell(Settings.BORDER_SYMBOL);
                }

                else if (i == 3 && j == 7) {                            //sortie
                    parking[i][j] = new Cell(Settings.EMPTY_SYMBOL);
                }

                else {                                                  //le reste en attendant de placer les véhicules
                    parking[i][j] = new Cell(Settings.EMPTY_SYMBOL);
                }

            }
        }

        /*
        ** Véhicules dans le parking
        */

        for (int k = 0; k < vehicles.size(); k++) {         //lecture de tous les véhicules pour les placer
            char symbole = vehicles.get(k).getSymbol();     //en conséquence

            for ( Coordinate coord : vehicles.get(k).getCoordinates() ) {  //remplace la cellule vide par
                parking[coord.posY][coord.posX] = new Cell(symbole);       //une cellule avec le bon symbole
            }

        } //

    }

    /**
     * Retourne le véhicule associé au symbole donné et qui est présent dans le stationnement.
     *
     * @param symbol le symbole associé au véhicule
     * @return le véhicule correspondant, null si le véhicule n'est pas trouvé
     */
    public Vehicle getVehicle(Character symbol) {

        Vehicle vehicInventePourLeRetour = new Vehicle('D', 0, null,null);

        for (Vehicle vehicDummy:
             vehicles) {

            if (symbol == vehicDummy.getSymbol()) {
                 return vehicDummy;
            }

        }

        return vehicInventePourLeRetour;
    }

    /**
     * Indique si le défi (challenge) est solutionné.
     *
     * @return true si le défi est résolu, false sinon
     */
    public boolean isSolved() {

        if (parking[3][7].getSymbol() == 'R') { //La voiture rouge est-elle sortie ?
            return true;
        }

    return false;

    }

    /**
     * Charge un défi et construit la grille de stationnement.
     *
     * @return true si le chargement a fonctionné, false sinon
     */
    private boolean load() {

        vehicles = new ArrayList<>();

        ArrayList<String> fichierLu = new ArrayList<>();

        try {
            File fileobj = new File(number + ".txt");
            Scanner reader = new Scanner(fileobj);
            int voitureRouge = 0;

            while(reader.hasNextLine()) {
                String[] vehicule = ( reader.nextLine().split("[|]") );
                Orientation or = Orientation.HORIZONTAL;
                switch (vehicule[3]) { //orientation
                    case "V":
                        or = Orientation.VERTICAL;
                        break;
                    case "H" :
                        or = Orientation.HORIZONTAL;
                }
                Coordinate coorvehicule = new Coordinate(Character.getNumericValue(vehicule[2].charAt(0)),
                        Character.getNumericValue(vehicule[2].charAt(2))); //coordonnées

                Vehicle vehiculeX = new Vehicle(vehicule[0].charAt(0),
                        Integer.parseInt(vehicule[1]),
                        coorvehicule,
                        or);

                if (vehiculeX.getSymbol() == 'R') {
                    voitureRouge ++;
                }
                vehicles.add(vehiculeX);
            }
            if (voitureRouge != 1) {
                throw new noRedCarException("Il n'y a aucune voiture rouge/il y a plus d'une seule voiture rouge");
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            printLastOneCompleted();
            return false;
        }


        return true;

    }

    /**
     * Charge un défi et retourne l'objet Challenge correspondant.
     *
     * @param number numéro du défi à charger
     * @return une instance de la classe Challenge (objet défi), null si le chargement échoue
     */
    public static Challenge loadChallenge(int number) {

        Challenge challenge = new Challenge(number);
        if (challenge.load()) {
            return challenge;
        }

        return null;
    }

    /**
     * Déplace (ou tente de déplacer) un véhicule dans le stationnement.
     *
     * @param command commande de déplacement (#N, #S, #E, #W)
     * @return le résultat de la tentative de déplacement (ce résultat peut être un déplacement)
     */
    public MoveResult moveVehicle(Command command) {

        Vehicle vehicABouger = getVehicle(command.getVehiculeCible());
        ArrayList<Coordinate> coordsABouger = vehicABouger.getCoordinates();


        //vérifier en premier si l'orientaton du véhicule et la direction sont compatibles
        if ((command.getDirection() == Direction.East || command.getDirection() == Direction.West)
                && vehicABouger.getOrientation() != Orientation.HORIZONTAL

                ||(command.getDirection() == Direction.North || command.getDirection() == Direction.South)
                && vehicABouger.getOrientation() != Orientation.VERTICAL) {
            System.out.println("Impossible direction");
            return MoveResult.IMPOSSIBLE_DIRECTION;
        }

        Coordinate coordAVerif; //La coordonnée que l'on vérifiera

        switch (command.getDirection()) {

            case East:
                coordAVerif = new Coordinate(coordsABouger.get(coordsABouger.size() - 1).posX + 1,
                                             coordsABouger.get(coordsABouger.size() - 1).posY);
              break;
            case West:
                coordAVerif = new Coordinate(coordsABouger.get(0).posX - 1,
                                             coordsABouger.get(coordsABouger.size() - 1).posY);
              break;
            case North:
                coordAVerif = new Coordinate(coordsABouger.get(coordsABouger.size() - 1).posX,
                        coordsABouger.get(0).posY - 1);
              break;
            default: //South
                coordAVerif = new Coordinate(coordsABouger.get(coordsABouger.size() - 1).posX,
                        coordsABouger.get(coordsABouger.size() - 1).posY + 1);
              break;


        }

        /*
        ** Bloqué ?
        */

        if (parking[coordAVerif.posY][coordAVerif.posX].getSymbol() == Settings.BORDER_SYMBOL) {
            System.out.println("Ce véhicule est coincé par la bordure dans cette direction.");
            return MoveResult.BLOCKED_BY_BORDER;
        }

        else if (parking[coordAVerif.posY][coordAVerif.posX].getSymbol() != Settings.EMPTY_SYMBOL &&
                 parking[coordAVerif.posY][coordAVerif.posX].getSymbol() != Settings.BORDER_SYMBOL ) {
            System.out.println("Ce véhicule est coincé par un autre véhicule dans cette direction.");
            return MoveResult.BLOCKED_BY_VEHICLE; //La cellule n'est pas vide, mais ce n'est pas une bordure non plus.
        }

        /*
        ** La voie est libre ?
        */

        switch (command.getDirection()) {

            case East:

                for (Coordinate coorddummy:
                     coordsABouger) {
                    coorddummy.posX += 1;
                }
              break;

            case West:
                for (Coordinate coorddummy:
                        coordsABouger) {
                    coorddummy.posX -= 1;
                }
              break;

            case South:
                for (Coordinate coorddummy:
                        coordsABouger) {
                    coorddummy.posY += 1;
                }
              break;

            default: //North
                for (Coordinate coorddummy:
                        coordsABouger) {
                    coorddummy.posY -= 1;
                }
              break;

        }

        for (Vehicle vehix:
             vehicles) {
            if (vehix.getSymbol() == 'R' && command.getVehiculeCible() == 'R') {
                if (vehix.getCoordinates().get(1).posX == 6 && command.getDirection() == Direction.East) {
                    return MoveResult.GOT_OUT;
                }
            }
        }

        return MoveResult.MOVED;

    }

    /**
     * Affiche le défi (titre et stationnement).
     */
    public void print() {

        buildParking();

        for (int i = 0; i < parking.length; i++) { //
            String line = "";

            for (int j = 0; j < parking[i].length; j++) { ////

                line += (parking[i][j].toString() + "  ");

            } ////
            System.out.println(line);
        } //

    }

    /**
     * Affiche le message de félicitation lorsque le joueur a complété le dernier défi.
     */
    public void printLastOneCompleted() {

        System.out.print("\n" + Settings.INDENTATION);

        System.out.println(Colour.YELLOW + "Félicitations, vous venez de compléter le dernier défi. À la prochaine!");
    }

}