import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;

abstract class Backgrounds
{
    int x;
    int y;
    int w;
    int h;
    
    public abstract void draw(Graphics g);
    public abstract void update();
    
    static BufferedImage loadImage(String filename)
    {
        BufferedImage m = null;
        try
        {
            m = ImageIO.read(new File(filename));
        } 
        catch(Exception e) {
                e.printStackTrace(System.err);
                System.exit(1);
        }
        return m;
    }
}