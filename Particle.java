
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
            if (x > Main.windowWidth)
                x -= Main.windowWidth;
            if (x < 0)
                x += Main.windowWidth;
            if(!Main.horizontalBorders){
                if (y > Main.windowHeight)
                    y -= Main.windowHeight;
                if (y < 0)
                    y += Main.windowHeight;
            }
        }
    }

    public void attract(double x, double y, double attraction) {
        double dx = x - this.x;
        double dy = y - this.y;

        double distance = Math.sqrt((dx*dx)+(dy*dy));

        this.x += dx/(distance*(1/attraction));
        this.y += dy/(distance*(1/attraction));
    }
}
