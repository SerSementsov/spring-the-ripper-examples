package screensaver;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


@Component
public abstract class ColorFrame extends JFrame {

    public ColorFrame() throws HeadlessException {
        setSize(300, 300);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void showOnRandomPlace(){
        Random random = new Random();
        setLocation(random.nextInt(1200), random.nextInt(900));
        getContentPane().setBackground(getColor());
        repaint();
    }

    protected abstract Color getColor();
}
