package assignment_2.datagram.unicast;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class Draw extends JFrame {

    private final Paper paper;

    public Draw(){
        this.paper = new Paper();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(this.paper, BorderLayout.CENTER);
        setSize(640, 480);
        setVisible(true);
    }

    public void drawAt(int x, int y){
        drawAt(new Point(x,y));
    }

    public void drawAt(Point point){
        if(point == null){
            return;
        }
        paper.addPoint(point);
    }

    public List<Point> getDrawnPoints() {
        return paper.getPoints();
    }
}
