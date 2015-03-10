float depth = 200;
float baba = 0;
float rz = 0;
float rx = 0;
float ry = 0;
float prz = 0;
float prx = 0;
float x ;
float y ;
float radius = 5;
float lBoard = 50;
Mover mover;
void setup() 
{
  size(500, 500, P3D); 
  noStroke();
  mover = new Mover();
}

void draw() {
  camera(width/2, height/2, depth, 250, 250, 0, 0, 1, 0); 
  directionalLight(50, 100, 125, 0, -1, 0); 
  ambientLight(102, 102, 102);
  background(200);
  translate(width/2, height/2, 0);
  ry = map(baba, 0, width, 0, PI); 
  rotateZ(rx); 
  rotateX(rz);
  rotateY(ry);
  box(lBoard, 10,lBoard);
  pushMatrix();
  mover.update();
  mover.checkEdges();
  mover.display();
  popMatrix();
}

void mouseDragged() 
{
  rz = prz+map(mouseY - y, -height, height, -PI/4, PI/4); 
  rx = prx +map(mouseX - x, -width, width, -PI/4, PI/4);
}
void mousePressed()
{
  x = mouseX;
  prx = rx;
  prz = rz;
  y = mouseY;
}


void keyPressed() { 
  if (key == CODED) {
    if (keyCode == LEFT) { 
      baba -= 50;
    } else if (keyCode == RIGHT) {
      baba += 50;
    }
  }
}

