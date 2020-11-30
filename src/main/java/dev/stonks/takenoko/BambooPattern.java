package dev.stonks.takenoko;

public class BambooPattern {

    private TileKind color;
    private int height;
    private int nbBamboo;

    public BambooPattern(TileKind color, int height){
        this.color=color;
        this.height=height;
        this.nbBamboo=1;
    }

    public BambooPattern(TileKind color, int height, int nbBamboo){
        this.color=color;
        this.height=height;
        this.nbBamboo=nbBamboo;
    }

    public int getHeight() {
        return height;
    }

    public int getNbBamboo() {
        return nbBamboo;
    }

    public TileKind getColor() {
        return color;
    }
}
