/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cascadeeffectsimulation;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Drew
 */
public class Vector {

    double i;
    double j;

    public Vector() {
    }

    public Vector(double i, double j) {
        this.i = i;
        this.j = j;
    }

    public Vector(Point p1, Point p2) {
        this.i = p2.x - p1.x;
        this.j = p2.y - p1.y;
    }

    public Vector(double mag, Angle angle) {
        double angleRad = angle.getRadians();
        i = Math.cos(angleRad) * mag;
        j = Math.sin(angleRad) * mag;
    }

    public static Vector add(Vector... vectors) {
        double i = 0;
        double j = 0;
        for (Vector vector : vectors) {
            i += vector.i;
            j += vector.j;
        }
        return new Vector(i, j);
    }

    // Instance Methods
    @Override
    public Vector clone() {
        return new Vector(i, j);
    }

    public double magnitude() {
        return Math.hypot(i, j);
    }

    public Vector add(Vector v) {
        if(v == null) return this.clone();
        return new Vector(i + v.i, j + v.j);
    }
    
    public Point endPoint(Point start) {
        return new Point(start.x + i, start.y + j);
    }

    public Vector multiply(double factor) {
        return new Vector(i * factor, j * factor);
    }

    public Vector makeMagnitude(double mag) {
        return multiply(mag / magnitude());
    }
    
    public Vector reduceMagnitude(double sub) {
        return makeMagnitude(magnitude() - sub);
    }
    
    public double dotProduct(Vector v) {
        return i * v.i + j * v.j;
    }
    
    public double dotProduct(Vector v, double exp) {
        double m1 = Math.pow(this.magnitude(), exp);
        double m2 = Math.pow(v.magnitude(), exp);
        return m1 * m2 * Math.cos(this.getAngle(v).getRadians());
    }

    public Angle getAngle() {
        Angle angle = new Angle(Math.toDegrees(Math.atan2(j, i)));
        return angle;
    }
    
    public Angle getAngle(Vector v) {
        double ang = Math.abs(getAngle().getDegrees() - v.getAngle().getDegrees());
        if(ang > 180) {
            ang = 360 - ang;
        }
        return new Angle(ang);
    }
    
    public int quadrant() {
        if(i == 0 && j == 0) return 0;
        if(i > 0 &&  j > 0)  return 1;
        if(i < 0 &&  j > 0)  return 2;
        if(i < 0 &&  j < 0)  return 3;
        if(i > 0 &&  j < 0)  return 4;
        if(i > 0 &&  j == 0) return 5;
        if(i == 0 && j > 0)  return 6;
        if(i < 0 &&  j == 0) return 7;
        if(i == 0 && j < 0)  return 8;
        return -1;
    }

    public double closestPassTo(Point start, Point p) {
        Vector h = new Vector(start, p);
        double a = getAngle().getDegrees() - h.getAngle().getDegrees();
        if (Math.abs(a) >= 90 || start.distance(p) > magnitude()) {
            return Integer.MAX_VALUE;
        }
        if (a < 0) {
            a += 360;
        }
        if (a > 180) {
            a = 360 - a;
        }
        double dist = h.magnitude() * Math.sin(Math.toRadians(a));
        return dist;
    }
    
    public Vector makeEndDistTo(Point start, Point p, double dist) {
        if(!(makeMagnitude(start.distance(p) * 2).closestPassTo(start, p) < dist)) {
            return this.clone();
        }
        Vector pVector = new Vector(start, p);
        double theta = Math.abs(pVector.getAngle().getDegrees() - this.getAngle().getDegrees());
        double pDist = start.distance(p);
        double mag = -Math.sqrt(Math.pow(dist, 2)
                        - Math.pow(Math.sin(Math.toRadians(theta)) * pDist, 2))
                        + Math.cos(Math.toRadians(theta)) * pDist;
        return this.makeMagnitude(mag);
    }
    
    public Vector rotate(Angle angle) {
        return new Vector(magnitude(), getAngle().add(angle));
    }
    
    public void drawVector(Graphics g, Point start) {
        g.setColor(Color.black);
        int x1 = (int)Math.round(start.x);
        int y1 = (int)Math.round(start.y);
        int x2 = x1 + (int)Math.round(i);
        int y2 = y1 + (int)Math.round(j);
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public String toString() {
        return "<" + round(i) + ", " + round(j) + "> ::: angle = " + round(getAngle().getDegrees()) + " ::: magnitude = " + round(magnitude()) + "\n";
    }
    
    private double round(double d) {
        return (double)((int)(d*1000)) / 1000;
    }
}