float depth = 200;
float baba = 0;
float rz = 0;
float rx = 0;
float ry = 0;
void setup() 
{
  size(500, 500, P3D); noStroke();
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

//box(100, 80, 60);
//translate(100, 0, 0);
//sphere(50);
//scale(1,0.07,1);
box(50,10,50);
}

void mouseDragged() 
{
 rz = map(mouseY, 0, height, -PI/4, PI/4); 
 rx = map(mouseX, 0, width,-PI/4, PI/4); 

}

void keyPressed() { if (key == CODED) {
if (keyCode == LEFT) { baba -= 50;
}
else if (keyCode == RIGHT) {
baba += 50; }
} }
