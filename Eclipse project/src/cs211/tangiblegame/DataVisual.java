package cs211.tangiblegame;

import processing.core.PGraphics;

/**
 * @author Nicolas Roussel, Nicolas Badoux, Charles Thiebaut
 * 
 * Cette classe représente la zone dédidée à la visualisation des données.
 */
public class DataVisual {
    
    private PGraphics surface;
    private TangibleGame parent;
    private int size;
    
    
    public DataVisual(TangibleGame p, int scoreBoardSize){
        parent = p;
        this.size = scoreBoardSize;
        surface = parent.createGraphics(parent.windowSizeHeight, size, TangibleGame.P2D);
    }
    
    public int size(){
        return this.size;
    }
    
    public void draw(){
        surface.noStroke();
        surface.beginDraw();
        surface.background(0);
        surface.endDraw();
        parent.image(surface, 0, parent.windowSizeHeight - size);
        

    }
    
    
}
