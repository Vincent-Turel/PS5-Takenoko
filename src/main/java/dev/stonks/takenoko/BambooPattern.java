package dev.stonks.takenoko;

public class BambooPattern {

    private TileKind color;
    private TileKind optionalColor1;
    private TileKind optionalColor2;
    private int height;
    private int nbBamboo;

    /**
     * Make a bamboo pattern :
     * @param color color of the bamboo
     * @param height the height of the bamboo (int between 1-4)
     */
    public BambooPattern(TileKind color, int height){
        this.color=color;
        this.height=height;
        this.nbBamboo=1;
    }

    /**
     * Make a bamboo pattern :
     * @param color color of the bamboo
     * @param height the height of the bamboo (int between 1-4)
     * @param nbBamboo the number of different bamboo
     */
    public BambooPattern(TileKind color, int height, int nbBamboo){
        this.color=color;
        this.height=height;
        this.nbBamboo=nbBamboo;
    }

    public BambooPattern(TileKind color, TileKind optionalColor1,TileKind optionalColor2, int height,int nbBamboo){
        this.color=color;
        this.optionalColor1=optionalColor1;
        this.optionalColor2=optionalColor2;
        this.height=height;
        this.nbBamboo=nbBamboo;
    }

    /**
     *GETTER FOR ALL @param :
     */

    public int getHeight() {
        return height;
    }

    public int getNbBamboo() {
        return nbBamboo;
    }

    public TileKind getColor() {
        return color;
    }

    public TileKind getOptionalColor1() {
        return optionalColor1;
    }

    public TileKind getOptionalColor2() {
        return optionalColor2;
    }
}
