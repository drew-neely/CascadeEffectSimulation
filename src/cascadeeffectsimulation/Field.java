package cascadeeffectsimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * @author Drew
 */
public class Field {

    static int pixelsPerFoot = 75;
    private Dimension size;
    private ArrayList<FieldObject> allFieldObjects = new ArrayList<>();
    private Bot bot;
    private FieldObject dragBot;

    public Field(Dimension dim) {
        size = dim;
    }

    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(0, 0, pixelsPerFoot * 12, pixelsPerFoot * 12);
        for (FieldObject fobj : allFieldObjects) {
            fobj.draw(g);
        }
        if (bot != null) {
            bot.draw(g);
        }
    }
    
    public boolean inObject(Point p) {
        for(FieldObject fobj : allFieldObjects) {
            if(fobj.contains(p)) {
                return true;
            }
        }
        return false;
    }

    public void init() {
        bot = new Bot(this, Color.blue, 1); // color and starting position of bot
        allFieldObjects.add(FieldObject.centerGoal(2)); // this number is the position that the center goal is in
        try {
            Thread thread = new Thread(bot);
            thread.start();
        } catch (Exception e) {
        }
        
        Point[] points = {new Point(-200,-200), new Point(-200, -100), new Point(-100, -100), new Point(-100, -200)};
        dragBot = new FieldObject(points);
        dragBot.setColor(Color.orange);
        allFieldObjects.add(dragBot);
    }
    
    public void moveDragBot(Point p) {
        dragBot.makeCenter(p);
    }
}
