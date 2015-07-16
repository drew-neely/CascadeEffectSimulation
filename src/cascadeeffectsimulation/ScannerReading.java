
package cascadeeffectsimulation;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * @author Drew
 */
public class ScannerReading {
    
    public Point relativeCenter;
    private Point actualCenter;
    private ArrayList<Vector> reading = new ArrayList<>();
    
    public ScannerReading(Point relativeCenter, Point actualCenter) {
        this.relativeCenter = relativeCenter;
        this.actualCenter = actualCenter;
    }
    
    public ScannerReading(Point relativeCenter, Point actualCenter, ArrayList<Vector> reading) {
        this.relativeCenter = relativeCenter;
        this.actualCenter = actualCenter;
        this.reading = reading;
    }
    
    public void add(Vector vector) {
        reading.add(vector);
    }
    
    public Point getEndPoint(int i) {
        return reading.get(i).endPoint(relativeCenter);
    }
    
    public ArrayList<Point> getEndPoints() {
        ArrayList<Point> points = new ArrayList<Point>();
        for(int i = 0; i < reading.size(); i++) {
            points.add(getEndPoint(i));
        }
        return points;
    }
    
    @Override
    public ScannerReading clone() {
        return new ScannerReading(relativeCenter, actualCenter, reading);
    }
    
    public void draw(Graphics g) {
        for(Vector v : reading) {
            v.endPoint(actualCenter).draw(g, 10);
        }
    }
    
}
