
package cascadeeffectsimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

/**
 * @author Drew
 */
public class FieldObject extends Polygon {
    
    protected Angle angle;
    
    protected Angle startAngle;
    protected Point startPos;
    
    public static FieldObject centerGoal(int pos) {
        double length = 32.0 / 12.0;
        double width = 8.2 / 12.0;
        double centerVal = (double) Field.pixelsPerFoot * 6;
        double dx = width / 2 * Field.pixelsPerFoot;
        double dy = length / 2 * Field.pixelsPerFoot;
        Point p1 = new Point(centerVal - dx, centerVal - dy);
        Point p2 = new Point(centerVal + dx, centerVal - dy);
        Point p3 = new Point(centerVal + dx, centerVal + dy);
        Point p4 = new Point(centerVal - dx, centerVal + dy);
        FieldObject centerGoal = new FieldObject(p1,p2,p3,p4);
        switch(pos) {
            case 1:
                centerGoal.rotate(new Angle(90));
                return centerGoal;
            case 2:
                centerGoal.rotate(new Angle(45));
                return centerGoal;
            case 3:
                return centerGoal;
        }
        return null;
    }
    
    private Color color = Color.green;
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public FieldObject(Point... points) {
        super(getXs(points), getYs(points), points.length);
    }
    
    public FieldObject(double[] xpoints, double[] ypoints, int npoints) {
        super(xpoints, ypoints, npoints);
    }
    
    public FieldObject(Polygon poly) {
        super(poly.xpoints, poly.ypoints, poly.npoints);
    }
    
    @Override
    public FieldObject clone() {
        return new FieldObject(xpoints, ypoints, npoints);
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        this.fillPolygon(g);
    }
    
////////////////////////////////////////////////////////////////////////////////
    
}
