
import java.util.*;

class HW03 extends App {
    void fireBullet(int bulletType, Vector3 color, Vector2 velocity, Vector2 position){
        for (int bulletIndex = 0; bulletIndex < bullets.length; ++bulletIndex) {
            Bullet bullet = bullets[bulletIndex];
            if (!bullet.alive) { 
                bullet.position = position; 
                bullet.velocity = velocity; 
                bullets[bulletIndex].radius = 2.0;
                bullet.color = color;
                bullet.alive = true;
                bullet.age = 0;
                bullet.type = bulletType;
                
                break;
            }
        }
    }
    boolean circleCircleIntersection(Vector2 position1, Vector2 position2, double radius1, double radius2 ){
        if (Vector2.distanceBetween( position1, position2) < radius1 + radius2){
            return true;
        }
        return false;
        }

    static class Player {
        Vector3 color;
        double radius;
        Vector2 position;
    }
    
    static class Turret {
        Vector3 color;
        double radius;
        Vector2 position;
        int framesSinceFired;
        int health; 
    }
    
    static class Bullet {
        Vector3 color;
        double radius;
        Vector2 position;
        Vector2 velocity;
        boolean alive;
        int age;
        int type;
        
        static final int TYPE_PLAYER = 0;
        static final int TYPE_TURRET = 1;
    }
    
    Player player;
    
    Turret[] turrets;
    
    Bullet[] bullets;
    
    void setup() { 
        player = new Player();
        player.color = new Vector3(Vector3.cyan);
        player.radius = 4.0;
        player.position = new Vector2(0.0, -40.0);
        
        
        turrets = new Turret[2];
        for (int turretIndex = 0; turretIndex < turrets.length; ++turretIndex){
            turrets[turretIndex] = new Turret();
            Turret turret = turrets[turretIndex];
            turret.color = Vector3.red;
            turret.health = 0;
            turret.radius = 8.0;
            if (turretIndex == 0){
                turret.position = new Vector2(0.0, 0.0);
                
            }
            else{
                turret.position = new Vector2(20.0, 20.0);
            }
        }
        
        bullets = new Bullet[256];
        for (int bulletIndex = 0; bulletIndex < bullets.length; ++bulletIndex) {
            bullets[bulletIndex] = new Bullet();
            Bullet bullet = new Bullet();
            bullet = bullets[bulletIndex];
            bullet.color = Vector3.green;
            bullet.radius = 9.0;
            bullet.velocity = new Vector2(0,0);
            bullet.position = new Vector2(0.0, 0.0);
            
        }
    }
    
    void loop() { 
      
        if (keyHeld('W')) { player.position.y += 1.0; }
        if (keyHeld('A')) { player.position.x -= 1.0; }
        if (keyHeld('S')) { player.position.y -= 1.0; }
        if (keyHeld('D')) { player.position.x += 1.0; }
        if (keyHeld('I')) { fireBullet(0, player.color, Vector2.up.times(2.0), player.position);}
        if (keyHeld('J')) { fireBullet(0, player.color, Vector2.left.times(2.0), player.position);}
        if (keyHeld('K')) { fireBullet(0, player.color, Vector2.down.times(2.0), player.position);}
        if (keyHeld('l')) { fireBullet(0, player.color, Vector2.right.times(2.0), player.position);}
        
        
        drawCircle(player.position, player.radius, player.color);
        
        
        for (int turretIndex = 0; turretIndex < turrets.length; ++turretIndex){
            Turret turret = turrets[turretIndex];
            
            
            if (turret.framesSinceFired++ == 32) {
                turret.framesSinceFired = 0;
                
                
                Vector2 velocity1 = Vector2.directionVectorFrom(turret.position, player.position);
                Vector2 velocity2 = Vector2.directionVectorFrom(player.position, turret.position);
                fireBullet(1, turret.color, velocity1, turret.position);              
                
            }
            if (turret.health < 3){
                drawCircle(turret.position, turret.radius, turret.color);
            }
        }
        
        
        for (int bulletIndex = 0; bulletIndex < bullets.length; ++bulletIndex) {
            Bullet bullet = bullets[bulletIndex];
            if (!bullet.alive) { continue; } // skip dead bullets
            for (int turretIndex = 0; turretIndex < turrets.length; ++turretIndex){
                Turret turret = turrets[turretIndex];
                if (bullet.type == 1){
                    if (circleCircleIntersection(player.position, bullet.position, player.radius, bullet.radius )){
                        reset();
                    }
                        
                        }
                if(bullet.type == 0){
                    if (circleCircleIntersection(turret.position, bullet.position, turret.radius, bullet.radius )){
                        bullet.alive = false;
                        turret.health++;
                    }
                        }
                }  
            
            
         
            if (bullet.age++ > 128) {
                bullet.alive = false;
            }
            
           
            bullet.position = bullet.position.plus(bullet.velocity);
            
            drawCircle(bullet.position, bullet.radius, bullet.color);
            }
        
        
        
        
    } 
    
    
    public static void main(String[] arguments) {
        App app = new HW03();
        app.setWindowBackgroundColor(0.0, 0.0, 0.0);
        app.setWindowSizeInWorldUnits(128.0, 128.0);
        app.setWindowCenterInWorldUnits(0.0, 0.0);
        app.setWindowHeightInPixels(512);
        app.setWindowTopLeftCornerInPixels(64, 64);
        app.run();
    }
    
    }
