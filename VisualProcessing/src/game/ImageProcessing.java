
package game;

import processing.core.*;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet{
    
    private PImage image;
    
    public void setup(){        
       size(800, 600);
       image = loadImage("../../board1.jpg");
       PImage result = createImage(image.width, image.height, RGB);
       for(int i = 0; i < result.width * result.height; i++){
           
       }
       noLoop();
    }
    
    public void draw(){
        image(image,0,0);
    }
    
}
