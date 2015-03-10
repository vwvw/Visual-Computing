





class Mover { 
  PVector gravityForce = new PVector(0, 0,0);
  PVector location ;
  PVector velocity;
  float gravityConstant = 1;

  float normalForce = 1;
  float mu = 0.01;
  float frictionMagnitude = normalForce * mu; 
 




  Mover() {
    location = new PVector(0, 0,-10); 
    velocity = new PVector(0, 0,0);

  }
  void update() { 
    location.add(velocity);
    gravityForce.x = sin(rz) * gravityConstant;
    gravityForce.z = sin(rx) * gravityConstant;
    PVector friction = velocity.get(); 
    friction.mult(-1);
    friction.normalize(); 
    friction.mult(frictionMagnitude);
  }

  void display() {
    translate(location.x, location.z, location.y);
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
    if(location.z > -10)
    {
      location.z = -10;
    }
  }
}

