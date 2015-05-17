package cs211.game;

import processing.core.PGraphics;

/**
 * @author Nicolas Roussel, Nicolas Badoux, Charles Thiebaut
 * 
 * Cette classe représente la zone dédidée à la visualisation des données.
 */
public class DataVisual {
    
    private PGraphics surface;
    private Game parent;
    private int size;
    
    
    public DataVisual(Game p, int scoreBoardSize){
        parent = p;
        this.size = scoreBoardSize;
        surface = parent.createGraphics(parent.windowSize, size, Game.P2D);
    }
    
    public int size(){
        return this.size;
    }
    
    public void draw(){
        surface.noStroke();
        surface.beginDraw();
        surface.background(0);
        surface.endDraw();
        parent.image(surface, 0, parent.windowSize - size);
        

    }
    
    
}
