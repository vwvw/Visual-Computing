package game;

import processing.core.PGraphics;

public class ScoreView {
    
    private Game parent;
    private int height;
    private int width;
    private PGraphics surface;
    private int score;
    private int lastHitScore;
    
    public ScoreView(Game p, int scoreViewHeight, int scoreViewWidth){
        parent = p;
        score = 0;
        lastHitScore = 0;
        height = scoreViewHeight;
        width = scoreViewWidth;
        surface = parent.createGraphics(width, height);
        
    }
    
    public void updateScore(int f){
        lastHitScore = (score <= 0) ? 0 : f;
        score = (score + f < 0) ? 0 : score + f;
    }
    
    public int score(){
        return score;
    }
    
    public void draw(){
        surface.noStroke();
        
        surface.beginDraw();
        surface.background(100);
        surface.fill(40);
        surface.rect(2, 2, width-4, height - 4);
        String s = "Total Score\n" + score + "\n\nVelocity\n" + parent.mover.ballVelocity.mag() + "\n\nLast Score\n" + lastHitScore;
        surface.fill(255);
        surface.textSize(8);
        surface.text(s, 6, 6, width -6, height -6);
        
        surface.endDraw();
        parent.image(surface, parent.offsetLeft*2 + parent.topViewSize, parent.windowSize - parent.scoreBoardSize
                + (parent.scoreBoardSize - height) / 2);
        
    }
}
