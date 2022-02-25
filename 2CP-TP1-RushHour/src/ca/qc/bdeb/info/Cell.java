package ca.qc.bdeb.info;

public class Cell {

    private char symbol;
    private Colour couleur;

    public Cell(char symbol) {

        this.symbol = symbol;
        this.couleur = Settings.get().getColour(symbol);

    }

    @Override
    public String toString() {
        return ("" + couleur + symbol);
    }

}
