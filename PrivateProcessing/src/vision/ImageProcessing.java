package vision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import processing.core.*;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet {

    private PImage m_image;
    private PImage m_result;

    private float[] tabSin;
    private float[] tabCos;

    private final float discretizationStepsPhi = 0.06f;
    private final float discretizationStepsR = 2.5f;

    int[] accumulator;

    private QuadGraph quadGraph;

    public void setup() {

        // PICK WHICH IMAGE YOU WANT HERE !
        m_image = loadImage("../../board4.jpg");

        size(m_image.width * 2 + 600, m_image.height);

        m_result = edgeDetection(m_image);

        float ang = 0;
        float inverseR = 1.f / discretizationStepsR;
        int phiDim = Math.round(PApplet.PI / discretizationStepsPhi);
        int rDim = Math.round(((m_image.width + m_image.height) * 2 + 1)
                / discretizationStepsR);

        tabSin = new float[phiDim];
        tabCos = new float[phiDim];

        for (int accPhi = 0; accPhi < phiDim; ang += discretizationStepsPhi, accPhi++) {
            tabSin[accPhi] = (float) (Math.sin(ang) * inverseR);
            tabCos[accPhi] = (float) (Math.cos(ang) * inverseR);
        }

        quadGraph = new QuadGraph();

        List<PVector> allLines = hough(m_result, 6);
        List<int[]> quads = getQuads(allLines);

        int[] bestQuad = getBestQuad(quads);
        List<PVector> bestLines = linesForQuad(bestQuad, allLines);
        List<PVector> intersections = getIntersections(bestLines);

     

        //List<int[]> tmp = new ArrayList<int[]>();
        // tmp.add(bestQuad);
        // drawQuads(tmp, bestLines);

        PImage houghImg = createImage(rDim + 2, phiDim + 2, ALPHA);
        for (int i = 0; i < accumulator.length; i++) {
            houghImg.pixels[i] = color(min(255, accumulator[i]));
        }
        houghImg.resize(600, m_image.height);
        houghImg.updatePixels();
        
        image(m_image, 0, 0);
        drawLines(bestLines);
        drawIntersections(intersections);
        image(houghImg, m_image.width, 0);
        image(m_result, m_image.width + houghImg.width, 0);

    }

    public void draw() {
        // nothing done in real time
    }

    public PImage edgeDetection(PImage image) {
        float[][] gaussian = { { 9, 12, 9 }, { 12, 15, 12 }, { 9, 12, 9 } };

        PImage tmp = createImage(image.width, image.height, RGB);

        tmp = brightnessThreshold(image, 36, 171);
        tmp = hueThreshold(tmp, 101, 134);
        tmp = saturationThreshold(tmp, 86, 250);
        tmp = blur(tmp, gaussian, 99);
        tmp = intensityThreshImage(tmp);
        tmp = sobel(tmp);

        return tmp;
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

    public PImage intensityThreshImage(PImage image) {
        PImage result = createImage(image.width, image.height, RGB);
        for (int x = 0; x < image.width; x++) {
            for (int y = 0; y < image.height; y++) {
                result.pixels[y * result.width + x] = (brightness(image.pixels[y
                        * result.width + x]) > 0) ? color(255) : color(0);
            }
        }
        return result;

    }

    public PImage brightnessThreshold(PImage image, float lowerThreshold,
            float upperThreshold) {
        PImage result = createImage(image.width, image.height, RGB);

        for (int x = 0; x < image.width; x++) {
            for (int y = 0; y < image.height; y++) {
                result.pixels[y * result.width + x] = (brightness(image.pixels[y
                        * result.width + x]) > lowerThreshold && brightness(image.pixels[y
                        * result.width + x]) < upperThreshold) ? image.pixels[y
                        * result.width + x] : color(0);

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
                        * result.width + x]) < upperThreshold) ? image.pixels[y
                        * result.width + x] : color(0);
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
                        * result.width + x]) <= upperThreshold) ? image.pixels[y
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

    public ArrayList<PVector> hough(PImage edgeImg, int nLines) {

        ArrayList<Integer> bestCandidates = new ArrayList<>();
        int minVotes = 180;
        ArrayList<PVector> vectors = new ArrayList<>();

        // dimensions of the accumulator
        int phiDim = Math.round(PApplet.PI / discretizationStepsPhi);
        int rDim = Math.round(((edgeImg.width + edgeImg.height) * 2 + 1)
                / discretizationStepsR);

        // our accumulator (with a 1 pix margin around)
        accumulator = new int[(phiDim + 2) * (rDim + 2)];

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
                        float r = x * tabCos[phiIndex] + y * tabSin[phiIndex];
                        int rInt = Math.round(((rDim - 1) / 2) + r);

                        int tmp = ((phiIndex + 1) * (rDim + 2) + rInt);
                        accumulator[tmp] += 1;
                    }
                }
            }
        }
        /*
         * PImage houghImg = createImage(rDim + 2, phiDim + 2, ALPHA); for (int
         * i = 0; i < accumulator.length; i++) { houghImg.pixels[i] =
         * color(min(255, accumulator[i])); }
         * 
         * houghImg.updatePixels();
         */

        int neighbourhood = 10;
        // only search around lines with more that this amount of votes
        // (to be adapted to your image)
        for (int accR = 0; accR < rDim; accR++) {
            for (int accPhi = 0; accPhi < phiDim; accPhi++) {
                // compute current index in the accumulator
                int idx = (accPhi + 1) * (rDim + 2) + accR + 1;
                if (accumulator[idx] > minVotes) {
                    boolean bestCandidate = true;
                    // iterate over the neighbourhood
                    for (int dPhi = -neighbourhood / 2; dPhi < neighbourhood / 2 + 1; dPhi++) {
                        // check we are not outside the image
                        if (accPhi + dPhi < 0 || accPhi + dPhi >= phiDim)
                            continue;
                        for (int dR = -neighbourhood / 2; dR < neighbourhood / 2 + 1; dR++) {

                            // check we are not outside the image
                            if (accR + dR < 0 || accR + dR >= rDim)
                                continue;
                            int neighbourIdx = (accPhi + dPhi + 1) * (rDim + 2)
                                    + accR + dR + 1;
                            if (accumulator[idx] < accumulator[neighbourIdx]) {
                                // the current idx is not a local maximum!
                                bestCandidate = false;
                                break;
                            }
                        }
                        if (!bestCandidate)
                            break;
                    }
                    if (bestCandidate) {
                        // the current idx *is* a local maximum
                        bestCandidates.add(idx);
                    }
                }
            }
        }

        Collections.sort(bestCandidates, houghComparator(accumulator));
        int bestCandidatesSize = bestCandidates.size();

        int maxLines = nLines < bestCandidatesSize ? nLines
                : bestCandidatesSize;

        for (int i = 0; i < maxLines; i++) {
            int idx = bestCandidates.get(i);
            int accPhi = Math.round(idx / (rDim + 2)) - 1;
            int accR = idx - (accPhi + 1) * (rDim + 2) - 1;
            float r = (accR - (rDim - 1) * (float) 0.5)
                    * (float) discretizationStepsR;
            float phi = accPhi * discretizationStepsPhi;
            vectors.add(new PVector(r, phi));
        }

        return vectors;

    }

    public Comparator<Integer> houghComparator(int[] accumulator) {
        return new Comparator<Integer>() {
            @Override
            public int compare(Integer l1, Integer l2) {
                if (accumulator[l1] > accumulator[l2]
                        || (accumulator[l1] == accumulator[l2] && l1 < l2))
                    return -1;
                return 1;
            }
        };
    }

    private void drawIntersections(List<PVector> intersections) {
        fill(255, 128, 0);
        for (PVector intersection : intersections) {
            ellipse(intersection.x, intersection.y, 10, 10);
        }
    }

    public ArrayList<PVector> getIntersections(List<PVector> lines) {
        ArrayList<PVector> intersections = new ArrayList<PVector>();

        for (int i = 0; i < lines.size() - 1; i++) {
            PVector line1 = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                PVector line2 = lines.get(j);
                // compute the intersection and add it to 'intersections'
                float cosPhi1 = PApplet.cos(line1.y);
                float cosPhi2 = PApplet.cos(line2.y);
                float sinPhi1 = PApplet.sin(line1.y);
                float sinPhi2 = PApplet.sin(line2.y);

                float d = cosPhi2 * sinPhi1 - cosPhi1 * sinPhi2;

                float x = line2.x * sinPhi1 - line1.x * sinPhi2;
                float y = -line2.x * cosPhi1 + line1.x * cosPhi2;

                x /= d;
                y /= d;
                intersections.add(new PVector(x, y));

            }
        }
        return intersections;
    }

    private void drawLines(List<PVector> lines) {
        for (PVector line : lines) {
            float r = line.x;
            float phi = line.y;

            int x0 = 0;
            int y0 = (int) (r / sin(phi));
            int x1 = (int) (r / cos(phi));
            int y1 = 0;
            int x2 = m_image.width;
            int y2 = (int) (-cos(phi) / sin(phi) * x2 + r / sin(phi));
            int y3 = m_image.width;
            int x3 = (int) (-(y3 - r / sin(phi)) * (sin(phi) / cos(phi)));
            // Finally, plot the lines
            stroke(204, 102, 0);
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

    private PVector intersection(PVector line1, PVector line2) {
        ArrayList<PVector> lines = new ArrayList<PVector>();
        lines.add(line1);
        lines.add(line2);
        ArrayList<PVector> intersections = getIntersections(lines);
        return intersections.get(0);
    }

    private List<int[]> getQuads(List<PVector> lines) {
        quadGraph.build(lines, m_image.width, m_image.height);
        List<int[]> quads = quadGraph.findCycles();
        List<int[]> quads2 = new ArrayList<int[]>(quads);

        for (int[] quad : quads2) {
            PVector l1 = lines.get(quad[0]);
            PVector l2 = lines.get(quad[1]);
            PVector l3 = lines.get(quad[2]);
            PVector l4 = lines.get(quad[3]);
            // (intersection() is a simplified version of the
            // intersections() method you wrote last week, that simply
            // return the coordinates of the intersection between 2 lines)
            PVector c12 = intersection(l1, l2);
            PVector c23 = intersection(l2, l3);
            PVector c34 = intersection(l3, l4);
            PVector c41 = intersection(l4, l1);
            // Choose a random, semi-transparent colour
            boolean isConvex = QuadGraph.isConvex(c12, c23, c34, c41);
            boolean validArea = QuadGraph.validArea(c12, c23, c34, c41,
                    (float) (m_image.width * m_image.height * 4),
                    (float) (m_image.height * m_image.width * 0.2));
            boolean nonFlatQuad = true;
            if (!(isConvex && validArea && nonFlatQuad)) {
                quads.remove(quad);
            }
        }
        return quads;
    }

    private void drawQuads(List<int[]> quads, List<PVector> lines) {
        for (int[] quad : quads) {
            PVector l1 = lines.get(quad[0]);
            PVector l2 = lines.get(quad[1]);
            PVector l3 = lines.get(quad[2]);
            PVector l4 = lines.get(quad[3]);
            // (intersection() is a simplified version of the
            // intersections() method you wrote last week, that simply
            // return the coordinates of the intersection between 2 lines)
            PVector c12 = intersection(l1, l2);
            PVector c23 = intersection(l2, l3);
            PVector c34 = intersection(l3, l4);
            PVector c41 = intersection(l4, l1);
            Random random = new Random();
            fill(color(min(255, random.nextInt(300)),
                    min(255, random.nextInt(300)),
                    min(255, random.nextInt(300)), 50));
            quad(c12.x, c12.y, c23.x, c23.y, c34.x, c34.y, c41.x, c41.y);
        }
    }

    private List<PVector> linesForQuad(int[] bestQuad, List<PVector> lines) {
        List<PVector> tmp = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tmp.add(lines.get(bestQuad[i]));
        }
        return tmp;
    }

    private int[] getBestQuad(List<int[]> quads) {
        if (quads.size() == 0) {
            throw new IllegalArgumentException(
                    "There should be at least one QUAD");
        }

        // What did you think, that we actually did extra work ?
        return quads.get(0);
    }

}
