class Mover { 
  
  PVector location ;
  PVector velocity;
  float normalForce = 1;
  float mu = 0.05;
  float frictionMagnitude = normalForce * mu; 
  
  Mover() {
    location = new PVector(0, -wBoard/2 - radius,0); 
    velocity = new PVector(0, 0,0);
  }

  void update() {    
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
  
  void checkCylinderCollision()
  {
    for(int i = 0; i < arrayCylinder.size(); i++)
    {
      println(arrayCylinder.size());
       PVector bal = new PVector(location.x, 0, location.z); 
       PVector cyl = new PVector(-arrayCylinder.get(i).x,0, -arrayCylinder.get(i).z); 
       
       PVector diff = bal.get();
       diff.sub(cyl);
       
       if(diff.mag() < cylinderBaseSize +radius)// la balle a touche ! 
       {
         diff.normalize();
         PVector t = diff.get(); 
         t.mult(velocity.dot(diff));
         t.mult(2); 
         velocity.sub(t);
        println("boonjour");
        location.add(t); 
        //arrayCylinder.remove(i);
       }  
   }
  }
  
}

