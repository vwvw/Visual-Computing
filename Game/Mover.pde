





class Mover { 
  
  PVector location ;
  PVector velocity;
  
  float normalForce = 1;
  float mu = 0.01;
  float frictionMagnitude = normalForce * mu; 
  
  Mover() {
    location = new PVector(0, 0,10); 
    velocity = new PVector(0, 0,0);

  }
  void update() { 
    

    PVector friction = velocity.get(); 
    friction.mult(-1);
    friction.normalize(); 
    friction.mult(frictionMagnitude);
    velocity.x = gravityForce.x;
    velocity.y = gravityForce.z;
    location.add(velocity);
    checkEdges();
  }

  void display() {
    translate(location.x, location.z, location.y);
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
    if (location.y > lBoard/2) { 
      location.y = lBoard/2;
      velocity.y = -velocity.y ;
    } else if (location.y < -lBoard/2) {
      location.y = -lBoard/2;
      velocity.y = -velocity.y ;
    }
    if(location.z > 10)
    {
      location.z = 10;
    }
  }
}

