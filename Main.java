//Created Jan 2019 by Daniil Grubich

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends JFrame {

    static Particle particles[];

    static int windowHeight;
    static int windowWidth;
    static boolean fullScrean = false;
    static Canvas canvas;
    static BufferStrategy bs;
    static Graphics2D g;

    static double attraction = 1;
    static JLabel lblInfo;

    static boolean InfiniteBorders = true;
    static boolean horizontalBorders = true;

    static boolean trace = false;
    static boolean BWMOde = false;
    static float hColor = 0;

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException,
            UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        prepareVariables();

        new Main(1000, 500, fullScrean);

    }

    public static void prepareVariables() {
        int nParticles = 10000;
        try {
            nParticles = Integer.parseInt(JOptionPane.showInputDialog(null, "Number of particles: "));
        } catch (Exception e) {
        }

        fullScrean = JOptionPane.showInputDialog(null, "Fullscrean?(-/+):").equals("+");

        particles = new Particle[nParticles];

    }

    Point getMousePositionOnTheCanvas() {
        return new Point(MouseInfo.getPointerInfo().getLocation().x - getX(),
                MouseInfo.getPointerInfo().getLocation().y - getY());
    }

    Main(int w, int h, boolean full) {
        super("Particles");
        setSize(w, h);
        if (full) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);

        }

        setLayout(new BorderLayout());

        add(lblInfo = new JLabel(""), BorderLayout.SOUTH);
        lblInfo.setForeground(Color.BLACK);
        lblInfo.setFont(new Font("Consolas", Font.BOLD, 15));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Random random = new Random();
        for (int i = 0; i < particles.length; i++)
            particles[i] = new Particle(random.nextInt(getWidth()), random.nextInt(getHeight()));

        canvas = new Canvas();
        canvas.setFocusable(false);
        add(canvas, BorderLayout.CENTER);
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();

        addMouseWheelListener(new MouseWheelHandlerClass());
        addKeyListener(new KeyHandlerClass());
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                g = (Graphics2D) bs.getDrawGraphics();
                windowHeight = getHeight();
                windowWidth = getWidth();

                Color particleColor = BWMOde ? Color.BLACK : Color.getHSBColor(hColor+=.001, 1.f, 1.f);
                if(hColor>1)
                    hColor = 0;

                if (!trace) {
                    if (!BWMOde)
                        g.setColor(Color.BLACK);
                        
                    else 
                        g.setColor(Color.WHITE);

                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                Point mousePoint = getMousePositionOnTheCanvas();

                for (Particle p : particles) {
                    p.attract(mousePoint.getX(), mousePoint.getY(), attraction);
                    p.integrate(InfiniteBorders);
                    g.setColor(particleColor);

                    displayParticle(g, p);

                }

                lblInfo.setText("Attraction: " + attraction + "   ∞ Borders: " + InfiniteBorders + "   Trace: " + !trace +  "   F1 - help");
                g.dispose();
                bs.show();

            }

        }, 1000 / 60, 1000 / 60);

    }

    private void displayParticle(Graphics2D g, Particle p) {
        g.drawLine((int) p.x, (int) p.y, (int) p.x, (int) p.y);
    }

}
