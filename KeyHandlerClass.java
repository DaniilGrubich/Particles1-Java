import java.awt.MouseInfo;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JOptionPane;

class KeyHandlerClass extends KeyAdapter {
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'B' || e.getKeyChar() == 'b')
            Main.InfiniteBorders = !Main.InfiniteBorders;
        else if (e.getKeyChar() == 't' || e.getKeyChar() == 'T')
            Main.trace = !Main.trace;
        else if (e.getKeyChar() == 'm' || e.getKeyChar() == 'M')
            Main.BWMOde = !Main.BWMOde;
        else if (e.getKeyChar() == 'n' || e.getKeyChar() == 'N')
            Main.horizontalBorders = !Main.horizontalBorders;
        else if (e.getKeyChar() == 'z' || e.getKeyChar() == 'Z')
            Main.attraction = 1;

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        else if (e.getKeyCode() == KeyEvent.VK_Q)
            Main.attraction = Main.attraction + .1;
        else if (e.getKeyCode() == KeyEvent.VK_A)
            Main.attraction = Main.attraction - .1;
        else if (e.getKeyCode() == KeyEvent.VK_W)
            Main.attraction = Main.attraction + .5;
        else if (e.getKeyCode() == KeyEvent.VK_S)
            Main.attraction = Main.attraction - .5;
        else if (e.getKeyCode() == KeyEvent.VK_E)
            Main.attraction = Main.attraction + 1;
        else if (e.getKeyCode() == KeyEvent.VK_D)
            Main.attraction = Main.attraction - 1;


        if (e.getKeyCode() == KeyEvent.VK_C) {
            for (Particle p : Main.particles) {
                Random random = new Random();

                int randomTheta = random.nextInt(361);
                p.x = MouseInfo.getPointerInfo().getLocation().getX() + Math.cos(randomTheta) * 300 + random.nextInt(10);
                p.y = MouseInfo.getPointerInfo().getLocation().getY() + Math.sin(randomTheta) * 300 + random.nextInt(10);
                p.oldY = p.y;
                p.oldX = p.x;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            for (Particle p : Main.particles) {
                Random random = new Random();

                p.x = random.nextInt(Main.windowWidth);
                p.y = random.nextInt(Main.windowHeight);
                p.oldX = p.x;
                p.oldY = p.y;

                Main.InfiniteBorders = true;
                Main.trace = false;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            String message = "Attaraction: \n" +
                    "'Q': -.1, 'A': +.1 \n" +
                    "'W': -.5, 'S': +.5 \n" +
                    "'E': -1, 'D': +1 \n" +
                    "'Z': attraction = 1 \n" +
                    "Scroll: change size */1.1\n" +
                    "---\n" +
                    "'R': Reset, 'M': Black-White mode \n" +
                    "'T': on/off trace, 'B': on/off Borders \n" +
                    "'C': Circle order \n" +
                    "'Esc': exit";

            JOptionPane.showMessageDialog(null, message);
        }

    }

}