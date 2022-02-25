package ca.qc.bdeb.info;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
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

                if (i == 0 || j == 0 || i == 7 || j == 7) {             //bordure
                    parking[i][j] = new Cell(Settings.BORDER_SYMBOL);
                }

                else if (i == 7 && j == 3) {                            //sortie
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
                parking[coord.posX][coord.posY] = new Cell(symbole);       //une cellule avec le bon symbole
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
                    case "h":
                        or = Orientation.VERTICAL;
                        break;
                    case "v" :
                        or = Orientation.HORIZONTAL;
                }
                Coordinate coorvehicule = new Coordinate(vehicule[2].charAt(0), vehicule[2].charAt(2)); //coordonnées

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

        /*
        ** Bloqué ?
        */

        if (parking[coordsABouger.get(coordsABouger.size() - 1).posX]          //est-ce que cette coordonnée est celle
                [coordsABouger.get(coordsABouger.size() - 1).posY].getSymbol() //d'une cellule de bordure ?
                == Settings.BORDER_SYMBOL) {
            return MoveResult.BLOCKED_BY_BORDER;
        }

        else if (parking[coordsABouger.get(coordsABouger.size() - 1).posX]     //est-ce que cette coordonnée est celle
                [coordsABouger.get(coordsABouger.size() - 1).posY].getSymbol() //d'une cellule occupée par un autre
                != Settings.EMPTY_SYMBOL                                       //véhicule, et non par une bordure ?
                &&
                parking[coordsABouger.get(coordsABouger.size() - 1).posX]
                        [coordsABouger.get(coordsABouger.size() - 1).posY].getSymbol()
                        != Settings.BORDER_SYMBOL) {
            return MoveResult.BLOCKED_BY_VEHICLE;
        }

        else if ((command.getDirection() == Direction.East || command.getDirection() == Direction.West) //*1
                 && vehicABouger.getOrientation() != Orientation.HORIZONTAL
                 ||(command.getDirection() == Direction.North || command.getDirection() == Direction.South)
                && vehicABouger.getOrientation() != Orientation.VERTICAL) {
            return MoveResult.IMPOSSIBLE_DIRECTION;
        }
        //*1 Est-ce que l'utilisateur essaie de bouger le véhicule dans la direction incompatible avec son orientation ?


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
                    coorddummy.posY -= 1;
                }
              break;

            default: //North
                for (Coordinate coorddummy:
                        coordsABouger) {
                    coorddummy.posY += 1;
                }
              break;

        }

        if (parking[7][3].getSymbol() == 'R') { //La voiture rouge est-elle sortie ?
            return MoveResult.GOT_OUT;
        }

        return MoveResult.MOVED;

    }

    /**
     * Affiche le défi (titre et stationnement).
     */
    public void print() {

        buildParking();

        for (int i = 0; i < parking.length; i++) { //

            for (int j = 0; j < parking[i].length; j++) { ////

                parking[i][j].toString();

            } ////

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