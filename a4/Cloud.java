import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.util.Random;
import java.awt.Graphics;

class Cloud extends Backgrounds
{
    static BufferedImage cloud_image = null;

    Cloud(int xx)
    {
        w = 199;
        h = 165;
        Random rand = new Random();
        y = rand.nextInt(150) + 1;
        x = xx;
        if (cloud_image == null); { cloud_image = loadImage("cloud.png"); }
    }
    public void draw(Graphics g)
    {
        g.drawImage(cloud_image, x, y, null);
        update();
        
    }
    public void update()
    {
        x -= 1;
        if (x < -w)
        {
            Random rand = new Random();
            y = rand.nextInt(150) + 1;
            x = 500;
        }
    }
}