
package cascadeeffectsimulation;

import java.awt.Graphics;

/**
 * @author Drew
 */
public class Position {
    Point pos;
    Angle angle;
    
    public Position(Point pos, Angle angle) {
        this.pos = pos.clone();
        this.angle = angle.clone();
    }
    
    public void rotate(Angle a) {
        angle = angle.add(a);
    }
    
    public void translate(Vector v) {
        pos.translate(v);
    }
    
    public Angle rotationToPos(Position currentPos) {
        double dx = pos.x - currentPos.pos.x;
        double dy = pos.y - currentPos.pos.y;
        
        Angle targetDir = new Angle(Math.toDegrees(Math.atan2(dy, dx)));
        targetDir = targetDir.getStandard();
        
        Angle turnAng = targetDir.subtract(currentPos.angle);
        turnAng = turnAng.getNearestTo0();
        
        return turnAng;
    }
    
    public double distToPos(Position currentPos) {
        double dx = pos.x - currentPos.pos.x;
        double dy = pos.y - currentPos.pos.y;
        return Math.hypot(dx, dy);
    }
    
    public Angle rotationToOrient(Position currentPos) {
        Angle targetDir = angle.getStandard();
        Angle turnAng = targetDir.subtract(currentPos.angle.getStandard());
        turnAng = turnAng.getNearestTo0();
        return turnAng;
    }
    
    @Override
    public Position clone() {
        return new Position(pos.clone(), angle.clone());
    }
    
    @Override
    public String toString() {
        return pos.toString() + " @ " + angle.toString();
    }
    
    public void draw(Graphics g) {
        //g.drawOval(i, i1, i2, i3);
    }
}
