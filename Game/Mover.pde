class Mover { 
  
  PVector location ;
  PVector velocity;
  float normalForce = 1;
  float mu = 0.01;
  float frictionMagnitude = normalForce * mu; 
  
  Mover() {
    location = new PVector(0, -10,0); 
    velocity = new PVector(0, 0,0);

  }
  void update() { 
    PVector friction = velocity.get(); 
    friction.mult(-1);
    friction.normalize(); 
    friction.mult(frictionMagnitude);
    velocity.add(gravityForce);
    velocity.add(friction);
    location.add(velocity);
    //checkEdges();
  }

  void display() {
    translate(location.x, location.y, location.z);
    sphere(radius);
  }
  void checkEdges() {
    if (location.x > lBoard/2) {
      location.x = lBoard/2;
      velocity.x = -velocity.x ;
    } else if (location.x < -lBoard/2) { 
      location.x = -lBoard/2;
      velocity.x = -velocity.x ;
    }
    if (location.z > lBoard/2) { 
      location.z = lBoard/2;
      velocity.z = -velocity.z ;
    } else if (location.z < -lBoard/2) {
      location.z = -lBoard/2;
      velocity.z = -velocity.z ;
    }
    if(location.y > -10)
    {
      location.y = -10;
    }
  }
}

