
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends JFrame {
    static JFrame window;
    static Particle particles[];
    static double attraction = 1;
    static JLabel lblInfo;
    static int size = 5;
    static int x, y;
    static boolean InfiniteBorders = true;
    static boolean horizontalBorders = true;
    static boolean trace = false;
    static int psrticleShape = 6; //0 - circle, 1 - point, 2 - line, 3 - triangle, 4 - square, 5 - pentagon, 6 - hexagon
    static boolean fill = true;
    static boolean fullScrean = false;
    static int nParticles;

    //Color variables
    static int colorStep = 10;
    static Color particleColor = Color.RED;
    static int colorCounter = 0;
    static boolean blue = false;
    static boolean green = false;
    static boolean red = true;


    static boolean BWMOde = false;

    static Timer timer = new Timer();
    static BufferStrategy bs;


    static Thread particlesProcessI;
    static Thread particlesProcessII;
    static Thread particlesProcessIII;

    private static void createGUI(int w, int h, boolean full){
        window = new JFrame("Particles");
        window.setSize(w, h);
        if(full){
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setUndecorated(true);
        }

        window.setLayout(new BorderLayout());
        window.add(lblInfo = new JLabel(""), BorderLayout.SOUTH);
        lblInfo.setForeground(Color.DARK_GRAY);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

    }

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        try {
            nParticles = Integer.parseInt(JOptionPane.showInputDialog(null, "Number of particles: "));
        }catch (Exception e){
            nParticles = 100;
        }

        if(JOptionPane.showInputDialog(null, "Fullscrean?(-/+):").equals("+"))
            fullScrean = true;
        else
            fullScrean = false;


        createGUI(720, 500, fullScrean);
        particles = new Particle[nParticles];
        Random random = new Random();

        for(int i = 0; i<particles.length; i++)
        {
            particles[i] = new Particle(random.nextInt(window.getWidth()), random.nextInt(window.getHeight()));
        }

        //myCanvas canvas;
        Canvas canvas = new Canvas();
        canvas.setFocusable(false);
        window.add(canvas, BorderLayout.CENTER);
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();


        x = MouseInfo.getPointerInfo().getLocation().x + window.getX();
        y = MouseInfo.getPointerInfo().getLocation().y + window.getY();

        canvas.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                size -= e.getWheelRotation();
            }
        });

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar()=='+')
                    size += 5;
                else if(e.getKeyChar()=='-')
                    size -= 5;
                else if(e.getKeyChar()=='B'||e.getKeyChar()=='b')
                    InfiniteBorders = !InfiniteBorders;
                else if(e.getKeyChar() == 't' || e.getKeyChar() == 'T')
                    trace = !trace;
                else if(e.getKeyChar() == 'm' || e.getKeyChar() == 'M')
                    BWMOde = !BWMOde;

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                else if (e.getKeyCode() == KeyEvent.VK_Q)
                    attraction = attraction + .1;
                else if (e.getKeyCode() == KeyEvent.VK_A)
                    attraction = attraction - .1;
                else if (e.getKeyCode() == KeyEvent.VK_W)
                    attraction = attraction + .5;
                else if (e.getKeyCode() == KeyEvent.VK_S)
                    attraction = attraction - .5;
                else if(e.getKeyCode() == KeyEvent.VK_E)
                    attraction = attraction + 1;
                else if(e.getKeyCode() == KeyEvent.VK_D)
                    attraction = attraction - 1;


                if (e.getKeyCode() == KeyEvent.VK_H){
                    if(psrticleShape == 6)
                        psrticleShape = 0;
                    else
                        psrticleShape++;
                } else if(e.getKeyCode() == KeyEvent.VK_F)
                    fill = !fill;
                else if (e.getKeyCode()==KeyEvent.VK_Z)
                    attraction = 1;
                else if (e.getKeyCode() == KeyEvent.VK_C) {
                    for (Particle p : particles) {

                        int randomTheta = random.nextInt(361);
                        p.x = x + Math.cos(randomTheta)*300+random.nextInt(10);
                        p.y = y + Math.sin(randomTheta)*300+random.nextInt(10);
                        p.oldY = p.y;
                        p.oldX = p.x;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    for (Particle p : particles) {
                        p.x = random.nextInt(window.getWidth());
                        p.y = random.nextInt(window.getHeight());
                        p.oldX = p.x;
                        p.oldY = p.y;
                        size = 5;

                        InfiniteBorders = true;
                        trace = false;
                    }
                } else if(e.getKeyCode() == KeyEvent.VK_N){
                    horizontalBorders = !horizontalBorders;
                }else if(e.getKeyCode() == KeyEvent.VK_F1){
                    String message = "Attaraction: \n" +
                            "'Q': -.1, 'A': +.1 \n" +
                            "'W': -.5, 'S': +.5 \n" +
                            "'E': -1, 'D': +1 \n" +
                            "'Z': attraction = 1 \n" +
                            "---\n" +
                            "Particle: \n" +
                            "Scroll: change size, 'H': change shape\n" +
                            "'F': on/off fill \n" +
                            "---- \n" +
                            "'R': Reset, 'M': Black-White mode \n" +
                            "'T': on/off trace, 'B': on/off Borders \n" +
                            "'C': Circle order \n" +
                            "'Esc': exit";

                    JOptionPane.showMessageDialog(null, message);
                }

            }
        });



//        float red = 1;
//        float green = (float) .1;
//        float blue = (float) .1;
//        double colorStep = .01;



        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int redi = particleColor.getRed();
                int greeni = particleColor.getGreen();
                int bluei = particleColor.getBlue();
                colorCounter++;

                if(redi >= 254) {
                    green = true;
                    red = false;
                    blue = false;
                }else if(greeni >= 254) {
                    blue = true;
                    green = false;
                    red = false;
                }else if(bluei >= 254) {
                    red = true;
                    blue = false;
                    green = false;
                }



                if (!BWMOde) {
                    System.out.println(redi);
                    System.out.println(greeni);
                    System.out.println(bluei);

                    try {
                        if (red)
                            particleColor = new Color(redi + 1, greeni - 1, bluei - 1);
                        else if (green)
                            particleColor = new Color(redi - 1, greeni + 1, bluei + 1);
                        else if (blue)
                            particleColor = new Color(redi + 1, greeni - 1, bluei + 1);
                    }catch (Exception e){particleColor = Color.RED;}
                } else
                particleColor = Color.BLACK;


                Graphics2D g = (Graphics2D)bs.getDrawGraphics();
                if(!trace) {
                    if(!BWMOde)
                        g.setColor(Color.black);
                    else
                        g.setColor(Color.WHITE);

                    g.fillRect(0, 0, window.getWidth(), window.getHeight());
                }

                x = MouseInfo.getPointerInfo().getLocation().x - window.getX();
                y = MouseInfo.getPointerInfo().getLocation().y - window.getY();

//                SwingUtilities.updateComponentTreeUI(window);

                for(Particle p : particles)
                {
                    p.attract(x, y, attraction);
                    p.integrate(InfiniteBorders);
                    g.setColor(particleColor);

                    displayParticle(g,p,psrticleShape, fill);


                }
//                particlesProcessI.start();
//                particlesProcessII.start();
//                particlesProcessIII.start();


               

                lblInfo.setText("#Particels: " + nParticles + "   Attraction: " + attraction + "   Size:" + size + "   ∞ Borders: " + InfiniteBorders +
                        "   Trace: " + !trace + "   Shape: " + psrticleShape + "   Fill: " + fill + "   F1 - help");
                g.dispose();
                bs.show();

            }
        }, 1000/60/2, 1000/60/2);



    }

    private static void displayParticle(Graphics2D g, Particle p, int type, boolean fill) {
        if (type == 1)
            g.drawLine((int)p.x, (int)p.y, (int)p.x, (int)p.y);
        else if (type == 2)
            g.drawLine((int)p.x, (int)p.y, (int)p.x+size, (int)p.y);
        else {

            if (type == 0) {
                if(fill)
                    g.fillOval((int) p.x - size / 2, (int) p.y - size / 2, size, size);
                else
                    g.drawOval((int)p.x - size/2, (int)p.y-size/2, size, size);
            }
            else if (type == 3){
                int xs[] = {(int)p.x, (int)p.x-size, (int)p.x-size/2};
                int ys[] = {(int)p.y, (int)p.y, (int)p.y-size};

                if(fill)
                    g.fillPolygon(xs, ys, 3);
                else
                    g.drawPolygon(xs, ys, 3);
            }
            else if (type == 4) {
                if(fill)
                    g.fillRect((int) p.x, (int) p.y, size, size);
                else
                    g.drawRect((int)p.x, (int)p.y, size, size);
            }
            else if (type == 5){
                int xs[] = {(int)p.x, (int)p.x+size/4, (int)p.x+size*3/4, (int)p.x+size, (int)p.x+size/2};
                int ys[] = {(int)p.y, (int)p.y-size/3, (int)p.y-size/3, (int)p.y, (int)p.y+size/3};

                if(fill)
                    g.fillPolygon(xs, ys, 5);
                else
                    g.drawPolygon(xs, ys, 5);
            }
            else if (type == 6){
                int xs[] = {(int)p.x, (int)p.x+size/3, (int)p.x+size*2/3, (int)p.x+size, (int)p.x+size*2/3, (int)p.x+size/3};
                int ys[] = {(int)p.y, (int)p.y-size/3, (int)p.y-size/3, (int)p.y, (int)p.y+size/3, (int)p.y+size/3};

                if(fill)
                    g.fillPolygon(xs, ys, 6);
                else
                    g.drawPolygon(xs, ys, 6);
            }

        }
    }

//    public static class ParticlesThread implements Runnable{
//        private Particle[] particles;
//        Graphics2D g;
//
//        ParticlesThread(Particle[] particles, BufferStrategy bs){
//            this.particles = particles;
//            g = (Graphics2D)bs.getDrawGraphics();
//        }
//
//        @Override
//        public void run() {
//            for (Particle p :
//                    particles) {
//                p.attract(x, y, attraction);
//                p.integrate(InfiniteBorders);
//                g.setColor(particleColor);
//
//                displayParticle(g,p,psrticleShape, fill);
//                bs.show();
//            }
//        }
//    }
}
