class Mover { 

  PVector location ;
  PVector velocity;
  float normalForce = 1;
  float mu = 0.03;
  float frictionMagnitude = normalForce * mu; 
  float bounceCoefficient = 0.7;

  Mover() {
    location = new PVector(0, -wBoard/2 - radius, 0); 
    velocity = new PVector(0, 0, 0);
  }

  void update() {    
    gravityForce.x = sin(rotZ) * gravityConstant;
    gravityForce.z = sin(rotX) * gravityConstant;
    PVector friction = velocity.get(); 
    friction.mult(-1);
    friction.normalize(); 
    friction.mult(frictionMagnitude);
    velocity.add(friction);
    velocity.add(gravityForce);
    location.add(velocity);
    checkEdges();
    checkCylinderCollision();
  }

  void display() {
    translate(location.x, location.y, location.z);
    sphere(radius);
  }

  void checkEdges() {
    if (location.x > lBoard/2) {
      location.x = lBoard/2;
      velocity.x = -velocity.x * bounceCoefficient;
    } else if (location.x < -lBoard/2) { 
      location.x = -lBoard/2;
      velocity.x = -velocity.x * bounceCoefficient;
    }

    if (location.z > lBoard/2) { 
      location.z = lBoard/2;
      velocity.z = -velocity.z * bounceCoefficient;
    } else if (location.z < -lBoard/2) {
      location.z = -lBoard/2;
      velocity.z = -velocity.z * bounceCoefficient;
    }
  }

  void checkCylinderCollision()
  {
    for (int i = 0; i < arrayCylinder.size (); i++)
    {
      PVector bal = new PVector(location.x, 0, location.z); 
      PVector cyl = new PVector(arrayCylinder.get(i).x - lBoard/2, 0, arrayCylinder.get(i).y - lBoard/2); 

      PVector normal = bal.get();
      normal.sub(cyl);

      if (normal.mag() < cylinderBaseSize +radius)// la balle a touche ! 
      {
        normal.normalize();
        float dotProd = velocity.dot(normal);
        velocity.sub(PVector.mult(normal, 2*dotProd));
        velocity.mult(bounceCoefficient);
        
        //we slightly move the ball's location so that it never colides 2 frames in a row (which would lead to weird interactions)
        location.x = arrayCylinder.get(i).x - lBoard/2 + (cylinderBaseSize + radius) * (normal.x);
        location.z = arrayCylinder.get(i).y - lBoard/2 + (cylinderBaseSize + radius) * (normal.z);
      }
    }
  }
}

