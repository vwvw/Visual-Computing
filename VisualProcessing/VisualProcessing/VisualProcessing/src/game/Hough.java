package game;

import processing.core.PImage;

public class Hough {


	public void hough(PImage edgeImg) {
		float discretizationStepsPhi = 0.06f;
		float discretizationStepsR = 2.5f;
		// dimensions of the accumulator
		int phiDim = (int) (Math.PI / discretizationStepsPhi);
		int rDim = (int) (((edgeImg.width + edgeImg.height) * 2 + 1) / discretizationStepsR);
		
		
		// our accumulator (with a 1 pix margin around)
		int[] accumulator = new int[(phiDim + 2) * (rDim + 2)];
		
		// Fill the accumulator: on edge points (ie, white pixels of the edge // image),
		//store all possible (r, phi) pairs describing lines going // through the point.
		for (int y = 0; y < edgeImg.height; y++) {
			for (int x = 0; x < edgeImg.width; x++) {
				// Are we on an edge?
				if (brightness(edgeImg.pixels[y * edgeImg.width + x]) != 0) {
					// ...determine here all the lines (r, phi) passing through
					// pixel (x,y), convert (r,phi) to coordinates in the
					// accumulator, and increment accordingly the accumulator.
					for(int phi = 0; phi < 2*Math.PI; phi += discretizationStepsPhi)
					{
						int r = (int)(x*Math.cos(phi)+y*Math.sin(phi));
						accumulator[phi * rDim + r] += 1;
					}
					
				} 
			}
		}
	    PImage houghImg = createImage(rDim + 2, phiDim + 2, ALPHA);
	    for (int i = 0; i < accumulator.length; i++) {
	        houghImg.pixels[i] = color(min(255, accumulator[i]));
	    }
	    houghImg.updatePixels();
	}


}
