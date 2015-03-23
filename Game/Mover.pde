class Mover { 

  PVector ballLocation ;
  PVector ballVelocity;
  float normalForce = 1;
  float mu = 0.03;
  float frictionMagnitude = normalForce * mu; 
  float bounceCoefficient = 0.7;

  Mover() {
    ballLocation = new PVector(0, -wBoard/2 - ballRadius, 0); 
    ballVelocity = new PVector(0, 0, 0);
  }

  void update() {    
    gravityForce.x = sin(rotZ) * gravityConstant;
    gravityForce.z = sin(rotX) * gravityConstant;
    PVector friction = ballVelocity.get(); 
    friction.mult(-1);
    friction.normalize(); 
    friction.mult(frictionMagnitude);
    ballVelocity.add(friction);
    ballVelocity.add(gravityForce);
    ballLocation.add(ballVelocity);
    checkEdges();
    checkCylinderCollision();
  }

  void display() {
    translate(ballLocation.x, ballLocation.y, ballLocation.z);
    sphere(ballRadius);
  }

  void checkEdges() {
    if (ballLocation.x > lBoard/2) {
      ballLocation.x = lBoard/2;
      ballVelocity.x = -ballVelocity.x * bounceCoefficient;
    } else if (ballLocation.x < -lBoard/2) { 
      ballLocation.x = -lBoard/2;
      ballVelocity.x = -ballVelocity.x * bounceCoefficient;
    }

    if (ballLocation.z > lBoard/2) { 
      ballLocation.z = lBoard/2;
      ballVelocity.z = -ballVelocity.z * bounceCoefficient;
    } else if (ballLocation.z < -lBoard/2) {
      ballLocation.z = -lBoard/2;
      ballVelocity.z = -ballVelocity.z * bounceCoefficient;
    }
  }

  void checkCylinderCollision()
  {
    for (int i = 0; i < arrayCylinderPosition.size (); i++)
    {
      PVector bal = new PVector(ballLocation.x, 0, ballLocation.z); 
      PVector cyl = new PVector(arrayCylinderPosition.get(i).x - lBoard/2, 0, arrayCylinderPosition.get(i).y - lBoard/2); 

      PVector normal = bal.get();
      normal.sub(cyl);

      if (normal.mag() < cylinderRadius +ballRadius)// la balle a touche ! 
      {
        normal.normalize();
        float dotProd = ballVelocity.dot(normal);
        ballVelocity.sub(PVector.mult(normal, 2*dotProd));
        ballVelocity.mult(bounceCoefficient);
        
        //we make sure that the ball stays on the border of the cylinder with which it colides
        ballLocation.x = arrayCylinderPosition.get(i).x - lBoard/2 + (cylinderRadius + ballRadius) * (normal.x);
        ballLocation.z = arrayCylinderPosition.get(i).y - lBoard/2 + (cylinderRadius + ballRadius) * (normal.z);
      }
    }
  }
}

