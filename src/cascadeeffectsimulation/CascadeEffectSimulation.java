package cascadeeffectsimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

/**
 * @author Drew
 */
public class CascadeEffectSimulation extends JPanel implements MouseInputListener {

// screen information
    int width = (int) (Field.pixelsPerFoot * 12) + 200;
    int height = (int) (Field.pixelsPerFoot * 12.2);
//    
// graphics information
    static int paintDelay = 10;
    static int runDelay = 10;
    static CascadeEffectSimulation animation = new CascadeEffectSimulation();
// field information
//
    Field field = new Field(new Dimension(width, height));
//
// start button action listener
    ActionListener startBtnLs = new ActionListener() {
        boolean start = true;

        @Override
        public void actionPerformed(ActionEvent e) {
            // Called when start button is pressed
        }
    };

    public static void main(String[] args) {
        animation.addMouseListener(animation);
        animation.addMouseMotionListener(animation);
        animation.setPreferredSize(new Dimension(animation.width, animation.height));
        animation.setBackground(Color.white);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame("Cascade Effect Simulation");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setLocation(5, 5);

                JButton startBtn = new JButton("Start");
                startBtn.setSize(new Dimension(180, 50));
                startBtn.setLocation(12 * Field.pixelsPerFoot + 10, 10);
                startBtn.addActionListener(null);
                startBtn.addActionListener(animation.startBtnLs);
                f.add(startBtn);

                f.add(animation);

                f.pack();
                f.setResizable(false);
                f.setVisible(true);
            }
        });

        animation.field.init();

        Timer timer = new Timer(true);
        TimerTask painting = new paintTiming();
        timer.schedule(painting, 0, paintDelay);
        TimerTask running = new runTiming();
        timer.schedule(running, 0, runDelay);
    }

    public static void reset() {
        //reset function
    }

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, 12 * Field.pixelsPerFoot + 5, 12 * Field.pixelsPerFoot + 5);
        try {
            animation.field.draw(g);
        } catch (ConcurrentModificationException exc) {
        }
    }

    //timing classes
    static class paintTiming extends TimerTask {

        @Override
        public void run() {
            animation.repaint();
        }
    }

    static class runTiming extends TimerTask {

        @Override
        public void run() {
            // update function
        }
    }

    //Mouse Listener Methods and attributes
    @Override
    public void mousePressed(MouseEvent e) {
        java.awt.Point point = new java.awt.Point(e.getPoint());

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point point = new Point(e.getPoint());
        animation.field.moveDragBot(point);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = new Point(e.getPoint());
        animation.field.moveDragBot(point);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}