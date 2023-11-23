package assignment_2.datagram.unicast;

import javax.swing.*;
import java.awt.*;

public class Draw extends JFrame {
    public Draw(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(new Paper(), BorderLayout.CENTER);
        setSize(640, 480);
        setVisible(true);
    }
}
