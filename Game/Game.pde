float rotVertical = 0;

//rotation that we do
float rotZ = 0;
float rotX = 0;
float rotY = 0;

//summation of the rotation done
float maxRotX;
float maxRotZ;
float maxRotY;

//the rotation that was already done
float previousRotZ = 0;
float previousRotX = 0;
//the position of the mouse at the begining of the movement
float mousePositionX;
float mousePositionY ;

//ball and board attributes
float radius = 5;
float lBoard = 50;

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
  camera(width/2, height/2, 200, 250, 250, 0, 0, -1, 0); 
  directionalLight(50, 100, 125, 0, -1, 0); 
  ambientLight(102, 102, 102);
  background(200);
  translate(width/2, height/2, 0);
  rotY = map(rotVertical, 0, width, 0, PI); 
  rotateZ(rotX); 
  rotateX(rotZ);
  rotateY(rotY);
  box(lBoard, 10, lBoard);

  gravityForce.x = -sin(rotZ) * gravityConstant;
  gravityForce.z = sin(rotX) * gravityConstant;
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
  previousRotX = rotX;
  previousRotZ = rotZ;
}


//when the mouse is dragged we compare the distance mouvec
void mouseDragged() 
{
  float temp =previousRotZ+map(mouseY - mousePositionY, -height, height, -PI/6, PI/6); 
  if (temp+maxRotZ > PI/3) rotZ =PI/3;
  else {
    rotZ = temp;
    maxRotZ = maxRotZ+rotZ;
  }


  float temp2 = previousRotX +map(mouseX - mousePositionX, -width, width, -PI/6, PI/6); 
  if (temp2+maxRotX > PI/3) rotX =PI/3;
  else {
    rotX = temp;
    maxRotX = maxRotX+rotX;
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

