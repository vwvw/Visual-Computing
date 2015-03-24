class Cylinder
{
  // cylinder declaration 
float cylinderRadius = 10; 
float cylinderHeight = 25; 
int cylinderResolution = 30;
PShape completeCylinder = new PShape();
PShape openCylinder = new PShape();
PShape cylinderTop = new PShape();
PShape cylinderBottom = new PShape();
boolean shiftMode = false; //true = place cylinder
ArrayList<PVector> arrayCylinderPosition = new ArrayList<PVector>();
ArrayList<PShape> arrayCylinderShape= new ArrayList<PShape>();



float minXBoundariesCylinder;
float maxXBoundariesCylinder;
float minYBoundariesCylinder;
float maxYBoundariesCylinder;

  
  Cylinder(){}
  
boolean canPlaceCylinder()
{
  //Check if we're trying to place a cylinder outside of the board
  float BoardOnScreenSize = screenX(lBoard/2- cylinderRadius, lBoard/2- cylinderRadius, wBoard/2+cylinderHeight) -  screenX(-lBoard/2+ cylinderRadius, -lBoard/2+ cylinderRadius, wBoard/2+cylinderHeight);
  minXBoundariesCylinder =  screenX(-lBoard/2+ cylinderRadius, -lBoard/2+ cylinderRadius, wBoard/2+cylinderHeight) ;
  maxXBoundariesCylinder = minXBoundariesCylinder + BoardOnScreenSize;
  minYBoundariesCylinder = screenY(-lBoard/2+ cylinderRadius, -lBoard/2+ cylinderRadius, wBoard/2+cylinderHeight);
  maxYBoundariesCylinder = minYBoundariesCylinder + BoardOnScreenSize; 
  boolean outsideX = mouseX >= minXBoundariesCylinder && mouseX <= maxXBoundariesCylinder;
  boolean outsideY = mouseY > minYBoundariesCylinder && mouseY < maxYBoundariesCylinder;


  //stop right now if we're trying to draw out of bounds
  if (!(outsideX && outsideY)) {
    return false;
  }

  //Check if we're trying to place a cylinder on an other
  PVector tmp = new PVector(map(mouseX, minXBoundariesCylinder, maxXBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius), map(mouseY, minYBoundariesCylinder, maxYBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius));

  for (int i = 0; i < arrayCylinderPosition.size (); i++) {
    if (PVector.sub(tmp, arrayCylinderPosition.get(i)).mag()  < cylinderRadius*2) {
      return false;
    }
  }

  //Check if we're trying to place a cylinder on the ball
  tmp = new PVector(tmp.x - lBoard/2, -wBoard/2 - ballRadius, tmp.y- lBoard/2);
  if (PVector.sub(tmp, mover.ballLocation).mag() < cylinderRadius + ballRadius) {
    return false;
  }
  return true;
}

//create a new shape for a cylinder, you can add it to cylinderShape Array
PShape cylinderShaper(float positionX, float positionY)
{

  // position x : centre du cylindre par rapport à la plaque. 
  // position y : centre du cylindre par rapport à la plaque. 

  float angle;
  float[] x = new float[cylinderResolution + 1]; 
  float[] y = new float[cylinderResolution + 1];
  completeCylinder = createShape(GROUP);

  //get the x and y position on a circle for all the sides
  for (int i = 0; i < x.length; i++) {
    angle = (TWO_PI / cylinderResolution) * i; 
    x[i] = sin(angle) * cylinderRadius + positionX ; 
    y[i] = cos(angle) * cylinderRadius + positionY ;
  }

  //corps
  openCylinder = createShape();
  openCylinder.beginShape(QUAD_STRIP);
  openCylinder.fill(color(#10B43D));

  //draw the border of the cylinder
  for (int i = 0; i < x.length; i++) { 
    openCylinder.vertex(x[i], y[i], wBoard/2); 
    openCylinder.vertex(x[i], y[i], cylinderHeight + wBoard/2);
  }
  openCylinder.endShape();

  cylinderTop = createShape();
  cylinderTop.beginShape(TRIANGLES);
  cylinderTop.fill(color(#10B43D));

  cylinderBottom = createShape();
  cylinderBottom.beginShape(TRIANGLES);
  cylinderBottom.fill(color(#10B43D));

  for (int i = 0; i< x.length-1; i++) {
    cylinderTop.vertex(x[i], y[i], cylinderHeight+ wBoard/2); 
    cylinderTop.vertex(x[i+1], y[i+1], cylinderHeight+ wBoard/2);
    cylinderTop.vertex(positionX, positionY, cylinderHeight+ wBoard/2);
    cylinderBottom.vertex(x[i], y[i], wBoard/2); 
    cylinderBottom.vertex(x[i+1], y[i+1], wBoard/2);
    cylinderBottom.vertex(positionX, positionY, wBoard/2);
  }

  cylinderBottom.endShape();
  cylinderTop.endShape();

  completeCylinder.addChild(openCylinder);
  completeCylinder.addChild(cylinderTop);
  completeCylinder.addChild(cylinderBottom);
  return completeCylinder;
}
  
  
  
}
