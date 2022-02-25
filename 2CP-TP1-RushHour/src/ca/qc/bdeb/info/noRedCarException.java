package ca.qc.bdeb.info;

public class noRedCarException extends RuntimeException {

    /**
     * s'il n'y a aucune voiture rouge dans le fichier
     *
     * @author  Théo Moukhallelati
     * @version 0.1
     * @since   2022-02-18
     */

    public noRedCarException(String errorMessage) {
        super(errorMessage);
    }

}
