import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseWheelHandlerClass implements MouseWheelListener{

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        float v = e.getWheelRotation();
        System.out.println(v);
        if(v>0)
            Main.attraction /= 1.1;
        else if(v<0)
            Main.attraction *= 1.1;

        System.out.println(Main.attraction);
    }
    
}
