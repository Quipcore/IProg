package assignment_2.datagram.unicast;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class Paper extends JPanel {
    private HashSet<Point> hs = new HashSet<>();

    public Paper(){
        setBackground(Color.WHITE);
        addMouseListener(new L1(this));
        addMouseMotionListener(new L2(this));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for(Point p : hs){
            g.fillOval(p.x,p.y,2,2);
        }
    }

    protected void addPoint(Point p){
        hs.add(p);
        repaint();
    }
}
