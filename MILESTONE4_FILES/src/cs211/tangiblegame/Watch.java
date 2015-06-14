package cs211.tangiblegame;

import java.awt.Image;

import processing.core.*;
import processing.video.*;

@SuppressWarnings("serial")
public class Watch extends PApplet {

    Movie cam;

    public void setup() {
        cam = new Movie(this, "../../testvideo.mp4");     
        cam.loop(); 
        size(640, 480);
    }

    @SuppressWarnings("deprecation")
    public void draw() {
       
        image(cam, 0, 0);

    }

    public void movieEvent(Movie m) {
        m.read();
    }
}
