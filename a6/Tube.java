import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;


class Tube extends Sprite
{

    static BufferedImage tube_image = null;
    Tube(int xx, int yy, Model m)
    {
        model = m;
        w = 55;
        h = 400;
        x = xx;	
        y = yy;
        if (tube_image == null)
            tube_image = loadImage("tube.png");
    }
    Tube(Tube copyme)
    {
        w = copyme.w;
        h = copyme.h;
        x = copyme.x;
        y = copyme.y;
        if (tube_image == null)
            tube_image = loadImage("tube.png");
    }
    Sprite cloneme()
    {
        return new Tube(this);
    }
    public void draw(Graphics g)
    {
        g.drawImage(tube_image, x - model.mario.x + model.mario.startX, y, null);
    }
    public void update()
    {
    }
    public boolean isTube()
    {
        return true;
    }
    
    
    Tube(Json ob)
    {
        w = 55;
        h = 400;
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        if (tube_image == null)
            tube_image = loadImage("tube.png");
    }
    Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("name", "Tube");
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }
    


}
