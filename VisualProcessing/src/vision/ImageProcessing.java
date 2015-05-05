package vision;

import processing.core.*;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet {

	private PImage m_image;
	private PImage m_result;

	public void setup() {
		m_image = loadImage("../../board1.jpg");
		size(m_image.width, m_image.height);
		m_result = createImage(m_image.width, m_image.height, RGB);    

		/*
        float[][] kernel1 = { { 0, 0, 0 }, { 0, 2, 0 }, { 0, 0, 0 } };
        float[][] kernel2 = { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } };

        float[][] sobelV = { { 0, 0, 0 }, { 1, 0, -1 }, { 0, 0, 0 } };
        float[][] sobelH = { { 0, 1, 0 }, { 0, 0, 0 }, { 0, -1, 0 } };

        float[][] gaussian = { { 90, 120, 90 }, { 120, 150, 120 },
                 { 90, 120, 90 } };

        float weight = 1.f;
        m_result = hueThreshold(m_image, 100, 140);
        m_result = brightnessBinaryThreshold(m_image, 123);
        m_result = convolute(m_image, kernel1, weight);
        m_result = blur(m_image, gaussian, 90);
        m_result = sobel(m_image);
		 */ 
		m_result = edgeDetection(m_image);
	}


	public void draw() {
		//image(m_result, 0, 0);
		hough(m_result);
	}


	public PImage edgeDetection(PImage image){

		PImage result = createImage(width, height, RGB);

		result = sobel(hueThreshold(saturationThreshold(reduceBrightnessThreshold(increaseBrightnessThreshold(m_image, 50), 180), 30, 220), 100,140));

		return result;
	}

	public PImage sobel(PImage image) {

		// kernels for Sobel
		int[][] kernelH = { { 0, 1, 0 }, { 0, 0, 0 }, { 0, -1, 0 } };
		int[][] kernelV = { { 0, 0, 0 }, { 1, 0, -1 }, { 0, 0, 0 } };

		// initialize result image
		PImage result = createImage(image.width, image.height, RGB);
		for (int i = 0; i < result.width * result.height; i++) {
			result.pixels[i] = color(0);
		}

		// initialize buffer and the it's Max
		float maxBuffer = MIN_FLOAT;
		float[] buffer = new float[result.width * result.height];

		// sobel algorithm
		for (int x = 1; x < image.width - 1; x++) {
			for (int y = 1; y < image.height - 1; y++) {
				int sumH = 0;
				int sumV = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						sumH += image.pixels[(y + j) * image.width + x + i]
								* kernelV[i + 1][j + 1];
						sumV += image.pixels[(y + j) * image.width + x + i]
								* kernelH[i + 1][j + 1];
					}
				}
				float sum = (float) Math.sqrt(Math.pow(sumH, 2)
						+ Math.pow(sumV, 2));
				maxBuffer = (sum > maxBuffer) ? sum : maxBuffer;
				buffer[y * result.width + x] = sum;
			}
		}

		// set pixel values according to sobel algorithm
		for (int y = 1; y < image.height - 2; y++) {
			for (int x = 1; x < image.width - 2; x++) {
				if (buffer[y * image.width + x] > (int) (maxBuffer * 0.3f)) {
					result.pixels[y * image.width + x] = color(255);
				} else {
					result.pixels[y * image.width + x] = color(0);
				}
			}
		}
		return result;
	}

	public PImage convolute(PImage image, float[][] kernel, float weight) {

		PImage result = createImage(image.width, image.height, ALPHA);

		for (int x = 1; x < image.width - 1; x++) {
			for (int y = 1; y < image.height - 1; y++) {
				int sum = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						sum += image.pixels[(y + j) * image.width + x + i]
								* kernel[i + 1][j + 1] / weight;
					}
				}
				result.pixels[y * image.width + x] = color(sum);
			}
		}
		return result;
	}

	public PImage brightnessBinaryThreshold(PImage image, float threshold) {
		PImage result = createImage(image.width, image.height, ALPHA);

		for (int x = 0; x < image.width; x++) {
			for (int y = 0; y < image.height; y++) {
				result.pixels[y * result.width + x] = (brightness(image.pixels[y
				                                                               * result.width + x]) > threshold) ? color(255)
				                                                            		   : color(0);
			}
		}
		return result;
	}

	public PImage reduceBrightnessThreshold(PImage image, float threshold) {
		PImage result = createImage(image.width, image.height, ALPHA);

		for (int x = 0; x < image.width; x++) {
			for (int y = 0; y < image.height; y++) {
				result.pixels[y * result.width + x] = (brightness(image.pixels[y
				                                                               * result.width + x]) > threshold) ? color(0) : image.pixels[y  * result.width + x];
			}
		}
		return result;
	}

	public PImage increaseBrightnessThreshold(PImage image, float threshold) {
		PImage result = createImage(image.width, image.height, ALPHA);

		for (int x = 0; x < image.width; x++) {
			for (int y = 0; y < image.height; y++) {
				result.pixels[y * result.width + x] = (brightness(image.pixels[y
				                                                               * result.width + x]) < threshold) ? color(0) : image.pixels[y  * result.width + x];
			}
		}
		return result;
	}

	public PImage saturationThreshold(PImage image, float lowerThreshold,
			float upperThreshold) {
		PImage result = createImage(image.width, image.height, ALPHA);

		for (int x = 0; x < image.width; x++) {
			for (int y = 0; y < image.height; y++) {
				result.pixels[y * result.width + x] = (saturation(image.pixels[y
				                                                               * result.width + x]) > lowerThreshold && saturation(image.pixels[y
				                                                                                                                                * result.width + x]) < upperThreshold) ? m_image.pixels[y
				                                                                                                                                                                                        * result.width + x]
				                                                                                                                                                                                        		: color(0);
			}
		}
		return result;
	}

	public PImage hueThreshold(PImage image, float lowerThreshold,
			float upperThreshold) {
		PImage result = createImage(image.width, image.height, ALPHA);

		for (int x = 0; x < image.width; x++) {
			for (int y = 0; y < image.height; y++) {
				result.pixels[y * result.width + x] = (hue(image.pixels[y
				                                                        * result.width + x]) > lowerThreshold && hue(image.pixels[y
				                                                                                                                  * result.width + x]) < upperThreshold) ? m_image.pixels[y
				                                                                                                                                                                          * result.width + x]
				                                                                                                                                                                        		  : color(0);
			}
		}
		return result;
	}

	public PImage hueBinaryThreshold(PImage image, float lowerThreshold,
			float upperThreshold) {
		PImage result = createImage(image.width, image.height, ALPHA);

		for (int x = 0; x < image.width; x++) {
			for (int y = 0; y < image.height; y++) {
				result.pixels[y * result.width + x] = (hue(image.pixels[y
				                                                        * result.width + x]) > lowerThreshold && hue(image.pixels[y
				                                                                                                                  * result.width + x]) < upperThreshold) ? color(255)
				                                                                                                                		  : color(0);
			}
		}
		return result;
	}

	public PImage blur(PImage image, float[][] kernel, float weight) {
		PImage result = createImage(image.width, image.height, RGB);

		for (int x = 1; x < image.width-1; x++) {
			for (int y = 1; y < image.height-1; y++) {
				int r = 0;
				int g = 0;
				int b = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {         
						r += red(image.pixels[(y + j)*image.width + x + i]) * kernel[i+1][j+1] / weight;
						g += green(image.pixels[(y + j)*image.width + x + i]) * kernel[i+1][j+1] / weight;
						b += blue(image.pixels[(y + j)*image.width + x + i]) * kernel[i+1][j+1] / weight;
					}
				}
				result.pixels[y * image.width + x] = color(r, g, b);
			} 

		}

		return result;

	}



	public void hough(PImage edgeImg) {
		float discretizationStepsPhi = 0.06f;
		float discretizationStepsR = 2.5f;
		// dimensions of the accumulator
		int phiDim = (int) (Math.PI / discretizationStepsPhi);
		int rDim = (int) (((edgeImg.width + edgeImg.height) * 2 + 1) / discretizationStepsR);
		// our accumulator (with a 1 pix margin around)
		int[] accumulator = new int[(phiDim + 2) * (rDim + 2)];
		// Fill the accumulator: on edge points (ie, white pixels of the edge // image), store all possible (r, phi) pairs describing lines going // through the point.
		for (int y = 0; y < edgeImg.height; y++) {
			for (int x = 0; x < edgeImg.width; x++) {
				// Are we on an edge?
				if (brightness(edgeImg.pixels[y * edgeImg.width + x]) != 0) {
					// ...determine here all the lines (r, phi) passing through
					// pixel (x,y), convert (r,phi) to coordinates in the
					// accumulator, and increment accordingly the accumulator.
					for(int phiIndex = 0; phiIndex < phiDim; phiIndex++)
					{
						for(int rIndex = 0; rIndex<rDim; rIndex++)
						{
							if(rIndex*discretizationStepsR
									==
									x*Math.cos(phiIndex*discretizationStepsPhi)+y*Math.sin(phiIndex*discretizationStepsPhi))
							{
								accumulator[phiIndex * rDim + rIndex] += 1;
							
							}
							println("e");
						}	
					}
				} 
			}
			
		}
		println("hello");
		PImage houghImg = createImage(rDim + 2, phiDim + 2, ALPHA);
		for (int i = 0; i < accumulator.length; i++) {
			houghImg.pixels[i] = color(min(255, accumulator[i]));
		}
		houghImg.updatePixels();
		image(houghImg, 0,0);
	}
	
}
