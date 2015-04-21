package visual8;

import processing.core.PApplet;
import processing.core.PImage;


public class Visual8 extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HScrollbar thresholdBar;
	HScrollbar hueBar;
	
	PImage img;
	public void setup() { 
		size(800, 600);
		thresholdBar = new HScrollbar(this,0, 580, 800, 20);
		hueBar = new HScrollbar(this, 0, 550, 800, 20);
		img = loadImage("board1.jpg");
		//noLoop(); 
	}
		// no interactive behaviour: draw() will be called only once. }
	
	
	public PImage convolute(PImage img) {
		float[][] kernel = { { 0, 0, 0 }, { 0, 2, 0 },
		{ 0, 0, 0 }};
		float weight = 1.f;
		// create a greyscale image (type: ALPHA) for output
		    PImage result = createImage(img.width, img.height, ALPHA);
		    
		    result.loadPixels();
		    
		    int N = 3; 
		    for(int x = 0; x < img.width; ++x)
		    {
		    	for(int y = 0; y < img.height;++y)
		    	{
		    		result.pixels[y * img.width + x] = 0; 
		    		int accu = 0; 
		    		for(int x1 = x-N/2; x1 < x + N/2; ++x1)
		    		{
		    			for(int y1 = y-N/2; y1 < y + N/2;++y1)
		    			{
		    			accu += img.pixels[y1*img.width + x1]*kernel[x1][y1]; 
		    			}
		    		}
		    		result.pixels[y * img.width + x] = (int)(accu / weight); 
		    		 
		    	}	
		    }
		    
		    
		return result; 
		}
	
	
	
	public void draw() 
	{
		hue((int)hueBar.getPos());
		
		PImage result = createImage(width, height, RGB);
			
			int thresholding = color(128,0,0)*((int)(1+hueBar.getPos()*100))/100; 
			//println(hueBar.getPos());
			// create a new, initially transparent, 'result' image 
			img.loadPixels();
			result.loadPixels();
			loadPixels();
			for(int i = 0; i < (img.width * img.height); i++) 
			{
		    // do something with the pixel img.pixels[i]
				if((img.pixels[i]) <  (int)(thresholding * (1+thresholdBar.getPos())))
				{
					int b = (int) (color(34, 55, 63)); 
					result.pixels[i] = b;
				}
				else
				{
					result.pixels[i] = img.pixels[i]; 
				}
				//println(img.pixels[i]);
				result.updatePixels();        
			}
			
			convolute(result);
			image(result, 0, 0);
			
			hueBar.display();
		    hueBar.update();
		    
			thresholdBar.display();
		    thresholdBar.update();
		    
		    
		    //println(thresholdBar.getPos());
	}
}

