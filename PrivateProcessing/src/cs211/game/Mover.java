package cs211.game;

import processing.core.*;

public class Mover {

    PVector ballLocation;
    PVector ballVelocity;
    float normalForce = 1;
    float mu = 0.03f;
    float frictionMagnitude = normalForce * mu;
    float bounceCoefficient = 0.7f;

    private Game parent;

    Mover(Game p) {
        parent = p;
        ballLocation = new PVector(0, -parent.wBoard / 2 - parent.ballRadius, 0);
        ballVelocity = new PVector(0, 0, 0);
    }

    void update() {
        parent.gravityForce.x = (float) (Math.sin(parent.rotZ) * parent.gravityConstant);
        parent.gravityForce.z = (float) (Math.sin(parent.rotX) * parent.gravityConstant);
        PVector friction = ballVelocity.get();
        friction.mult(-1);
        friction.normalize();
        friction.mult(frictionMagnitude);
        ballVelocity.add(friction);
        ballVelocity.add(parent.gravityForce);
        ballLocation.add(ballVelocity);
        checkEdges();
        checkCylinderCollision(); 
    }

    void display() {
        parent.view3D.getPGraphics().translate(ballLocation.x, ballLocation.y,
                ballLocation.z);
       PGraphics tmp = parent.view3D.getPGraphics();
       tmp.fill(220, 0, 0);
       tmp.sphere(parent.ballRadius);
       tmp.fill(149, 215, 237);
    }

    void checkEdges() {
        if(ballLocation.x > parent.lBoard / 2 || ballLocation.x < -parent.lBoard / 2 ||ballLocation.z > parent.lBoard / 2 || ballLocation.z < -parent.lBoard / 2){
            parent.scoreView.updateScore(-Math.round(ballVelocity.mag()));
        }
        
        if (ballLocation.x > parent.lBoard / 2) {
            ballLocation.x = parent.lBoard / 2;
            ballVelocity.x = -ballVelocity.x * bounceCoefficient;
        } else if (ballLocation.x < -parent.lBoard / 2) {
            ballLocation.x = -parent.lBoard / 2;
            ballVelocity.x = -ballVelocity.x * bounceCoefficient;
        }

        if (ballLocation.z > parent.lBoard / 2) {
            ballLocation.z = parent.lBoard / 2;
            ballVelocity.z = -ballVelocity.z * bounceCoefficient;
        } else if (ballLocation.z < -parent.lBoard / 2) {
            ballLocation.z = -parent.lBoard / 2;
            ballVelocity.z = -ballVelocity.z * bounceCoefficient;
        }
        
    }

    void checkCylinderCollision() {
        for (int i = 0; i < parent.arrayCylinderPosition.size(); i++) {
            PVector bal = new PVector(ballLocation.x, 0, ballLocation.z);
            PVector cyl = new PVector(parent.arrayCylinderPosition.get(i).x
                    - parent.lBoard / 2, 0,
                    parent.arrayCylinderPosition.get(i).y - parent.lBoard / 2);

            PVector normal = bal.get();
            normal.sub(cyl);

            if (normal.mag() < parent.cylinderRadius + parent.ballRadius)//la balle touche
            {
                parent.scoreView.updateScore(Math.round(ballVelocity.mag()));
                normal.normalize();
                float dotProd = ballVelocity.dot(normal);
                ballVelocity.sub(PVector.mult(normal, 2 * dotProd));
                ballVelocity.mult(bounceCoefficient);

                // we make sure that the ball stays on the border of the
                // cylinder with which it colides
                ballLocation.x = parent.arrayCylinderPosition.get(i).x
                        - parent.lBoard / 2
                        + (parent.cylinderRadius + parent.ballRadius)
                        * (normal.x);
                ballLocation.z = parent.arrayCylinderPosition.get(i).y
                        - parent.lBoard / 2
                        + (parent.cylinderRadius + parent.ballRadius)
                        * (normal.z);
            }
        }
    }
}
