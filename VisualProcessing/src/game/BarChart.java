package game;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;

public class BarChart {
    
    private Game parent;
    int timeInSecs;
    PGraphics surface;
    int height; 
    int width;
    ArrayList<Integer> scores;
    
    
    public BarChart(Game p, int barChartHeight){
        parent = p;
        timeInSecs = PApplet.second();
        height = barChartHeight;
        width = parent.windowSize - (int)(parent.offsetLeft)*4 - parent.topViewSize - parent.scoreViewWidth;
        surface = parent.createGraphics(width, barChartHeight);
        scores = new ArrayList<>();
        scores.add(parent.scoreView.score());
    }
    
    public void draw(){    
        int tmp = (PApplet.second() - timeInSecs) % 60;
        tmp = tmp < 0 ? tmp + 60 : tmp;
        if(tmp >= 2){
            timeInSecs = PApplet.second();
            scores.add(parent.scoreView.score());        
        }
        
        surface.noStroke();
        surface.beginDraw();
        surface.background(40);
        
        surface.fill(222, 0,0);
        for(int i = 0; i < scores.size(); i++){
            int score = scores.get(i);
            int rects = score/4;
           
            for(int j = 0; j < rects; j++){
                
                float rectWidth = (width * parent.scrollBar.getPos() / scores.size()) - 1;
                rectWidth = rectWidth < 1 ? 1 : rectWidth;
                surface.rect(i*rectWidth + i, height-5*(j+1) - j, rectWidth, 5);
            }
        }
        surface.endDraw();
        
        int xOffset = (int)(parent.offsetLeft)*3 + parent.topViewSize + parent.scoreViewWidth;
        parent.image(surface, xOffset, parent.windowSize -parent.scoreBoardSize + 10 );    
    }

}
