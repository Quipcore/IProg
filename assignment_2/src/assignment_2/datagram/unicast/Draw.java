package assignment_2.datagram.unicast;

import javax.swing.*;
import java.awt.*;

public class Draw extends JFrame {

    Paper paper;

    public Draw(){
        this.paper = new Paper();


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(this.paper, BorderLayout.CENTER);
        setSize(640, 480);
        setVisible(true);
    }

    public void drawAt(int x, int y){
        paper.addPoint(new Point(x,y));
    }


}
