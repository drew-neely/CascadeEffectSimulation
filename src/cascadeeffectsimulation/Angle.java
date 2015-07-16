
package cascadeeffectsimulation;

/**
 * @author Drew
 */
public class Angle {
    private double deg;
    
    public Angle(double deg) {
        this.deg = deg;
    }
    
    private double standardAngle(double deg) {
        return deg - Math.floor(deg / 360) * 360;
    }
    
    public double getDegrees() {
        return deg;
    }
    
    public double getRadians() {
        return Math.toRadians(deg);
    }
    
    public Angle add(Angle a) {
        return new Angle(this.deg + a.getDegrees());
    }
    
    public Angle add(double theta) {
        return new Angle(this.deg + theta);
    }
    
    public Angle subtract(Angle a) {
        return new Angle(getDegrees() - a.getDegrees());
    }
    
    public Angle subtract(double theta) {
        return new Angle(getDegrees() - theta);
    }
    
    //public Angle  
    
    public double sin() {
        return Math.sin(getRadians());
    }
    
    public double cos() {
        return Math.cos(getRadians());
    }
    
    public double tan() {
        return Math.tan(getRadians());
    }
    
    @Override
    public Angle clone() {
        return new Angle(deg);
    }
    
    @Override
    public String toString() {
        return deg + "°";
    }
}
