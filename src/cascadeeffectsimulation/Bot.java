package cascadeeffectsimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Drew
 */
public class Bot extends FieldObject implements Runnable {

    /*
     * In order to make this simulation realistic, it is important
     * not to use the actual coordinates of the bot on the screen.
     * The bot must keep up with its own relative coordinates and use
     * the relative coordinates in its logic.
     * The real coordinates are part of the FieldObject class, not the bot class
     * and are used only for rendering.
     */
    private Field field;
    public static double size = Field.pixelsPerFoot * 1.5;
    private Color color;
    private IRScanner[] scanners = new IRScanner[2];
    private Position relativePos = new Position(new Point(0,0), new Angle(0));
//    private Point relativePos = new Point(0, 0);
//    private Angle relativeAngle = new Angle(0);
    private ArrayList<Position> path = new ArrayList<>();
    private ArrayList<ScannerReading> readings = new ArrayList<>();

    // Blue on bottom, Red on top
    // ground pos = 1 :: ramp pos = 2
    public Bot(Field field, Color color, int pos) {
        super(makePoly(color, pos));
        this.field = field;
        this.color = color;
        this.angle = startAngle(color);
        this.startAngle = this.angle.clone();
        this.startPos = this.getCenter();
        scanners[0] = new IRScanner(this, new Point(20, 20), new Angle[]{new Angle(-120), new Angle(60)});
        scanners[1] = new IRScanner(this, new Point(size - 20, 20), new Angle[]{new Angle(-60), new Angle(120)});
        scanners[0].startScan();
        scanners[1].startScan();
        makePath();
    }

    @Override
    public void run() {
        for(Position pos : path) {
            goTo(pos);
        }
        System.out.println("Simulation Complete");
    }
    
    public void goTo(Position pos) {
        turnFor(pos.rotationToPos(relativePos), 30);
        moveFor(pos.distToPos(relativePos), 1);
        turnFor(pos.rotationToOrient(relativePos), 30);
    }

    // turns the bot over a period of time
    public void turnFor(Angle angle, double degPerSecond) {
        Angle toTravel = angle.clone();
        int dirSign = (int) Math.signum(angle.getDegrees());
        long startTime = (new Date()).getTime();
        while (toTravel.getDegrees() * dirSign > 0) {
            long currentTime = (new Date()).getTime();
            double seconds = (double) (currentTime - startTime) / 1000;
            //int mult = (angle.getDegrees() > 180 || angle.getDegrees() < 0)? -1 : 1;
            Angle turnAng = new Angle(dirSign * (degPerSecond * seconds) - angle.subtract(toTravel).getDegrees());
            rotate(turnAng);
            relativePos.rotate(turnAng);
            toTravel = toTravel.subtract(turnAng);
            this.angle = this.angle.add(turnAng);
            try {
                Thread.sleep(12l);
            } catch (InterruptedException ex) {
            }
        }
        
        // <editor-fold defaultstate="collapsed" desc="Multi-threaded turning - Doesn't stop execution">
//        if (turnDist.getDegrees() == 0) {
//            turnComplete = true;
//            return;
//        } else {
//            turnComplete = false;
//        }
//        System.out.println(turnDist);
//        int dirSign = (int) Math.signum(turnDist.getDegrees());
//        long currentTime = (new Date()).getTime();
//        double timeDif = (double) (currentTime - lastTurn) / 1000;
//        System.out.println(timeDif);
//        Angle travelDist = new Angle(dirSign * timeDif * turnSpeed);
//        if (turnDist.getDegrees() * dirSign < .2) {
//            travelDist = turnDist;
//        }
//        System.out.println(travelDist);
//        rotate(travelDist);
//        this.angle = this.angle.add(travelDist);
//        turnDist = turnDist.subtract(travelDist);
//        lastTurn = currentTime;
//        System.out.println();
        // </editor-fold>
    }

    //moves the bot over a period of time
    public void moveFor(double feet, double feetPerSecond) {
        double dist = feet * Field.pixelsPerFoot;
        long startTime = (new Date()).getTime();
        double distTraveled = 0;
        while (distTraveled < dist) {
            long currentTime = (new Date()).getTime();
            double seconds = (double) (currentTime - startTime) / 1000;
            double moveDist = feetPerSecond * Field.pixelsPerFoot * seconds - distTraveled;
            Vector disp = new Vector(moveDist, angle);
            Vector relativeDisp = new Vector(moveDist, relativePos.angle);
            translate(disp);
            relativePos.translate(relativeDisp.multiply((double) 1 /Field.pixelsPerFoot));
            distTraveled += moveDist;
            try {
                Thread.sleep(12l);
            } catch (InterruptedException ex) {
            }
        }
    }
    
    public void makePath() {
        Position p1 = new Position(new Point(2,1), new Angle(0));
        Position p2 = new Position(new Point(2,-1), new Angle(0));
        Position p3 = new Position(new Point(5,3), new Angle(0));
        path.add(p1);
        path.add(p2);
        path.add(p3);
    }

    public void addReading(ScannerReading scannerReading) {
        this.readings.add(scannerReading);
    }

    // <editor-fold defaultstate="collapsed" desc="Old scan function. Stops Execution. Returns centered result of old ScannerReading">
    /*
     public ScannerReading[] scan() {

     class Scanner implements Runnable {

     private IRScanner scanner;
     public ScannerReading result;
     public boolean isDone = false;

     public Scanner(IRScanner scanner) {
     this.scanner = scanner;
     }

     public void scan() {
     Thread thread = new Thread(this);
     thread.start();
     }

     @Override
     public void run() {
     result = scanner.scan();
     isDone = true;
     }
     }

     Scanner scanner0 = new Scanner(scanners[0]);
     Scanner scanner1 = new Scanner(scanners[1]);
     scanner0.scan();
     scanner1.scan();
     while (!scanner0.isDone || !scanner1.isDone) {
     try {
     Thread.sleep(12l);
     } catch (Exception e) {
     }
     }
     System.out.println();
     ScannerReading[] result = new ScannerReading[]{scanner0.result, scanner1.result};
     readings.add(result[0]);
     readings.add(result[1]);
     return result;
     }
     */
    // </editor-fold>
    private static Polygon makePoly(Color color, int pos) {
        double hsize = size / 2;
        double[] xpoints = {-hsize, -hsize, hsize, hsize};
        double[] ypoints = {-hsize, hsize, hsize, -hsize};
        Polygon bot = new Polygon(xpoints, ypoints, 4);
        if (color.equals(Color.blue)) {
            bot.translate(6 * Field.pixelsPerFoot, 11 * Field.pixelsPerFoot);
            if (pos == 2) {
                bot.translate(5 * Field.pixelsPerFoot, 0);
            }
        } else if (color.equals(Color.red)) {
            bot.translate(6 * Field.pixelsPerFoot, Field.pixelsPerFoot);
            if (pos == 2) {
                bot.translate(5 * Field.pixelsPerFoot, 0);
            }
        } else {
            return null;
        }
        return bot;
    }

    private static Angle startAngle(Color color) {
        if (color.equals(Color.blue)) {
            return new Angle(270);
        } else if (color.equals(Color.red)) {
            return new Angle(90);
        } else {
            return null;
        }
    }

    public Angle getAngle() {
        return angle;
    }

    public Angle getRelativeAngle() {
        return relativePos.angle;
    }
    
    public Position getRelativePos() {
        return relativePos.clone();
    }

    public Field getField() {
        return field;
    }

    public Point getFrontLeft() {
        Point center = getCenter();
        double hypot = (size / 2) * Math.sqrt(2);
        Angle theta = angle.add(-45);
        double x = center.x + theta.cos() * hypot;
        double y = center.y + theta.sin() * hypot;

        return new Point(x, y);
    }
    
    public Point getRelativeFrontLeft() {
        double hypot = (size / 2) * Math.sqrt(2);
        Angle theta = relativePos.angle.add(-45);
        double x = relativePos.pos.x + theta.cos() * hypot;
        double y = relativePos.pos.y + theta.sin() * hypot;
        
        return new Point(x, y);
    }

    @Override
    public void draw(Graphics g) {
        System.out.println(relativePos);
        g.setColor(color);
        this.fillPolygon(g);
        Vector dir = new Vector(.75 * Field.pixelsPerFoot, angle);
        dir.drawVector(g, getCenter());
        for (int i = 0; i < scanners.length; i++) {
            if (scanners[i] != null) {
                scanners[i].draw(g);
            }
        }
        for (ScannerReading sr : readings) {
            if (sr != null) {
                sr.draw(g);
            }
        }
    }
}
