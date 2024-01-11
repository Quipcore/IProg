package assignment_2.datagram.unicast;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Paper extends JPanel {
    private final List<Point> points = new ArrayList<>();

    public Paper(){
        setBackground(Color.WHITE);
        addMouseListener(new L1(this));
        addMouseMotionListener(new L2(this));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for(int i = 0; i < points.size(); i++){
            Point p = points.get(i);
            g.fillOval(p.x,p.y,2,2);
        }
    }

    public void addPoint(Point p){
        points.add(p);
        repaint();
    }

    public List<Point> getPoints(){
        return points;
    }
}
