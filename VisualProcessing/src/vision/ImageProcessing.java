
package vision;

import processing.core.*;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet{
    
    private PImage image;
    private PImage image2;
    private PImage result;
    private ThresholdBar thBar; 
    private ThresholdBar thBar2; 

    
    public void setup(){        
       image = loadImage("../../board1.jpg");
       image2 = loadImage("../../board1.jpg");
       size(image.width*2, image.height + 40);
       result = createImage(image.width, image.height, RGB);
       thBar = new ThresholdBar(this, 0, image.height, image.width, 20);
       thBar2 = new ThresholdBar(this, 0, image.height+20, image.width, 20);
   }
    
    public void draw(){
        thBar.display();
        thBar2.display();
        thBar2.update();
        thBar.update();
        float threshold = 255 * thBar.getPos();
        float threshold2 = 255 * thBar2.getPos();
        
        for(int i = 0; i < result.width * result.height; i++){
            result.pixels[i] = this.hue(image.pixels[i]) > threshold  && this.hue(image.pixels[i]) < threshold2 ?   image.pixels[i] : color(0);  
            result.updatePixels();
        }
        image(result,0,0);
        image(image2,image.width, 0);
    }
    
}
