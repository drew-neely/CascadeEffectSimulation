/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cascadeeffectsimulation;

import java.awt.Graphics;

/**
 *
 * @author Drew
 */
public class Point {

    double x;
    double y;

    public Point() {
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setLocation(Point p) {
        this.x = p.x;
        this.y = p.y;
    }
    
    public Point getLocation() {
        return new Point(x,y);
    }
    
    public boolean equals(Point p) {
        return p.x == x && p.y == y;
    }

    public double distance(Point p) {
        return Math.hypot(x - p.x, y - p.y);
    }

    public double distance(double x1, double x2) {
        return Math.hypot(x - x1, y - x2);
    }
    
    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
    }
    
    public void draw(Graphics g, double diam) {
        g.fillOval((int)(x - diam / 2), (int)(y - diam / 2), (int)diam, (int)diam);
    }
    
    public String toString() {
        return "(" + x + ", " + y + ")"; 
    }
}
