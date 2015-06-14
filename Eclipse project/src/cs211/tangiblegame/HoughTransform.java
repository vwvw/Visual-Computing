package cs211.tangiblegame;

import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Capture;

@SuppressWarnings("serial")
public class HoughTransform extends PApplet {
    Capture cam;
    PImage img;
    ImageProcessing imP;

    public void setup() {
        //System.loadLibrary("D:/Programmes/Utility/processing-2.2.1-windows64/processing-2.2.1/modes/java/libraries/video/library/windows64");
        size(640, 480);
        String[] cameras = Capture.list();
        if (cameras.length == 0) {
            println("There are no cameras available for capture.");
            exit();
        } else {
            println("Available cameras:");
            for (int i = 0; i < cameras.length; i++) {
                println(cameras[i]);
            }
            cam = new Capture(this, cameras[3]);
            cam.start();
        }
    }

    public void draw() {
        if (cam.available() == true) {
            cam.read();
        }
        img = cam.get();
        
        PImage tmp = imP.edgeDetection(img);
        image(tmp, 0, 0);
    }
    
   
}