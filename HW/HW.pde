//float x = 0.0; 
//
//
//void setup() {
//  size(400,300,P2D);
//  frameRate(3); 
//}
//
//void draw() {
//  background(255,204,0); 
//  ellipse(x,height/2,40,40);
//  x += 2;
//  if (x > width + 40) 
//  {
//   x = -40.0; 
//   }
//}
//
//boolean isMoving = true; 
//
//void mousePressed(){
//}
//if (isMoving) { 
//  noLoop(); 
//  isMoving = false;
//}
//else {
//  loop(); 
//  isMoving = true; 
//}
//}

//noFill();
//beginShape(); 
//for(int i=0; i<20; i++){
//  int y = i%2; 
//  vertex(i*10,50+y*10); 
//}
//endShape();



//void setup() { 
//  size(400, 800, P2D); 
//  noLoop(); 
//  }
//
//void draw() 
//{
//background(255, 204, 0);
//translate(width/2, height/2); // position your leaf at the center of the window 
//leaf();
//}
//
//void leaf () { 
//    beginShape();
//    vertex(100.0, -70.0);
//    bezierVertex(90.0, -60.0, 40.0, -100.0, 0.0, 0.0);
//    bezierVertex(0.0, 0.0, 100.0, 40.0, 100.0, -70.0);
//  endShape();
//}

void setup() { 
  size(400, 800, P2D); 
  noLoop();
}

void draw() { 
  background(255, 204, 0); 
  plant(15, 0.4, 0.8);
}


void leaf() { 
    beginShape();
    vertex(100.0, -70.0);
    bezierVertex(90.0, -60.0, 40.0, -100.0, 0.0, 0.0);
    bezierVertex(0.0, 0.0, 100.0, 40.0, 100.0, -70.0);
  endShape();
}

void plant(int numLeaves, float minLeafScale, float maxLeafScale) { 
  line(width/2, 0, width/2, height); // the plant's stem
  int gap = height/numLeaves; // vertical spacing between leaves 
  float angle = 0;
  for (int i=0; i<numLeaves; i++) {
        int x = width/2;
        int y = gap*i + (int)random(gap);
        float scale = random(minLeafScale, maxLeafScale);
        
        pushMatrix();
            translate(x,y);
            rotate(angle);
            if(i%3 ==0) 
            {
              scale(0.5);
            }
            
              
              leaf();
           
             
            
            
        popMatrix();
        
        angle += PI; // alternate the side for each leaf
  } 
}


  
  



