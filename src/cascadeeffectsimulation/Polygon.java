package cascadeeffectsimulation;

import java.awt.Graphics;

/**
 * @author Drew
 */
public class Polygon {

    double[] xpoints;
    double[] ypoints;
    int npoints;

    public Polygon(double[] xpoints, double[] ypoints, int npoints) {
        this.xpoints = xpoints;
        this.ypoints = ypoints;
        this.npoints = npoints;
    }

    public void translate(double dx, double dy) {
        for (int i = 0; i < npoints; i++) {
            xpoints[i] += dx;
            ypoints[i] += dy;
        }
    }

    public void translate(Vector v) {
        Angle angle = v.getAngle();
        double mag = v.magnitude();
        translate(angle.cos() * mag, angle.sin() * mag);
    }

    public void rotate(Angle angle) {
        double deg = angle.getDegrees();
        Point center = getCenter();
        double[] newxs = new double[npoints];
        double[] newys = new double[npoints];
        for (int i = 0; i < npoints; i++) {
            newxs[i] = xpoints[i] - center.x;
            newys[i] = ypoints[i] - center.y;
            double hypot = Math.hypot(newxs[i], newys[i]);
            double theta = Math.atan2(newys[i], newxs[i]);
            newxs[i] = Math.cos(theta + Math.toRadians(deg)) * hypot;
            newys[i] = Math.sin(theta + Math.toRadians(deg)) * hypot;
            newxs[i] += center.x;
            newys[i] += center.y;
        }
        xpoints = newxs;
        ypoints = newys;
    }

    private Point[] getVerts() {
        Point[] verts = new Point[npoints];
        for (int i = 0; i < verts.length; i++) {
            verts[i] = new Point(xpoints[i], ypoints[i]);
        }
        return verts;
    }

    @Override
    public Polygon clone() {
        return new Polygon(xpoints, ypoints, npoints);
    }

    public Point getCenter() {
        return new Point(average(xpoints), average(ypoints));
    }

    private double average(double[] nums) {
        double sum = 0;
        for (double i : nums) {
            sum += i;
        }
        return sum / nums.length;
    }

    protected static double[] getXs(Point... points) {
        double[] xs = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            xs[i] = points[i].x;
        }
        return xs;
    }

    protected static double[] getYs(Point... points) {
        double[] ys = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            ys[i] = (int) points[i].y;
        }
        return ys;
    }

    public boolean contains(Point p) {
        java.awt.Polygon aproxPoly = getAproximatePoly();
        java.awt.Point aproxPoint = new java.awt.Point((int)Math.round(p.x), (int)Math.round(p.y));
        return aproxPoly.contains(aproxPoint);
    }
    

    public java.awt.Polygon getAproximatePoly() {
        int[] xints = new int[npoints];
        int[] yints = new int[npoints];
        for (int i = 0; i < npoints; i++) {
            xints[i] = (int) Math.round(xpoints[i]);
            yints[i] = (int) Math.round(ypoints[i]);
        }
        return new java.awt.Polygon(xints, yints, npoints);
    }

    public void fillPolygon(Graphics g) {
        g.fillPolygon(getAproximatePoly());
    }
}
