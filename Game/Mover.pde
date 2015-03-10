





class Mover { 
  PVector gravityForce = new PVector(0, 0);
  PVector location;
  PVector velocity;
  float gravityConstant = 1;

  float normalForce = 1;
  float mu = 0.01;
  float frictionMagnitude = normalForce * mu; 
  PVector friction = velocity.get(); 




  Mover() {
    location = new PVector(width/2, height/2,0); 
    velocity = new PVector(1, 1,0);
    gravityForce.x = sin(rz) * gravityConstant;
    gravityForce.z = sin(rx) * gravityConstant;
    friction.mult(-1);
    friction.normalize(); 
    friction.mult(frictionMagnitude);
  }
  void update() { 
    location.add(velocity);
  }

  void display() {


    translate(width/2, height/2, 0);
    ry = map(baba, 0, width, 0, PI); 
    rotateZ(rx); 
    rotateX(rz);
    rotateY(ry);
    box(50, 10, 50);
    translate(location.x, -10, location.y);
    sphere(radius);
  }
  void checkEdges() {
    if (location.x > width) {
      location.x = 0;
    } else if (location.x < 0) { 
      location.x = width;
    }
    if (location.y > height) { 
      location.y = 0;
    } else if (location.y < 0) {
      location.y = height;
    }
  }
}

