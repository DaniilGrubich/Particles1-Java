
public class Particle {
    double x, y;
    double oldX, oldY;

    public Particle(double x, double y){
        this.x = x;
        this.y = y;
        oldX = x;
        oldY = y;
    }

    public void integrate(boolean borders) {
        double velX;
        double velY;
        velX = x - oldX;
        velY = y - oldY;

        oldX = x;
        oldY = y;

        x+=velX;
        y+=velY;

        if(!borders) {
            if (x > Main.window.getWidth())
                x -= Main.window.getWidth();
            if (x < 0)
                x += Main.window.getWidth();
            if(!Main.horizontalBorders){
                if (y > Main.window.getHeight())
                    y -= Main.window.getHeight();
                if (y < 0)
                    y += Main.window.getHeight();
            }
        }
    }

    public void attract(double x, double y, double attraction) {
//        System.out.println(x + "-" + this.x + "-" + y + "-" + this.y);
        double dx = x - this.x;
        double dy = y - this.y;

        double distance = Math.sqrt((dx*dx)+(dy*dy));
//        System.out.println("distance: " + distance);

        this.x += dx/(distance*(1/attraction));
        this.y += dy/(distance*(1/attraction));
//        System.out.println("---" + this.x + "--" + this.y);
    }
}
