
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;
import java.util.ArrayList;

abstract class Sprite
{
    int x;
    int y;
    int w;
    int h;
    int prevX;
    int prevY;
    double speed;
    double vert_velocity;
    boolean direction; //true = to the right
    boolean toBeRemoved = false;
    Model model;
    
    public abstract void draw(Graphics g);
    public abstract void update();
    
    static BufferedImage loadImage(String filename)
    {
        BufferedImage m = null;
        try
        {
            m = ImageIO.read(new File(filename));
        } 
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return m;
    }
    public void setModel(Model m)
    {
        model = m;
    }
    public boolean clickSprite (int click_x, int click_y)
    {
        if (click_x > this.x + w) return false;
        if (click_x < this.x) return false;
        if (click_y > this.y + h) return false;
        if (click_y < this.y) return false;
        return true;
    }
    public void collisonCorrection(ArrayList<Sprite> sprites)
    {
        int i = sprites.indexOf(this);
        for (int j = 0; j < sprites.size(); j++)
        {
            Sprite tmp = sprites.get(j);
            if (spriteOverlap(tmp) && i != j) //check if s overlaps with any other sprite
            {
                if (this instanceof Mario) //if the current sprite is a mario
                {
                    ((Mario)this).moveFromSprite(tmp); //move mario from collison
                }
                else if (this instanceof Goomba)
                {
                    ((Goomba)this).moveFromSprite(tmp); //otherwise move goomba from collison
                }
                else if (this instanceof Fireball)
                {
                    ((Fireball)this).toBeRemoved = true; //if fireball hits another sprite (not a tube)
                    if (tmp instanceof Goomba)
                    {
                        ((Goomba)tmp).onFire = true; //set the Goomba on fire
                    }
                    else
                    {
                        ((Fireball)this).toBeRemoved = false;
                        ((Fireball)this).moveFromSprite(tmp); //bounce off tubez
                    }
                }
            }
        }
    }

    public boolean spriteOverlap(Sprite b) //compares sprites' hitboxes
    {
        if ((x + w) <= b.x)
            return false;
        if (x >= (b.x + b.w))
            return false;
        if ((y + h) <= b.y)
            return false;
        if (y >= (b.y + b.h))
            return false;

        return true;
        
    }
   
}
