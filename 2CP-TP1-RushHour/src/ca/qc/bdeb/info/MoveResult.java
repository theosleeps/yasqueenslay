package ca.qc.bdeb.info;

public enum MoveResult {

    MOVED, //il a bougé mais la partie n'est pas encore résolue
    GOT_OUT, //il est sorti du stationnement
    BLOCKED_BY_BORDER, //la bordure l'empêche de bouger
    BLOCKED_BY_VEHICLE, //une autre voiture le coince
    IMPOSSIBLE_DIRECTION //impossible de bouger dans cette direction

}
