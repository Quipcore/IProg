package assignment_2.datagram.unicast;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;

public class L2 implements MouseMotionListener {

    private final Paper paper;

    public L2(Paper paper){
        this.paper = paper;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        paper.addPoint(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
