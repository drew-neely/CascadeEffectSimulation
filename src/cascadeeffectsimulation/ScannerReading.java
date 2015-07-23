
package cascadeeffectsimulation;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * @author Drew
 */
public class ScannerReading {
    
    
    public IRScanner scanner;
    
    private Point relativeStart;
    private Point actualStart;
    private Vector relativeReading;
    
    public ScannerReading(Point relativeStart, Point actualStart, IRScanner scanner, Vector relativeReading) {
        this.relativeStart = relativeStart;
        this.actualStart = actualStart;
        this.scanner = scanner;
        this.relativeReading = relativeReading;
    }
    
    public Point getEndPoint() {
        return relativeReading.endPoint(relativeStart);
    }
    
    @Override
    public ScannerReading clone() {
        return new ScannerReading(relativeStart, actualStart, scanner, relativeReading);
    }
    
    public void draw(Graphics g) {
        relativeReading.rotate(scanner.bot.startAngle).multiply(Field.pixelsPerFoot).endPoint(actualStart).draw(g, 10);
    }
    
}
