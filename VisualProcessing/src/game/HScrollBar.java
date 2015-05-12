package game;

import processing.core.PApplet;
import processing.core.PGraphics;

class HScrollbar {
    
  int barWidth;  //Bar's width in pixels
  int barHeight; //Bar's height in pixels
  float xPosition;  //Bar's x position in pixels
  float yPosition;  //Bar's y position in pixels
  
  float sliderPosition, newSliderPosition;    //Position of slider
  float sliderPositionMin, sliderPositionMax; //Max and min values of slider
  
  boolean mouseOver;  //Is the mouse over the slider?
  boolean locked;     //Is the mouse clicking and dragging the slider now?

  Game parent; 
  
  PGraphics surface;
  
  /**
   * @brief Creates a new horizontal scrollbar
   * 
   * @param x The x position of the top left corner of the bar in pixels
   * @param y The y position of the top left corner of the bar in pixels
   * @param w The width of the bar in pixels
   * @param h The height of the bar in pixels
   */
  HScrollbar (Game p, float x, float y, int h) {
    parent = p;
    int width = parent.windowSize - (int)(parent.offsetLeft)*4 - parent.topViewSize - parent.scoreViewWidth;
    barWidth = width;
    barHeight = h;
    int xOffset = (int)(parent.offsetLeft)*3 + parent.topViewSize + parent.scoreViewWidth;
    xPosition = xOffset;
    yPosition = parent.windowSize - 5 - barHeight;
    
    sliderPosition = xPosition + barWidth/2 - barHeight/2;
    newSliderPosition = sliderPosition;
    
    sliderPositionMin = xPosition;
    sliderPositionMax = xPosition + barWidth - barHeight;
    
    surface = parent.createGraphics(barWidth, barHeight, PApplet.P2D);
  }

  /**
   * @brief Updates the state of the scrollbar according to the mouse movement
   */
  void update() {
    if (isMouseOver()) {
      mouseOver = true;
    }
    else {
      mouseOver = false;
    }
    if (parent.mousePressed && mouseOver) {
      locked = true;
    }
    if (!parent.mousePressed) {
      locked = false;
    }
    if (locked) {
      newSliderPosition = constrain(parent.mouseX - barHeight/2, sliderPositionMin, sliderPositionMax);
    }
    if (PApplet.abs(newSliderPosition - sliderPosition) > 1) {
      sliderPosition = sliderPosition + (newSliderPosition - sliderPosition);
    }
  }

  /**
   * @brief Clamps the value into the interval
   * 
   * @param val The value to be clamped
   * @param minVal Smallest value possible
   * @param maxVal Largest value possible
   * 
   * @return val clamped into the interval [minVal, maxVal]
   */
  float constrain(float val, float minVal, float maxVal) {
    return PApplet.min(PApplet.max(val, minVal), maxVal);
  }

  /**
   * @brief Gets whether the mouse is hovering the scrollbar
   *
   * @return Whether the mouse is hovering the scrollbar
   */
  boolean isMouseOver() {
    if (parent.mouseX > xPosition && parent.mouseX < xPosition+barWidth &&
            parent.mouseY > yPosition && parent.mouseY < yPosition+barHeight) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * @brief Draws the scrollbar in its current state
   */ 
  void display() {
    surface.noStroke();
    surface.beginDraw();
    surface.fill(204);
    surface.rect(0, 0, barWidth, barHeight);
    if (mouseOver || locked) {
        surface.fill(0, 0, 0);
    }
    else {
        surface.fill(102, 102, 102);
    }
    surface.rect(sliderPosition - xPosition, 0, barHeight, barHeight);
    surface.endDraw();
    
    parent.image(surface, xPosition, yPosition);
  }

  /**
   * @brief Gets the slider position
   * 
   * @return The slider position in the interval [0,1] corresponding to [leftmost position, rightmost position]
   */
  float getPos() {
    return (sliderPosition - xPosition)/(barWidth - barHeight);
  }
}