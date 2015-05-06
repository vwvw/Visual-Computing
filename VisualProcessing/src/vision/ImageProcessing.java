package vision;

import processing.core.*;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet {

    private PImage m_image;
    private PImage m_result;

    public void setup() {
        m_image = loadImage("../../board1.jpg");
        size(m_image.width, m_image.height);
        /*
         * float[][] kernel2 = { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } };
         * float[][] kernel1 = { { 0, 0, 0 }, { 0, 2, 0 }, { 0, 0, 0 } }; float
         * weight = 2.f;
         * 
         * float[][] gaussian = { { 9, 12, 9 }, { 12, 15, 12 }, { 9, 12, 9 } };
         * 
         * float[][] sobelV = { { 0, 0, 0 }, { 1, 0, -1 }, { 0, 0, 0 } };
         * float[][] sobelH = { { 0, 1, 0 }, { 0, 0, 0 }, { 0, -1, 0 } };
         * 
         * m_result = blur(m_image, gaussian, 99); m_result =
         * hueThreshold(m_image, 100, 140); m_result =
         * brightnessBinaryThreshold(m_image, 123); m_result =
         * convoluteGreyScale(m_image, kernel1, weight); m_result =
         * sobel(m_image); m_result = edgeDetection(m_image); m_result =
         * edgeDetection(m_image);
         */

        m_result = edgeDetection(m_image);
    }

    public void draw() {
        hough(m_result);
    }

    public PImage edgeDetection(PImage image) {
        float[][] gaussian = { { 9, 12, 9 }, { 12, 15, 12 }, { 9, 12, 9 } };

        PImage result = createImage(width, height, RGB);

        result = sobel(hueBinaryThreshold(
                saturationThreshold(
                        reduceBrightnessThreshold(
                                increaseBrightnessThreshold(
                                        blur(m_image, gaussian, 99), 60), 150),
                        85, 155), 110, 138));
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
                if (buffer[y * image.width + x] > Math.round(maxBuffer * 0.3f)) {
                    result.pixels[y * image.width + x] = color(255);
                } else {
                    result.pixels[y * image.width + x] = color(0);
                }
            }
        }
        return result;
    }

    public PImage convoluteGreyScale(PImage image, float[][] kernel,
            float weight) {

        PImage result = createImage(image.width, image.height, ALPHA);

        for (int x = 1; x < image.width - 1; x++) {
            for (int y = 1; y < image.height - 1; y++) {
                float sum = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        sum += brightness(image.pixels[(y + j) * image.width
                                + x + i])
                                * kernel[i + 1][j + 1] / weight;
                    }
                }
                result.pixels[y * image.width + x] = color(sum);
            }
        }
        return result;
    }

    public PImage brightnessBinaryThreshold(PImage image, float threshold) {
        PImage result = createImage(image.width, image.height, RGB);

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
        PImage result = createImage(image.width, image.height, RGB);

        for (int x = 0; x < image.width; x++) {
            for (int y = 0; y < image.height; y++) {
                result.pixels[y * result.width + x] = (brightness(image.pixels[y
                        * result.width + x]) > threshold) ? color(0)
                        : image.pixels[y * result.width + x];
            }
        }
        return result;
    }

    public PImage increaseBrightnessThreshold(PImage image, float threshold) {
        PImage result = createImage(image.width, image.height, RGB);

        for (int x = 0; x < image.width; x++) {
            for (int y = 0; y < image.height; y++) {
                result.pixels[y * result.width + x] = (brightness(image.pixels[y
                        * result.width + x]) < threshold) ? color(0)
                        : image.pixels[y * result.width + x];
            }
        }
        return result;
    }

    public PImage saturationThreshold(PImage image, float lowerThreshold,
            float upperThreshold) {
        PImage result = createImage(image.width, image.height, RGB);

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
        PImage result = createImage(image.width, image.height, RGB);

        for (int x = 0; x < image.width; x++) {
            for (int y = 0; y < image.height; y++) {
                result.pixels[y * result.width + x] = (hue(image.pixels[y
                        * result.width + x]) >= lowerThreshold && hue(image.pixels[y
                        * result.width + x]) <= upperThreshold) ? m_image.pixels[y
                        * result.width + x]
                        : color(0);
            }
        }
        return result;
    }

    public PImage hueBinaryThreshold(PImage image, float lowerThreshold,
            float upperThreshold) {
        PImage result = createImage(image.width, image.height, RGB);

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

        for (int i = 0; i < image.width; i++) {
            result.pixels[i] = image.pixels[i];
            result.pixels[image.width * (image.height - 1) + i] = image.pixels[i];
        }

        for (int x = 1; x < image.width - 1; x++) {
            for (int y = 1; y < image.height - 1; y++) {
                int r = 0;
                int g = 0;
                int b = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        r += (red(image.pixels[(y + j) * image.width + x + i]) / weight)
                                * kernel[i + 1][j + 1];
                        g += (green(image.pixels[(y + j) * image.width + x + i]) / weight)
                                * kernel[i + 1][j + 1];
                        b += (blue(image.pixels[(y + j) * image.width + x + i]) / weight)
                                * kernel[i + 1][j + 1];
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
        int phiDim = Math.round(this.PI / discretizationStepsPhi);
        int rDim = Math.round(((edgeImg.width + edgeImg.height) * 2 + 1) / discretizationStepsR);
        
        // our accumulator (with a 1 pix margin around)
        int[] accumulator = new int[(phiDim + 2) * (rDim + 2)];
        
        // Fill the accumulator: on edge points (ie, white pixels of the edge //
        // image), store all possible (r, phi) pairs describing lines going //
        // through the point.
        for (int y = 0; y < edgeImg.height; y++) {
            for (int x = 0; x < edgeImg.width; x++) {
                // Are we on an edge?
                if (brightness(edgeImg.pixels[y * edgeImg.width + x]) != 0) {
                    // ...determine here all the lines (r, phi) passing through
                    // pixel (x,y), convert (r,phi) to coordinates in the
                    // accumulator, and increment accordingly the accumulator.
                    for (int phiIndex = 0; phiIndex < phiDim; phiIndex++) {
                        float r = (x * cos(phiIndex * discretizationStepsPhi)/ discretizationStepsR +
                                y* sin(phiIndex * discretizationStepsPhi)/ discretizationStepsR);
                        int rInt = Math.round(((rDim - 1) / 2) + r);
                        accumulator[((phiIndex+1) * (rDim+2) +  rInt)] += 1;
                    }
                }
            }
        }
        PImage houghImg = createImage(rDim + 2, phiDim + 2, ALPHA);
        for (int i = 0; i < accumulator.length; i++) {
            houghImg.pixels[i] = color(min(255, accumulator[i]));
        }
        houghImg.updatePixels();

        image((m_image), 0, 0);

        for (int idx = 0; idx < accumulator.length; idx++) {
            if (accumulator[idx] > 200) {
                // first, compute back the (r, phi) polar coordinates:
                int accPhi = Math.round(idx / (rDim + 2)) - 1;
                int accR = idx - (accPhi + 1) * (rDim + 2) - 1;
                float r = (accR - (rDim - 1) * (float) 0.5)
                        * (float) discretizationStepsR;
                float phi = accPhi * discretizationStepsPhi;
                // Cartesian equation of a line: y = ax + b
                // in polar, y = (-cos(phi)/sin(phi))x + (r/sin(phi))
                // => y = 0 : x = r / cos(phi)
                // => x = 0 : y = r / sin(phi)
                // compute the intersection of this line with the 4 borders of
                // // the image
                int x0 = 0;
                int y0 = Math.round(r / sin(phi));
                int x1 = Math.round(r / cos(phi));
                int y1 = 0;
                int x2 = edgeImg.width;
                int y2 = Math.round(-cos(phi) / sin(phi) * x2 + r / sin(phi));
                int y3 = edgeImg.width;
                int x3 = Math.round(-(y3 - r / sin(phi)) * (sin(phi) / cos(phi)));
                // Finally, plot the lines
                stroke(204, 102, 0);
                // line(0,0,100, 100);
                if (y0 > 0) {
                    if (x1 > 0)
                        line(x0, y0, x1, y1);
                    else if (y2 > 0)
                        line(x0, y0, x2, y2);
                    else
                        line(x0, y0, x3, y3);
                } else {
                    if (x1 > 0) {
                        if (y2 > 0)
                            line(x1, y1, x2, y2);
                        else
                            line(x1, y1, x3, y3);
                    } else
                        line(x2, y2, x3, y3);
                }
            }
        }

    }
}
