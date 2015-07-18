package cascadeeffectsimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Drew
 */
public class IRScanner {

    public Bot bot;
    private Point relativePos;
    private Angle relativeAngle;
    private Angle[] relativeAngleRange;
    private double range = Field.pixelsPerFoot * 3;
    private static double degPerSecond = 80;
    private double reading;
    private ScannerReading scannerReading = null;
    private int updateDelay = 35;

    public IRScanner(Bot bot, Point relativePos, Angle[] relativeAngleRange) {
        this.bot = bot;
        this.relativePos = relativePos;
        this.relativeAngle = new Angle(0);
        this.relativeAngleRange = relativeAngleRange;
        Timer timer = new Timer(true);
        TimerTask update = new updateReading(this);
        timer.schedule(update, 0, updateDelay);
    }

    static class updateReading extends TimerTask {

        IRScanner scanner;

        public updateReading(IRScanner scanner) {
            this.scanner = scanner;
        }

        @Override
        public void run() {
            scanner.update();
        }
    }

    private void update() {
        Vector readingVector = new Vector(range, getAngle());
        boolean vectorShortened = false;
        while (!clearReading(readingVector)) {
            readingVector = readingVector.reduceMagnitude(Field.pixelsPerFoot / 12);
            vectorShortened = true;
        }
        
        if(scannerReading != null && vectorShortened) {
            Vector relativeReadingVector = new Vector(readingVector.magnitude(), relativeAngle.add(bot.getRelativeAngle()));
            scannerReading.add(relativeReadingVector);
        }
        reading = readingVector.magnitude();
    }
    
    public ScannerReading scan() {
        turnTo(relativeAngleRange[0]);
        scannerReading = new ScannerReading(relativePos, getActualPos(), this);
        turnTo(relativeAngleRange[1]);
        ScannerReading output = scannerReading.clone();
        scannerReading = null;
        turnTo(new Angle(0));
        return output;
    }

    private boolean clearReading(Vector v) {
        for (int i = 0; i < v.magnitude(); i += Field.pixelsPerFoot / 12) {
            Vector subVector = v.reduceMagnitude(i);
            if (bot.getField().inObject(subVector.endPoint(getActualPos()))) {
                return false;
            }
        }
        return true;
    }

    public void turnTo(Angle targetAngle) {
        //relativeAngle = targetAngle.clone();
        Angle angle = targetAngle.subtract(relativeAngle);
        int dirSign = (int) Math.signum(angle.getDegrees());
        Angle toTravel = angle.clone();
        long startTime = (new Date()).getTime();
        while (toTravel.getDegrees() * dirSign > 0) {
            long currentTime = (new Date()).getTime();
            double seconds = (double) (currentTime - startTime) / 1000;
            Angle turnAng = new Angle(dirSign * (degPerSecond * seconds) - angle.subtract(toTravel).getDegrees());
            //System.out.println(relativeAngle.toString() + " ::: " + turnAng.toString());
            relativeAngle = relativeAngle.add(turnAng);
            toTravel = toTravel.subtract(turnAng);
            try {
                Thread.sleep(12l);
            } catch (InterruptedException ex) {
            }
        }
    }

    public Point getActualPos() {
        Point tl = bot.getFrontLeft();
        double hypot = Math.hypot(relativePos.x, relativePos.y);
        Angle theta = new Angle(Math.toDegrees(Math.atan2(relativePos.y, relativePos.x)));
        theta = theta.add(90).add(bot.getAngle());
        double x = tl.x + theta.cos() * hypot;
        double y = tl.y + theta.sin() * hypot;

        return new Point(x, y);
    }

    public Angle getRelativeAngle() {
        return relativeAngle;
    }

    public Angle getAngle() {
        return relativeAngle.add(bot.getAngle());
    }

    public double getReading() {
        return reading;
    }

    public void draw(Graphics g) {
        Point pos = getActualPos();
        g.setColor(Color.black);
        g.fillOval((int) pos.x - 8, (int) pos.y - 8, 16, 16);
        Vector dir = new Vector(reading, getAngle());
        dir.drawVector(g, getActualPos());
    }
}
