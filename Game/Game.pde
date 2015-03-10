
//processing convention

//    -y
//    |
//    |
//    |
//    |
//    |__________ x
//   /
//  /
// /
// z





float rotVertical = 0;

//rotation that we do
float rotX = 0;
float rotZ = 0;
float rotYNeg = 0;

//summation of the rotation done
float maxrotZ;
float maxrotX;
float maxrotYNeg;

//the rotation that was already done
float previousrotX = 0;
float previousrotZ = 0;
//the position of the mouse at the begining of the movement
float mousePositionX;
float mousePositionY ;

//ball and board attributes
float radius = 5;
float lBoard = 150;

//movement attributes
PVector gravityForce = new PVector(0, 0, 0);
float gravityConstant = 0.3;
Mover mover;

void setup() 
{
  size(500, 500, P3D); 
  noStroke();
  mover = new Mover();
}

void draw() {
  //ambient settings
  camera(width, -height, 400, 250, 250, 0, 0, 1, 0); 
  directionalLight(50, 100, 125, 0, 1, 0); 
  ambientLight(102, 102, 102);
  background(200);

  //we move the coodinates to have the board in the center of the window
  translate(width/2, height/2, 0);
  


  //we map the vertical rotation induced by the arrow keys to a rotation in the z axis
  rotYNeg = map(rotVertical, 0, width, 0, PI); 
  rotateY(-rotYNeg);
  rotateZ(rotZ); 
  rotateX(rotX);
  
  
  box(lBoard, 10, lBoard);
  gravityForce.x = -sin(rotX) * gravityConstant;
  gravityForce.z = sin(rotZ) * gravityConstant;
  pushMatrix();
  mover.update();
  mover.checkEdges();
  mover.display();
  popMatrix();
}

//we save the mouse position, and archive the rotation level
void mousePressed()
{
  mousePositionX = mouseX;
  mousePositionY = mouseY;
  previousrotZ = rotZ;
  previousrotX = rotX;
}


//when the mouse is dragged we compare the distance mouvec
void mouseDragged() 
{
  float temp =previousrotX+map(mouseY - mousePositionY, -height, height, -PI/6, PI/6); 
  if (temp+maxrotX > PI/3) rotX =PI/3;
  else {
    rotX = temp;
    maxrotX = maxrotX+rotX;
  }


  float temp2 = previousrotZ +map(mouseX - mousePositionX, -width, width, -PI/6, PI/6); 
  if (temp2+maxrotZ > PI/3) rotZ =PI/3;
  else {
    rotZ = temp;
    maxrotZ = maxrotZ+rotZ;
  }
}



//vertical rotation with arrow keys
void keyPressed() { 
  if (key == CODED) {
    if (keyCode == LEFT) { 
      rotVertical -= 50;
    } else if (keyCode == RIGHT) {
      rotVertical += 50;
    }
  }
}

